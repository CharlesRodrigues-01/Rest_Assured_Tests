package br.com.zup.serverest.model;

public class ProductToCart {

    private String idProduto;
    private Integer quantidade;

    public ProductToCart(String idProduto, Integer quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
