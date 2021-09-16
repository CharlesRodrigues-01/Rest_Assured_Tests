package bases;

import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;

public abstract class BaseApi {

    @BeforeAll
    public static void beforeAllTests() {
        baseURI = "https://serverest.dev/";
    }
}

