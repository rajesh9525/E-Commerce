package E_commers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import E_commers.model.Order;
import E_commers.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

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
    @GetMapping("/orders/buy")
    public String showBuyPage() {
        return "buy-product";
    }

    @PostMapping("/orders/buy")
    public String buyProduct(
            @RequestParam String productName,
            @RequestParam int quantity,
            @RequestParam String address) {

        return "redirect:/customer/dashboard";
    }
    
}