package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleAPITests {

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

    @ValueSource(ints = {1, 2})
    @ParameterizedTest
    public void listOfUsersIsDefinedSizeTest(int pageNumber) {
        int perPageNumber = 6;
        String queryUrl = "https://reqres.in/api/users";

        Response response =
                given()
                        .queryParam("page", pageNumber)
                        .when()
                        .log().all()
                        .get(queryUrl)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("per_page", is(perPageNumber))
                        .extract().response();

        int idCount = response.jsonPath().getList("data.id").size();
        assertEquals(perPageNumber, idCount);
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

    @DisplayName("Registration without password")
    @Test
    public void negativeRegistrationTest() {
        String email = "eve.holt@reqres.in";
        String data = String.format("{ \"email\": \"%s\"}", email);

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .log().all()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Missing password"));

    }

    @DisplayName("Registration without email")
    @Test
    public void negative2RegistrationTest() {
        String password = "pistol";
        String data = String.format("{ \"password\": \"%s\"}", password);

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .log().all()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Missing email or username"));

    }

    @DisplayName("Only defined users can register")
    @Test
    public void negative3RegistrationTest() {
        String email = "eve12321.holt@reqres.in";
        String password = "pistol22";
        String data = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .log().all()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));

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
                .body("name", is(name))
                .body("job", is(job));
    }
}