package finalProject.doughnut_drop.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record orderResponseDTO (
        Long orderId,
        Long customerId,
        String customerName,
        String mobileNumber,
        String fulfillmentType,
        String paymentMethod,
        String deliveryAddress,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<orderItemsResponseDTO> items
){}

