package tests.produtos;

import bases.BaseProduct;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static br.com.zup.serverest.builder.CartBuilder.createCart;
import static br.com.zup.serverest.builder.CartBuilder.deleteCart;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.updateUserPermission;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteProductTest extends BaseProduct {

    private String nonExistentId = "123";
    private List<String> products = new ArrayList<>();

    @Test
    @DisplayName("Must delete a product by ID")
    public void mustDeleteProductByID() {

        var productId = createProduct(token);

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ productId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @DisplayName("Should return error when product doesn't exist")
    public void shouldReturnErrorWhenProductDoesNotExist() {

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ nonExistentId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Test
    @DisplayName("Must not delete a product by user without authentication")
    public void mustNotDeleteProductByUserWithoutAuthentication() {

        var productId = createProduct(token);

        given()
                .when().delete("produtos/"+ productId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário " +
                        "do token não existe mais"));

        deleteProduct(productId, token);
    }

    @Test
    @DisplayName("Must not delete a product without admin permission")
    public void mustNotDeleteProductWithoutAdminPermission() {

        var adminUser = "false";
        var nonAdminUser = "true";
        var productId = createProduct(token);
        updateUserPermission(userId, adminUser);

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ productId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"));

        updateUserPermission(userId, nonAdminUser);
        deleteProduct(productId, token);
    }

    @Test
    @DisplayName("Must not delete product with registered cart")
    public void mustNotDeleteProductWithRegisteredCart() {

        var productId = createProduct(token);
        products.add(productId);
        createCart(products, token);

        given()
                .header("Authorization", token)
                .when().delete("produtos/"+ productId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido excluir produto que faz parte de carrinho"));

        deleteCart(token);
        deleteProduct(productId, token);
    }
}
