package br.com.zup.serverest.builder;

import br.com.zup.serverest.model.Cart;
import br.com.zup.serverest.model.ProductToCart;
import io.restassured.http.ContentType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class CartBuilder {

    private CartBuilder() {}

    public static Cart buildCart(List<String> products) {

        List<ProductToCart> productsList = new ArrayList<>();
        var amount = 1;

        for(String product : products) {
            String productId = get("produtos/" + product).then().extract().path("_id");
            ProductToCart productToCart = new ProductToCart(productId, amount);
            productsList.add(productToCart);
        }
        return new Cart(productsList);
    }

    public static String createCart(List<String> products, String token) {

        return given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .body(buildCart(products))
                .when().post("carrinhos")
                .then()
                    .extract().path("_id");
    }

    public static void deleteCart(String token){
        given().header("Authorization", token)
                .when().delete("carrinhos/cancelar-compra");
    }

    public static Integer getAmountOfCarts(String typeSearch, String parameter){
        return given().param(typeSearch, parameter)
                .when().get("carrinhos")
                .then().extract().path("quantidade");
    }
}
