package bases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static service.LoginServices.gerarToken;
import static service.ProdutoServices.cadastrarProduto;
import static service.ProdutoServices.excluirProduto;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public abstract class BaseCarrinho extends BaseApi{

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
}
