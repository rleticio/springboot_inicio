package com.estudo.rafsburguer.exceptions;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(){
        super("Pedido não encontrado.");
    }
}
