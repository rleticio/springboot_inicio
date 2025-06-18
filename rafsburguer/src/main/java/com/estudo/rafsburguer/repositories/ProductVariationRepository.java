package com.estudo.rafsburguer.repositories;

import com.estudo.rafsburguer.entities.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {

    @Query("select pv from ProductVariation pv where pv.id = :productVariationId and pv.product.id = productVariationId")
    Optional<ProductVariation> findProductIdAndProductVariationId(@Param("productId") long productId, @Param("productVariationId") long productVariationId);
}
