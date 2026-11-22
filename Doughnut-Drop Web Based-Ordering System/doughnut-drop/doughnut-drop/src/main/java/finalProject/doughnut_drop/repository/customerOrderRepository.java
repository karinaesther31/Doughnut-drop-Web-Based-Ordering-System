package finalProject.doughnut_drop.repository;
import finalProject.doughnut_drop.entity.customerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface customerOrderRepository extends JpaRepository<customerOrder, Long> {
}
