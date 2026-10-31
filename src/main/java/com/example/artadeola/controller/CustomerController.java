
package com.example.artadeola.controller;

import com.example.artadeola.dto.CustomerRegistrationRequest;
import com.example.artadeola.entity.Customer;
import com.example.artadeola.repository.CustomerRepository;
import com.example.artadeola.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerService customerService,
                              CustomerRepository customerRepository,
                              PasswordEncoder passwordEncoder) {

        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // Register a new customer

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {

        try {
            System.out.println("Registration endpoint reached");
            System.out.println("Received name: " + request.getName());
            System.out.println("Received email: " + request.getEmail());
            System.out.println("Password received: " +
                    (request.getPassword() != null &&
                            !request.getPassword().isBlank()));

            if (request.getName() == null ||
                    request.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Name is required.");
            }

            if (request.getEmail() == null ||
                    request.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Email is required.");
            }

            if (request.getPassword() == null ||
                    request.getPassword().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Password is missing.");
            }

            Customer customer = new Customer();
            customer.setName(request.getName().trim());
            customer.setEmail(request.getEmail().trim());
            customer.setPassword(request.getPassword());


            Customer savedCustomer =
                    customerService.createCustomer(customer);

            // Never return the encrypted password to the browser.
            savedCustomer.setPassword(null);

            return ResponseEntity.ok(savedCustomer);

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(409)
                    .body("This email may already be registered.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Registration failed. Check the backend console.");
        }
    }
    // Create a customer
    @PostMapping
    public ResponseEntity<?> createCustomer(
            @RequestBody Customer customer) {
        System.out.println("Customer name:" + customer.getName());
        System.out.println("Customer email:" + customer.getEmail());
        System.out.println("Password recieved:" +
                (customer.getPassword() !=null &&
                        !customer.getPassword().isBlank()));

        Customer savedCustomer = customerService.createCustomer(customer);
        savedCustomer.setPassword(null);
        return ResponseEntity.ok(savedCustomer);
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {

        List<Customer> customers =
                customerService.getAllCustomers();

        customers.forEach(customer ->
                customer.setPassword(null));

        return ResponseEntity.ok(customers);
    }

    // Get a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(
            @PathVariable Long id) {

        Customer customer =
                customerService.getCustomerById(id);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setPassword(null);

        return ResponseEntity.ok(customer);
    }

    // Update a customer
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer updatedCustomer) {

        try {
            Customer customer =
                    customerService.updateCustomer(
                            id, updatedCustomer);

            if (customer == null) {
                return ResponseEntity.notFound().build();
            }

            customer.setPassword(null);

            return ResponseEntity.ok(customer);

        } catch (RuntimeException exception) {

            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            exception.getMessage() == null
                                    ? "Customer update failed."
                                    : exception.getMessage())
            );
        }
    }

    // Delete a customer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable Long id) {

        boolean deleted =
                customerService.deleteCustomer(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                Map.of("message",
                        "Customer deleted successfully.")
        );
    }
    @PostMapping("/login")

    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password are required."));
        }

        Optional<Customer> optionalCustomer =
                customerRepository.findByEmail(email.trim());

        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password."));
        }

        Customer customer = optionalCustomer.get();
        System.out.println("Customer found: " + customer.getEmail());
        System.out.println("Stored password matches: "
                + password.equals(customer.getPassword()));

        if (!passwordEncoder.matches(password, customer.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password."));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", customer.getId());
        response.put("name", customer.getName());
        response.put("email", customer.getEmail());
        response.put("phoneNumber", customer.getPhoneNumber());
        response.put("message", "Login successful.");

        return ResponseEntity.ok(response);
    }

}