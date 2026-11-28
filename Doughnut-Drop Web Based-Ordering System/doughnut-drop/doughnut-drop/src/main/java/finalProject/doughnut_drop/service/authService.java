package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.dto.registerRequestDTO;
import finalProject.doughnut_drop.dto.userProfileResponseDTO;
import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class authService {

    private final userAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public authService(userAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public userProfileResponseDTO registerCustomer(registerRequestDTO req) {
        String cleanUsername = req.username().trim();
        String cleanEmail = req.email().trim().toLowerCase();

        boolean usernameTaken = userRepository.findAll().stream()
                .anyMatch(u -> cleanUsername.equalsIgnoreCase(u.getUsername()));
        if (usernameTaken) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        boolean emailTaken = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail() != null && cleanEmail.equalsIgnoreCase(u.getEmail()));
        if (emailTaken) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        String fulfillment = req.defaultFulfillmentType().trim().toUpperCase();
        String payment = req.defaultPaymentMethod().trim().toUpperCase();
        String defaultAddress = (req.defaultDeliveryAddress() == null || req.defaultDeliveryAddress().isBlank())
                ? "Not set"
                : req.defaultDeliveryAddress().trim();

        userAccount account = new userAccount();
        account.setFullName(req.fullName().trim());
        account.setUsername(cleanUsername);
        account.setEmail(cleanEmail);
        account.setMobileNumber(req.mobileNumber().trim());
        account.setDefaultDeliveryAddress(defaultAddress);
        account.setDefaultFulfillmentType(fulfillment);
        account.setDefaultPaymentMethod(payment);
        account.setRole("ROLE_USER");
        account.setPassword(null);

        userAccount saved = userRepository.save(account);
        return toProfileDTO(saved);
    }

    public userProfileResponseDTO loginCustomerByUsername(String username) {
        String cleanUsername = username.trim();

        userAccount account = userRepository.findAll().stream()
                .filter(u -> cleanUsername.equalsIgnoreCase(u.getUsername()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer account not found"));

        if (!"ROLE_USER".equalsIgnoreCase(account.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please use staff login for staff or admin accounts");
        }

        return toProfileDTO(account);
    }

    public userProfileResponseDTO loginStaffOrAdmin(String username, String password) {
        String cleanUsername = username.trim();

        userAccount account = userRepository.findAll().stream()
                .filter(u -> cleanUsername.equalsIgnoreCase(u.getUsername()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        boolean isAdminOrStaff = "ROLE_ADMIN".equalsIgnoreCase(account.getRole())
                || "ROLE_STAFF".equalsIgnoreCase(account.getRole());

        if (!isAdminOrStaff || account.getPassword() == null || !passwordEncoder.matches(password, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return toProfileDTO(account);
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