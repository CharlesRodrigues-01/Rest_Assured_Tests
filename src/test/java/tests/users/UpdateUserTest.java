package tests.users;

import bases.BaseApi;
import br.com.zup.serverest.factory.SimulationDataFactory;
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

class UpdateUserTest extends BaseApi {

    protected static SimulationDataFactory simulationDataFactory = new SimulationDataFactory();
    private final String nonExistentId = simulationDataFactory.generateId();

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Updating users")
    @Story("STORY - User")
    @DisplayName("Should create an user if ID doesn't exist")
    void shouldCreateAnUserIfIDDoesNotExist(){

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
    @DisplayName("Should edit an existing user")
    void shouldEditAnExistingUser(){

        var userId = createUser();

        var updatedUser = buildUser();
        updatedUser.setNome(simulationDataFactory.genereteName());
        updatedUser.setEmail(simulationDataFactory.genereteEmail());
        updatedUser.setPassword(simulationDataFactory.generetePassword());

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
    @DisplayName("Should not update or create user with existing e-mail and non-existent ID")
    void shouldNotUpdateOrCreateUserWithExistingEmailAndNonExistentID(){

        var userId = createUser();

        var updatedUser = buildUser();
        updatedUser.setNome(simulationDataFactory.genereteName());
        updatedUser.setPassword(simulationDataFactory.generetePassword());

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
