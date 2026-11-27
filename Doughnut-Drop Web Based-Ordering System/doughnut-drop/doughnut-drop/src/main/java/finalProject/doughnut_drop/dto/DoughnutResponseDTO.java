package finalProject.doughnut_drop.dto;

import java.math.BigDecimal;

public record DoughnutResponseDTO(
        Long id,
        String flavorName,
        BigDecimal price,
        Integer stockQuantity,
        String availabilityStatus
) {}