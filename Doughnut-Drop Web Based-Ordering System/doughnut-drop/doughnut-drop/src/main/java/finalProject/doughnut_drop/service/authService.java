package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.dto.registerRequestDTO;
import finalProject.doughnut_drop.dto.userProfileResponseDTO;
import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class authService implements UserDetailsService {

    private final userAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public authService(userAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public userProfileResponseDTO registerCustomer(registerRequestDTO req) {
        String cleanUsername = req.username().trim();
        String cleanEmail = req.email().trim().toLowerCase();

        if (userAccountRepository.existByUsername(cleanUsername)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }
        if (userAccountRepository.existByEmail(cleanEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered");
        }

        userAccount account = new userAccount(
                req.fullname().trim(),
                cleanUsername,
                cleanEmail,
                req.mobileNumber().trim(),
                req.defaultDeliveryAddress().trim(),
                passwordEncoder.encode("CUSTOMER_NO_PASSWORD"),
                "ROLE_USER"
        );

        userAccount saved = userAccountRepository.save(account);
        return toProfileDTO(saved);
    }

    public userProfileResponseDTO getByUsername(String username) {
        userAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toProfileDTO(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String cleanRole = account.getRole().replace("ROLE_", "");

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(cleanRole)
                .build();
    }

    private userProfileResponseDTO toProfileDTO(userAccount account) {
        return new userProfileResponseDTO(
                account.getId(),
                account.getFullName(),
                account.getUsername(),
                account.getEmail(),
                account.getMobileNumber(),
                account.getDefaultDeliveryAddress(),
                account.getRole()
        );
    }
}