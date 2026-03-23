package E_commers.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	  List<Order> findBySellerId(Long sellerId);
	  List<Order> findByCustomerName(String customerName);

      List<Order> findByUserId(Long userId);
}

