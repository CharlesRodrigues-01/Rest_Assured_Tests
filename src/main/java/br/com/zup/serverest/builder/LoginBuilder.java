package br.com.zup.serverest.builder;

import io.restassured.http.ContentType;
import br.com.zup.serverest.model.Login;

import static io.restassured.RestAssured.given;

public class LoginBuilder {

    public static Login buildLogin() {
        return new Login(UserBuilder.buildUser().getEmail(), UserBuilder.buildUser().getPassword());
    }

    public static String getToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(buildLogin())
                .when().post("login")
                .then().log().all()
                .extract().path("authorization");
    }
}
