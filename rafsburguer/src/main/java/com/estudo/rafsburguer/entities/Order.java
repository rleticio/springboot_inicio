package com.estudo.rafsburguer.entities;

import com.estudo.rafsburguer.enums.PaymentMethod;
import com.estudo.rafsburguer.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDENTE;

    private BigDecimal amount;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    //Referenciada to local
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    //Referenciada to local
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    @PrePersist
    private void configureCreatedDate(){
        this.createdDate = LocalDateTime.now();
    }
}
