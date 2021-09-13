package tests.produtos;

import bases.BaseApi;
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
    @DisplayName("Must find a product by ID")
    public void mustFindProductByID(){

        given().param("_id", productId)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("_id", productId)))
                .body("produtos.get(0)._id", equalTo(productId));
    }

    @Test
    @DisplayName("Must find a product by name")
    public void mustFindProductByName(){
        var name = "TV";

        given().param("nome", name)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("nome", name)))
                .body("produtos.get(0).nome", equalTo(name));
    }

    @Test
    @DisplayName("Must find a product by price")
    public void mustFindProductByPrice(){
        var price = "2900";

        given().param("preco", price)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("preco", price)))
                .body("produtos.get(0).preco", equalTo(Integer.parseInt(price)));
    }

    @Test
    @DisplayName("Must find a product by description")
    public void mustFindProductByDescription(){
        var description = "LG";

        given().param("descricao", description)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("descricao", description)))
                .body("produtos.get(0).descricao", equalTo(description));
    }

    @Test
    @DisplayName("Must find a product by amount")
    public void mustFindProductByAmount(){
        var amount = "5";

        given().param("quantidade", amount)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(getAmountOfProducts("quantidade", amount)))
                .body("produtos.get(0).quantidade", equalTo(Integer.parseInt(amount)));
    }
}
