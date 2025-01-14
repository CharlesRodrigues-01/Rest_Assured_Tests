package tests.products;

import bases.BaseApi;
import br.com.zup.serverest.builder.ProductBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetProductsTest extends BaseApi {

    protected static String userId;
    protected static String token;
    protected static String productId;

    @BeforeAll
    public static void setUp(){
        userId = createUser();
        token = getToken();
        productId = createProduct(token);
    }

    @AfterAll
    public static void tearDown(){
        deleteProduct(productId, token);
        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Getting products")
    @Story("STORY - Products")
    @DisplayName("Should find a product by ID")
    void shouldFindProductByID(){

        given().param("_id", productId)
            .when().get("produtos")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("_id", productId)),
                "produtos.get(0)._id", equalTo(productId));
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Getting products")
    @Story("STORY - Products")
    @DisplayName("Should find a product by name")
    void shouldFindProductByName(){
        var name = ProductBuilder.buildProduct().getNome();

        given().param("nome", name)
            .when().get("produtos")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("nome", name)),
                "produtos.get(0).nome", equalTo(name));
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Getting products")
    @Story("STORY - Products")
    @DisplayName("Should find a product by price")
    void shouldFindProductByPrice(){
        var price = ProductBuilder.buildProduct().getPreco().toString();

        given().param("preco", price)
            .when().get("produtos")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("preco", price)),
                "produtos.get(0).preco", equalTo(Integer.parseInt(price)));
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Getting products")
    @Story("STORY - Products")
    @DisplayName("Should find a product by description")
    void shouldFindProductByDescription(){
        var description = ProductBuilder.buildProduct().getDescricao();

        given().param("descricao", description)
            .when().get("produtos")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("descricao", description)),
                "produtos.get(0).descricao", equalTo(description));
    }

    @Test
    @Epic("EPIC - Product Test Epic")
    @Feature("FEATURE - Getting products")
    @Story("STORY - Products")
    @DisplayName("Should find a product by amount")
    void shouldFindProductByAmount(){
        var amount = ProductBuilder.buildProduct().getQuantidade().toString();

        given().param("quantidade", amount)
            .when().get("produtos")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("quantidade", amount)),
                "produtos.get(0).quantidade", equalTo(Integer.parseInt(amount)));
    }
}
