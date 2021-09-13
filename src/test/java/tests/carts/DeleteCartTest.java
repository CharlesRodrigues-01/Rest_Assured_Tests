package tests.carts;

import bases.BaseCart;
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
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCartTest extends BaseCart {

    private List<String> products = new ArrayList<>();

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Deleting carts")
    @Story("STORY - Carts")
    @DisplayName("Must delete a cart")
    public void mustDeleteCart() {

        products.add(ProductId);
        createCart(products, token);

        given()
                .header("Authorization", token)
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Deleting carts")
    @Story("STORY - Carts")
    @DisplayName("Should return error when cart doesn't exist")
    public void shouldReturnErrorWhenCartDoesNotExist() {

        given()
                .header("Authorization", token)
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Não foi encontrado carrinho para esse usuário"));
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Deleting carts")
    @Story("STORY - Carts")
    @DisplayName("Must not delete a cart by user without authentication")
    public void mustNotDeleteCartByUserWithoutAuthentication() {

        products.add(ProductId);
        createCart(products, token);

        given()
                .when().delete("carrinhos/concluir-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));

        deleteCart(token);
    }
}