package E_commers.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 ADD THIS

import E_commers.model.Cart;
import E_commers.model.CartItem;
import E_commers.model.Product;
import E_commers.repo.CartItemRepository;
import E_commers.repo.CartRepository;
import E_commers.repo.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional  // 👈 FIX HERE
    public Cart getCartByCustomer(String customerName) {
        Cart cart = cartRepository.findByCustomerName(customerName);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerName(customerName);
            cart = cartRepository.save(cart);
        }

        // Load product data
        for (CartItem item : cart.getItems()) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            item.setProduct(p);
        }

        return cart;
    }

    @Transactional  // 👈 ADD THIS ALSO
    public void addToCart(String customerName, Long productId) {
        Cart cart = getCartByCustomer(customerName);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + 1);
                cartItemRepository.save(item);
            } else {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProductId(product.getId());
                newItem.setProductName(product.getProductName());
                newItem.setPrice(product.getProductprice());
                newItem.setQuantity(1);
                cartItemRepository.save(newItem);
            }
        }
    }

    @Transactional  // 👈 ADD
    public void removeFromCart(String customerName, Long cartItemId) {
        Cart cart = getCartByCustomer(customerName);
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
    }

    @Transactional  // 👈 ADD
    public void clearCart(String customerName) {
        Cart cart = getCartByCustomer(customerName);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)  // 👈 OPTIONAL
    public double getCartTotal(String customerName) {
        Cart cart = getCartByCustomer(customerName);
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}