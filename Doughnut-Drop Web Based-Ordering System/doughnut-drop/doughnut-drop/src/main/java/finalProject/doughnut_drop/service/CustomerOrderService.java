package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.entity.*;
import finalProject.doughnut_drop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    //1. DISPLAY ALL DONUTS (HOME / MENU)

    public List<Product> getALLProducts()
    {return productRepository.findByActiveTrue();}



    //2. VIEW PRODUCT DETAILS

    public Product getProductDetails (Long productId){
        return ProductRepository.findById(productId)
                .filter(Product::isActive)
                .orElseThrow(() -> new RuntimeException("Donut not found"));
    }


    //3. ADD DONUT TO CART

    @Transactional
    public CartItem addToCart(
            Long customerId,
            Long productId,
            int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero");

        }

        Product product = getProductDetails(productId);

        if (product.getStock() < quantity) {
            throw new RuntimeException(
                    "No enough stock available");
        }

        CartItem cartItem = cartRepository
                .findByCustomerIdAndProductId(
                        customerId, productId)
                .orElse(new CartItem());

        int newQuantity = cartItem.getId() == null
                ? quantity
                : cartItem.getQuantity() + quantity;
        if (newQuantity > product.getStock()) {
            throw new RuntimeException(
                    "Requested stock exceeded");
        }

        cartItem.setCustomerId(customerId);
        cartItem.setProduct(product);
        cartItem.setQuantity(newQuantity);
        return cartRepository.save(cartItem);

    }

    //4.VIEW CUSTOMER CART

    public List<CartItem> getCart(Long customerId) {

        return cartRepository.findByCustomerId(customerId);
    }

    //5. UPDATE CART QUANTITY

    @Transactional
    public CartItem updateCart(
            Long customerId,
            Long cartItemId,
            int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero");
        }
        CartItem item = cartRepository.findById(cartItemId)
                .filter(cart -> cart.getCustomerId()
                        .equals(customerId))
                .orElseThrow();
        new RuntimeException("CartItem not found");
        if (quantity > item.getProduct().getStock()) {
            throw new RuntimeException(
                    "No enough stock available");

            if (quantity > item.getProduct().getStock()) {
                throw new RuntimeException(
                        "no enough stock available");

            }
            item.setQuantity(quantity);
            return cartRepository.save(item);
        }

    }

    //6. REMOVE ITEM FROM THE CART

    public void removeFromCart(
            Long customerId,
            Long cartItemId) {

        CartItem item = cartRepository.findById(cartItemId)
                .filter(cart -> cart.getCustomerId()
                        .equals(customerId))
                .orElseThrow(() ->
                        new RuntimeException(" Cart Item not found"));
        cartRepository.delete(item);

    }

    //7.COMPUTE TOTAL PRICE

    public double calculateTotal(Long customerId) {

        List<CartItem> cartItems =
                cartRepository.findByCustomerId(customerId);

        return cartItems.stream()
                .mapToDouble(item ->
                        item.getProduct() .getPrice()
                                * item.getQuantity())
                .sum();
    }

    //8.CHECKOUT / CREATE CUSTOMER ORDER

    @Transactional
    public CustomerOrder checkout(
            Long customerId,
            String customerName,
            String deliveryAddress,
            String contactNumber) throws RuntimeException {

        List<CartItem> cartItems =
                cartRepository.findByCustomerId(customerId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Your cart is empty");
        }
        CustomerOrder order = new CustomerOrder();

        order.setCustomerId(customerId);
        order.setCustomerName(customerName);
        order.setDeliveryAddress(deliveryAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();

        double total = 0;
        for (CartItem cart : cartItems) {

            Product product = productRepository
                    .findById(cart.getProduct().getId())
                    .orElseThrow();
            new RuntimeException("Product not found;");
            if (!product.isActive()
                    || product.getStock() < cart.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for" + product.getName());
            }
            OrderItem item = new OrderItem();

            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());

            double subtotal =
                    product.getPrice() * cart.getQuantity();

            item.setSubtotal(subtotal);
            total += subtotal;


            //Reduce available stock
            product.setStock(
                    product.getStock() - cart.getQuantity());

            productRepository.save(product);

            orderItems.add(item);

            order.setOrderItems(orderItems);
            order.setTotalAmount(total);

            CustomerOrder savedOrder =
                    orderRepository.save(order);


            //CLEAR CUSTOMER CART AFTER SUCCESSFUL CHECKOUT
            cartRepository.deleteAll(cartItems);

            return savedOrder;
        }
        //9.VIEW CUSTOMER ORDER HISTORY

        public List<CustomerOrder> getOrderHistory(Long customerId) {
            return orderRepository.findByCustomerId(customerId);
        }
    }


