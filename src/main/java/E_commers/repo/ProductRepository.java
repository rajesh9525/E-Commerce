package E_commers.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.Product;
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	
	List<Product> findByStatus(String status); 
		
	  List<Product> findBySellername(String sellername); 
	  
	  List<Product> findBySellerEmailAndStatus(String email, String status);
	  
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

	List<Product> findBySellernameAndStatus(String sellername, String string);

	List<Product> findBySellerEmail(String email);

	
}
