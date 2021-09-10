package service;

import io.restassured.http.ContentType;
import br.com.zup.serverest.model.ProdutoRequest;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProdutoServices {

    public static ProdutoRequest construirProduto() {
        ProdutoRequest produto = new ProdutoRequest("TV", 2900, "LG", 5);
        return produto;
    }

    public static String cadastrarProduto(String token) {

        String response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirProduto())
                .when().post("produtos")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");
        return response;
    }

    public static void excluirProduto(String id, String token){
        given().header("Authorization", token)
                .when().delete("produtos/"+ id)
                .then()
                .log().all();
    }
}
