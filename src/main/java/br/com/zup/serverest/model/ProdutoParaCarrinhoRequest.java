package br.com.zup.serverest.model;

public class ProdutoParaCarrinhoRequest {

    private String idProduto;
    private Integer quantidade;

    public ProdutoParaCarrinhoRequest(String idProduto, Integer quantidade) {
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
