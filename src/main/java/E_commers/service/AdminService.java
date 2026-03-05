package E_commers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Product;
import E_commers.model.ProductRequest;
import E_commers.model.User;
import E_commers.model.UserActivity;
import E_commers.repo.AdminRepository;
import E_commers.repo.ProductRepository;
import E_commers.repo.UserRepository;
import E_commers.repo.ActivityRepository;
import E_commers.repo.ProductRequestRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRequestRepository productRequestRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ActivityRepository activityRepository;

    // ===== CHECK ADMIN EXISTS =====
    public boolean isAdminPresent(){
        return adminRepository.count() > 0;
    }

    // ===== ALL USERS =====
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // ===== BLOCK USER =====
    public void blockUser(Long id){
        User user = userRepository.findById(id).orElseThrow();
        user.setStatus("BLOCKED");
        userRepository.save(user);
    }

    // ===== DELETE USER =====
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    // ===== PRODUCT REQUEST LIST =====
    public List<ProductRequest> getAllRequests(){
        return (List<ProductRequest>) productRequestRepository.findAll();
    }

    // ===== APPROVE PRODUCT =====
    // When admin approves a request, we:
    // 1. Mark the request as APPROVED in ProductRequest table
    // 2. Copy the product data into the Product table so customers can see it
    public void approveProduct(Long id){
        ProductRequest req = productRequestRepository.findById(id).orElseThrow();
        req.setStatus("APPROVED");
        productRequestRepository.save(req);

        // Copy approved product into the Product table for customer visibility
        Product product = new Product();
        product.setProductName(req.getProductname());
        product.setProductdetails(req.getProductdetails());
        product.setProductprice(req.getProductprice());
        product.setProductimage(req.getProductimage());
        product.setStatus("APPROVED");
        productRepository.save(product);
    }

    // ===== REJECT PRODUCT =====
    public void rejectProduct(Long id){
        ProductRequest req = productRequestRepository.findById(id).orElseThrow();
        req.setStatus("REJECTED");
        productRequestRepository.save(req);
    }

    // ===== USER ACTIVITY =====
    public List<UserActivity> getAllActivity(){
        return activityRepository.findAll();
    }
}

