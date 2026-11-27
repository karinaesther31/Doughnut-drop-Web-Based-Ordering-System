package finalProject.doughnut_drop.repository;
import finalProject.doughnut_drop.entity.customerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface  customerOrderRepository
        extends JpaRepository<customerOrder, Long> {

    List<customerOrder> findByCustomerIdAndActiveTrue(Long customerId);
    List<customerOrder> findByCustomerId(Long customerId);
}





