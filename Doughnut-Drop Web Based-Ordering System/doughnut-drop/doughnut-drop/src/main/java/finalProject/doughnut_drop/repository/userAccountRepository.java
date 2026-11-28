package finalProject.doughnut_drop.repository;

import finalProject.doughnut_drop.entity.userAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface userAccountRepository extends JpaRepository<userAccount, Long> {
    Optional<userAccount> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);
}