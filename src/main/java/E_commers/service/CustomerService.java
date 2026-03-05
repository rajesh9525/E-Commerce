package E_commers.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Order;
import E_commers.model.Product;
import E_commers.repo.OrderRepository;
import E_commers.repo.ProductRepository;

@Service
public class CustomerService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;


    // ===== VIEW ALL PRODUCTS =====
    public List<Product> getAllProducts(){
        return (List<Product>) productRepository.findAll();
    }

    // ===== SEARCH PRODUCT =====
    public List<Product> searchProduct(String keyword){
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    // ===== BUY PRODUCT =====
    public void buyProduct(Order order){
        orderRepository.save(order);
    }
}

