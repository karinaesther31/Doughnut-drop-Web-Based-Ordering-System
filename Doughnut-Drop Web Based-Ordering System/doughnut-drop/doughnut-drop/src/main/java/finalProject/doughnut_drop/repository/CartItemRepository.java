package finalProject.doughnut_drop.repository;

import finalProject.doughnut_drop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem>findByCustomerId(Long customerId);

    Optional<CartItem>findByCustomerIdAndProductId(Long customerId, Long productId);
}

public class CartItemRepository {
}
