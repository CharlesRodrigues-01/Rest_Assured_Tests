package br.com.zup.serverest.model;

import java.util.List;

public class CarrinhoRequest {

    private List<ProdutoParaCarrinhoRequest> produtos;

    public CarrinhoRequest(List<ProdutoParaCarrinhoRequest> produtos) {
        this.produtos = produtos;
    }

    public List<ProdutoParaCarrinhoRequest> getProdutos() {
        return produtos;
    }
}
