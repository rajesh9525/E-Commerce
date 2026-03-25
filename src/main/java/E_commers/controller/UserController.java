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

        // Record registration activity
        userService.recordActivity(user.getName(), "Registered as " + user.getLogintype());

        return "redirect:/login";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session) {

        User u = userService.findByEmail1(email);

        // ← ADD THIS NULL CHECK
        if (u == null) {
            return "redirect:/login?error=notfound";
        }

        if (u.getPassword() != null && org.mindrot.jbcrypt.BCrypt.checkpw(password, u.getPassword())) {

            session.setAttribute("role", u.getLogintype());
            session.setAttribute("username", u.getName());
            session.setAttribute("email", u.getEmail());
            session.setAttribute("userid", u.getId()); // ← make sure this is here

            userService.recordActivity(u.getName(), "Logged in as " + u.getLogintype());

            if (u.getLogintype().equals("ADMIN"))    return "redirect:/admin/dashboard";
            if (u.getLogintype().equals("SELLER"))   return "redirect:/sellerdashboard";
            if (u.getLogintype().equals("DELIVERY")) return "redirect:/delivery/orders";
            if (u.getLogintype().equals("CUSTOMER")) return "redirect:/dashboard";
        }
    
            // Record login activity
            userService.recordActivity(u.getName(), "Logged in as " + u.getLogintype());

            // ===== ADMIN =====
            if(u.getLogintype().equals("ADMIN")){
                return "redirect:/admin/dashboard";
            }

            // ===== SELLER =====
            if(u.getLogintype().equals("SELLER")){
                return "redirect:/sellerdashboard";
            }

            // ===== DELIVERY =====
            if(u.getLogintype().equals("DELIVERY")){
                return "redirect:/dashboard";
            }

            // ===== CUSTOMER =====
            if(u.getLogintype().equals("CUSTOMER")){
                return "redirect:/dashboard";
            }
        

         return "redirect:/login?error=wrongpassword";
    }
    
    

}
