package tests.users;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.UserBuilder.*;

public class UpdateUserTest extends BaseApi {

    private String nonExistentId = "123";

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Updating users")
    @Story("STORY - User")
    @DisplayName("Must create an user if ID doesn't exist")
    public void mustCreateAnUserIfIDDoesNotExist(){

        String userId = given()
                .contentType(ContentType.JSON)
                .body(buildUser())
                .when().put("usuarios/"+nonExistentId)
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Updating users")
    @Story("STORY - User")
    @DisplayName("Must edit an existing user")
    public void mustEditAnExistingUser(){

        var userId = createUser();

        var updatedUser = buildUser();
        updatedUser.setNome("Updated User");
        updatedUser.setEmail("updated@qa.com.br");
        updatedUser.setPassword("321");

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when().put("usuarios/"+userId)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Updating users")
    @Story("STORY - User")
    @DisplayName("Must not update or create user with existing e-mail and non-existent ID")
    public void mustNotUpdateOrCreateUserWithExistingEmailAndNonExistentID(){

        var userId = createUser();

        var updatedUser = buildUser();
        updatedUser.setNome("Updated User");
        updatedUser.setPassword("321");

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when().put("usuarios/"+nonExistentId)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Este email já está sendo usado"));

        deleteUser(userId);
    }
}
