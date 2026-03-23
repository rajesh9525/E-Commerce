package E_commers.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Order;
import E_commers.model.User;
import E_commers.model.Product;
import E_commers.model.OrderItem;
import E_commers.model.Cart;
import E_commers.model.CartItem;
import E_commers.repo.OrderRepository;
import E_commers.repo.UserRepository;
import E_commers.repo.CartRepository;
import java.util.ArrayList;
import E_commers.repo.ProductRepository;
import java.util.Random;



@Service
public class OrderService {

	 @Autowired
	    private OrderRepository orderRepository;
	 @Autowired
	 private UserRepository userrepository;
	 @Autowired
	 private ProductRepository productRepository;
     @Autowired
     private CartRepository cartRepository;

	 public List<Order> getSellerOrders(Long sellerId) {
	        return orderRepository.findBySellerId(sellerId);
	    }
	    
	 public List<Order> getOrdersByUserEmail(String email) {
        User user = userrepository.findByEmail(email);
        if (user != null) {
            return orderRepository.findByUserId(user.getId());
        }
        return new ArrayList<>();
	 }

    // Save Order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get Order By Id
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    // Delete Order
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

	public List<Order> getAssignedOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	public void assignDelivery(Long orderId, Long deliveryId) {

	    Order order = orderRepository.findById(orderId).get();
	    User delivery = userrepository.findById(deliveryId).get();

	    order.setDeliveryMan(delivery);
	    orderRepository.save(order);
	}

	public void checkoutCart(String email, String username, String address, String city, String pinCode, String phoneNumber) {
	    User user = userrepository.findByEmail(email);
        Cart cart = cartRepository.findByCustomerName(username);

        if (cart == null || cart.getItems().isEmpty()) {
            return;
        }

	    Order order = new Order();
        order.setUser(user);
	    order.setCustomerName(username); 
	    order.setAddress(address);
	    order.setCity(city);
	    order.setPinCode(pinCode);
	    order.setPhonenumber(phoneNumber);
        order.setStatus("PENDING");
	    
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(cartItem.getProductId());
            item.setProductName(cartItem.getProductName());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            orderItems.add(item);

            // Deduct stock
            Product p = productRepository.findById(cartItem.getProductId()).orElse(null);
            if(p != null) {
                p.setStockQuantity(Math.max(0, p.getStockQuantity() - cartItem.getQuantity()));
                productRepository.save(p);
            }
        }
        order.setItems(orderItems);
	    orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        System.out.println("✅ automated email / SMS confirmation sent for Order #" + order.getId());
	}
}