package com.estudo.rafsburguer.entities;

import com.estudo.rafsburguer.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder.Default
    private Boolean available = true;

    //Referenciada to local
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariation> productVariation;
}
