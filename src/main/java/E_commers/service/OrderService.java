package E_commers.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Order;
import E_commers.model.User;
import E_commers.repo.OrderRepository;
import E_commers.repo.UserRepository;



@Service
public class OrderService {

	 @Autowired
	    private OrderRepository orderRepository;
	 @Autowired
	 private UserRepository userrepository;

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

	public void placeOrder(String productName, int quantity, String address) {
		// TODO Auto-generated method stub
		
	}
}