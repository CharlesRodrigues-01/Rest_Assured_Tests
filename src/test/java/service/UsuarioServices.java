package service;

import io.restassured.http.ContentType;
import br.com.zup.serverest.model.UsuarioRequest;

import static io.restassured.RestAssured.given;

public class UsuarioServices {

   public static UsuarioRequest construirUsuario() {
        UsuarioRequest usuario = new UsuarioRequest("Usuário autorizado",
                "autorizado@qa.com.br",
                "1234",
                "true");
        return usuario;
    }

    public static String cadastrarUsuario() {

        String response = given()
                .contentType(ContentType.JSON)
                .body(construirUsuario())
                .when().post("usuarios")
                .then().log().all()
                .extract().path("_id");
        return response;
    }

    public static void excluirUsuario(String id){
        given()
                .when().delete("usuarios/"+ id)
                .then()
                .log().all();
    }

    public static void atualizarPermissaoUsuario(String idUsuarioCadastrado, String administrador){
        var usuarioAtualizado = construirUsuario();
        usuarioAtualizado.setAdministrador(administrador);

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when().put("usuarios/"+idUsuarioCadastrado)
                .then().log().all();
    }
}
