


package com.example.artadeola.controller;

import com.example.artadeola.entity.Payment;
import com.example.artadeola.repository.PaymentRepository;
import com.example.artadeola.service.PaystackService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final PaystackService paystackService;

    public PaymentController(
            PaymentRepository paymentRepository,
            PaystackService paystackService) {
        this.paymentRepository = paymentRepository;
        this.paystackService = paystackService;
    }

    // 1. CREATE PAYMENT
    @PostMapping
    public ResponseEntity<?> createPayment(
            @RequestBody Payment payment) {
        try {
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedPayment);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Unable to create payment record"));
        }
    }

    // 2. GET ALL PAYMENTS
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    // 3. GET PAYMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Payment not found"));
        }

        return ResponseEntity.ok(payment.get());
    }

    // 4. UPDATE PAYMENT
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(
            @PathVariable Long id,
            @RequestBody Payment updatedPayment) {

        Optional<Payment> existing = paymentRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Payment not found"));
        }

        Payment payment = existing.get();

        if (updatedPayment.getAmount() != null) {
            payment.setAmount(updatedPayment.getAmount());
        }

        if (updatedPayment.getStatus() != null) {
            payment.setStatus(updatedPayment.getStatus());
        }

        if (updatedPayment.getPaymentReference() != null) {
            payment.setPaymentReference(updatedPayment.getPaymentReference());
        }

        if (updatedPayment.getOrder() != null) {
            payment.setOrder(updatedPayment.getOrder());
        }

        return ResponseEntity.ok(paymentRepository.save(payment));
    }

    // 5. DELETE PAYMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        if (!paymentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Payment not found"));
        }

        paymentRepository.deleteById(id);

        return ResponseEntity.ok(
                Map.of("message", "Payment deleted successfully"));
    }

    // 6. INITIALIZE PAYSTACK PAYMENT
    @PostMapping("/initialize")
    public ResponseEntity<?> initializePaystackPayment(
            @RequestBody Map<String, Object> request) {

        try {
            Object emailObject = request.get("email");
            Object amountObject = request.get("amount");

            if (emailObject == null || amountObject == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("message", "Email and amount are required"));
            }

            String email = emailObject.toString().trim();
            long amount = new BigDecimal(amountObject.toString())
                    .longValueExact();

            if (email.isBlank() || amount <= 0) {
                return ResponseEntity.badRequest().body(
                        Map.of("message", "Invalid email or amount"));
            }

            Map<String, Object> response =
                    paystackService.initializePayment(email, amount);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(
                    Map.of("message", "Payment initialization failed",
                            "details", e.getMessage() == null
                                    ? "Unknown error" : e.getMessage()));
        }
    }

    // 7. VERIFY PAYSTACK PAYMENT
    @GetMapping("/verify/{reference}")
    public ResponseEntity<?> verifyPaystackPayment(
            @PathVariable String reference) {

        try {
            Map<String, Object> response =
                    paystackService.verifyPayment(reference);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(
                    Map.of("message", "Payment verification failed"));
        }
    }
}