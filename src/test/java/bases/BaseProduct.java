package bases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static br.com.zup.serverest.builder.LoginBuilder.getToken;
import static br.com.zup.serverest.builder.UserBuilder.*;

public abstract class BaseProduct extends BaseApi{

    protected static String userId;
    protected static String token;

    @BeforeAll
    protected static void setUp(){
        userId = createUser();
        token = getToken();
    }

    @AfterAll
    protected static void tearDown(){
        deleteUser(userId);
    }
}
