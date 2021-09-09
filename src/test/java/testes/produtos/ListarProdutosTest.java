package testes.produtos;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.LoginServices.gerarToken;
import static service.ProdutoServices.cadastrarProduto;
import static service.ProdutoServices.excluirProduto;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public class ListarProdutosTest extends BaseApi {

    protected static String idUsuarioCadastrado;
    protected static String token;
    protected static String idProdutoCadastrado;

    @BeforeAll
    public static void setUp(){
        idUsuarioCadastrado = cadastrarUsuario();
        token = gerarToken();
        idProdutoCadastrado = cadastrarProduto(token);
    }

    @AfterAll
    public static void tearDown(){
        excluirProduto(idProdutoCadastrado, token);
        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve listar um produto por id")
    public void deveListarProdutoPorId(){

        given().param("_id", idProdutoCadastrado)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(retornarTamanhoLista("_id", idProdutoCadastrado)))
                .body("produtos.get(0)._id", equalTo(idProdutoCadastrado));
    }

    @Test
    @DisplayName("Deve listar um produto por nome")
    public void deveListarProdutoPorNome(){
        var nome = "TV";

        given().param("nome", nome)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(retornarTamanhoLista("nome", nome)))
                .body("produtos.get(0).nome", equalTo(nome));
    }

    @Test
    @DisplayName("Deve listar produtos por preco")
    public void deveListarProdutoPorPreco(){
        var preco = "2900";

        given().param("preco", preco)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(retornarTamanhoLista("preco", preco)))
                .body("produtos.get(0).preco", equalTo(Integer.parseInt(preco)));
    }

    @Test
    @DisplayName("Deve listar um produto por descrição")
    public void deveListarProdutoPorDescricao(){
        var descricao = "LG";

        given().param("descricao", descricao)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(retornarTamanhoLista("descricao", descricao)))
                .body("produtos.get(0).descricao", equalTo(descricao));
    }

    @Test
    @DisplayName("Deve listar produtos por quantidade")
    public void deveListarProdutoPorQuantidade(){
        var quantidade = "5";

        given().param("quantidade", quantidade)
                .when().get("produtos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos.size()", equalTo(retornarTamanhoLista("quantidade", quantidade)))
                .body("produtos.get(0).quantidade", equalTo(Integer.parseInt(quantidade)));
    }

    public Integer retornarTamanhoLista(String tipoBusca, String parametro){
        Integer quantidade = given().param(tipoBusca, parametro)
                .when().get("produtos")
                .then().extract().path("quantidade");
        return quantidade;
    }
}
