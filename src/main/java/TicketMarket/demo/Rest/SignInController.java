package TicketMarket.demo.Rest;

import TicketMarket.demo.DAO.UserRepository;
import TicketMarket.demo.Entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignInController {
    private UserRepository repository ;
@Autowired
    public SignInController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/") // Map the root URL to the signin page
    public String homePage() {
        return "signInForm";
    }
    @RequestMapping("/signin")
    public String signIn(){
        return "signInForm";
    }
    @RequestMapping("/processLogin")
    public String processLogin (HttpServletRequest http ,HttpSession session ,  Model model){ // HttpSession session
    String username = http.getParameter("username");
    String password = http.getParameter("password");
    if (repository.isUsernameAndPasswordOK(username , password)){
        User user = repository.findByUsername(username); // Ensure this method exists in your repository
        session.setAttribute("loggedInUser", user); // Store user in session
        model.addAttribute("user", user); // Pass user to the model
        return "redirect:/home";
    }
    else {
        model.addAttribute("error", "Username or password are wrong");
        return "signInForm";
    }
    }
    @RequestMapping("/sucessLogin")
    public String SuccessLogin (){
    return "home";
    }
}
