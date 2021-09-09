package produtos;

import bases.BaseProduto;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.ProdutoServices.*;
import static service.UsuarioServices.atualizarPermissaoUsuario;

public class CadastrarProdutoTest extends BaseProduto {

    @Test
    @DisplayName("Deve cadastrar um produto")
    public void deveCadastrarProduto(){

        String idProdutoCadastrado = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirProduto())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve cadastrar um produto com mesmo nome de produto existente")
    public void naoDeveCadastrarProdutoComNomeExistente(){

        var idProdutoCadastrado = cadastrarProduto(token);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirProduto())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Já existe produto com esse nome"));

        excluirProduto(idProdutoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve cadastrar um produto por usuário sem autenticação")
    public void naoDeveCadastrarProdutoSemAutenticacao(){

        given()
                .contentType(ContentType.JSON)
                .body(construirProduto())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou " +
                        "usuário do token não existe mais"));
    }

    @Test
    @DisplayName("Não deve cadastrar um produto por usuário sem permissão de administrador")
    public void naoDeveCadastrarProdutoSemPermissaoDeAdministrador(){

        var usuarioAdministrador = "false";
        var usuarioNaoAdministrador = "true";
        atualizarPermissaoUsuario(idUsuarioCadastrado, usuarioAdministrador);

        String idProdutoCadastrado = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirProduto())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"))
                .extract().path("_id");

        atualizarPermissaoUsuario(idUsuarioCadastrado, usuarioNaoAdministrador);
    }
}
