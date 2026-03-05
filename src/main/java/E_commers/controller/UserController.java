package E_commers.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import E_commers.model.User;
import E_commers.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {

        user.setLogindate(LocalDate.now());

        userService.saveUser(user);

        return "redirect:/login";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            HttpSession session) {

        User u = userService.findByEmail1(email);

        if(u != null) {

            session.setAttribute("role", u.getLogintype());

            // ===== ADMIN =====
            if(u.getLogintype().equals("ADMIN")){
                return "redirect:/dashboard";
            }

            // ===== SELLER =====
            if(u.getLogintype().equals("SELLER")){
                return "redirect:/dashboard";
            }

            // ===== DELIVERY =====
            if(u.getLogintype().equals("DELIVERY")){
                return "redirect:/dashboard";
            }

            // ===== CUSTOMER =====
            if(u.getLogintype().equals("CUSTOMER")){
                return "redirect:/dashboard";
            }
        }

        return "redirect:/login";
    }
    
    

}
