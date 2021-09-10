package testes.carrinhos;

import bases.BaseApi;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.CarrinhoServices.cadastrarCarrinho;
import static service.CarrinhoServices.excluirCarrinho;
import static service.LoginServices.gerarToken;
import static service.ProdutoServices.cadastrarProduto;
import static service.ProdutoServices.excluirProduto;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public class ListarCarrinhosTest extends BaseApi {

    protected static String idUsuarioCadastrado;
    protected static String token;
    protected static String idProdutoCadastrado;
    protected static String idCarrinhoCadastrado;
    protected static List<String> produtos = new ArrayList<>();

    @BeforeAll
    public static void setUp(){
        idUsuarioCadastrado = cadastrarUsuario();
        token = gerarToken();
        idProdutoCadastrado = cadastrarProduto(token);
        produtos.add(idProdutoCadastrado);
        idCarrinhoCadastrado = cadastrarCarrinho(produtos, token);
    }

    @AfterAll
    public static void tearDown(){
        excluirCarrinho(idCarrinhoCadastrado, token);
        excluirProduto(idProdutoCadastrado, token);
        excluirUsuario(idUsuarioCadastrado);
    }

    @Test
    @DisplayName("Deve listar um carrinho por id")
    public void deveListarCarrinhoPorId(){

        given().param("_id", idCarrinhoCadastrado)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(retornarTamanhoLista("_id", idCarrinhoCadastrado)))
                .body("carrinhos.get(0)._id", equalTo(idCarrinhoCadastrado));
    }

    @Test
    @DisplayName("Deve listar um carrinho por preço total")
    public void deveListarCarrinhoPorPrecoTotal(){
        var precoTotal = "2900";

        given().param("precoTotal", precoTotal)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(retornarTamanhoLista("precoTotal", precoTotal)))
                .body("carrinhos.get(0).precoTotal", equalTo(Integer.parseInt(precoTotal)));
    }

    @Test
    @DisplayName("Deve listar um carrinho por quantidade total")
    public void deveListarCarrinhoPorQuantidadeTotal(){
        var quantidadeTotal = "1";

        given().param("quantidadeTotal", quantidadeTotal)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(retornarTamanhoLista("quantidadeTotal", quantidadeTotal)))
                .body("carrinhos.get(0).quantidadeTotal", equalTo(Integer.parseInt(quantidadeTotal)));
    }

    @Test
    @DisplayName("Deve listar um carrinho pelo id do usuário")
    public void deveListarCarrinhoPeloIdDoUsuario(){

        given().param("idUsuario", idUsuarioCadastrado)
                .when().get("carrinhos")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos.size()", equalTo(retornarTamanhoLista("idUsuario", idUsuarioCadastrado)))
                .body("carrinhos.get(0).idUsuario", equalTo(idUsuarioCadastrado));
    }

    public Integer retornarTamanhoLista(String tipoBusca, String parametro){
        Integer quantidade = given().param(tipoBusca, parametro)
                .when().get("carrinhos")
                .then().extract().path("quantidade");
        return quantidade;
    }
}
