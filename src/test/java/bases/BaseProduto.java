package bases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static service.LoginServices.gerarToken;
import static service.UsuarioServices.cadastrarUsuario;
import static service.UsuarioServices.excluirUsuario;

public abstract class BaseProduto extends BaseApi{

    protected static String idUsuarioCadastrado;
    protected static String token;

    @BeforeAll
    public static void setUp(){
        idUsuarioCadastrado = cadastrarUsuario();
        token = gerarToken();
    }

    @AfterAll
    public static void tearDown(){
        excluirUsuario(idUsuarioCadastrado);
    }
}
