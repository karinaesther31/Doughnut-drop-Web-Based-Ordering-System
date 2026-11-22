package finalProject.doughnut_drop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record orderRequestDTO (
    @NotBlank(message = "Customers Name is required") String customerName,
    @NotBlank(message = "Order items cannot be empty") List<OrderLineRequestDTO> items
) {}


