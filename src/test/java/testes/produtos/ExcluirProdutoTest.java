package testes.produtos;

import bases.BaseProduto;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.CarrinhoServices.cadastrarCarrinho;
import static service.CarrinhoServices.excluirCarrinho;
import static service.ProdutoServices.cadastrarProduto;
import static service.ProdutoServices.excluirProduto;
import static service.UsuarioServices.atualizarPermissaoUsuario;

public class ExcluirProdutoTest extends BaseProduto {

    private String idInexistente = "123";
    private List<String> produtos = new ArrayList<>();

    @Test
    @DisplayName("Deve excluir um produto por id")
    public void deveExcluirProduto() {

        var idProdutoCadastrado = cadastrarProduto(token);

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ idProdutoCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar mensagem de erro quando produto não existir")
    public void naoDeveExcluirProdutoInexistente() {

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ idInexistente)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Test
    @DisplayName("Não deve excluir um produto sem autorização")
    public void naoDeveExcluirProdutoSemAutorizacao() {

        var idProdutoCadastrado = cadastrarProduto(token);

        given()
                .when().delete("produtos/"+ idProdutoCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));

        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve excluir um produto por usuário que não é administrador")
    public void naoDeveExcluirProdutoSemPermissao() {

        var usuarioAdministrador = "false";
        var usuarioNaoAdministrador = "true";
        var idProdutoCadastrado = cadastrarProduto(token);
        atualizarPermissaoUsuario(idUsuarioCadastrado, usuarioAdministrador);

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ idProdutoCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"));

        atualizarPermissaoUsuario(idUsuarioCadastrado, usuarioNaoAdministrador);
        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve deletar produto com carrinho cadastrado")
    public void naoDeveDeletarProdutoComCarrinho() {

        var idProdutoCadastrado = cadastrarProduto(token);
        produtos.add(idProdutoCadastrado);
        var idCarrinhoCadastrado = cadastrarCarrinho(produtos, token);

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ idProdutoCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido excluir produto que faz parte de carrinho"));

        excluirCarrinho(idCarrinhoCadastrado, token);
        excluirProduto(idProdutoCadastrado, token);
    }
}
