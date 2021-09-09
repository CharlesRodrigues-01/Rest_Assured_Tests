package testes.produtos;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.LoginServices.gerarToken;
import static service.ProdutoServices.cadastrarProduto;
import static service.ProdutoServices.excluirProduto;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public class BuscarProdutoPorIdTest extends BaseApi {

    @Test
    @DisplayName("Deve buscar um produto por id")
    public void deveBuscarProdutoPorId(){

        var idUsuarioCadastrado = cadastrarUsuario();
        var token = gerarToken();
        var idProdutoCadastrado = cadastrarProduto(token);

        given()
                .when().get("produtos/"+ idProdutoCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(idProdutoCadastrado));

        excluirProduto(idProdutoCadastrado, token);
        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve retornar erro quando produto não existir")
    public void naoDeveBuscarProdutoInexistente(){
        var idInexistente = "123";

        given()
                .when().get("produtos/"+ idInexistente)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não encontrado"));
    }
}
