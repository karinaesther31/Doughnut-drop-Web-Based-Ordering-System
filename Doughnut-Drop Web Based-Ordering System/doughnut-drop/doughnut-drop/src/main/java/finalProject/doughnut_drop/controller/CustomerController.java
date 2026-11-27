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

    public CustomerController(
            CustomerOrderService customerService){
         this.customerService = customerService;
    }

    // HOME / MENU

    @GetMapping("/products")
    public List<Product> getProducts(){
        return customerService.getAllProducts();
    }

    //PRODUCT DETAILS

    @GetMapping("/products/{id}")
    public Product getProducts(@PathVariable Long id){
        return customerService.getProductDetails(id);
    }

    // ADD TO CART

    @PostMapping("/{customerId}/cart/add")
    public CartItem addToCart(
            @PathVariable Long customerId,
            @RequestBody Map<String,Integer>request){
        Long productId = request.get("productId").longValue();
        int quantity = request.get("quantity");

        return customerService.addToCart(
                customerId, productId, quantity);
    }

    // VIEW CART

    @GetMapping("/{customerId}/cart")
    public List<CartItem> getCart(
            @PathVariable Long customerId){

        return customerService.getCart(customerId);
    }

    //UPDATE CART

    @PutMapping("/{customerId}/cart/{cartItemId}")
    public CartItem updateCart(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId,
            @RequestBody Map<String,Integer>request){

        return customerService.updateCart(
                custmerId,
                cartItemId,
                request,get("quantity"));
    }

    //REMOVE CART ITEM

    @DeleteMapping("/{customerId}/cart/{cartItemId")
    public String removeFromCart(
            @PathVariable Long customerId,
            @PathVariable Long cartItemId){

        customerService.removeFromCart(
                customerId, cartItem);
        return "Item removed from cart successfully";
    }
    //COMPUTE TOTAL

    @GetMapping("/{customerId}/cart/total")
    public double calculateTotal(
            @PathVariable Long customerId){

        return customerService.calculateTotal(customerId);
    }

    // CHECKOUT

    @PostMapping("/{customerId}/checkout")
    public customerOrder checkout(
            @PathVariable Long customerId,
            @RequestBody Map<String,Integer>request){

        return customerService.checkout(
                customerId
                request.get("customerName"),
                request.get("deliveryAddress"),
                request.get("contactNumber"));
    }
    //ORDER HISTORY

    @GetMapping("?{customerId}/orders}")
    public List<customerOrder> getOrderHistory(
            @PathVariable Long customerId){
        return customerService.getOrderHistory(customerId);
    }
}
