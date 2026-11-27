package finalProject.doughnut_drop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record DoughnutRequestDTO(
        @NotBlank(message = "Flavor name is required")
        @Pattern(
                regexp = "(Chocolate|White Chocolate|Strawberry|Ube|Matcha)",
                message = "Flavor must be Chocolate, White Chocolate, Strawberry, Ube, or Matcha"
        )
        String flavorName,

        @NotNull(message = "Prices is required")
        @DecimalMin(value = "125.00", inclusive = true, message = "Prices must be 125.00 per order (1 box)")
        BigDecimal price,
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity
){}

