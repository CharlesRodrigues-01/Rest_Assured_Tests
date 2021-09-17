package tests.users;

import bases.BaseApi;
import br.com.zup.serverest.builder.UserBuilder;
import br.com.zup.serverest.factory.SimulationDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static br.com.zup.serverest.builder.UserBuilder.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetUsersTest extends BaseApi {

    protected static String userId;

    @BeforeAll
    public static void setUp(){
        userId = createUser();
    }

    @AfterAll
    public static void tearDown(){
        deleteUser(userId);
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Getting users")
    @Story("STORY - User")
    @DisplayName("Should find a user by ID")
    void shouldFindUserByID(){

        given().param("_id", userId)
            .when().get("usuarios")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(getAmountOfUsers("_id", userId)),
                        "usuarios.get(0)._id", equalTo(userId));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Getting users")
    @Story("STORY - User")
    @DisplayName("Should find a user by name")
    void shouldFindUserByName(){

        var name =  UserBuilder.buildUser().getNome();

        given().param("nome", name)
            .when().get("usuarios")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(getAmountOfUsers("nome", name)),
                "usuarios.get(0).nome", equalTo(name));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Getting users")
    @Story("STORY - User")
    @DisplayName("Should find a user by e-mail")
    void shouldFindUserByEmail(){
        var email = UserBuilder.buildUser().getEmail();

        given().param("email", email)
            .when().get("usuarios")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(getAmountOfUsers("email", email)),
                "usuarios.get(0).email", equalTo(email));
    }

    @Test
    @Epic("EPIC - User Test Epic")
    @Feature("FEATURE - Getting users")
    @Story("STORY - User")
    @DisplayName("Should find a user by password")
    void shouldFindUserByPassword(){
        var password = UserBuilder.buildUser().getPassword();

        given().param("password", password)
            .when().get("usuarios")
            .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(getAmountOfUsers("password", password)),
                "usuarios.get(0).password", equalTo(password));
    }
}
