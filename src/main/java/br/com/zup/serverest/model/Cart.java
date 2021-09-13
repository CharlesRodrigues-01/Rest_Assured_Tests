package br.com.zup.serverest.model;

import java.util.List;

public class Cart {

    private List<ProductToCart> produtos;

    public Cart(List<ProductToCart> produtos) {
        this.produtos = produtos;
    }

    public List<ProductToCart> getProdutos() {
        return produtos;
    }
}
