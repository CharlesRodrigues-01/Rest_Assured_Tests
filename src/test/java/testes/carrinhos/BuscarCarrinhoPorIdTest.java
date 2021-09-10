package testes.carrinhos;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.CarrinhoServices.cadastrarCarrinho;
import static service.CarrinhoServices.excluirCarrinho;
import static service.LoginServices.gerarToken;
import static service.ProdutoServices.cadastrarProduto;
import static service.ProdutoServices.excluirProduto;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public class BuscarCarrinhoPorIdTest extends BaseApi {

    @Test
    @DisplayName("Deve buscar um carrinho por id")
    public void deveBuscarCarrinhoPorId(){

        var idUsuarioCadastrado = cadastrarUsuario();
        var token = gerarToken();
        var idProdutoCadastrado = cadastrarProduto(token);
        List<String> produtos = new ArrayList<>();
        produtos.add(idProdutoCadastrado);
        var idCarrinhoCadastrado = cadastrarCarrinho(produtos, token);

        given()
                .when().get("carrinhos/"+ idCarrinhoCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(idCarrinhoCadastrado));

        excluirCarrinho(idCarrinhoCadastrado, token);
        excluirProduto(idProdutoCadastrado, token);
        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve retornar erro quando carrinho não existir")
    public void naoDeveBuscarCarrinhoInexistente(){
        var idInexistente = "123";

        given()
                .when().get("carrinhos/"+ idInexistente)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Carrinho não encontrado"));
    }
}
