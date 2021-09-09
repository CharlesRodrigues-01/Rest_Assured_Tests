package service;

import io.restassured.http.ContentType;
import model.LoginRequest;

import static io.restassured.RestAssured.given;
import static service.UsuarioServices.*;

public class LoginServices {

    public static LoginRequest construirLogin() {
        LoginRequest login = new LoginRequest(construirUsuario().getEmail(), construirUsuario().getPassword());
        return login;
    }

    public static String gerarToken() {
        String token = given()
                .contentType(ContentType.JSON)
                .body(construirLogin())
                .when().post("login")
                .then().log().all()
                .extract().path("authorization");
        return token;
    }
}
