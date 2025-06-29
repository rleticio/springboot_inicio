package com.estudo.rafsburguer.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="order_items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    //Referenciada to local
    @ManyToOne
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;

    //Referenciada to local
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
