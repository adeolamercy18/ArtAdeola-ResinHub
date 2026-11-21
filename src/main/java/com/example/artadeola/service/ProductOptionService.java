
package com.example.artadeola.service;

import com.example.artadeola.entity.Product;
import com.example.artadeola.entity.ProductOption;
import com.example.artadeola.repository.ProductOptionRepository;
import com.example.artadeola.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public ProductOptionService(
            ProductOptionRepository productOptionRepository,
            ProductRepository productRepository) {

        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
    }

    public List<ProductOption> getAllOptions() {
        return productOptionRepository.findAll();
    }

    public List<ProductOption> getOptionsByProduct(Long productId) {
        return productOptionRepository.findByProductId(productId);
    }

    public ProductOption getOptionById(Long id) {
        return productOptionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product option not found"));
    }

    public ProductOption saveOption(Long productId, ProductOption option) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        option.setProduct(product);

        return productOptionRepository.save(option);
    }

    public ProductOption updateOption(Long id, ProductOption updatedOption) {

        ProductOption existingOption = getOptionById(id);

        existingOption.setName(updatedOption.getName());
        existingOption.setPrice(updatedOption.getPrice());
        existingOption.setAvailable(updatedOption.getAvailable());
        existingOption.setQuantity(updatedOption.getQuantity());

        return productOptionRepository.save(existingOption);
    }

    public void deleteOption(Long id) {
        productOptionRepository.deleteById(id);
    }
    public List<ProductOption> saveOptionsBulk(
            Long productId, List<ProductOption> options) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("product not found"));
        for (ProductOption option : options) {
            option.setProduct(product);
        }
        return productOptionRepository.saveAll(options);
    }
}