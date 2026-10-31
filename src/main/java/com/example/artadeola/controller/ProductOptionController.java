
package com.example.artadeola.controller;

import com.example.artadeola.entity.Product;
import com.example.artadeola.entity.ProductOption;
import com.example.artadeola.repository.ProductOptionRepository;
import com.example.artadeola.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-options")
@CrossOrigin(origins = "*")
public class ProductOptionController {

    private final ProductOptionRepository optionRepository;
    private final ProductService productService;

    public ProductOptionController(
            ProductOptionRepository optionRepository,
            ProductService productService) {

        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    // Add one option to a product
    @PostMapping("/{productId}")
    public ResponseEntity<?> addOption(
            @PathVariable Long productId,
            @RequestBody ProductOption option) {

        Product product = productService.getProductById(productId)
                .orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        option.setId(null);
        option.setProduct(product);

        ProductOption savedOption =
                optionRepository.save(option);

        return ResponseEntity.ok(savedOption);
    }

    // Add multiple options to a product at once
    @PostMapping("/{productId}/bulk")
    public ResponseEntity<?> addMultipleOptions(
            @PathVariable Long productId,
            @RequestBody List<ProductOption> options) {

        Product product = productService.getProductById(productId)
                .orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (options == null || options.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please provide at least one product option.");
        }

        for (ProductOption option : options) {
            option.setId(null);
            option.setProduct(product);
        }

        List<ProductOption> savedOptions =
                optionRepository.saveAll(options);

        return ResponseEntity.ok(savedOptions);
    }

    // Get all options belonging to a product
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductOption>> getOptions(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                optionRepository.findByProductId(productId)
        );
    }
}

