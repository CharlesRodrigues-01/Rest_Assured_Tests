package br.com.zup.serverest.builder;

import br.com.zup.serverest.model.Product;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class ProductBuilder {

    private ProductBuilder() {}

    public static Product buildProduct() {
        return new Product("TV", 2900, "LG", 5);
    }

    public static String createProduct(String token) {

        return given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .body(buildProduct())
                .when().post("produtos")
                .then()
                    .extract().path("_id");
    }

    public static void deleteProduct(String userId, String token){
        given().header("Authorization", token)
            .when().delete("produtos/"+ userId);
    }

    public static Integer getAmountOfProducts(String typeSearch, String parameter){
        return given().param(typeSearch, parameter)
                .when().get("produtos")
                .then().extract().path("quantidade");
    }
}
