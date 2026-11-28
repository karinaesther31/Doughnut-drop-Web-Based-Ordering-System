package finalProject.doughnut_drop.repository;

import finalProject.doughnut_drop.entity.DoughnutItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DoughnutRepository extends JpaRepository<DoughnutItem, Long> {
    List<DoughnutItem> findByFlavorNameIgnoreCase(String code);

}