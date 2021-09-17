package tests.products;

import bases.BaseProduct;
import br.com.zup.serverest.factory.SimulationDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;

class UpdateProductTest extends BaseProduct {

    private String nonExistentId = simulationDataFactory.generateId();

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Updating products")
    @Story("STORY - Products")
    @DisplayName("Should create an product if ID doesn't exist")
    void shouldCreateAnProductIfIDDoesNotExist(){

        String productId = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(buildProduct())
            .when().put("produtos/"+nonExistentId)
            .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        deleteProduct(productId, token);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Updating products")
    @Story("STORY - Products")
    @DisplayName("Should edit an existing user")
    void shouldEditAnExistingUser(){

        var productId = createProduct(token);
        var amount = 5;

        var updatedProduct = buildProduct();
        updatedProduct.setNome(simulationDataFactory.genereteNameProduct());
        updatedProduct.setPreco(simulationDataFactory.generetePriceProduct() );
        updatedProduct.setDescricao(simulationDataFactory.genereteDescriptionProduct());
        updatedProduct.setQuantidade(amount);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(updatedProduct)
            .when().put("produtos/"+productId)
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        deleteProduct(productId, token);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Updating products")
    @Story("STORY - Products")
    @DisplayName("Should not update or create product with existing e-mail and non-existent ID")
    void shouldNotUpdateOrCreateProductWithExistingEmailAndNonExistentID(){

        var productId = createProduct(token);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(buildProduct())
            .when().put("produtos/"+nonExistentId)
            .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Já existe produto com esse nome"));

        deleteProduct(productId, token);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Updating products")
    @Story("STORY - Products")
    @DisplayName("Should not update or create product without admin permission")
    void shouldNotUpdateOrCreateProductWithoutAdminPermission(){

        var adminUser = "false";
        var nonAdminUser = "true";
        updateUserPermission(userId, adminUser);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(buildProduct())
            .when().put("produtos/"+nonExistentId)
            .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"));

        updateUserPermission(userId, nonAdminUser);
    }
}
