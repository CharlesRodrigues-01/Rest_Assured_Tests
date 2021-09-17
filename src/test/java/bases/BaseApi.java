package bases;

import br.com.zup.serverest.factory.SimulationDataFactory;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.baseURI;

public abstract class BaseApi {

    protected static SimulationDataFactory simulationDataFactory = new SimulationDataFactory();

    @BeforeAll
    public static void beforeAllTests() {
        baseURI = "https://serverest.dev/";
    }
}

