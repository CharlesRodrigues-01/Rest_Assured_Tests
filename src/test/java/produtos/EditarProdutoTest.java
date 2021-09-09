package produtos;

import bases.BaseProduto;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.ProdutoServices.*;
import static service.UsuarioServices.*;

public class EditarProdutoTest extends BaseProduto {

    private String idInexistente = "123";

    @Test
    @DisplayName("Deve cadastrar um produto caso o ID for inexistente")
    public void deveCadastrarProdutoInexistente(){

        String idProdutoCadastrado = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(construirProduto())
                .when().put("produtos/"+idInexistente)
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Deve editar um produto existente")
    public void deveEditarProdutoExistente(){

        var idProdutoCadastrado = cadastrarProduto(token);

        var produtoAtualizado = construirProduto();
        produtoAtualizado.setNome("Celular");
        produtoAtualizado.setPreco(2900);
        produtoAtualizado.setDescricao("S9");
        produtoAtualizado.setQuantidade(5);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(produtoAtualizado)
                .when().put("produtos/"+idProdutoCadastrado)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve atualizar ou criar produto com nome existente e ID inexistente")
    public void naoDeveAtualizarNemCriarProdutoComNomeExistenteEIdInexistente(){

        var idProdutoCadastrado = cadastrarProduto(token);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(construirProduto())
                .when().put("produtos/"+idInexistente)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Já existe produto com esse nome"));

        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve atualizar ou criar produto sem autorização")
    public void naoDeveAtualizarNemCriarProdutoSemAutorizacao(){

        var usuarioAdministrador = "false";
        var usuarioNaoAdministrador = "true";
        atualizarPermissaoUsuario(idUsuarioCadastrado, usuarioAdministrador);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(construirProduto())
                .when().put("produtos/"+idInexistente)
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"));

        atualizarPermissaoUsuario(idUsuarioCadastrado, usuarioNaoAdministrador);
    }
}
