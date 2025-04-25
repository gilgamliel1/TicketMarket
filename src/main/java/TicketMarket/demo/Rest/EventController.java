package TicketMarket.demo.Rest;

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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/event")
@Controller
public class EventController {
    private EventRepository eventRepository ;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository ;
    @Autowired
    public EventController(EventRepository eventRepository, TicketRepository ticketRepository , UserRepository userRepository , TransactionRepository transactionRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository  = userRepository ;
        this.transactionRepository = transactionRepository ;
    }


    @GetMapping("/createEvent")
    public String createEvent(HttpSession session){
        if (session.getAttribute("loggedInUser") == null ) return "redirect:/signin";
        return "createEventForm";
    }
    @RequestMapping(value = "/processEvent" , method = RequestMethod.POST)
    public String processEvent(HttpServletRequest http ,  HttpSession session , Model model){
        if (session.getAttribute("loggedInUser") == null ) return "redirect:/signin";
        String name = http.getParameter("event_name");
        LocalDateTime event_time = LocalDateTime.parse(http.getParameter("event_date"));
        String eventLoc = http.getParameter("eventLoc");
        String event_desc = http.getParameter("event_desc");
        String tag = http.getParameter("tag");
        User temp = (User) session.getAttribute("loggedInUser");
        if (eventRepository.isEventExist(name))
        {
            model.addAttribute("error" , "Event name alreday exist! ");
            return "createEventForm";
        }
        eventRepository.save(new Event(name , event_time , eventLoc , event_desc ,temp.getUser_name() , tag));
        return "eventCreatedSuccess";
    }
    @RequestMapping("/success_event_created")
    public String success_event_created(HttpSession session){
        if (session.getAttribute("loggedInUser") == null ) return "redirect:/signin";
        return "eventCreatedSuccess";
    }

    @GetMapping("/{id}")
    public String eventPage(@PathVariable int id ,HttpSession session , Model model){
        if (session.getAttribute("loggedInUser") == null ) return "redirect:/signin";
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);

        return "eventPage";
    }
    @GetMapping("/{id}/Tickets")
    public String eventTickets(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/signin";

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

        return "eventTicketsPage";
    }
    @GetMapping("/{id}/Tickets/newTicket")
    public String newEventTicket(@PathVariable int id  ,Model model){
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event" , event);
        return "newEventTicketForm";
    }
    @RequestMapping(value = "/{id}/Tickets/processNewTicket", method = RequestMethod.POST)
    public String processNewEventTicket(@PathVariable int id, HttpServletRequest http, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "No User Found!");
            return "redirect:/signin";
        }
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);

        // Get and validate the price
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

        // Get other parameters
        String description = http.getParameter("description");
        String serialKey = http.getParameter("serialKey");

        // Validate serial key
        if (!verifyTicket(serialKey)) {
            model.addAttribute("error", "Serial key is not valid.");
            return "newEventTicketForm";
        }
        Ticket temp = new Ticket(id , user.getUser_id(),  price , description , "available" , serialKey);
        // Save the new ticket
        ticketRepository.save(temp);

        // Redirect to success page
        return "ticketCreateSuccess";
    }
    public static boolean verifyTicket(String serialKey){
        return true ;
    }
    @GetMapping("/{eventId}/Tickets/{TicketId}")
    public String buyEventTicket(@PathVariable int eventId , @PathVariable int TicketId , HttpSession session  , Model model){
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "No User Found!");
            return "redirect:/signin";
        }
        Ticket ticket = ticketRepository.findById(TicketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("ticket" , ticket);
        model.addAttribute("event" , event);
        model.addAttribute("loggedInUser" , user);
        return "buyTicketPage";
    }
    @RequestMapping("/{eventId}/Tickets/{TicketId}/processBuyingTicket")
    public String processBuyingTicket(@PathVariable int eventId, @PathVariable int TicketId, HttpSession session, Model model) {
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

        processBuyingTicket(buyer, seller, ticket, event);

        model.addAttribute("ticket", ticket);
        model.addAttribute("event", event);
        return "successBuyingTicketPage";
    }

    public void processBuyingTicket(User buyer , User seller , Ticket ticket , Event event ){
        Transaction transaction = new Transaction(ticket.getTicket_id() , buyer.getUser_id() , seller.getUser_id(),  LocalDateTime.now() , ticket.getPrice());
        buyer.setBalance(buyer.getBalance() - ticket.getPrice());
        seller.setBalance(seller.getBalance() + ticket.getPrice());
        ticket.setStatus("sold");
        transactionRepository.save(transaction);
        ticketRepository.save(ticket);
        userRepository.save(buyer);
        userRepository.save(seller);

    }
    @GetMapping("{eventId}/Tickets/{TicketID}/modify")
    public String modifyMyTicket(@PathVariable int eventId, @PathVariable int TicketID, HttpSession session, Model model) {
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
    public String processModifyMyTicket(@PathVariable int eventId, @PathVariable int TicketID, HttpSession session, HttpServletRequest request, Model model) {
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





}

