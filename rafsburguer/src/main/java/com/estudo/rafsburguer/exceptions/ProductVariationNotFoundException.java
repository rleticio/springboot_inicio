package com.estudo.rafsburguer.exceptions;

public class ProductVariationNotFoundException extends RuntimeException {

    public ProductVariationNotFoundException(){
        super("Variação de produto não encontrada.");
    }
}
