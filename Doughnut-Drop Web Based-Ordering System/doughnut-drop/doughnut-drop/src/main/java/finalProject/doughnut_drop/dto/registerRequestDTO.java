package finalProject.doughnut_drop.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record registerRequestDTO (
   @NotBlank(message = "Full name is required") String fullname,
   @NotBlank(message = "Username is required") String username,
   @NotBlank(message = "Email is required")
   @Email(message = "Invalid email format") String email,
   @NotBlank(message = "Mobile number is required") String mobileNumber,
   @NotBlank(message = "Default delivery address is required") String defaultDeliveryAddress,
   @NotBlank(message = "Password is required") String password
){}
