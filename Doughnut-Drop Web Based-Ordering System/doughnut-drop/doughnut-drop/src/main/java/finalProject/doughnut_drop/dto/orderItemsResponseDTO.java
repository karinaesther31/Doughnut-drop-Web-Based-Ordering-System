package finalProject.doughnut_drop.dto;
 import java.math.BigDecimal;

public record orderItemsResponseDTO (
    Long DoughtnutId,
    String flavorName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
){}

