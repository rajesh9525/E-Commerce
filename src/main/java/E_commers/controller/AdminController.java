package E_commers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import E_commers.model.Order;
import E_commers.model.Product;
import E_commers.model.ProductRequest;
import E_commers.model.User;
import E_commers.repo.ProductRequestRepository;
import E_commers.service.AdminService;
import E_commers.service.OrderService;
import E_commers.service.UserService;
import E_commers.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminservice;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRequestRepository productrequestrepository;

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Order> orders = orderService.getAllOrders();
        List<User> delivery = userService.getUsersByRole("DELIVERY");

        System.out.println(">>> DELIVERY USERS FOUND: " + delivery.size());

        if (users == null) users = new ArrayList<>();
        if (orders == null) orders = new ArrayList<>();
        if (delivery == null) delivery = new ArrayList<>();
        
        java.util.Map<Long, Product> productMap = new java.util.HashMap<>();
        java.util.Map<Long, Double> amountMap = new java.util.HashMap<>();
        
        for(Order o : orders) { 
            double total = 0.0;
            if(o.getProductid() != null) {
                Product p = productService.getProductById(o.getProductid());
                if (p != null) {
                    productMap.put(o.getProductid(), p);
                    total = p.getProductprice() * (o.getQuantity() != null ? o.getQuantity() : 1);
                }
            } else if (o.getItems() != null && !o.getItems().isEmpty()) {
                for (E_commers.model.OrderItem item : o.getItems()) {
                    if (item.getProductId() != null) {
                        Product p = productService.getProductById(item.getProductId());
                        if (p!=null) productMap.put(item.getProductId(), p);
                    }
                    total += item.getPrice() * item.getQuantity();
                }
            }
            amountMap.put(o.getId(), total);
        }

        model.addAttribute("productMap", productMap);
        model.addAttribute("amountMap", amountMap);
        model.addAttribute("users", users);
        model.addAttribute("orders", orders);
        model.addAttribute("delivery", delivery);
        model.addAttribute("requests", adminservice.getAllRequests());
        model.addAttribute("activity", adminservice.getAllActivity());

        return "admin-dashboard";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/block/{id}")
    public String blockUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setStatus("BLOCKED");
        userService.saveUser(user);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/update-status")
    public String updateOrderStatus(Long orderId, String status) {
        Order order = orderService.getOrderById(orderId);
        order.setStatus(status);
        orderService.save(order);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/assign-delivery")
    public String assignDelivery(Long orderId, Long deliveryId) {
        Order order = orderService.getOrderById(orderId);
        order.setDeliveryid(deliveryId);
        orderService.save(order);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/approve-product/{id}")
    public String approveProduct(@PathVariable Long id) {
        ProductRequest req = productrequestrepository.findById(id).orElse(null);
        if (req != null) {
            req.setStatus("APPROVED");
            productrequestrepository.save(req);

            Product p = new Product();
            p.setProductName(req.getProductname());
            p.setProductdetails(req.getProductdetails());
            p.setProductprice(req.getProductprice());
            p.setProductimage(req.getProductimage());
            p.setSellername(req.getSellername());
            p.setSellerEmail(req.getSellerEmail());
            p.setStatus("APPROVED");
            p.setAddproductdate(LocalDate.now());
            productService.save(p);  // ✅ use injected service
            
            
        }
        return "redirect:/admin/dashboard";
    }
    
    
}