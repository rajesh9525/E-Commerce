package E_commers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import E_commers.model.User;
import E_commers.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    
    @GetMapping("/")
    public String homePage() {
        return "home"; 
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "dashboard";
    }

    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            HttpSession session,
                            Model model) {

        User u = UserService.findByEmail(email);

        if(u != null) {

            session.setAttribute("role", u.getLogintype());

            model.addAttribute("role", u.getLogintype()); // ADD THIS

            return "dashboard";
        }

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; 
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate(); // destroy session

        return "redirect:/login"; // go to login page
    }
   
    @GetMapping("/delivery")
    public String deliverypage() {
    	return"delivery_orders";
    }
    

}
