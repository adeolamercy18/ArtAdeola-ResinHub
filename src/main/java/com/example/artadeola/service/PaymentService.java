package com.example.artadeola.service;

import com.example.artadeola.entity.Order;
import com.example.artadeola.entity.Payment;
import com.example.artadeola.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }
    public Payment createPayment(Payment payment){
        return paymentRepository.save(payment);
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    public Optional<Payment> getPaymentById(Long Id) {
        return paymentRepository.findById(Id);
    }
    public Payment updatePayment(Payment payment){
        return paymentRepository.save(payment);
    }
    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }
}
