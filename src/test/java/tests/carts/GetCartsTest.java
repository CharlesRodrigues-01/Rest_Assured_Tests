package tests.carts;

import bases.BaseApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static br.com.zup.serverest.builder.CartBuilder.*;
import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetCartsTest extends BaseApi {

    protected static String userId;
    protected static String token;
    protected static String productId;
    protected static String cartId;
    protected static List<String> products = new ArrayList<>();

    @BeforeAll
    public static void setUp(){
        userId = createUser();
        token = getToken();
        productId = createProduct(token);
        products.add(productId);
        cartId = createCart(products, token);
    }

    @AfterAll
    public static void tearDown(){
        deleteCart(token);
        deleteProduct(productId, token);
        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Getting carts")
    @Story("STORY - Carts")
    @DisplayName("Should find a cart by ID")
    void shouldFindCartByID(){

        given().param("_id", cartId)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(getAmountOfCarts("_id", cartId)))
                .body("carrinhos.get(0)._id", equalTo(cartId));
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Getting carts")
    @Story("STORY - Carts")
    @DisplayName("Should find a cart by total price")
    void shouldFindCartByTotalPrice(){
        var totalPrice = "2900";

        given().param("precoTotal", totalPrice)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(getAmountOfCarts("precoTotal", totalPrice)))
                .body("carrinhos.get(0).precoTotal", equalTo(Integer.parseInt(totalPrice)));
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Getting carts")
    @Story("STORY - Carts")
    @DisplayName("Should find a cart by total amount")
    void shouldFindCartByTotalAmount(){
        var totalAmount = "1";

        given().param("quantidadeTotal", totalAmount)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(getAmountOfCarts("quantidadeTotal", totalAmount)))
                .body("carrinhos.get(0).quantidadeTotal", equalTo(Integer.parseInt(totalAmount)));
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Getting carts")
    @Story("STORY - Carts")
    @DisplayName("Should find a cart by user id")
    void shouldFindCartByUserId(){

        given().param("idUsuario", userId)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(getAmountOfCarts("idUsuario", userId)))
                .body("carrinhos.get(0).idUsuario", equalTo(userId));
    }
}
