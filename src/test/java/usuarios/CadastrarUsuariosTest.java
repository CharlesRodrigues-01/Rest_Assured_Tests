package usuarios;

import bases.BaseApi;
import io.restassured.http.ContentType;
import model.UsuarioRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.UsuarioServices.*;

public class CadastrarUsuariosTest extends BaseApi {

    @Test
    @DisplayName("Deve cadastrar um usuário")
    public void deveCadastrarUsuario(){

        String idUsuarioCadastrado = given()
                .contentType(ContentType.JSON)
                .body(construirUsuario())
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        excluirUsuario(idUsuarioCadastrado);

    }

    @Test
    @DisplayName("Não deve cadastrar um usuário com e-mail que já existe")
    public void naoDeveCadastrarUsuarioComEmailExistente() {

        var idUsuarioCadastrado = cadastrarUsuario();

        given()
                .contentType(ContentType.JSON)
                .body(construirUsuario())
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Este email já está sendo usado"));

        excluirUsuario(idUsuarioCadastrado);

    }

    @Test
    @DisplayName("Não deve cadastrar um usuário com nome em branco")
    public void naoDeveCadastrarUsuarioComNomeEmBranco(){

        UsuarioRequest novoUsuario = construirUsuario();
        novoUsuario.setNome("");

        given()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome não pode ficar em branco"));
    }

    @Test
    @DisplayName("Não deve cadastrar um usuário com e-mail inválido")
    public void naoDeveCadastrarUsuarioComEmailInvalido(){

        UsuarioRequest novoUsuario = construirUsuario();
        novoUsuario.setEmail("testeqa.com.br");

        given()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    @DisplayName("Não deve cadastrar um usuário com e-mail em branco")
    public void naoDeveCadastrarUsuarioComEmailEmBranco(){

        UsuarioRequest novoUsuario = construirUsuario();
        novoUsuario.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email não pode ficar em branco"));
    }

    @Test
    @DisplayName("Não deve cadastrar um usuário com senha em branco")
    public void naoDeveCadastrarUsuarioComSenhaEmBranco(){

        UsuarioRequest novoUsuario = construirUsuario();
        novoUsuario.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password não pode ficar em branco"));
    }

    @Test
    @DisplayName("Não deve cadastrar um usuário com 'administrador' em branco")
    public void naoDeveCadastrarUsuarioSemPermissaoDeAdministrador(){

        UsuarioRequest novoUsuario = construirUsuario();
        novoUsuario.setAdministrador("");

        given()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }
}
