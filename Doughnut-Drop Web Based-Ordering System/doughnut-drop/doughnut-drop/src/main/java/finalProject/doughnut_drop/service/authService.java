package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.dto.registerRequestDTO;
import finalProject.doughnut_drop.dto.userProfileResponseDTO;
import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class authService {

    private final userAccountRepository repository;

    public authService(userAccountRepository repository) {
        this.repository = repository;
    }

    public userProfileResponseDTO registerCustomer(registerRequestDTO req) {
        String normalizedUsername = req.username().trim();
        String normalizedEmail = req.email().trim().toLowerCase();

        if (repository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists: " + normalizedUsername);
        }

        if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + normalizedEmail);
        }

        userAccount account = new userAccount(
                req.fullName().trim(),
                normalizedUsername,
                normalizedEmail,
                req.mobileNumber().trim(),
                req.defaultDeliveryAddress().trim(),
                normalizeFulfillment(req.defaultFulfillmentType()),
                normalizePayment(req.defaultPaymentMethod()),
                "ROLE_USER"
        );

        userAccount savedAccount = repository.save(account);
        return toProfileDTO(savedAccount);
    }

    public userProfileResponseDTO loginCustomerByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        userAccount account = repository.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer account not found"));

        if (!"ROLE_USER".equalsIgnoreCase(account.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Use staff/admin login for this account");
        }

        return toProfileDTO(account);
    }

    private String normalizeFulfillment(String rawValue) {
        String clean = rawValue.trim().toUpperCase();
        if (!clean.equals("PICK_UP") && !clean.equals("DELIVERY")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fulfillment type must be PICK_UP or DELIVERY");
        }
        return clean;
    }

    private String normalizePayment(String rawValue) {
        String clean = rawValue.trim().toUpperCase();
        if (!clean.equals("CASH") && !clean.equals("ONLINE_PAYMENT")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method must be CASH or ONLINE_PAYMENT");
        }
        return clean;
    }

    private userProfileResponseDTO toProfileDTO(userAccount account) {
        return new userProfileResponseDTO(
                account.getId(),
                account.getFullName(),
                account.getUsername(),
                account.getEmail(),
                account.getMobileNumber(),
                account.getDefaultDeliveryAddress(),
                account.getDefaultFulfillmentType(),
                account.getDefaultPaymentMethod(),
                account.getRole()
        );
    }
}