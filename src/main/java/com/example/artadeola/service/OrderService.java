
package com.example.artadeola.service;

import com.example.artadeola.entity.Order;
import com.example.artadeola.entity.OrderItem;
import com.example.artadeola.entity.Product;
import com.example.artadeola.repository.OrderRepository;
import com.example.artadeola.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Order order) {

        if (order == null || order.getItems() == null
                || order.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Your order must contain products.");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItem requestedItem : order.getItems()) {

            if (requestedItem.getProduct() == null
                    || requestedItem.getProduct().getId() == null) {
                throw new IllegalArgumentException(
                        "Product ID is required.");
            }
            int quantity = requestedItem.getQuantity();

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero.");
            }

            Product product = productRepository.findById(
                    requestedItem.getProduct().getId()
            ).orElseThrow(() ->
                    new IllegalArgumentException("Product not found."));

            BigDecimal price = product.getPrice();

            if (price == null
                    || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Please set a valid price for " + product.getName());
            }

            total = total.add(
                    price.multiply(
                            BigDecimal.valueOf(requestedItem.getQuantity())));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(price);
            item.setOrder(order);

            items.add(item);
        }

        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order.setItems(items);

        return orderRepository.save(order);
    }
}