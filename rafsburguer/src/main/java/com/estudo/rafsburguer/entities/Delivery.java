package com.estudo.rafsburguer.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="deliveries")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_name")
    private String receiverName;

    private String address;

    @Column(name = "house_number")
    private String houseNumber;

    private String complement;

    private String distric;

    @Column(name = "zip_code")
    private String zipCode;

    private String city;

    private String state;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
