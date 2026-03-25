package E_commers.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import E_commers.model.Product;
import E_commers.repo.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getRequests() {
        return productRepository.findByStatus("PENDING");
    }

    public List<Product> getAllProduct() {
        return (List<Product>) productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product p = existingProduct.get();
            p.setProductName(updatedProduct.getProductName());
            p.setProductdetails(updatedProduct.getProductdetails());
            p.setProductprice(updatedProduct.getProductprice());
            p.setAddproductdate(updatedProduct.getAddproductdate());
            p.setProductimage(updatedProduct.getProductimage());
            return productRepository.save(p);
        }

        return null;
    }

    public String deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Product Deleted Successfully";
        }
        return "Product Not Found";
    }

    public void save(Product p) {
        productRepository.save(p); 
    }

    public List<Product> getProductBySeller(String email) {
        return productRepository.findBySellerEmail(email);
    }
}