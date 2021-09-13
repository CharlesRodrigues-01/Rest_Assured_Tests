package tests.users;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import br.com.zup.serverest.model.User;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.UserBuilder.*;

public class PostUserTest extends BaseApi {

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must create a user")
    public void mustCreateUser(){

        String userId = given()
                .contentType(ContentType.JSON)
                .body(buildUser())
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must not create a user with existing e-mail")
    public void mustNotCreateUserWithExistingEmail() {

        var userId = createUser();

        given()
                .contentType(ContentType.JSON)
                .body(buildUser())
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Este email já está sendo usado"));

        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must not create a user with blank name")
    public void mustNotCreateUserWithBlankName(){

        User newUser = buildUser();
        newUser.setNome("");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome não pode ficar em branco"));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must not create a user with invalid e-mail")
    public void mustNotCreateUserWithInvalidEmail(){

        User newUser = buildUser();
        newUser.setEmail("testeqa.com.br");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must not create a user with blank e-mail")
    public void mustNotCreateUserWithBlankEmail(){

        User newUser = buildUser();
        newUser.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email não pode ficar em branco"));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must not create a user with blank password")
    public void mustNotCreateUserWithBlankPassword(){

        User newUser = buildUser();
        newUser.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password não pode ficar em branco"));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Posting users")
    @Story("STORY - User")
    @DisplayName("Must not create a user with blank administrator")
    public void mustNotCreateUserWithBlankAdministrator(){

        User newUser = buildUser();
        newUser.setAdministrador("");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post("usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }
}
