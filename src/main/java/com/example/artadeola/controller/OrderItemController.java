package com.example.artadeola.controller;

import com.example.artadeola.entity.Order;
import com.example.artadeola.entity.OrderItem;
import com.example.artadeola.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orderItems")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController (OrderItemService orderItemService){

        this.orderItemService = orderItemService;
    }
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {

        return orderItemService.createOrderItem(orderItem);
    }
    @GetMapping
    public List<OrderItem> getAllOrderItem() {
        return orderItemService.getAllOrderItems();
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public OrderItem updateOrderItem(@PathVariable Long id,
                             @RequestBody OrderItem orderItem) {
        orderItem.setId(id);
        return orderItemService.updateOrderItem(orderItem);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}


