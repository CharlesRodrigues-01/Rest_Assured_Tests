package tests.users;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.UserBuilder.*;

public class FindUserByIdTest extends BaseApi {

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Finding users")
    @Story("STORY - User")
    @DisplayName("Must find a user by ID")
    public void mustFindUserByID(){
        var userId = createUser();

        given()
                .when().get("usuarios/"+ userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(userId));

        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Finding users")
    @Story("STORY - User")
    @DisplayName("Should return error when user doesn't exist")
    public void shouldReturnErrorWhenUserDoesNotExist(){
        var nonExistentId = "123";

        given()
                .when().get("usuarios/"+ nonExistentId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Usuário não encontrado"));
    }
}
