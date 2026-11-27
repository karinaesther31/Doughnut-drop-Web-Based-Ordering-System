package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.entity.*;
import finalProject.doughnut_drop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerOrderService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartRepository;
    private final customerOrderRepository orderRepository;

    public CustomerOrderService(
            ProductRepository productRepository,
            CartItemRepository cartRepository,
            customerOrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    // 1. DISPLAY ALL DONUTS (HOME / MENU)
    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    // 2. VIEW PRODUCT DETAILS
    public Product getProductDetails(Long productId) {
        return productRepository.findById(productId)
                .filter(Product::isActive)
                .orElseThrow(() -> new RuntimeException("Donut not found"));
    }

    // 3. ADD DONUT TO CART
    @Transactional
    public CartItem addToCart(
            Long customerId,
            Long productId,
            int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Product product = getProductDetails(productId);

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        CartItem cartItem = cartRepository
                .findByCustomerIdAndProductId(customerId, productId)
                .orElse(new CartItem());

        int newQuantity = cartItem.getId() == null
                ? quantity
                : cartItem.getQuantity() + quantity;

        if (newQuantity > product.getStock()) {
            throw new RuntimeException("Requested stock exceeded");
        }

        cartItem.setCustomerId(customerId);
        cartItem.setProduct(product);
        cartItem.setQuantity(newQuantity);
        return cartRepository.save(cartItem);
    }

    // 4. VIEW CUSTOMER CART
    public List<CartItem> getCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    // 5. UPDATE CART QUANTITY
    @Transactional
    public CartItem updateCart(
            Long customerId,
            Long cartItemId,
            int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        CartItem item = cartRepository.findById(cartItemId)
                .filter(cart -> cart.getCustomerId().equals(customerId))
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        if (quantity > item.getProduct().getStock()) {
            throw new RuntimeException("Not enough stock available");
        }

        item.setQuantity(quantity);
        return cartRepository.save(item);
    }

    // 6. REMOVE ITEM FROM THE CART
    public void removeFromCart(
            Long customerId,
            Long cartItemId) {

        CartItem item = cartRepository.findById(cartItemId)
                .filter(cart -> cart.getCustomerId().equals(customerId))
                .orElseThrow(() -> new RuntimeException("Cart Item not found"));

        cartRepository.delete(item);
    }

    // 7. COMPUTE TOTAL PRICE
    public double calculateTotal(Long customerId) {
        List<CartItem> cartItems = cartRepository.findByCustomerId(customerId);

        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // 8. CHECKOUT / CREATE CUSTOMER ORDER
    @Transactional
    public customerOrder checkout(
            Long customerId,
            String customerName,
            String deliveryAddress,
            String mobileNumber) {

        return checkout(
                customerId,
                customerName,
                deliveryAddress,
                mobileNumber,
                "DELIVERY",
                "CASH");
    }

    @Transactional
    public customerOrder checkout(
            Long customerId,
            String customerName,
            String deliveryAddress,
            String mobileNumber,
            String fulfillmentType,
            String paymentMethod) {

        List<CartItem> cartItems = cartRepository.findByCustomerId(customerId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Your cart is empty");
        }

        customerOrder order = new customerOrder();
        order.setCustomerId(customerId);
        order.setCustomerName(customerName);
        order.setMobileNumber(mobileNumber != null ? mobileNumber : "N/A");
        order.setFulfillmentType(fulfillmentType != null ? fulfillmentType : "DELIVERY");
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "CASH");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cart : cartItems) {
            Product product = productRepository.findById(cart.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!product.isActive() || product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cart.getQuantity()));

            orderItem item = new orderItem();
            item.setDoughnutId(product.getId());
            item.setFlavorName(product.getName());
            item.setQuantity(cart.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);

            order.addItem(item);
            total = total.add(lineTotal);

            // Reduce available stock
            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(total);

        customerOrder savedOrder = orderRepository.save(order);

        // CLEAR CUSTOMER CART AFTER SUCCESSFUL CHECKOUT
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    // 9. VIEW CUSTOMER ORDER HISTORY
    public List<customerOrder> getOrderHistory(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}