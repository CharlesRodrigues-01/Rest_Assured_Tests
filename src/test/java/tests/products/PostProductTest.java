package tests.products;

import bases.BaseProduct;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.serverest.builder.UserBuilder.updateUserPermission;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static br.com.zup.serverest.builder.ProductBuilder.*;

class PostProductTest extends BaseProduct {

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Posting products")
    @Story("STORY - Products")
    @DisplayName("Should create a product")
    void shouldCreateProduct(){

        String productId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildProduct())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        deleteProduct(productId, token);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Posting products")
    @Story("STORY - Products")
    @DisplayName("Should not create a product with existing name")
    void shouldNotCreateUserWithExistingName(){

        var productId = createProduct(token);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildProduct())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Já existe produto com esse nome"));

        deleteProduct(productId, token);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Posting products")
    @Story("STORY - Products")
    @DisplayName("Should not create a product by user without authentication")
    void shouldNotCreateProductByUserWithoutAuthentication(){

        given()
                .contentType(ContentType.JSON)
                .body(buildProduct())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou " +
                        "usuário do token não existe mais"));
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Posting products")
    @Story("STORY - Products")
    @DisplayName("Should not create product without admin permission")
    void shouldNotCreateProductWithoutAdminPermission(){

        var adminUser = "false";
        var nonAdminUser = "true";
        updateUserPermission(userId, adminUser);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildProduct())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Rota exclusiva para administradores"))
                .extract().path("_id");

        updateUserPermission(userId, nonAdminUser);
    }
}
