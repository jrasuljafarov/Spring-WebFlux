package com.example.productservice.service;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> getAll() {
        return this.productRepository.findAll()
                .map(EntityDtoUtil::toDto);
    }

    public Mono<ProductDto> findProductById(String id) {
        return this.productRepository.findById(id)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<ProductDto> insertProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(EntityDtoUtil::toEntity)
                .flatMap(this.productRepository::insert)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono) {
        return this.productRepository.findById(id)
                .flatMap(product -> productDtoMono.map(EntityDtoUtil::toEntity).doOnNext(p -> p.setId(id)))
                .flatMap(product -> this.productRepository.save(product).map(EntityDtoUtil::toDto));
    }

    public Mono<Void> deleteProductById(String id) {
        return this.productRepository.deleteById(id);
    }

}
