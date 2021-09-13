package tests.produtos;

import bases.BaseProduct;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;

public class UpdateProductTest extends BaseProduct {

    private String nonExistentId = "123";

    @Test
    @DisplayName("Must create an product if ID doesn't exist")
    public void mustCreateAnProductIfIDDoesNotExist(){

        String productId = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(buildProduct())
                .when().put("produtos/"+nonExistentId)
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        deleteProduct(productId, token);
    }

    @Test
    @DisplayName("Must edit an existing user")
    public void mustEditAnExistingUser(){

        var productId = createProduct(token);

        var updatedProduct = buildProduct();
        updatedProduct.setNome("Celular");
        updatedProduct.setPreco(2900);
        updatedProduct.setDescricao("S9");
        updatedProduct.setQuantidade(5);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(updatedProduct)
                .when().put("produtos/"+productId)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        deleteProduct(productId, token);
    }

    @Test
    @DisplayName("Must not update or create product with existing e-mail and non-existent ID")
    public void mustNotUpdateOrCreateProductWithExistingEmailAndNonExistentID(){

        var productId = createProduct(token);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(buildProduct())
                .when().put("produtos/"+nonExistentId)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Já existe produto com esse nome"));

        deleteProduct(productId, token);
    }

    @Test
    @DisplayName("Must not update or create product without admin permission")
    public void mustNotUpdateOrCreateProductWithoutAdminPermission(){

        var adminUser = "false";
        var nonAdminUser = "true";
        updateUserPermission(userId, adminUser);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(buildProduct())
                .when().put("produtos/"+nonExistentId)
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"));

        updateUserPermission(userId, nonAdminUser);
    }
}
