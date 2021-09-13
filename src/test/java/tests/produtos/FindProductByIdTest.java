package tests.produtos;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FindProductByIdTest extends BaseApi {

    @Test
    @DisplayName("Must find a product by ID")
    public void mustFindProductByID(){

        var userId = createUser();
        var token = getToken();
        var productId = createProduct(token);

        given()
                .when().get("produtos/"+ productId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(productId));

        deleteProduct(productId, token);
        deleteUser(userId);
    }

    @Test
    @DisplayName("Should return error when product doesn't exist")
    public void shouldReturnErrorWhenProductDoesNotExist(){
        var nonExistentId = "123";

        given()
                .when().get("produtos/"+ nonExistentId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não encontrado"));
    }
}
