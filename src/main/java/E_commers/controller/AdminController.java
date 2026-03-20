package E_commers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import E_commers.model.ProductRequest;
import E_commers.model.User;
import E_commers.repo.ProductRequestRepository;
import E_commers.service.AdminService;
import E_commers.service.UserService;
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminservice;

    @Autowired
    private UserService userservice;

    @Autowired
    private ProductRequestRepository productrequstrepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        // Sync any previously-approved products to the Product table
        adminservice.syncApprovedProducts();

        // Registered + Login users
        model.addAttribute("users", userservice.getAllUsers());

        List<ProductRequest> pendingProducts =
                productrequstrepository.findByStatus("PENDING");

        model.addAttribute("requests", pendingProducts);

        model.addAttribute("activity", adminservice.getAllActivity());
        
        model.addAttribute("orders", adminservice.getAllOrders());

        return "admin-dashboard";
    }

    @GetMapping("/product-requests")
    public String productRequests(Model model){
        model.addAttribute("requests", adminservice.getAllRequests());
        return "admin/product-approvall";
    }

    @GetMapping("/activity")
    public String activity(Model model){
        model.addAttribute("activity", adminservice.getAllActivity());
        return "admin/activity";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        userservice.deleteUserById(id);

        return "redirect:/admin/dashboard";
    }
    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable Long id) {

        User user = userservice.getUserById(id);
        user.setStatus("BLOCKED");
        userservice.saveUser(user);

        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/approve-product/{id}")
    public String approveProduct(@PathVariable Long id) {
        adminservice.approveProduct(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/reject-product/{id}")
    public String rejectProduct(@PathVariable Long id) {
        adminservice.rejectProduct(id);
        return "redirect:/admin/dashboard";
    }
    
}