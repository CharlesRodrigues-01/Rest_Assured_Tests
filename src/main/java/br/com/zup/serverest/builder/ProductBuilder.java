package br.com.zup.serverest.builder;

import io.restassured.http.ContentType;
import br.com.zup.serverest.model.Product;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProductBuilder {

    public static Product buildProduct() {
        return new Product("TV", 2900, "LG", 5);
    }

    public static String createProduct(String token) {

        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(buildProduct())
                .when().post("produtos")
                .then().log().all()
                .extract().path("_id");
    }

    public static void deleteProduct(String userId, String token){
        given().header("Authorization", token)
                .when().delete("produtos/"+ userId)
                .then()
                .log().all();
    }

    public static Integer getAmountOfProducts(String typeSearch, String parameter){
        return given().param(typeSearch, parameter)
                .when().get("produtos")
                .then().extract().path("quantidade");
    }
}
