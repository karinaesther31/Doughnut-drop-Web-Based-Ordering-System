package finalProject.doughnut_drop.controller;
import finalProject.doughnut_drop.dto.registerRequestDTO;
import finalProject.doughnut_drop.dto.userProfileResponseDTO;
import finalProject.doughnut_drop.service.authService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")

public class authController {
    public record CustomerLoginRequestDTO(
            @NotBlank(message = "Username is required") String username
    ) {
    }

    private final authService authService;

    public authController(authService authService) {
        this.authService = authService;

    }

    @PostMapping("/register")
    public ResponseEntity<userProfileResponseDTO> register(@Valid @RequestBody registerRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerCustomer(req));
    }
    @PostMapping("/customer-login")
    public userProfileResponseDTO customerLogin(@Valid @RequestBody CustomerLoginRequestDTO req,
                                                HttpServletRequest request) {
        userProfileResponseDTO profile = authService.getByUsername(req.username().trim());

        if (!"ROLE_USER".equals(profile.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Staff/Admin accounts must sign in with password");
        }

        UserDetails userDetails = authService.loadUserByUsername(profile.username());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        return profile;
    }

    @GetMapping("/me")
    public userProfileResponseDTO me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not signed in");
        }
        return authService.getByUsername(authentication.getName());
    }
}