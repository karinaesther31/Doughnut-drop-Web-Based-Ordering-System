package finalProject.doughnut_drop.repository;
import finalProject.doughnut_drop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product>findByActiveTrue() ;
}


