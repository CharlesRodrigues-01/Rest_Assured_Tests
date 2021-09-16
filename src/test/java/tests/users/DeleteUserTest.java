package tests.users;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static br.com.zup.serverest.builder.CartBuilder.createCart;
import static br.com.zup.serverest.builder.CartBuilder.deleteCart;
import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.UserBuilder.*;

class DeleteUserTest extends BaseApi {

    private String nonExistentId = "123";

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Deleting users")
    @Story("STORY - User")
    @DisplayName("Should delete a user by ID")
    void shouldDeleteUserByID() {
        var userId = createUser();

        given()
                .when().delete("usuarios/"+ userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Deleting users")
    @Story("STORY - User")
    @DisplayName("Should return error when user doesn't exist")
    void shouldReturnErrorWhenUserDoesNotExist() {

        given()
                .when().delete("usuarios/"+ nonExistentId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Deleting users")
    @Story("STORY - User")
    @DisplayName("Should not delete user with registered cart")
    void shouldNotDeleteUserWithRegisteredCart() {

        List<String> products = new ArrayList<>();

        var userID = createUser();
        var token = getToken();
        var productId = createProduct(token);
        products.add(productId);
        createCart(products, token);

        given()
                .when().delete("usuarios/"+ userID)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido excluir usuário com carrinho cadastrado"));

        deleteCart(token);
        deleteProduct(productId, token);
        deleteUser(userID);
    }
}
