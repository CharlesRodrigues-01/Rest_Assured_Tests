package bases;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.baseURI;

public abstract class BaseApi {

    @BeforeAll
    public static void beforeAllTests() {
        baseURI = "https://serverest.dev/";
    }
}
