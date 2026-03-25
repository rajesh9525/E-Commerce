package E_commers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Product;
import E_commers.model.ProductRequest;
import E_commers.model.User;
import E_commers.model.UserActivity;
import E_commers.model.Order;
import E_commers.repo.AdminRepository;
import E_commers.repo.ProductRepository;
import E_commers.repo.UserRepository;
import E_commers.repo.OrderRepository;
import E_commers.repo.ActivityRepository;
import E_commers.repo.ProductRequestRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

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

    // ===== ALL ORDERS =====
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
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

    public void approveProduct(Long id){
        ProductRequest req = productRequestRepository.findById(id).orElseThrow();
        req.setStatus("APPROVED");
        productRequestRepository.save(req);

        // Only copy if not already in the Product table (avoid duplicates)
        List<Product> existing = productRepository.findByStatus("APPROVED");
        boolean alreadyExists = existing.stream()
            .anyMatch(p -> req.getProductname() != null
                        && req.getProductname().equalsIgnoreCase(p.getProductName()));

        if (!alreadyExists) {
            Product product = new Product();
            product.setProductName(req.getProductname());
            product.setProductdetails(req.getProductdetails());
            product.setProductprice(req.getProductprice());
            product.setProductimage(req.getProductimage());
            product.setStatus("APPROVED");
            productRepository.save(product);
        }
    }

    // ===== SYNC all previously-approved requests into Product table =====
    // Called on admin dashboard load to fix any products approved before the fix
    public void syncApprovedProducts(){
        List<ProductRequest> approved = productRequestRepository.findByStatus("APPROVED");
        List<Product> existing = (List<Product>) productRepository.findAll();

        for (ProductRequest req : approved) {
            boolean alreadyExists = existing.stream()
                .anyMatch(p -> req.getProductname() != null
                            && req.getProductname().equalsIgnoreCase(p.getProductName()));
            if (!alreadyExists) {
                Product product = new Product();
                product.setProductName(req.getProductname());
                product.setProductdetails(req.getProductdetails());
                product.setProductprice(req.getProductprice());
                product.setProductimage(req.getProductimage());
                product.setSellername(req.getSellername()); 
                product.setStatus("APPROVED");
                productRepository.save(product);
            }
        }
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
