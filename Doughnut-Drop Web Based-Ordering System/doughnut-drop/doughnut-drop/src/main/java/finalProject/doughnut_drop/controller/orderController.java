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

    private final orderService orderService;
    public orderController(orderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public List<orderResponseDTO> listOrders(){
        return orderService.getAllOrders();

    }
    @PostMapping
    public ResponseEntity<orderResponseDTO> placeOrder(@Valid @RequestBody orderRequestDTO req){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(req));

    }
}
