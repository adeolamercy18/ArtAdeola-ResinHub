

package com.example.artadeola.controller;

import com.example.artadeola.entity.ProductOption;
import com.example.artadeola.service.ProductOptionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-options")
@CrossOrigin(origins = "*")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    // Get all product options
    @GetMapping
    public ResponseEntity<List<ProductOption>> getAllOptions() {
        return ResponseEntity.ok(productOptionService.getAllOptions());
    }

    // Get options belonging to a particular product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductOption>> getOptionsByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                productOptionService.getOptionsByProduct(productId));
    }

    // Get a single product option
    @GetMapping("/{id}")
    public ResponseEntity<ProductOption> getOptionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productOptionService.getOptionById(id));
    }

    // Add an option to a product
    @PostMapping("/product/{productId}")
    public ResponseEntity<ProductOption> saveOption(
            @PathVariable Long productId,
            @RequestBody ProductOption option) {

        return ResponseEntity.ok(
                productOptionService.saveOption(productId, option));
    }

    // Update an option
    @PutMapping("/{id}")
    public ResponseEntity<ProductOption> updateOption(
            @PathVariable Long id,
            @RequestBody ProductOption option) {

        return ResponseEntity.ok(
                productOptionService.updateOption(id, option));
    }

    // Delete an option
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {

        productOptionService.deleteOption(id);

        return ResponseEntity.noContent().build();
    }
    @PostMapping("/product/{productId}/bulk")
    public ResponseEntity<List<ProductOption>> saveOptionsBulk(
            @PathVariable Long productId,
            @RequestBody List<ProductOption> options) {
        return ResponseEntity.ok(
                productOptionService.saveOptionsBulk(productId, options));
    }
}