package tests.carts;

import bases.BaseCart;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import br.com.zup.serverest.model.Cart;
import br.com.zup.serverest.model.ProductToCart;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static br.com.zup.serverest.builder.CartBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostCartTest extends BaseCart {

    private List<String> products = new ArrayList<>();
    private List<ProductToCart> productsList = new ArrayList<>();

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Posting carts")
    @Story("STORY - Carts")
    @DisplayName("Must create a cart")
    public void mustCreateCart(){

        products.add(ProductId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildCart(products))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"));

        deleteCart(token);
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Posting carts")
    @Story("STORY - Carts")
    @DisplayName("Should not create a cart with duplicate products")
    public void shouldNotCreateCartWithDuplicateProducts(){

        products.add(ProductId);
        products.add(ProductId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildCart(products))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido possuir produto duplicado"));

    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Posting carts")
    @Story("STORY - Carts")
    @DisplayName("Should not create a cart with unregistered products")
    public void shouldNotCreateCartWithUnregisteredProducts(){

        var productId = "123";
        var amount = 1;
        ProductToCart productToCart = new ProductToCart(productId, amount);
        productsList.add(productToCart);
        Cart cartWithUnregisteredProduct = new Cart(productsList);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(cartWithUnregisteredProduct)
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não encontrado"));

    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Posting carts")
    @Story("STORY - Carts")
    @DisplayName("Should not create a cart if the stock quantity is insufficient")
    public void shouldNotCreateCartIfTheStockQuantityIsInsufficient(){

        var insufficientQuantity = 6;
        ProductToCart productToCart = new ProductToCart(
                ProductId, insufficientQuantity);
        productsList.add(productToCart);
        Cart cartWithStockQuantityIsInsufficient = new Cart(productsList);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(cartWithStockQuantityIsInsufficient)
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não possui quantidade suficiente"));
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Posting carts")
    @Story("STORY - Carts")
    @DisplayName("Must not create more than one cart by user")
    public void mustNotCreateMoreThanOneCartByUser(){

        products.add(ProductId);
        createCart(products, token);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildCart(products))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido ter mais de 1 carrinho"));

        deleteCart(token);
    }

    @Test
    @Epic("EPIC - Cart Test Epic")
    @Feature("FEATURE - Posting carts")
    @Story("STORY - Carts")
    @DisplayName("Must not create a cart by user without authentication")
    public void mustNotCreateCartByUserWithoutAuthentication(){

        products.add(ProductId);

        given()
                .contentType(ContentType.JSON)
                .body(buildCart(products))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário " +
                        "do token não existe mais"));
    }
}
