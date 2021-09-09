package service;

import io.restassured.http.ContentType;
import model.CarrinhoRequest;
import model.ProdutoParaCarrinhoRequest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class CarrinhoServices {

    public static CarrinhoRequest construirCarrinho(List<String> produtosCadastrados) {

        List<ProdutoParaCarrinhoRequest> listaDeProdutos = new ArrayList<>();
        var quantidadePedido = 1;

        for(String produto : produtosCadastrados) {
            String idProduto = get("produtos/" + produto).then().extract().path("_id");
            Integer quantidade = get("produtos/" + produto).then().extract().path("quantidade");
            ProdutoParaCarrinhoRequest produtoParaCarrinho = new ProdutoParaCarrinhoRequest(idProduto, quantidadePedido);
            listaDeProdutos.add(produtoParaCarrinho);
        }
        CarrinhoRequest carrinho = new CarrinhoRequest(listaDeProdutos);
        return carrinho;
    }

    public static String cadastrarCarrinho(List<String> produtosCadastrados, String token) {

        String idCarrinhoCadastrado = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirCarrinho(produtosCadastrados))
                .when().post("carrinhos")
                .then().log().all()
                .extract().path("_id");

        return idCarrinhoCadastrado;
    }

    public static void excluirCarrinho(String id, String token){
        given().header("Authorization", token)
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all();
    }
}
