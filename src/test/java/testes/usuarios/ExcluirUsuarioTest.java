package testes.usuarios;

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
import static service.UsuarioServices.*;

public class ExcluirUsuarioTest extends BaseApi {

    private String idInexistente = "123";

    @Test
    @DisplayName("Deve excluir um usuário por id")
    public void deveExcluirUsuario() {
        var idUsuarioCadastrado = cadastrarUsuario();

        given()
                .when().delete("usuarios/"+ idUsuarioCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar mensagem de erro quando usuário não existir")
    public void naoDeveExcluirUsuarioInexistente() {

        given()
                .when().delete("usuarios/"+ idInexistente)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Test
    @DisplayName("Não deve deletar usuário com carrinho cadastrado")
    public void naoDeveDeletarUsuarioComCarrinhoCadastrado() {

        List<String> produtos = new ArrayList<>();

        var idUsuarioCadastrado = cadastrarUsuario();
        var token = gerarToken();
        var idProdutoCadastrado = cadastrarProduto(token);
        produtos.add(idProdutoCadastrado);
        var idCarrinhoCadastrado = cadastrarCarrinho(produtos, token);

        given()
                .when().delete("usuarios/"+ idUsuarioCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido excluir usuário com carrinho cadastrado"));

        excluirCarrinho(idCarrinhoCadastrado, token);
        excluirProduto(idProdutoCadastrado, token);
        excluirUsuario(idUsuarioCadastrado);
    }
}
