package tests;

import com.github.javafaker.Faker;
import models.LoginRequestModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ImprovedAPITests {


    @Test
    public void createUserTest() {
        Faker fake = new Faker();
        String name = fake.pokemon().name();
        String job = fake.job().title();
        String data = String.format("{ \"name\": \"%s\", \"job\": \"%s\" }", name, job);

        LoginRequestModel abc = new LoginRequestModel();
        abc.setEmail("123");

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
