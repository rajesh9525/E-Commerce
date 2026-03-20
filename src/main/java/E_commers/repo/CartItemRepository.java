package E_commers.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
