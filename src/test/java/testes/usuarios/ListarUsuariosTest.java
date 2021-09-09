package testes.usuarios;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public class ListarUsuariosTest extends BaseApi {

    protected static String idUsuarioCadastrado;

    @BeforeAll
    public static void setUp(){
        idUsuarioCadastrado = cadastrarUsuario();
    }

    @AfterAll
    public static void tearDown(){
        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve listar um usuario por id")
    public void deveListarUsuarioPorId(){

        given().param("_id", idUsuarioCadastrado)
                .when().get("usuarios")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(retornarTamanhoLista("_id", idUsuarioCadastrado)))
                .body("usuarios.get(0)._id", equalTo(idUsuarioCadastrado));
    }

    @Test
    @DisplayName("Deve listar um usuario por nome")
    public void deveListarUsuarioPorNome(){
        var nome = "Usuário autorizado";

        given().param("nome", nome)
                .when().get("usuarios")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(retornarTamanhoLista("nome", nome)))
                .body("usuarios.get(0).nome", equalTo(nome));
    }

    @Test
    @DisplayName("Deve listar um usuario por e-mail")
    public void deveListarUsuarioPorEmail(){
        var email = "autorizado@qa.com.br";

        given().param("email", email)
                .when().get("usuarios")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(retornarTamanhoLista("email", email)))
                .body("usuarios.get(0).email", equalTo(email));
    }

    @Test
    @DisplayName("Deve listar um usuario por password")
    public void deveListarUsuarioPorPassword(){
        var password = "1234";

        given().param("password", password)
                .when().get("usuarios")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.size()", equalTo(retornarTamanhoLista("password", password)))
                .body("usuarios.get(0).password", equalTo(password));
    }

    public Integer retornarTamanhoLista(String tipoBusca, String parametro){
        Integer quantidade = given()
                .param(tipoBusca, parametro)
                .when().get("usuarios")
                .then().extract().path("quantidade");
        return quantidade;
    }
}
