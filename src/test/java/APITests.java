import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class APITests {

    @Test
    public void testTest() {
        get("https://www.google.ru/?hl=ru")
                .then()
                .statusCode(200);

    }

    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    @ParameterizedTest
    public void postTest(int id) {
        given()
                .when()
                .log().all()
                .get("https://reqres.in/api/users/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", is(id));

    }

    @Test
    public void registrationTest() {
        String email = "Guiseppe@reqres.in";
        String password = "12345678";
        String data = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);
        String data1 = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .contentType(ContentType.JSON)
                .body(data1)
                .when()
                .log().all()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200);

    }
}
