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
        
        // FIX: Ensure orders is not null before looping
        if (orders == null) {
            orders = new java.util.ArrayList<>(); 
        }
        
        java.util.Map<Long, Product> productMap = new java.util.HashMap<>();
        for(Order o : orders) { // This is where line 57 was crashing
            if(o.getProductid() != null) {
                Product p = productService.getProductById(o.getProductid());
                productMap.put(o.getProductid(), p);
            }
            if (o.getItems() != null && !o.getItems().isEmpty()) {
                for (E_commers.model.OrderItem item : o.getItems()) {
                    if (item.getProductId() != null) {
                        Product p = productService.getProductById(item.getProductId());
                        productMap.put(item.getProductId(), p);
                    }
                }
            }
        }
        
        model.addAttribute("orders", orders);
        model.addAttribute("productMap", productMap);
        
        return "customer-orders";
    }
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/orders/seller"; // Or wherever your admin page is
    }

    @PostMapping("/assign-delivery")
    public String assignDelivery(@RequestParam Long orderId, @RequestParam(required = false) Long deliveryId) {
        orderService.assignDelivery(orderId, deliveryId);
        return "redirect:/orders/seller";
    }

    @PostMapping("/buy")
    public String showBuyPage(@RequestParam("productId") Long productId, Model model) {
        Product p = productService.getProductById(productId);
        model.addAttribute("p", p);
        return "buy-product";
    }

    @PostMapping("/confirm")
    public String buyProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("pinCode") String pinCode,
            @RequestParam("phoneNumber") String phoneNumber, // New parameter
            HttpSession session) {

        String email = (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";
        }

        
        orderService.placeAutomatedOrder(productId, email, address, city, pinCode, phoneNumber);

        return "redirect:/orders/my-orders";
    }

    @PostMapping("/customer/order/update")
    public String customerCancelReturn(@RequestParam Long orderId, @RequestParam String status, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if(email == null) return "redirect:/login";
        Order o = orderService.getOrderById(orderId);
        if (o != null && o.getUser() != null && o.getUser().getEmail().equals(email)) {
            if ("CANCELLED".equals(status) || "RETURNED".equals(status)) {
                orderService.updateOrderStatus(orderId, status);
            }
        }
        return "redirect:/orders/my-orders";
    }
    
}