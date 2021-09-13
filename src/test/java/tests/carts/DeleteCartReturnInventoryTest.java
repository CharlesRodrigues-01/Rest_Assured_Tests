package tests.carts;

import bases.BaseCart;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static br.com.zup.serverest.builder.CartBuilder.createCart;
import static br.com.zup.serverest.builder.CartBuilder.deleteCart;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCartReturnInventoryTest extends BaseCart {

    private List<String> products = new ArrayList<>();

    @Test
    @DisplayName("Must delete a product and return inventory")
    public void mustDeleteProductAndReturnInventory() {

        products.add(ProductId);
        createCart(products, token);

        given()
                .header("Authorization", token)
                .when().delete("carrinhos/cancelar-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso. Estoque dos produtos reabastecido"));
    }

    @Test
    @DisplayName("Should return error when cart doesn't exist")
    public void shouldReturnErrorWhenCartDoesNotExist() {

        given()
                .header("Authorization", token)
                .when().delete("carrinhos/cancelar-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Não foi encontrado carrinho para esse usuário"));
    }

    @Test
    @DisplayName("Must not delete a  cart by user without authentication")
    public void mustNotDeleteCartByUserWithoutAuthentication() {

        products.add(ProductId);
        createCart(products, token);

        given()
                .when().delete("carrinhos/cancelar-compra")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário " +
                        "do token não existe mais"));

        deleteCart(token);
    }

}
