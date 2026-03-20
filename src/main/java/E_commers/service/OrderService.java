package E_commers.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Order;
import E_commers.model.User;
import E_commers.model.Product;
import E_commers.repo.OrderRepository;
import E_commers.repo.UserRepository;
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

	 public List<Order> getSellerOrders(Long sellerId) {
	        return orderRepository.findBySellerId(sellerId);
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

	public void placeAutomatedOrder(Long productId, String customerName, String address, String city, String pinCode) {
		Product product = productRepository.findById(productId).orElse(null);
		if (product != null && product.getStockQuantity() > 0) {
			// Reduce Stock
			product.setStockQuantity(product.getStockQuantity() - 1);
			productRepository.save(product);
			
			// Auto assign delivery
			List<User> deliveryMen = userrepository.findByLogintype("DELIVERY");
			User assignedMan = null;
			if (deliveryMen != null && !deliveryMen.isEmpty()) {
				int randomIndex = new Random().nextInt(deliveryMen.size());
				assignedMan = deliveryMen.get(randomIndex);
			}

			// Create Order
			Order order = new Order();
			order.setProductId(productId);
			order.setCustomerName(customerName);
			order.setAddress(address);
			order.setCity(city);
			order.setPinCode(pinCode);
			order.setQuantity(1);
			order.setStatus("PLACED");
			order.setDeliveryMan(assignedMan);
			
			// Optional: store seller ID if the product's seller is known, although product entity doesn't seem to hold seller ID currently
			
			orderRepository.save(order);
		}
	}
}