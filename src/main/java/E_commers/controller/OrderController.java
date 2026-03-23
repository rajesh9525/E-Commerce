package E_commers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import E_commers.model.Order;
import E_commers.service.OrderService;
import E_commers.model.Product;
import E_commers.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/seller")
    public String sellerOrders(Model model) {

        Long sellerId = 1L;

        model.addAttribute("orders", orderService.getSellerOrders(sellerId));

        return "seller_orders";
    }
    @GetMapping("/delivery/orders")
    public String viewDeliveryOrders(Model model) {

        List<Order> orders = orderService.getAssignedOrders();

        model.addAttribute("orders", orders);

        return "delivery-orders";
    }

    @GetMapping("/my-orders")
    public String myOrders(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            return "redirect:/login";
        }
        
        List<Order> orders = orderService.getOrdersByUserEmail(email);
        
        // Pass products so we can show names/images. 
        // Order only saves productId, but let's grab the actual product for easier template rendering.
        java.util.Map<Long, Product> productMap = new java.util.HashMap<>();
        for(Order o : orders) {
            if(o.getProductId() != null) {
                Product p = productService.getProductById(o.getProductId());
                productMap.put(o.getProductId(), p);
            }
        }
        
        model.addAttribute("orders", orders);
        model.addAttribute("productMap", productMap);
        
        return "customer-orders";
    }

    @PostMapping("/buy")
    public String showBuyPage(@RequestParam("productId") Long productId, Model model) {
        Product p = productService.getProductById(productId);
        model.addAttribute("p", p);
        return "buy-product";
    }

    @PostMapping("/confirm-checkout")
    public String checkoutCart(
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("pinCode") String pinCode,
            @RequestParam("phoneNumber") String phoneNumber,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        if (username == null || email == null) {
            return "redirect:/login";
        }

        orderService.checkoutCart(email, username, address, city, pinCode, phoneNumber);

        return "redirect:/orders/my-orders";
    }
    
}