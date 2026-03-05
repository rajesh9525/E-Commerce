package E_commers.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.Admin;
import E_commers.model.Product;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	List<Product> findByStatus(String status);
}

