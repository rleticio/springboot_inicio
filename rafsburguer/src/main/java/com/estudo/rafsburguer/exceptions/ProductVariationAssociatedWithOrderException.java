package com.estudo.rafsburguer.exceptions;

public class ProductVariationAssociatedWithOrderException extends RuntimeException {

    public ProductVariationAssociatedWithOrderException(){
        super("A variação do produto não pode ser excluída porque está associada a um ou mais pedidos.");
    }
}
