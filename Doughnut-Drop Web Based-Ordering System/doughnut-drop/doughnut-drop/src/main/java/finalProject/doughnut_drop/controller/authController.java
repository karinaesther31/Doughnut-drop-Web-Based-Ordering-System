package finalProject.doughnut_drop.controller;

import finalProject.doughnut_drop.dto.registerRequestDTO;
import finalProject.doughnut_drop.dto.userProfileResponseDTO;
import finalProject.doughnut_drop.service.authService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class authController {

    private final authService service;

    public authController(authService service) {
        this.service = service;
    }

    public record CustomerLoginRequestDTO(
            @NotBlank(message = "Username is required")
            String username
    ) {}

    public record StaffLoginRequestDTO(
            @NotBlank(message = "Username is required")
            String username,

            @NotBlank(message = "Password is required")
            String password
    ) {}

    @PostMapping("/register")
    public ResponseEntity<userProfileResponseDTO> registerCustomer(@Valid @RequestBody registerRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerCustomer(req));
    }

    @PostMapping("/login")
    public userProfileResponseDTO loginCustomer(@Valid @RequestBody CustomerLoginRequestDTO req) {
        return service.loginCustomerByUsername(req.username());
    }

    @PostMapping("/staffAdmin-login")
    public userProfileResponseDTO loginStaffOrAdmin(@Valid @RequestBody StaffLoginRequestDTO req) {
        return service.loginStaffOrAdmin(req.username(), req.password());
    }
}