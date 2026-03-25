package E_commers.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import E_commers.model.Order;
import E_commers.model.Product;
import E_commers.repo.OrderRepository;
import E_commers.service.OrderService;
import E_commers.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderRepository orderrepository;

    @GetMapping("/orders")
    public String viewOrders(Model model, HttpSession session) {

        // Print ALL session attributes
        Long deliveryId = (Long) session.getAttribute("userid");
        String username = (String) session.getAttribute("username");
        
        System.out.println(">>> SESSION userid = " + deliveryId);
        System.out.println(">>> SESSION username = " + username);

        // Print ALL orders and their deliveryid
        List<Order> allOrders = orderService.getAllOrders();
        for (Order o : allOrders) {
            System.out.println(">>> Order #" + o.getId() 
                + " | deliveryid=" + o.getDeliveryid() 
                + " | deliveryMan=" + (o.getDeliveryMan() != null ? o.getDeliveryMan().getId() : "null"));
        }

        List<Order> orders = orderService.getOrdersByDeliveryId(deliveryId);
        System.out.println(">>> FILTERED ORDERS COUNT: " + orders.size());

        model.addAttribute("orders", orders);
        model.addAttribute("productMap", new java.util.HashMap<>());
        model.addAttribute("priceMap", new java.util.HashMap<>());
        return "delivery_orders";
    }

    @GetMapping("/status")
    public String allStatus(Model model, HttpSession session) {
        Long deliveryId = (Long) session.getAttribute("userid");
        List<Order> orders = orderService.getOrdersByDeliveryId(deliveryId);

        Map<Long, String> productMap = new HashMap<>();
        for (Order o : orders) {
            if (o.getProductid() != null) {
                Product p = productService.getProductById(o.getProductid());
                if (p != null) productMap.put(o.getProductid(), p.getProductName());
            }
        }

        model.addAttribute("orders", orders);
        model.addAttribute("productMap", productMap);
        return "delivery_orders";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/delivery/orders";
    }
    @GetMapping("/delivery/dashboard")
    public String deliveryDashboard(Model model, HttpSession session) {

        String email = (String) session.getAttribute("email");

        List<Order> orders = orderrepository.findByDeliveryid(email);

        model.addAttribute("orders", orders);

        return "delivery-dashboard";
    }
}