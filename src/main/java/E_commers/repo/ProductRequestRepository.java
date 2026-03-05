package E_commers.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.ProductRequest;

@Repository
public interface ProductRequestRepository extends CrudRepository<ProductRequest, Long> {

	List<ProductRequest> findByStatus(String string);

	List<ProductRequest> findBySellername(String sellername);
	
	
}


