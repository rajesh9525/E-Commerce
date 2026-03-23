package E_commers.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import E_commers.model.Order;
import E_commers.model.Product;
import java.util.List;
import java.util.stream.Collectors;
import E_commers.service.CustomerService;
import E_commers.service.OrderService;
import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private OrderService orderservice;
    
    @GetMapping("/dashboard")
    public String customerDashboard() {
        return "customer-dashboard";  // create this HTML
    }

    @GetMapping("/view-products")
    public String viewProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minRating,
            Model model, HttpSession session) {
            
        session.setAttribute("role", "CUSTOMER");
        List<Product> products = customerService.getAllProducts();
        
        if (category != null && !category.isEmpty()) {
            products = products.stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .collect(Collectors.toList());
        }
        if (minRating != null) {
            products = products.stream()
                .filter(p -> p.getRating() >= minRating)
                .collect(Collectors.toList());
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", List.of("Electronics", "Clothing", "Home", "Sports", "Uncategorized"));
        return "customer-dashboard";
    }
    
//   @GetMapping("/order/buy")
//    public String buyProduct(@ModelAttribute Order order) {
//        customerService.buyProduct(order);
//        return "buy-product";
//    }
   
   
}

