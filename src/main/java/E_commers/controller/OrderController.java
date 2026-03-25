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

    @Autowired
    private E_commers.service.UserService userService;

    @GetMapping("/seller")
    public String sellerOrders(Model model, HttpSession session) {

        Long sellerId = (Long) session.getAttribute("userid");
        if (sellerId == null) return "redirect:/login";

        List<Order> orders = orderService.getAllOrders();
        
        java.util.Map<Long, Product> productMap = new java.util.HashMap<>();
        for(Order o : orders) { 
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
        
        // Suppress generic casting warnings by utilizing raw usage locally or strictly casting to support older interfaces.
        List<E_commers.model.User> deliveryPartners = (List<E_commers.model.User>) userService.getUsersByRole("DELIVERY");

        model.addAttribute("orders", orders);
        model.addAttribute("productMap", productMap);
        model.addAttribute("deliveryPartners", deliveryPartners);

        return "seller_orders";
    }
    @GetMapping("/delivery/orders")
    public String viewDeliveryOrders(Model model, HttpSession session) {
        Long deliveryId = (Long) session.getAttribute("userid");
        if (deliveryId == null) return "redirect:/login";

        List<Order> orders = orderService.getOrdersByDeliveryId(deliveryId);

        if (orders == null) orders = new java.util.ArrayList<>();

        java.util.Map<Long, String> productMap = new java.util.HashMap<>();
        java.util.Map<Long, Double> amountMap = new java.util.HashMap<>();
        java.util.Map<Long, Double> priceMap = new java.util.HashMap<>();

        for(Order o : orders) { 
            double total = 0.0;
            StringBuilder pNames = new StringBuilder();
            
            if(o.getProductid() != null) {
                Product p = productService.getProductById(o.getProductid());
                if (p != null) {
                    pNames.append(p.getProductName());
                    total = p.getProductprice() * (o.getQuantity() != null ? o.getQuantity() : 1);
                }
            } else if (o.getItems() != null && !o.getItems().isEmpty()) {
                for (E_commers.model.OrderItem item : o.getItems()) {
                    if (pNames.length() > 0) pNames.append(", ");
                    if (item.getProductName() != null) {
                        pNames.append(item.getProductName());
                    } else if (item.getProductId() != null) {
                        Product p = productService.getProductById(item.getProductId());
                        if (p != null) pNames.append(p.getProductName());
                    }
                    total += item.getPrice() * item.getQuantity();
                }
            }
            productMap.put(o.getId(), pNames.toString());
            priceMap.put(o.getId(), total);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("productMap", productMap);
        model.addAttribute("priceMap", priceMap);

        return "delivery-orders";
    }

    @GetMapping("/my-orders")
    public String myOrders(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            return "redirect:/login";
        }
        
        List<Order> orders = orderService.getOrdersByUserEmail(email);
        
        System.out.println(">>>> [DEBUG] Fetching My-Orders for EMAIL: " + email);
        System.out.println(">>>> [DEBUG] Total Orders Found: " + (orders != null ? orders.size() : "NULL"));
        
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