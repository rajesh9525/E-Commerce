package E_commers.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import E_commers.model.Order;
import E_commers.service.DeliveryService;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        List<Order> orders = deliveryService.getAllOrders();
        model.addAttribute("orders", orders);
        return "delivery_orders";
    }

    @GetMapping("/pickup/{id}")
    public String pickup(@PathVariable Long id) {
        deliveryService.pickupOrder(id);
        return "redirect:/delivery/orders";
    }

    @GetMapping("/deliver/{id}")
    public String deliver(@PathVariable Long id) {
        deliveryService.deliverOrder(id);
        return "redirect:/delivery/orders";
    }


    @GetMapping("/status")
    public String allStatus(Model model) {
        model.addAttribute("orders", deliveryService.getAllOrders());
        return "delivery_status";
    }

    @GetMapping("/status/{status}")
    public String deliveryStatus(@PathVariable String status, Model model) {
        List<Order> orders = deliveryService.getOrdersByStatus(status);
        model.addAttribute("orders", orders);
        return "delivery_status";
    }
}

