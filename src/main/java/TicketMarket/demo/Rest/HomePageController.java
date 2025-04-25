package TicketMarket.demo.Rest;

import TicketMarket.demo.DAO.EventRepository;
import TicketMarket.demo.Entity.Event;
import TicketMarket.demo.Entity.User;
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

    @Autowired
    public HomePageController(EventRepository eventRepository) {
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

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroys the session
        return "redirect:/signin"; // Redirect to login/signin page
    }


}
