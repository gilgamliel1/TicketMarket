package TicketMarket.demo.Rest;

import TicketMarket.demo.DAO.EventRepository;
import TicketMarket.demo.Entity.Event;
import TicketMarket.demo.Entity.User;
import TicketMarket.demo.DAO.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomePageController {
    private final EventRepository eventRepository;
        private UserRepository userRepository;


    @Autowired
    public HomePageController(EventRepository eventRepository , UserRepository userRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @RequestMapping("/home")
    public String homePage(
            @RequestParam(required = false) String tag,
            HttpSession session,
            Model model) {

        // Ensure the user is logged in
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/signin";
        }
        model.addAttribute("user", user);

        // Handle event filtering by tag
        List<Event> events;
        if (tag != null && tag.equals(session.getAttribute("currentTag"))) {
            session.removeAttribute("currentTag"); // Clear current tag
            events = eventRepository.findAllEvents(); // Show all events
            model.addAttribute("selectedTag", ""); // No tag selected
        } else {
            session.setAttribute("currentTag", tag); // Set current tag
            events = tag != null ? eventRepository.findByTag(tag) : eventRepository.findAllEvents();
            model.addAttribute("selectedTag", tag);
        }
        model.addAttribute("events", events);
        return "home";
    }

    @RequestMapping("/deposit")
    public String depositPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/signin";
        }
        model.addAttribute("user", user);
        return "deposit";
    }

    @RequestMapping(value = "/deposit/process", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public String processDeposit(HttpSession session, Model model, @RequestParam("amount") int amount) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/signin";
        }
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive.");
            model.addAttribute("user", user);
            return "deposit";
        }
        if (user.getBalance() + amount > 100_000) {
            model.addAttribute("error", "Balance cannot exceed 100,000.");
            model.addAttribute("user", user);
            return "deposit";
        }
        user.setBalance(user.getBalance() + amount);
        model.addAttribute("user", user);
        session.setAttribute("loggedInUser", user);
        // If you have a UserRepository, save the user:
        userRepository.save(user);
        return "redirect:/home";
    }

    @RequestMapping("/withdraw")
    public String withdrawPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/signin";
        }
        model.addAttribute("user", user);
        return "withdraw";
    }

    @RequestMapping(value = "/withdraw/process", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public String processWithdraw(HttpSession session, Model model, @RequestParam("amount") int amount) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/signin";
        }
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive.");
            model.addAttribute("user", user);
            return "withdraw";
        }
        if (user.getBalance() < amount) {
            model.addAttribute("error", "Insufficient balance.");
            model.addAttribute("user", user);
            return "withdraw";
        }
        user.setBalance(user.getBalance() - amount);
        model.addAttribute("user", user);
        session.setAttribute("loggedInUser", user);
        // If you have a UserRepository, save the user:
        userRepository.save(user);
        return "redirect:/home";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroys the session
        return "redirect:/signin"; // Redirect to login/signin page
    }
}
