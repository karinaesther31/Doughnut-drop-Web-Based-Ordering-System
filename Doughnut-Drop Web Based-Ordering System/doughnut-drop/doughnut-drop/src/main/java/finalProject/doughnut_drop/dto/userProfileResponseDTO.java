package finalProject.doughnut_drop.dto;

public record userProfileResponseDTO(
        Long id,
        String fullname,
        String username,
        String email,
        String mobileNumber,
        String defaultDeliveryAddress,
        String defaultFulfillmentType,
        String defaultPaymentMethod,
        String role
){}
