package finalProject.doughnut_drop.dto; // Package ng mga Data Transfer Object class

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record orderRequestDTO(
                               @NotNull(message = "Customer ID is required")
                               Long customerId,

                               @NotBlank(message = "Fulfillment type is required")
                               @Pattern(
                                       regexp = "(?i)^(PICK_UP|DELIVERY)$",
                                       message = "Fulfillment type must be PICK_UP or DELIVERY"
                               )
                               String fulfillmentType,

                               String deliveryAddress,

                               @NotBlank(message = "Payment method is required")
                               @Pattern(
                                       regexp = "(?i)^(CASH|ONLINE_PAYMENT)$",
                                       message = "Payment method must be CASH or ONLINE_PAYMENT"
                               )
                               String paymentMethod,

                               @NotEmpty(message = "Order must contain at least one item")
                               List<@Valid OrderLineRequestDTO> items
) {}