package finalProject.doughnut_drop.controller;

import finalProject.doughnut_drop.dto.orderRequestDTO;
import finalProject.doughnut_drop.dto.orderResponseDTO;
import finalProject.doughnut_drop.service.orderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class orderController {

    private final orderService service;
    public orderController(orderService service) {
        this.service = service;
    }
    @PostMapping("/preview")
    public orderResponseDTO previewOrder(@Valid @RequestBody orderRequestDTO req) {
        return service.previewOrder(req);
    }

    @PostMapping
    public ResponseEntity<orderResponseDTO> placeOrder(@Valid @RequestBody orderRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.placeOrder(req));
    }

    @GetMapping
    public List<orderResponseDTO> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public orderResponseDTO getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<orderResponseDTO> getOrdersByCustomer(@PathVariable Long customerId) {
        return service.getOrdersByCustomer(customerId);
    }
}