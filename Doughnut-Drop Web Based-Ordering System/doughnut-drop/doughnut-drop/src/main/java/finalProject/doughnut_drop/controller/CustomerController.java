package finalProject.doughnut_drop.controller;

import finalProject.doughnut_drop.entity.*;
import finalProject.doughnut_drop.service.CustomerOrderService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerOrderService customerService;

    public CustomerController(CustomerOrderService customerService) {
        this.customerService = customerService;
    }

    // HOME / MENU
    @GetMapping("/products")
    public List<Product> getProducts() {
        return customerService.getAllProducts();
    }

    // PRODUCT DETAILS
    @GetMapping("/products/{id}")
    public Product getProductDetails(@PathVariable Long id) {
        return customerService.getProductDetails(id);
    }

    // ADD TO CART
    @PostMapping("/{customerId}/cart/add")
    public CartItem addToCart(
            @PathVariable Long customerId,
            @RequestBody Map<String, Integer> request) {

        Long productId = request.get("productId").longValue();
        int quantity = request.get("quantity");

        return customerService.addToCart(customerId, productId, quantity);
    }

    // VIEW CART
    @GetMapping("/{customerId}/cart")
    public List<CartItem> getCart(@PathVariable Long customerId) {
        return customerService.getCart(customerId);
    }

    // UPDATE CART
    @PutMapping("/{customerId}/cart/{cartItemId}")
    public CartItem updateCart(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId,
            @RequestBody Map<String, Integer> request) {

        return customerService.updateCart(
                customerId,
                cartItemId,
                request.get("quantity"));
    }

    // REMOVE CART ITEM
    @DeleteMapping("/{customerId}/cart/{cartItemId}")
    public String removeFromCart(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId) {

        customerService.removeFromCart(customerId, cartItemId);
        return "Item removed from cart successfully";
    }

    // COMPUTE TOTAL
    @GetMapping("/{customerId}/cart/total")
    public double calculateTotal(@PathVariable Long customerId) {
        return customerService.calculateTotal(customerId);
    }

    // CHECKOUT
    @PostMapping("/{customerId}/checkout")
    public customerOrder checkout(
            @PathVariable Long customerId,
            @RequestBody Map<String, Object> request) {

        String customerName = request.get("customerName") != null
                ? request.get("customerName").toString()
                : null;
        String deliveryAddress = request.get("deliveryAddress") != null
                ? request.get("deliveryAddress").toString()
                : null;

        Object rawMobile = request.get("mobileNumber") != null
                ? request.get("mobileNumber")
                : request.get("contactNumber");
        String mobileNumber = rawMobile != null ? rawMobile.toString() : null;

        String fulfillmentType = request.get("fulfillmentType") != null
                ? request.get("fulfillmentType").toString()
                : "DELIVERY";
        String paymentMethod = request.get("paymentMethod") != null
                ? request.get("paymentMethod").toString()
                : "CASH";

        return customerService.checkout(
                customerId,
                customerName,
                deliveryAddress,
                mobileNumber,
                fulfillmentType,
                paymentMethod);
    }

    // ORDER HISTORY
    @GetMapping("/{customerId}/orders")
    public List<customerOrder> getOrderHistory(@PathVariable Long customerId) {
        return customerService.getOrderHistory(customerId);
    }
}