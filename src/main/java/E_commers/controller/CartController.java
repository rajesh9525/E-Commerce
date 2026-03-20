package E_commers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import E_commers.service.CartService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/view")
    public String viewCart(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("cart", cartService.getCartByCustomer(username));
        model.addAttribute("total", cartService.getCartTotal(username));
        
        return "customer-cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            cartService.addToCart(username, productId);
        }
        return "redirect:/customer/view-products";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartItemId") Long cartItemId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            cartService.removeFromCart(username, cartItemId);
        }
        return "redirect:/cart/view";
    }
}
