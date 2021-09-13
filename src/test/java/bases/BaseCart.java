package bases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.ProductBuilder.*;
import static br.com.zup.serverest.builder.UserBuilder.*;

public abstract class BaseCart extends BaseApi{

    protected static String userId;
    protected static String token;
    protected static String ProductId;

    @BeforeAll
    protected static void setUp(){
        userId = createUser();
        token = getToken();
        ProductId = createProduct(token);
    }

    @AfterAll
    protected static void tearDown(){
        deleteProduct(ProductId, token);
        deleteUser(userId);
    }
}
