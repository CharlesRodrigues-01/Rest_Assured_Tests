package br.com.zup.serverest.builder;

import io.restassured.http.ContentType;
import br.com.zup.serverest.model.User;

import static io.restassured.RestAssured.given;

public class UserBuilder {

   public static User buildUser() {
       return new User("Authorized User",
               "authorized@qa.com.br",
               "1234",
               "true");
    }

    public static String createUser() {

        return given()
                .contentType(ContentType.JSON)
                .body(buildUser())
                .when().post("usuarios")
                .then().log().all()
                .extract().path("_id");
    }

    public static void deleteUser(String userId){
        given()
                .when().delete("usuarios/"+ userId)
                .then()
                .log().all();
    }

    public static void updateUserPermission(String userId, String admin){
        var updatedUser = buildUser();
        updatedUser.setAdministrador(admin);

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when().put("usuarios/"+userId)
                .then().log().all();
    }

    public static Integer getAmountOfUsers(String typeSearch, String parameter){
        return given()
                .param(typeSearch, parameter)
                .when().get("usuarios")
                .then().extract().path("quantidade");
    }
}
