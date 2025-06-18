package com.estudo.rafsburguer.mappers;

import com.estudo.rafsburguer.dto.input.product.CreateProductVariationDto;
import com.estudo.rafsburguer.dto.output.product.RecoveryProductDto;
import com.estudo.rafsburguer.dto.output.product.RecoveryProductVariationDto;
import com.estudo.rafsburguer.entities.Product;
import com.estudo.rafsburguer.entities.ProductVariation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // O target especifica o campo de destino no RecoveryProductDto.
    // O  qualifiedByName especifica o método de mapeamento que deve ser usado para preencher esse campo.
    @Mapping(target = "productVariations", qualifiedByName = "mapProductVariationToRecoveryProductVariationDto")
    RecoveryProductDto mapProductToRecoveryProductDto(Product product);

    @Named("mapProductVariationToRecoveryProductVariationDto")
    @IterableMapping(qualifiedByName = "mapProductVariationToRecoveryProductVariationDto")
    List<RecoveryProductVariationDto> mapProductVariationToRecoveryProductVariationDto(List<ProductVariation> productVariations);

    @Named("mapProductVariationToRecoveryProductVariationDto")
    RecoveryProductVariationDto mapProductVariationToRecoveryProductVariationDto(ProductVariation productVariation);

    ProductVariation mapCreateProductVariationDtoToProductVariation(CreateProductVariationDto createProductVariationDto);
}
