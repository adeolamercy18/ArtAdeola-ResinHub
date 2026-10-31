package com.example.artadeola.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal amount;
    private String status;
    private String PaymentReference;

    public Payment(){
    }

    public Payment(Long id, Order order, BigDecimal amount, String status, String paymentReference) {
        this.id = id;
        this.order = order;
        this.amount = amount;
        this.status = status;
        PaymentReference = paymentReference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentReference() {
        return PaymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        PaymentReference = paymentReference;
    }
}
