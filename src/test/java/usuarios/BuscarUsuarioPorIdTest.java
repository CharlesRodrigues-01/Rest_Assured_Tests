package usuarios;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.UsuarioServices.*;

public class BuscarUsuarioPorIdTest extends BaseApi {

    @Test
    @DisplayName("Deve buscar um usuario por id")
    public void deveBuscarUsuarioPorId(){
        var idUsuarioCadastrado = cadastrarUsuario();

        given()
                .when().get("usuarios/"+ idUsuarioCadastrado)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(idUsuarioCadastrado));

        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve retornar erro quando usuário não existir")
    public void naoDeveBuscarUsuarioInexistente(){
        var idInexistente = "123";

        given()
                .when().get("usuarios/"+ idInexistente)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Usuário não encontrado"));
    }
}
