package finalProject.doughnut_drop.Config;

import finalProject.doughnut_drop.entity.DoughnutItem;
import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.DoughnutRepository;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class dataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            DoughnutRepository doughnutRepository,
            userAccountRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return _ -> {
            if (doughnutRepository.count() == 0) {
                BigDecimal boxPrice = new BigDecimal("125.00");
                doughnutRepository.saveAll(List.of(
                        new DoughnutItem("Chocolate", boxPrice, 20),
                        new DoughnutItem("White Chocolate", boxPrice, 20),
                        new DoughnutItem("Strawberry", boxPrice, 20),
                        new DoughnutItem("Ube", boxPrice, 20),
                        new DoughnutItem("Matcha", boxPrice, 20)
                ));
            }

            boolean adminExists = userRepository.findAll().stream()
                    .anyMatch(u -> "admin".equalsIgnoreCase(u.getUsername()));

            if (!adminExists) {
                userAccount admin = new userAccount();
                admin.setFullName("Doughnut Drop Staff/Admin");
                admin.setUsername("admin");
                admin.setEmail("admin@doughnutdrop.local");
                admin.setMobileNumber("09000000000");
                admin.setDefaultDeliveryAddress("Doughnut Drop Store");
                admin.setDefaultFulfillmentType("PICK_UP");
                admin.setDefaultPaymentMethod("CASH");
                admin.setRole("ROLE_ADMIN");
                admin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(admin);
            }
        };
    }
}