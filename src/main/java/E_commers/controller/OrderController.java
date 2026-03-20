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
    @PostMapping("/buy")
    public String showBuyPage(@RequestParam("productId") Long productId, Model model) {
        Product p = productService.getProductById(productId);
        model.addAttribute("p", p);
        return "buy-product";
    }

    @PostMapping("/confirm")
    public String buyProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("customerName") String customerName,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("pinCode") String pinCode) {

        orderService.placeAutomatedOrder(productId, customerName, address, city, pinCode);

        return "redirect:/customer/dashboard";
    }
    
}