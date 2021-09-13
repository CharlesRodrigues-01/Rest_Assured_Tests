package tests.carts;

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
import static br.com.zup.serverest.builder.UserBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FindCartByIdTest extends BaseApi {

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Finding carts")
    @Story("STORY - Carts")
    @DisplayName("Must find a cart by ID")
    public void mustFindCartByID(){

        var userId = createUser();
        var token = getToken();
        var productId = createProduct(token);
        List<String> products = new ArrayList<>();
        products.add(productId);
        var cartId = createCart(products, token);

        given()
                .when().get("carrinhos/"+ cartId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(cartId));

        deleteCart(token);
        deleteProduct(productId, token);
        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Finding carts")
    @Story("STORY - Carts")
    @DisplayName("Should return error when cart doesn't exist")
    public void shouldReturnErrorWhenCartDoesNotExist(){
        var nonExistentId = "123";

        given()
                .when().get("carrinhos/"+ nonExistentId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Carrinho não encontrado"));
    }
}
