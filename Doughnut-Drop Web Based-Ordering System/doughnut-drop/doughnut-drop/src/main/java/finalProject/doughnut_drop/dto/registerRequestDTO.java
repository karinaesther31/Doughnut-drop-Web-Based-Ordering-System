package finalProject.doughnut_drop.dto; // Package ng DTO classes

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record registerRequestDTO(
                                  @NotBlank(message = "Full name is required")
                                  String fullName,

                                  @NotBlank(message = "Username is required")
                                  String username,

                                  @NotBlank(message = "Email is required")
                                  @Email(message = "Invalid email format")
                                  String email,
                                  @NotBlank(message = "Mobile number is required")
                                  String mobileNumber,

                                  @NotBlank(message = "Default delivery address is required")
                                  String defaultDeliveryAddress,

                                  @NotBlank(message = "Default fulfillment type is required")
                                  @Pattern(
                                          regexp = "(?i)^(PICK_UP|DELIVERY)$",
                                          message = "Default fulfillment type must be PICK_UP or DELIVERY"
                                  )
                                  String defaultFulfillmentType,

                                  @NotBlank(message = "Default payment method is required")
                                  @Pattern(
                                          regexp = "(?i)^(CASH|ONLINE_PAYMENT)$",
                                          message = "Default payment method must be CASH or ONLINE_PAYMENT"
                                  )
                                  String defaultPaymentMethod
) {}