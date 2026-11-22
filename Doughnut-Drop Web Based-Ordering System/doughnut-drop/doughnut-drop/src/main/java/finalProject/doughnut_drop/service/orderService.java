package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.dto.*;
import finalProject.doughnut_drop.entity.*;
import finalProject.doughnut_drop.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class orderService {

    private final customerOrderRepository orderRepository;
    private final DoughnutRepository doughnutRepository;

    public orderService(customerOrderRepository orderRepository, DoughnutRepository doughnutRepository) {
        this.orderRepository = orderRepository;
        this.doughnutRepository = doughnutRepository;
    }

    @Transactional
    public orderResponseDTO createOrder(orderRequestDTO req) {
        customerOrder order = new customerOrder(
                req.customerName().trim(),
                BigDecimal.ZERO,
                LocalDateTime.now()
        );

        BigDecimal grandTotal = BigDecimal.ZERO;
        List<orderItemsResponseDTO> responseItems = new ArrayList<>();

        for (OrderLineRequestDTO line : req.items()) {
            DoughnutItem item = doughnutRepository.findById(line.DoughnutId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Doughnut not found: " + line.DoughnutId()));

            if (item.getStockQuantity() < line.quantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Insufficient stock for " + item.getFlavorName() + " (available: " + item.getStockQuantity() + ")"
                );
            }

            item.setStockQuantity(item.getStockQuantity() - line.quantity());
            doughnutRepository.save(item);

            BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(line.quantity()));
            grandTotal = grandTotal.add(subtotal);

            orderItem orderItem = new orderItem(order, item, line.quantity(), subtotal);
            order.addItem(orderItem);

            responseItems.add(new orderItemsResponseDTO(
                    item.getId(),
                    item.getCode(),
                    item.getFlavorName(),
                    line.quantity(),
                    item.getPrice(),
                    subtotal
            ));
        }

        order.setTotalAmount(grandTotal);
        customerOrder saved = orderRepository.save(order);

        return new orderResponseDTO(
                saved.getId(),
                saved.getCustomerName(),
                saved.getTotalAmount(),
                saved.getCreatedAt(),
                responseItems
        );
    }

    public List<orderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            List<orderItemsResponseDTO> items = order.getItems().stream()
                    .map(oi -> new orderItemsResponseDTO(
                            oi.getDoughnutItem().getId(),
                            oi.getDoughnutItem().getCode(),
                            oi.getDoughnutItem().getFlavorName(),
                            oi.getQuantity(),
                            oi.getDoughnutItem().getPrice(),
                            oi.getSubtotal()
                    ))
                    .toList();

            return new orderResponseDTO(
                    order.getId(),
                    order.getCustomerName(),
                    order.getTotalAmount(),
                    order.getCreatedAt(),
                    items
            );
        }).toList();
    }
}
