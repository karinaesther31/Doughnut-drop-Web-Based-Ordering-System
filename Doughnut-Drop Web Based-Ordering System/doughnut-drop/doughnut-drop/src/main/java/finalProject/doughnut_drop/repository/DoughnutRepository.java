package finalProject.doughnut_drop.repository;

import finalProject.doughnut_drop.entity.DoughnutItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoughnutRepository extends JpaRepository<DoughnutItem, Long> {
    Optional<DoughnutItem> findByCodeIgnoreCase(String code);
    List<DoughnutItem> findByGlazeTypeIgnoreCase(String glazeType);
}