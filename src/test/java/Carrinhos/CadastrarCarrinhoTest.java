package Carrinhos;

import bases.BaseCarrinho;
import io.restassured.http.ContentType;
import model.CarrinhoRequest;
import model.ProdutoParaCarrinhoRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static service.CarrinhoServices.*;

public class CadastrarCarrinhoTest extends BaseCarrinho {

    private List<String> produtos = new ArrayList<>();
    private List<ProdutoParaCarrinhoRequest> listaDeProdutos = new ArrayList<>();

    @Test
    @DisplayName("Deve cadastrar um carrinho")
    public void deveCadastrarCarrinho(){

        produtos.add(idProdutoCadastrado);

        String idCarrinhoCadastrado = given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirCarrinho(produtos))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract().path("_id");

        excluirCarrinho(idCarrinhoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve cadastrar um carrinho com produtos duplicados")
    public void naoDeveCadastrarCarrinhoComProdutosDuplicados(){

        produtos.add(idProdutoCadastrado);
        produtos.add(idProdutoCadastrado);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirCarrinho(produtos))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido possuir produto duplicado"))
                .extract().path("_id");

    }

    @Test
    @DisplayName("Não deve cadastrar um carrinho com produtos não cadastrados")
    public void naoDeveCadastrarCarrinhoComProdutosNaoCadastrados(){

        var idProduto = "123";
        var quantidade = 1;
        ProdutoParaCarrinhoRequest produtoParaCarrinho = new ProdutoParaCarrinhoRequest(idProduto, quantidade);
        listaDeProdutos.add(produtoParaCarrinho);
        CarrinhoRequest carrinhoComProdutoInexistente = new CarrinhoRequest(listaDeProdutos);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(carrinhoComProdutoInexistente)
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não encontrado"))
                .extract().path("_id");

    }

    @Test
    @DisplayName("Não deve cadastrar um carrinho se a quantidade do estoque for insuficiente")
    public void naoDeveCadastrarCarrinhoComEstoqueInsuficiente(){

        ProdutoParaCarrinhoRequest produtoParaCarrinho = new ProdutoParaCarrinhoRequest(
                idProdutoCadastrado, 6);
        listaDeProdutos.add(produtoParaCarrinho);
        CarrinhoRequest carrinhoComEstoqueInsuficiente = new CarrinhoRequest(listaDeProdutos);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(carrinhoComEstoqueInsuficiente)
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Produto não possui quantidade suficiente"))
                .extract().path("_id");
    }

    @Test
    @DisplayName("Não deve cadastrar mais de um carrinho por usuário")
    public void naoDeveCadastrarMaisDeUmCarrinhoPorUsuario(){

        produtos.add(idProdutoCadastrado);
        var idCarrinhoCadastrado = cadastrarCarrinho(produtos, token);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(construirCarrinho(produtos))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Não é permitido ter mais de 1 carrinho"))
                .extract().path("_id");

        excluirCarrinho(idCarrinhoCadastrado, token);
    }

    @Test
    @DisplayName("Não deve cadastrar um carrinho sem permissão de administrador")
    public void naoDeveCadastrarCarrinhoSemPermissao(){

        produtos.add(idProdutoCadastrado);

        given()
                .contentType(ContentType.JSON)
                .body(construirCarrinho(produtos))
                .when().post("carrinhos")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .extract().path("_id");
    }
}
