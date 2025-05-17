package TicketMarket.demo.Rest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;
import TicketMarket.demo.Util.QRUtils;
import TicketMarket.demo.DAO.EventRepository;
import TicketMarket.demo.DAO.TicketRepository;
import TicketMarket.demo.DAO.TransactionRepository;
import TicketMarket.demo.DAO.UserRepository;
import TicketMarket.demo.Entity.Event;
import TicketMarket.demo.Entity.Ticket;
import TicketMarket.demo.Entity.Transaction;
import TicketMarket.demo.Entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/event")
@Controller
public class EventController {
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SERIAL_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    public EventController(EventRepository eventRepository, TicketRepository ticketRepository,
            UserRepository userRepository, TransactionRepository transactionRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public static String generateSerialKey() {
        StringBuilder serialKey = new StringBuilder(SERIAL_KEY_LENGTH);
        for (int i = 0; i < SERIAL_KEY_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            serialKey.append(CHARACTERS.charAt(randomIndex));
        }
        return serialKey.toString();
    }

    @GetMapping("/createEvent")
    public String createEvent(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null)
            return "redirect:/signin";
        return "createEventForm";
    }

    @RequestMapping(value = "/processEvent", method = RequestMethod.POST)
    public String processEvent(HttpServletRequest http, HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null)
            return "redirect:/signin";

        // Retrieve event details
        String name = http.getParameter("event_name");
        LocalDateTime event_time = LocalDateTime.parse(http.getParameter("event_date"));
        String eventLoc = http.getParameter("eventLoc");
        String event_desc = http.getParameter("event_desc");
        String tag = http.getParameter("tag");
        String createTicketsToggle = http.getParameter("create-tickets-toggle");

        // Set generated_by_us based on createTicketsToggle
        boolean generatedByUs = "on".equals(createTicketsToggle);

        User temp = (User) session.getAttribute("loggedInUser");

        // Check if the event already exists
        if (eventRepository.isEventExist(name)) {
            model.addAttribute("error", "Event name already exists!");
            return "createEventForm";
        }

        // Create the event
        Event newEvent = new Event(name, event_time, eventLoc, event_desc, temp.getUser_name(), tag, generatedByUs);
        eventRepository.save(newEvent); // Ensure the event is saved before creating tickets
        System.out.println("Event created with ID: " + newEvent.getEvent_id());

        // Check if the "create tickets" checkbox is checked
        if (generatedByUs) {
            // Retrieve ticket details
            String ticketCountStr = http.getParameter("ticket_count");
            String ticketPriceStr = http.getParameter("ticket_price");

            try {
                int ticketCount = Integer.parseInt(ticketCountStr);
                int ticketPrice = Integer.parseInt(ticketPriceStr);

                if (ticketCount <= 0 || ticketPrice <= 0) {
                    model.addAttribute("error", "Ticket count and price must be positive integers.");
                    return "createEventForm";
                }

                // Create tickets for the event
                for (int i = 0; i < ticketCount; i++) {
                    String serialKey = generateSerialKey();
                    Ticket ticket = new Ticket(newEvent.getEvent_id(), temp.getUser_id(), ticketPrice,
                            "Ticket " + (i + 1), serialKey, true, true , "");
                    ticketRepository.save(ticket);
                    System.out.println("Ticket created with Serial Key: " + serialKey);
                }
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid ticket count or price. Please enter valid numbers.");
                return "createEventForm";
            }
        }

        return "eventCreatedSuccess";
    }

    @RequestMapping("/success_event_created")
    public String success_event_created(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null)
            return "redirect:/signin";
        return "eventCreatedSuccess";
    }

    @PostMapping("/{eventId}/Tickets/{ticketId}/uploadPdf")
public String uploadTicketPdf(@PathVariable int eventId,
                              @PathVariable int ticketId,
                              @RequestParam("file") MultipartFile file,
                              HttpSession session,
                              Model model) {
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/signin";
    }

    Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));

    if (ticket.getSeller_id() != user.getUser_id()) {
        model.addAttribute("error", "You are not authorized to upload a file for this ticket.");
        return "redirect:/event/" + eventId + "/Tickets";
    }

    if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
        model.addAttribute("error", "Invalid file type. Please upload a PDF.");
        return "redirect:/event/" + eventId + "/Tickets";
    }

    try {
        String uploadDir = "uploads/tickets/";
        Files.createDirectories(Paths.get(uploadDir));
        String fileName = "ticket_" + ticketId + "_" + UUID.randomUUID() + ".pdf";
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        ticket.setPdfUrl(filePath.toString());
        ticketRepository.save(ticket);
        return "redirect:/event/" + eventId + "/Tickets";
    } catch (IOException e) {
        model.addAttribute("error", "File upload failed.");
        return "redirect:/event/" + eventId + "/Tickets";
    }
}

    @GetMapping("/{id}")
    public String eventPage(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null)
            return "redirect:/signin";
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);

        return "eventPage";
    }

    @GetMapping("/{id}/Tickets")
public String eventTickets(@PathVariable int id, HttpSession session, Model model) {
    if (session.getAttribute("loggedInUser") == null)
        return "redirect:/signin";

    Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    model.addAttribute("event", event);

    // Fetch tickets and their sellers
    List<Ticket> ticketList = ticketRepository.availableTicketsByEventId(id);
    Map<Integer, String> sellerUsernames = new HashMap<>();

    for (Ticket ticket : ticketList) {
        User seller = userRepository.findById(ticket.getSeller_id())
                .orElseThrow(() -> new RuntimeException("Seller not found for ticket ID " + ticket.getTicket_id()));
        sellerUsernames.put(ticket.getTicket_id(), seller.getUser_name());
    }

    model.addAttribute("ticketsList", ticketList);
    model.addAttribute("sellerUsernames", sellerUsernames);
    model.addAttribute("eventAvailableTicketsCount", eventRepository.amountOfAvilableTickets(id));
    model.addAttribute("eventSoldTicketsCount", eventRepository.amountOfSoldTickets(id));
    model.addAttribute("eventLookingForTicketsCount", eventRepository.amountOfLookingForTickets(id));
    model.addAttribute("loggedInUser", session.getAttribute("loggedInUser")); // ✅ Add this line

    return "eventTicketsPage";
}


    @GetMapping("/{id}/Tickets/newTicket")
    public String newEventTicket(@PathVariable int id, Model model) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        return "newEventTicketForm";
    }

@RequestMapping(value = "/{id}/Tickets/processNewTicket", method = RequestMethod.POST)
public String processNewEventTicket(@PathVariable int id,
                                    @RequestParam("file") MultipartFile file,
                                    HttpServletRequest http,
                                    HttpSession session,
                                    Model model) {
    // 1) Authenticate
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        model.addAttribute("error", "No User Found!");
        return "redirect:/signin";
    }

    // 2) Load event
    Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
    model.addAttribute("event", event);

    // 3) Parse & validate price
    String priceStr = http.getParameter("price");
    int price;
    try {
        price = Integer.parseInt(priceStr);
        if (price <= 0) {
            model.addAttribute("error", "Price must be a positive integer.");
            return "newEventTicketForm";
        }
    } catch (NumberFormatException e) {
        model.addAttribute("error", "Invalid price value. Please enter a valid number.");
        return "newEventTicketForm";
    }

    // 4) Read description
    String description = http.getParameter("description");

    // 5) Extract serialKey from PDF QR Code
    String serialKey = null;
    if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
        model.addAttribute("error", "You must upload a valid PDF file containing a QR code.");
        return "newEventTicketForm";
    }
    try {
        // write to temp file
        Path tempFile = Files.createTempFile("ticket_pdf_", ".pdf");
        file.transferTo(tempFile.toFile());

        // extract QR payload
        serialKey = QRUtils.extractQRCodeFromPDF(tempFile);
        Files.deleteIfExists(tempFile);

        if (serialKey == null || ticketRepository.isTicketAlredayForSale(serialKey, event.getEvent_id())) {
            model.addAttribute("error", "No QR code found in the uploaded PDF.");
            return "newEventTicketForm";
        }
    } catch (IOException e) {
        model.addAttribute("error", "Failed to process the uploaded PDF file.");
        return "newEventTicketForm";
    }

    // 6) Create or enable ticket based on “generated_by_us”
    if (event.isGenerated_by_us()) {
        System.out.println("Event is generated by us !!!");
        // verify that QR serialKey matches one of our pre-generated tickets
        boolean valid = ticketRepository.verifyTicket(event.getEvent_id(), serialKey);
        if (!valid) {
            model.addAttribute("error", "Invalid serial key for this event.");
            System.out.println("Ticket Is not Valid !!!");

            return "newEventTicketForm";
        }
        Ticket existing = ticketRepository.findBySerialKey(serialKey, event.getEvent_id());
        if (existing == null) {
            System.out.println("Ticket Is not exists !!!");
            model.addAttribute("error", "Serial key not found.");
            return "newEventTicketForm";
        }
        existing.setFor_sale(true);
        existing.setPrice(price);
        existing.setDesc(description);
        ticketRepository.save(existing);
    } else {
        // create brand-new ticket if not system-generated
        Ticket t = new Ticket(
            id,
            user.getUser_id(),
            price,
            description,
            serialKey,
            true,    // for_sale
            false,   // is_sold
            null     // pdfUrl or any other field
        );
        ticketRepository.save(t);
    }

    return "ticketCreateSuccess";
}


        @GetMapping("/{id}/generateTickets")
        public String generateTicketsForm(@PathVariable int id, HttpSession session, Model model) {
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) {
                return "redirect:/signin"; // Redirect to sign-in page if the user is not logged in
            }

            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            if (!event.getEvent_owner().equals(user.getUser_name())) {
                model.addAttribute("error", "You are not authorized to generate tickets for this event.");
                return "redirect:/event/" + id;
            }

            model.addAttribute("event", event);
            return "generateTicketsForm"; // Reuse the create event form for ticket generation
}

        @PostMapping("/{id}/generateTickets")
        public String processGenerateTickets(@PathVariable int id, HttpServletRequest http, HttpSession session, Model model) {
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) {
                return "redirect:/signin"; // Redirect to sign-in page if the user is not logged in
            }

            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            if (!event.getEvent_owner().equals(user.getUser_name())) {
                model.addAttribute("error", "You are not authorized to generate tickets for this event.");
                return "redirect:/event/" + id;
            }

            // Retrieve ticket details
            String ticketCountStr = http.getParameter("ticket_count");
            String ticketPriceStr = http.getParameter("ticket_price");

            try {
                int ticketCount = Integer.parseInt(ticketCountStr);
                int ticketPrice = Integer.parseInt(ticketPriceStr);

                if (ticketCount <= 0 || ticketPrice <= 0) {
                    model.addAttribute("error", "Ticket count and price must be positive integers.");
                    return "generateTicketsForm";
                }

                // Generate tickets for the event
                for (int i = 0; i < ticketCount; i++) {
                    String serialKey = generateSerialKey();
                    Ticket ticket = new Ticket(event.getEvent_id(), user.getUser_id(), ticketPrice,
                            "Generated Ticket " + (i + 1), serialKey, true, true , "");
                    ticketRepository.save(ticket);
                    System.out.println("Generated ticket with Serial Key: " + serialKey);
                }
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid ticket count or price. Please enter valid numbers.");
                return "generateTicketsForm";
            }

            return "redirect:/event/" + id; // Redirect back to the event page
        }

    
    public boolean generatedByUsTicket(String serialKey) {
        return ticketRepository.generatedByUsTicket(serialKey);
    }

    
    public boolean verifyTicket(int eventId , String serialKey) {
        return ticketRepository.verifyTicket(eventId , serialKey);
    }

    @GetMapping("/{eventId}/Tickets/{TicketId}")
    public String buyEventTicket(@PathVariable int eventId, @PathVariable int TicketId, HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "No User Found!");
            return "redirect:/signin";
        }
        Ticket ticket = ticketRepository.findById(TicketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("ticket", ticket);
        model.addAttribute("event", event);
        model.addAttribute("loggedInUser", user);
        return "buyTicketPage";
    }

    @RequestMapping("/{eventId}/Tickets/{TicketId}/processBuyingTicket")
    public String processBuyingTicket(@PathVariable int eventId, @PathVariable int TicketId, HttpSession session,
            Model model) {
        User buyer = (User) session.getAttribute("loggedInUser");
        if (buyer == null) {
            model.addAttribute("error", "No User Found!");
            return "redirect:/signin";
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Ticket ticket = ticketRepository.findById(TicketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        User seller = userRepository.findById(ticket.getSeller_id())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        if (buyer.getUser_id() == seller.getUser_id()) {
            model.addAttribute("error", "You cannot buy your own ticket.");
            model.addAttribute("ticket", ticket);
            model.addAttribute("event", event);
            model.addAttribute("loggedInUser", buyer); // Ensure the user data is added back
            return "buyTicketPage";
        }

        if (buyer.getBalance() < ticket.getPrice()) {
            model.addAttribute("error", "Insufficient balance to purchase the ticket.");
            model.addAttribute("ticket", ticket);
            model.addAttribute("event", event);
            model.addAttribute("loggedInUser", buyer); // Ensure the user data is added back
            return "buyTicketPage";
        }

        // Process the ticket purchase
        if (ticket.isGenerated_by_us()) {
            processBuyingOurTicket(buyer, seller, ticket, event);
        } else {
            processBuyingTicket(buyer, seller, ticket, event);
        }


        model.addAttribute("ticket", ticket);
        model.addAttribute("event", event);
        return "successBuyingTicketPage";
    }

    public void processBuyingTicket(User buyer, User seller, Ticket ticket, Event event) {
        Transaction transaction = new Transaction(ticket.getTicket_id(), buyer.getUser_id(), seller.getUser_id(),
                LocalDateTime.now(), ticket.getPrice());
        transactionRepository.save(transaction);

        // balance handeling
        buyer.setBalance(buyer.getBalance() - ticket.getPrice());
        seller.setBalance(seller.getBalance() + ticket.getPrice());

        // ticket handeling
        ticket.setFor_sale(false);
        ticket.set_is_sold(true);

        ticket.setSeller_id(buyer.getUser_id());

        userRepository.save(buyer);
        userRepository.save(seller);

    }

    public void processBuyingOurTicket(User buyer, User seller, Ticket ticket, Event event) {
        Transaction transaction = new Transaction(ticket.getTicket_id(), buyer.getUser_id(), seller.getUser_id(),
                LocalDateTime.now(), ticket.getPrice());
        transactionRepository.save(transaction);

        // balance handeling
        buyer.setBalance(buyer.getBalance() - ticket.getPrice());
        seller.setBalance(seller.getBalance() + ticket.getPrice());

        // ticket handeling
        ticket.setFor_sale(false);

        ticket.setSeller_id(buyer.getUser_id());
        ticket.setSerialKey(generateSerialKey());

        userRepository.save(buyer);
        userRepository.save(seller);

    }

    @GetMapping("{eventId}/Tickets/{TicketID}/modify")
    public String modifyMyTicket(@PathVariable int eventId, @PathVariable int TicketID, HttpSession session,
            Model model) {
        User seller = (User) session.getAttribute("loggedInUser");
        if (seller == null) {
            model.addAttribute("error", "No user found");
            return "redirect:/signin";
        }

        Ticket ticket = ticketRepository.findById(TicketID)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if (ticket.getSeller_id() != seller.getUser_id()) {
            model.addAttribute("error", "You are not the owner of this ticket");
            return "redirect:/event/" + eventId + "/Tickets";
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        model.addAttribute("ticket", ticket);
        return "modifyTicket";
    }

    @RequestMapping("/{eventId}/Tickets/{TicketID}/ProcessModify")
    public String processModifyMyTicket(@PathVariable int eventId, @PathVariable int TicketID, HttpSession session,
            HttpServletRequest request, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "No user found!");
            return "redirect:/signin";
        }
        Ticket ticket = ticketRepository.findById(TicketID)
                .orElseThrow(() -> new RuntimeException("No Ticket Found"));
        if (ticket.getSeller_id() != user.getUser_id()) {
            model.addAttribute("error", "You are not authorized to modify this ticket.");
            return "redirect:/event/" + eventId + "/Tickets";
        }
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            ticketRepository.delete(ticket);
            return "redirect:/event/" + eventId + "/Tickets";
        }

        if ("modify".equals(action)) {
            // Modify the ticket
            int price = Integer.parseInt(request.getParameter("price"));
            String description = request.getParameter("description");

            ticket.setPrice(price);
            ticket.setDesc(description);
            ticketRepository.save(ticket);

            return "redirect:/event/" + eventId + "/Tickets";
        }

        throw new IllegalArgumentException("Invalid action: " + action);
    }

   @GetMapping("/myTickets")
public String myTickets(HttpSession session, Model model) {
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/signin";
    }

    List<Ticket> myTickets = ticketRepository.findBySellerId(user.getUser_id());
    Map<Integer,String> ticketEventDetails = new HashMap<>();
    for (Ticket ticket : myTickets) {
        Event event = eventRepository.findById(ticket.getEvent_id())
            .orElseThrow(() -> new RuntimeException("Event not found"));
        ticketEventDetails.put(
            ticket.getTicket_id(),
            event.getEvent_name() + " (Date: " + event.getEvent_date() + ")"
        );
    }

    // ← Add the logged-in user so Thymeleaf can resolve ${loggedInUser}
    model.addAttribute("loggedInUser", user);
    model.addAttribute("myTickets", myTickets);
    model.addAttribute("ticketEventDetails", ticketEventDetails);
    return "myTicketsPage";
}


}
