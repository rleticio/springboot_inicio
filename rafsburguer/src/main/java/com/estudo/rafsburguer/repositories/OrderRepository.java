package com.estudo.rafsburguer.repositories;

import com.estudo.rafsburguer.entities.Order;
import com.estudo.rafsburguer.enums.Status;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByStatus(Status statusName, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

}
