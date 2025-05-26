package TicketMarket.demo.Rest;

import TicketMarket.demo.DAO.UserRepository;
import TicketMarket.demo.Entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignUpController {
    private static UserRepository repository ;
    @Autowired
    public SignUpController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/signupForm")
    public String signupForm(){
        return "signUpForm";
    }

    @RequestMapping(value = "/processform", method = RequestMethod.POST)
    public String processForm(HttpServletRequest http , Model model){
        String userName = http.getParameter("username");
        String firstName = http.getParameter("firstName");
        String lastName = http.getParameter("lastName");
        String userIdNumberStr = http.getParameter("userIdNumber");
        String email = http.getParameter("email");
        String password = http.getParameter("password");
        String verifypassword = http.getParameter("verifypassword");
        String bio = http.getParameter("bio");
        model.addAttribute("username", userName);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("userIdNumber", userIdNumberStr);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("verifypassword", verifypassword);
        model.addAttribute("bio", bio);

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$")) {
        model.addAttribute("error", "Invalid email format. Please enter a valid email like user@example.com");
        return "signUpForm";
    }

        if (repository.isUsernameExist(userName)){
            model.addAttribute("error", "Username already exists. Please choose another.");
            return "signUpForm";
        }
        int userIdNumber;
        try {
            userIdNumber = Integer.parseInt(userIdNumberStr);
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid user ID number. Please enter a valid number.");
            return "signUpForm";
        }
        if (isIsraeliIdNumber(userIdNumberStr) == false){
            model.addAttribute("error", "Invalid Israeli ID number. Please enter a valid number.");
            return "signUpForm";
        }
        if (repository.isUserIdExist(userIdNumberStr)){
            model.addAttribute("error", "ID already exists. Please choose another.");
            return "signUpForm";
        }
        if (!password.equals(verifypassword)){
            model.addAttribute("error", "passwords are not matching");
            return "signUpForm";
        }
        if (repository.isEmailExist(email)){
            model.addAttribute("error" ,"email is already in use" );
            return "signUpForm";
        }
        repository.save(new User(userName , firstName , lastName , userIdNumber , email , password , "none" , "none"));
        return "signUpSuccess";
    }
    @RequestMapping("/signupSuccess")
    public String signupSuccess(){
        return "signUpSuccess";
    }

    public static boolean isIsraeliIdNumber(String id) {
    id = id.trim();
    if (id.length() > 9 || !id.matches("\\d+")) return false;
    // Pad with leading zeros if needed
    while (id.length() < 9) {
        id = "0" + id;
    }
    int sum = 0;
    for (int i = 0; i < 9; i++) {
        int digit = Character.getNumericValue(id.charAt(i));
        int step = digit * ((i % 2) + 1);
        sum += (step > 9) ? step - 9 : step;
    }
    return sum % 10 == 0;
}

}
