package finalProject.doughnut_drop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DoughnutRequestDTO(
        @NotBlank(message = "Code is required") String code,
        @NotBlank(message = "Flavor name is required") String flavorName,
        @NotBlank(message = "Glaze type is required") String glazeType,
        @NotNull @DecimalMin(value = "0.01", message = "Price must be positive") BigDecimal price,
        @NotNull @Min(value = 0, message = "Stock cannot be negative") Integer stockQuantity
) {}