import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

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
        String email = "eve.holt@reqres.in";
        String password = "pistol";
        String data = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .log().all()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200);

    }


    @Test
    public void deleteTest() {
        int ID = 2;
        given()
                .when()
                .log().all()
                .delete("https://reqres.in/api/users/" + ID)
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void goodFaviconTest() {
        given()
                .when()
                .log().all()
                .get("https://www.google.com/favicon.ico")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void createUserTest() {
        Faker fake = new Faker();
        String name = fake.pokemon().name();
        String job = fake.job().title();
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String data = String.format("{ \"name\": \"%s\", \"job\": \"%s\" }", name, job);

        given()
                .when()
                .log().all()
                .contentType(JSON)
                .body(data)
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("name",is(name))
                .body("job",is(job))
                .body("createdAt",startsWith(formattedDate));

    }
}
