package finalProject.doughnut_drop.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record orderResponseDTO (
        Long orderId,
        String customerName,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<orderItemsResponseDTO> items
){}

