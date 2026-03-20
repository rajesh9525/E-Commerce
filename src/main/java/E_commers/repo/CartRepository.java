package E_commers.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerName(String customerName);
}
