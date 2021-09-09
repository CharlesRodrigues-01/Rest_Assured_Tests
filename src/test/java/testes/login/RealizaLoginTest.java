package testes.login;

import bases.BaseApi;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.LoginServices.construirLogin;
import static service.UsuarioServices.*;

public class RealizaLoginTest extends BaseApi {

    @Test
    @DisplayName("Deve gerar um token a partir de um usuario cadastrado")
    public void deveGerarToken(){

        var idUsuarioCadastrado = cadastrarUsuario();

        given()
                .contentType(ContentType.JSON)
                .body(construirLogin())
        .when().post("login")
        .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Login realizado com sucesso"));

        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Não deve gerar um token se o e-mail for inválido")
    public void naoDeveGerarTokenComEmailInvalido(){

        var login = construirLogin();
        login.setEmail("fulanoqa.com");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser um email válido"));;
    }

    @Test
    @DisplayName("Não deve gerar um token com um e-mail inexistente")
    public void naoDeveGerarTokenComEmailInexistente(){

        var login = construirLogin();
        login.setEmail("sicrano@qa.com");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Email e/ou senha inválidos"));;
    }

    @Test
    @DisplayName("Não deve gerar um token com um e-mail em branco")
    public void naoDeveGerarTokenComEmailEmBranco(){

        var login = construirLogin();
        login.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email não pode ficar em branco"));;
    }

    @Test
    @DisplayName("Não deve gerar um token de usuário com uma senha inexistente")
    public void naoDeveGerarTokenComSenhaInexistente(){

        var login = construirLogin();
        login.setPassword("123");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Email e/ou senha inválidos"));;
    }

    @Test
    @DisplayName("Não deve gerar um token de usuário com uma senha em branco")
    public void naoDeveGerarTokenComSenhaEmBranco(){

        var login = construirLogin();
        login.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password não pode ficar em branco"));;
    }

}
