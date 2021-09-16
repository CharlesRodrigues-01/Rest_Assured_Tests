package tests.login;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.serverest.builder.LoginBuilder.buildLogin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.UserBuilder.*;

 class LoginTest extends BaseApi {

    @Test
    @Epic("EPIC - Login Test Epic")
    @Feature("FEATURE - Generating token")
    @Story("STORY - Login")
    @DisplayName("Should generate a user token")
    void shouldGenerateUserToken(){

        var userId = createUser();

        given()
                .contentType(ContentType.JSON)
                .body(buildLogin())
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Login realizado com sucesso"));

        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - Login Test Epic")
    @Feature("FEATURE - Generating token")
    @Story("STORY - Login")
    @DisplayName("Should not generate a token with invalid e-mail")
    void shouldNotGenerateTokenWithInvalidEmail(){

        var login = buildLogin();
        login.setEmail("fulanoqa.com");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    @Epic("EPIC - Login Test Epic")
    @Feature("FEATURE - Generating token")
    @Story("STORY - Login")
    @DisplayName("Should not generate a token with unregistered e-mail")
    void shouldNotGenerateTokenWithUnregisteredEmail(){

        var login = buildLogin();
        login.setEmail("sicrano@qa.com");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }

    @Test
    @Epic("EPIC - Login Test Epic")
    @Feature("FEATURE - Generating token")
    @Story("STORY - Login")
    @DisplayName("Should not generate a token with blank e-mail")
    void shouldNotGenerateTokenWithBlankEmail(){

        var login = buildLogin();
        login.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email não pode ficar em branco"));
    }

    @Test
    @Epic("EPIC - Login Test Epic")
    @Feature("FEATURE - Generating token")
    @Story("STORY - Login")
    @DisplayName("Should not generate a token with non-existent password")
    void shouldNotGenerateTokenWithNonExistentPassword(){

        var login = buildLogin();
        login.setPassword("123");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }

    @Test
    @Epic("EPIC - Login Test Epic")
    @Feature("FEATURE - Generating token")
    @Story("STORY - Login")
    @DisplayName("Should not generate a token with blank password")
    void shouldNotGenerateTokenWithBlankPassword(){

        var login = buildLogin();
        login.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(login)
                .when().post("login")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password não pode ficar em branco"));
    }

}
