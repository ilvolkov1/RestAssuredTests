import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;

public class APITests {

    @Test
    public void TestTest(){
        get("https://www.google.ru/?hl=ru")
                .then()
                .statusCode(200);

    }
}
