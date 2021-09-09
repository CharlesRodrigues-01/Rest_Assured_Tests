package testes.usuarios;

import bases.BaseApi;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.UsuarioServices.*;

public class EditarUsuárioTest extends BaseApi {

    private String idInexistente = "123";

    @Test
    @DisplayName("Deve cadastrar um usuário caso o ID for inexistente")
    public void deveCadastrarUsuarioInexistente(){

        String idUsuarioCadastrado = given()
                .contentType(ContentType.JSON)
                .body(construirUsuario())
                .when().put("usuarios/"+idInexistente)
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve editar um usuário existente")
    public void deveEditarUsuarioExistente(){

        var idUsuarioCadastrado = cadastrarUsuario();

        var usuarioAtualizado = construirUsuario();
        usuarioAtualizado.setNome("Pessoa atualizada");
        usuarioAtualizado.setEmail("atualiza@qa.com.br");
        usuarioAtualizado.setPassword("321");

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when().put("usuarios/"+idUsuarioCadastrado)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));

        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Não deve atualizar ou criar usuário com e-mail existente e ID inexistente")
    public void naoDeveAtualizarNemCriarUsuarioComEmailExistenteEIdInexistente(){

        var idUsuarioCadastrado = cadastrarUsuario();

        var usuarioAtualizado = construirUsuario();
        usuarioAtualizado.setNome("Pessoa atualizada");
        usuarioAtualizado.setPassword("321");

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when().put("usuarios/"+idInexistente)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Este email já está sendo usado"));

        excluirUsuario(idUsuarioCadastrado);
    }
}
