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

    private record StaffAdmin(String username, String password, String role) {
    }

    @Bean
    CommandLineRunner seedDatabase(
            DoughnutRepository doughnutRepository,
            userAccountRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
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

            List<StaffAdmin> privilegeAccounts = List.of(
                    new StaffAdmin("Admin01", "admin1231", "ROLE_ADMIN"),
                    new StaffAdmin("Staff01", "Staff1231", "ROLE_ADMIN")
            );

            for (StaffAdmin seed : privilegeAccounts) {
                boolean exists = userRepository.findAll().stream()
                        .anyMatch(u -> seed.username().equalsIgnoreCase(u.getUsername()));

                if (!exists) {
                    userAccount account = new userAccount();
                    account.setFullName(seed.username());
                    account.setUsername(seed.username());
                    account.setEmail(seed.username().toLowerCase() + "@doughnutdrop.local");
                    account.setMobileNumber("09000000000");
                    account.setDefaultDeliveryAddress("Doughnut Drop Store");
                    account.setDefaultFulfillmentType("PICK_UP");
                    account.setDefaultPaymentMethod("CASH");
                    account.setRole(seed.role());
                    account.setPassword(passwordEncoder.encode(seed.password()));
                    userRepository.save(account);
                }
            }
        };
    }
}