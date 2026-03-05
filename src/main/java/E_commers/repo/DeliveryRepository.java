package E_commers.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.Order;


@Repository
public interface DeliveryRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(String status);

}