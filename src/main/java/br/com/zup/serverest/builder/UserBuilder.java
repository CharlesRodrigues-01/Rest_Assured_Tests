package br.com.zup.serverest.builder;

import br.com.zup.serverest.model.User;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;

public class UserBuilder {

    private UserBuilder() {}

    public static User buildUser() {
       return new User("Authorized User",
               "authorized@qa.com.br",
               "12test",
               "true");
    }

    public static String createUser() {

        return given()
                    .contentType(ContentType.JSON)
                    .body(buildUser())
                .when().post("usuarios")
                .then()
                    .extract().path("_id");
    }

    public static void deleteUser(String userId){
        delete("usuarios/"+ userId);
    }

    public static void updateUserPermission(String userId, String admin){
        var updatedUser = buildUser();
        updatedUser.setAdministrador(admin);

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
            .when().put("usuarios/"+userId);
    }

    public static Integer getAmountOfUsers(String typeSearch, String parameter){
        return given()
                .param(typeSearch, parameter)
            .when().get("usuarios")
            .then().extract().path("quantidade");
    }
}
