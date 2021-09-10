package testes.carrinhos;

import bases.BaseCarrinho;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.CarrinhoServices.cadastrarCarrinho;
import static service.CarrinhoServices.excluirCarrinho;

public class ExcluirCarrinhoTest extends BaseCarrinho {

    private List<String> produtos = new ArrayList<>();


    @Test
    @DisplayName("Deve excluir o carrinho cadastrado")
    public void deveExcluirCarrinho() {

        produtos.add(idProdutoCadastrado);
        cadastrarCarrinho(produtos, token);

        given()
                .header("Authorization", token)
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @DisplayName("Não deve excluir o carrinho inexistente")
    public void naoDeveExcluirCarrinhoInexistente() {

        given()
                .header("Authorization", token)
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Não foi encontrado carrinho para esse usuário"));
    }

    @Test
    @DisplayName("Não deve excluir o carrinho sem autorização")
    public void naoDeveExcluirCarrinhoSemAutorizacao() {

        produtos.add(idProdutoCadastrado);
        var idCarrinhoCadastrado = cadastrarCarrinho(produtos, token);

        given()
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));

        excluirCarrinho(idCarrinhoCadastrado, token);
    }
}
