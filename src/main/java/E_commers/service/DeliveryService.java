package E_commers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Order;
import E_commers.repo.DeliveryRepository;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

  
    public List<Order> getAllOrders() {
        return deliveryRepository.findAll();
    }


    public Order pickupOrder(Long orderId) {
        Order order = deliveryRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus("PICKED_FROM_SELLER");
            return deliveryRepository.save(order);
        }
        return null;
    }


    public Order deliverOrder(Long orderId) {
        Order order = deliveryRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus("DELIVERED");
            return deliveryRepository.save(order);
        }
        return null;
    }


    public List<Order> getOrdersByStatus(String status) {
        return deliveryRepository.findByStatus(status);
    }

}

