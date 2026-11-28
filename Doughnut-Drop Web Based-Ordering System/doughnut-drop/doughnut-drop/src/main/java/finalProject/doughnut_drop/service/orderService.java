package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.dto.OrderLineRequestDTO;
import finalProject.doughnut_drop.dto.orderItemsResponseDTO;
import finalProject.doughnut_drop.dto.orderRequestDTO;
import finalProject.doughnut_drop.dto.orderResponseDTO;
import finalProject.doughnut_drop.entity.DoughnutItem;
import finalProject.doughnut_drop.entity.customerOrder;
import finalProject.doughnut_drop.entity.orderItem;
import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.DoughnutRepository;
import finalProject.doughnut_drop.repository.customerOrderRepository;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class orderService {

    public static final BigDecimal BOX_PRICE = new BigDecimal("125.00");

    private final customerOrderRepository orderRepository;
    private final DoughnutRepository doughnutRepository;
    private final userAccountRepository userRepository;

    public orderService(
            customerOrderRepository orderRepository,
            DoughnutRepository doughnutRepository,
            userAccountRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.doughnutRepository = doughnutRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public orderResponseDTO previewOrder(orderRequestDTO req) {
        userAccount customer = findCustomer(req.customerId());
        String fulfillment = normalizeFulfillment(req.fulfillmentType());
        String payment = normalizePayment(req.paymentMethod());
        String resolvedAddress = resolveDeliveryAddress(fulfillment, req.deliveryAddress(), customer);

        Map<Long, Integer> mergedQuantities = mergeCartItems(req.items());
        List<orderItemsResponseDTO> previewItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : mergedQuantities.entrySet()) {
            DoughnutItem doughnut = doughnutRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + entry.getKey()));

            int qty = entry.getValue();
            if (doughnut.getStockQuantity() < qty) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Insufficient stock for " + doughnut.getFlavorName() + " (available: " + doughnut.getStockQuantity() + ")"
                );
            }

            BigDecimal lineTotal = BOX_PRICE.multiply(BigDecimal.valueOf(qty));
            totalAmount = totalAmount.add(lineTotal);

            previewItems.add(new orderItemsResponseDTO(
                    doughnut.getId(),
                    doughnut.getFlavorName(),
                    qty,
                    BOX_PRICE,
                    lineTotal
            ));
        }

        return new orderResponseDTO(
                null,
                customer.getId(),
                customer.getFullName(),
                customer.getMobileNumber(),
                fulfillment,
                resolvedAddress,
                payment,
                "PREVIEW",
                totalAmount,
                LocalDateTime.now(),
                previewItems
        );
    }

    @Transactional
    public orderResponseDTO placeOrder(orderRequestDTO req) {
        userAccount customer = findCustomer(req.customerId());
        String fulfillment = normalizeFulfillment(req.fulfillmentType());
        String payment = normalizePayment(req.paymentMethod());
        String confirmedAddress = resolveDeliveryAddress(fulfillment, req.deliveryAddress(), customer);

        Map<Long, Integer> mergedQuantities = mergeCartItems(req.items());

        customerOrder order = new customerOrder();
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getFullName());
        order.setMobileNumber(customer.getMobileNumber());
        order.setFulfillmentType(fulfillment);
        order.setDeliveryAddress(confirmedAddress);
        order.setPaymentMethod(payment);
        order.setStatus("CONFIRMED");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : mergedQuantities.entrySet()) {
            DoughnutItem doughnut = doughnutRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + entry.getKey()));

            int qty = entry.getValue();
            if (doughnut.getStockQuantity() < qty) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Insufficient stock for " + doughnut.getFlavorName() + " (available: " + doughnut.getStockQuantity() + ")"
                );
            }

            doughnut.setStockQuantity(doughnut.getStockQuantity() - qty);
            doughnutRepository.save(doughnut);

            BigDecimal lineTotal = BOX_PRICE.multiply(BigDecimal.valueOf(qty));
            totalAmount = totalAmount.add(lineTotal);

            orderItem item = new orderItem(
                    doughnut.getId(),
                    doughnut.getFlavorName(),
                    qty,
                    BOX_PRICE,
                    lineTotal
            );
            order.addItem(item);
        }

        order.setTotalAmount(totalAmount);
        customerOrder savedOrder = orderRepository.save(order);
        return toDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<orderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public orderResponseDTO getOrderById(Long id) {
        customerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id));
        return toDTO(order);
    }

    @Transactional(readOnly = true)
    public List<orderResponseDTO> getOrdersByCustomer(Long customerId) {
        findCustomer(customerId);
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toDTO)
                .toList();
    }

    private userAccount findCustomer(Long customerId) {
        if (customerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID is required");
        }
        userAccount account = userRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found: " + customerId));
        if (!"ROLE_USER".equalsIgnoreCase(account.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not a customer account");
        }
        return account;
    }

    private Map<Long, Integer> mergeCartItems(List<OrderLineRequestDTO> items) {
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order items must not be empty");
        }
        Map<Long, Integer> merged = new LinkedHashMap<>();
        for (OrderLineRequestDTO line : items) {
            if (line == null || line.doughnutId() == null || line.quantity() == null || line.quantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each order item must have a valid doughnutId and positive quantity");
            }
            merged.merge(line.doughnutId(), line.quantity(), Integer::sum);
        }
        return merged;
    }

    private String normalizeFulfillment(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fulfillment type is required");
        }
        String clean = rawValue.trim().toUpperCase();
        if (!clean.equals("PICK_UP") && !clean.equals("DELIVERY")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fulfillment type must be PICK_UP or DELIVERY");
        }
        return clean;
    }

    private String normalizePayment(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method is required");
        }
        String clean = rawValue.trim().toUpperCase();
        if (!clean.equals("CASH") && !clean.equals("ONLINE_PAYMENT")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method must be CASH or ONLINE_PAYMENT");
        }
        return clean;
    }

    private String resolveDeliveryAddress(String fulfillment, String confirmedAddress, userAccount customer) {
        if ("PICK_UP".equals(fulfillment)) {
            return "N/A - Pick-Up";
        }
        if (confirmedAddress != null && !confirmedAddress.isBlank()) {
            return confirmedAddress.trim();
        }
        return customer.getDefaultDeliveryAddress();
    }

    private orderResponseDTO toDTO(customerOrder order) {
        List<orderItemsResponseDTO> itemDTOs = order.getItems().stream()
                .map(i -> new orderItemsResponseDTO(
                        i.getDoughnutId(),
                        i.getFlavorName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getLineTotal()
                ))
                .toList();

        return new orderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getMobileNumber(),
                order.getFulfillmentType(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemDTOs
        );
    }
}