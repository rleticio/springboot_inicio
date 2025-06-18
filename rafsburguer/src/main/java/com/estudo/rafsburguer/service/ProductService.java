package com.estudo.rafsburguer.service;

import com.estudo.rafsburguer.dto.input.product.CreateProductDto;
import com.estudo.rafsburguer.dto.input.product.CreateProductVariationDto;
import com.estudo.rafsburguer.dto.input.product.UpdateProductDto;
import com.estudo.rafsburguer.dto.input.product.UpdateProductVariationDto;
import com.estudo.rafsburguer.dto.output.product.RecoveryProductDto;
import com.estudo.rafsburguer.entities.Product;
import com.estudo.rafsburguer.entities.ProductVariation;
import com.estudo.rafsburguer.enums.Category;
import com.estudo.rafsburguer.exceptions.*;
import com.estudo.rafsburguer.mappers.ProductMapper;
import com.estudo.rafsburguer.repositories.OrderItemRepository;
import com.estudo.rafsburguer.repositories.ProductRepository;
import com.estudo.rafsburguer.repositories.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductMapper productMapper;


    public RecoveryProductDto createProduct(CreateProductDto createProductDto) {

        List<ProductVariation> productVariations = createProductDto.productVariations().stream()
                .map(CreateProductVariationDto -> productMapper.mapCreateProductVariationDtoToProductVariation(CreateProductVariationDto))
                .toList();

        Product product = Product.builder()
                .name(createProductDto.name())
                .description(createProductDto.description())
                .category(Category.valueOf(createProductDto.category().toUpperCase()))
                .productVariation(productVariations)
                .available(createProductDto.available())
                .build();
        /*
        Se o produto estiver com available = false e possuir alguma variaçao com availabe = true, gera exceção
        product.getProductVariations() retorna uma lista de variações
        .stream() converte a lista em um stream para podermos aplicar operações como filtros ou verificações
        .anyMatch(ProductVariation::getAvailable) retorna true se alguma variação do produto estiver disponível (ou seja, o getAvailable() retornar true)
        SEM O METHOD REFERENCE ficaria .anyMatch(productVariation -> productVariation.getAvailable())
        */
                if (!product.getAvailable() && product.getProductVariation().stream().anyMatch(ProductVariation::getAvailable)){
            throw new ProductVariationUnavailableException();
        }

        //Relaciona cada variação com o produto
        //pois o dto da variação não possui o campo de relacionamento, apenas na entidade.
        //for (ProductVariation productVariation : productVariations) {
        //    productVariation.setProduct(product);
        //}
        productVariations.forEach(productVariation -> productVariation.setProduct(product));

        Product productSaved = productRepository.save(product);
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public RecoveryProductDto createProductVariation(Long productId, CreateProductVariationDto createProductVariationDto) {

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductVariation productVariation = productMapper.mapCreateProductVariationDtoToProductVariation(createProductVariationDto);

        productVariation.setProduct(product);
        ProductVariation productVariationSaved = productVariationRepository.save(productVariation);

        product.getProductVariation().add(productVariationSaved);
        //productRepository.save(product);

        return productMapper.mapProductToRecoveryProductDto(productVariationSaved.getProduct());
    }
    
    public Page<RecoveryProductDto> getProducts(Pageable pageable){
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public Page<RecoveryProductDto> getProductsByCategory(String categoryname, Pageable pageable){
        Page<Product> productPage = productRepository.findByCategory(Category.valueOf(categoryname.toUpperCase()), pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public Page<RecoveryProductDto> getProductsByName(String productName, Pageable pageable){
        Page<Product> productPage = productRepository.findByNameContaining(productName,pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public RecoveryProductDto getProductById(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return productMapper.mapProductToRecoveryProductDto(product);
    }

    public RecoveryProductDto updateProductPart(Long productId, UpdateProductDto updateProductDto){
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (updateProductDto.name() != null){
            product.setName(updateProductDto.name());
        }

        if (updateProductDto.description() != null){
            product.setDescription(updateProductDto.description());
        }

        if (updateProductDto.available() != null){
            product.setAvailable(updateProductDto.available());
        }

        if (!product.getAvailable()) {
            product.getProductVariation().forEach(productVariation -> productVariation.setAvailable(false));
        }

        return productMapper.mapProductToRecoveryProductDto(productRepository.save(product));
    }

    public RecoveryProductDto updateProductVariation(Long productId, Long productVariationId, UpdateProductVariationDto updateProductVariationDto){
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductVariation productVariation = product.getProductVariation().stream()
                .filter(productVariationInProduct -> productVariationInProduct.getId().equals(productVariationId))
                .findFirst()//ao encontrar, nao continua percorrendo a lista
                .orElseThrow(ProductVariationNotFoundException::new);

        if (updateProductVariationDto.sizeName() != null) {
            productVariation.setSizeName(updateProductVariationDto.sizeName());
        }

        if (updateProductVariationDto.description() != null){
            productVariation.setDescription(updateProductVariationDto.description());
        }

        if (updateProductVariationDto.available() != null){
            //se o produto estiver com available = false, variação tem que estar falsa
            if (!productVariation.getProduct().getAvailable() && updateProductVariationDto.available()){
                throw new ProductVariationUnavailableException();
            }

            productVariation.setAvailable(updateProductVariationDto.available());
        }

        if (updateProductVariationDto.price() != null){
            productVariation.setPrice(updateProductVariationDto.price());
        }

        // alterações na lista de variações são atualizadas na tabela de variações devido à anotação de relacionamento na entidade produto
        // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        Product productSaved =productRepository.save(product);
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public void deleteProductId(Long productId){
        if (!productRepository.existsById(productId)){
            throw new ProductNotFoundException();
        }

        if (orderItemRepository.findFirstByProductId(productId).isPresent()){
            throw new ProductAssociatedWithOrderException();
        }

        productRepository.deleteById(productId);
    }

    public void deleteProductVariationById(Long productId, Long productVariationId){

        ProductVariation productVariation = productVariationRepository
                .findProductIdAndProductVariationId(productId,productVariationId)
                .orElseThrow(ProductVariationNotFoundException::new);

        if (orderItemRepository.findFirstByProductVariationId(productVariationId).isPresent()){
            throw new ProductVariationAssociatedWithOrderException();
        }

        productVariationRepository.deleteById(productVariationId);
    }

}
