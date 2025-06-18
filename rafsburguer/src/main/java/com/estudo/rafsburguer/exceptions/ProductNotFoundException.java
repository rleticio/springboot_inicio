package com.estudo.rafsburguer.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(){
        super("Produto não encontrado.");
    }
}
