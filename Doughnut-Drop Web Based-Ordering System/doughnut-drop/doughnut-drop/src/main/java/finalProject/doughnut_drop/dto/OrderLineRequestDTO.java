package finalProject.doughnut_drop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderLineRequestDTO(
        @NotNull(message = "Doughnut ID is required")
        Long doughnutId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1 box")
        Integer quantity
) {}