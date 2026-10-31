package com.example.artadeola.service;

import com.example.artadeola.entity.Order;
import com.example.artadeola.entity.OrderItem;
import com.example.artadeola.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository){
        this.orderItemRepository = orderItemRepository;
    }
    public OrderItem createOrderItem(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    }
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }
    public Optional<OrderItem> getOrderItemById(Long Id) {
        return orderItemRepository.findById(Id);
    }
    public OrderItem updateOrderItem(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    }
    public void deleteOrderItem(Long id){
        orderItemRepository.deleteById(id);
    }
}
