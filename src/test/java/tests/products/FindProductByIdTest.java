package tests.products;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class FindProductByIdTest extends BaseApi {

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Finding products")
    @Story("STORY - Products")
    @DisplayName("Should find a product by ID")
    void shouldFindProductByID(){

        var userId = createUser();
        var token = getToken();
        var productId = createProduct(token);

        given()
            .when().get("produtos/"+ productId)
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(productId));

        deleteProduct(productId, token);
        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Finding products")
    @Story("STORY - Products")
    @DisplayName("Should return error when product doesn't exist")
    void shouldReturnErrorWhenProductDoesNotExist(){
        var nonExistentId = "123";

        given()
            .when().get("produtos/"+ nonExistentId)
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não encontrado"));
    }
}
