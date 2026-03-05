package E_commers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import E_commers.model.Product;
import E_commers.model.ProductRequest;
import E_commers.model.User;
import E_commers.repo.ProductRepository;
import E_commers.repo.ProductRequestRepository;
import E_commers.service.AdminService;
import E_commers.service.ProductService;
import E_commers.service.UserService;
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminservice;

    @Autowired
    private UserService userservice;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productrepository;
    
    @Autowired
    private ProductRequestRepository productrequstrepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        // Registered + Login users
        model.addAttribute("users", userservice.getAllUsers());
        
        List<ProductRequest> pendingProducts =
                productrequstrepository.findByStatus("PENDING");

        model.addAttribute("requests", pendingProducts);

        model.addAttribute("requests", pendingProducts);

        model.addAttribute("requests", productService.getRequests());

        model.addAttribute("activity", adminservice.getAllActivity());

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
    
    @GetMapping("/admin/approve-product/{id}")
    public String approveProduct(@PathVariable Long id) {

        Product p = productrepository.findById(id).orElse(null);

        if (p != null) {
            p.setStatus("APPROVED");
            productrepository.save(p);
        }

        return "redirect:/admin-dashboard";
    }
    @GetMapping("/admin/reject-product/{id}")
    public String rejectProduct(@PathVariable Long id) {

        Product p = productrepository.findById(id).orElse(null);

        if (p != null) {
            p.setStatus("REJECTED");
            productrepository.save(p);
        }

        return "redirect:/admin-dashboard";
    }
    
}