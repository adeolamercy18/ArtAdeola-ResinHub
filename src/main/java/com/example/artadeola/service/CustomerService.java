
package com.example.artadeola.service;

import com.example.artadeola.entity.Customer;
import com.example.artadeola.repository.CustomerRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register or create a customer
    public Customer createCustomer(Customer customer) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered.");
        }

        customer.setPassword(
                passwordEncoder.encode(customer.getPassword())
        );

        return customerRepository.save(customer);
    }

    // Register a customer using the registration endpoint
    public Customer registerCustomer(Customer customer) {
        return createCustomer(customer);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get customer by ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Update a customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Optional<Customer> optionalCustomer =
                customerRepository.findById(id);

        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer existingCustomer = optionalCustomer.get();

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        if (updatedCustomer.getPassword() != null
                && !updatedCustomer.getPassword().isBlank()) {

            existingCustomer.setPassword(
                    passwordEncoder.encode(updatedCustomer.getPassword())
            );
        }

        return customerRepository.save(existingCustomer);
    }

    // Delete customer
    public boolean deleteCustomer(Long id) {

        if (!customerRepository.existsById(id)) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;
    }
    public boolean authenticateCustomer(String email, String password) {
        Optional<Customer> customerOptional =
                customerRepository.findByEmail(email);
        if (customerOptional.isEmpty()) {
            return false;
        }
        Customer customer = customerOptional.get();
        return passwordEncoder.matches(
                password,
                customer.getPassword()
        );
    }
}