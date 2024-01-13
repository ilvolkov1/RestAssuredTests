package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BasicSpec.BasicRequestSpec;
import static specs.BasicSpec.BasicResponseSpec;

public class SimpleAPITests {

    @DisplayName("1st test")
    @Test
    public void testTest() {
        get("https://www.google.ru/?hl=ru")
                .then()
                .statusCode(200);

    }

    @DisplayName("Get User by ID")
    @ValueSource(ints = {1, 2, 3, 4})
    @ParameterizedTest
    public void postTest(int id) {
        given(BasicRequestSpec)
                .when()
                .get("https://reqres.in/api/users/" + id)
                .then()
                .spec(BasicResponseSpec)
                .statusCode(200)
                .body("data.id", is(id));

    }

    @DisplayName("List of Users is defined size")
    @ValueSource(ints = {1, 2})
    @ParameterizedTest
    public void listOfUsersIsDefinedSizeTest(int pageNumber) {
        int perPageNumber = 6;
        String queryUrl = "https://reqres.in/api/users";

        Response response =
                given(BasicRequestSpec)
                        .queryParam("page", pageNumber)
                        .when()
                        .get(queryUrl)
                        .then()
                        .spec(BasicResponseSpec)
                        .statusCode(200)
                        .body("per_page", is(perPageNumber))
                        .extract().response();

        int idCount = response.jsonPath().getList("data.id").size();
        assertEquals(perPageNumber, idCount);
    }

    @DisplayName("Successful registration")
    @Test
    public void registrationTest() {
        String email = "eve.holt@reqres.in";
        String password = "pistol";
        String data = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        given(BasicRequestSpec)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .spec(BasicResponseSpec)
                .statusCode(200);
    }

    @DisplayName("Registration without password")
    @Test
    public void negativeRegistrationTest() {
        String email = "eve.holt@reqres.in";
        String data = String.format("{ \"email\": \"%s\"}", email);

        given(BasicRequestSpec)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .spec(BasicResponseSpec)
                .statusCode(400)
                .body("error", is("Missing password"));

    }

    @DisplayName("Registration without email")
    @Test
    public void negative2RegistrationTest() {
        String password = "pistol";
        String data = String.format("{ \"password\": \"%s\"}", password);

        given(BasicRequestSpec)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .spec(BasicResponseSpec)
                .statusCode(400)
                .body("error", is("Missing email or username"));

    }

    @DisplayName("Only defined users can register")
    @Test
    public void negative3RegistrationTest() {
        String email = "eve12321.holt@reqres.in";
        String password = "pistol22";
        String data = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        given(BasicRequestSpec)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .spec(BasicResponseSpec)
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));

    }

    @DisplayName("Deleting user by ID")
    @Test
    public void deleteTest() {
        int ID = 2;
        given(BasicRequestSpec)
                .when()
                .delete("https://reqres.in/api/users/" + ID)
                .then()
                .spec(BasicResponseSpec)
                .statusCode(204);
    }

    @DisplayName("Favicon is good")
    @Test
    public void goodFaviconTest() {
        given(BasicRequestSpec)
                .when()
                .get("https://www.google.com/favicon.ico")
                .then()
                .spec(BasicResponseSpec)
                .statusCode(200);
    }

    @DisplayName("Create new User basic test")
    @Test
    public void createUserTest() {
        Faker fake = new Faker();
        String name = fake.pokemon().name();
        String job = fake.job().title();
        String data = String.format("{ \"name\": \"%s\", \"job\": \"%s\" }", name, job);

        given(BasicRequestSpec)
                .when()
                .body(data)
                .post("https://reqres.in/api/users")
                .then()
                .spec(BasicResponseSpec)
                .statusCode(201)
                .body("name", is(name))
                .body("job", is(job));
    }
}