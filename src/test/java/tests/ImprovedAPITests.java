package tests;

import com.github.javafaker.Faker;
import models.CreateUserRequestModel;
import models.CreateUserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.CreateUserSpec.createUserRequestSpec;
import static specs.CreateUserSpec.createUserResponseSpec;

public class ImprovedAPITests {

    @DisplayName("Create new User with Lombok Model")
    @Test
    public void createUserWithLombokTest() {
        Faker fake = new Faker();

        CreateUserRequestModel createUserRequestBody = new CreateUserRequestModel();
        createUserRequestBody.setName(fake.pokemon().name());
        createUserRequestBody.setJob(fake.job().title());

        CreateUserResponseModel createUserResponse = given()
                .when()
                .log().all()
                .contentType(JSON)
                .body(createUserRequestBody)
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(CreateUserResponseModel.class);

        assertThat(createUserResponse.getJob()).isEqualTo(createUserRequestBody.getJob());
        assertThat(createUserResponse.getName()).isEqualTo(createUserRequestBody.getName());
        assertThat(createUserResponse.getId()).isNotEmpty();
        assertThat(createUserResponse.getCreatedAt()).isNotEmpty();


    }

    @DisplayName("Create new User with spec")
    @Test
    public void createUserWithSpecsTest() {
        Faker fake = new Faker();

        CreateUserRequestModel createUserRequestBody = new CreateUserRequestModel();
        createUserRequestBody.setName(fake.pokemon().name());
        createUserRequestBody.setJob(fake.job().title());

        CreateUserResponseModel createUserResponse =
                step("Create new user", () -> given(createUserRequestSpec)
                        .body(createUserRequestBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class));

        step("Verify user Name", () ->
                assertThat(createUserResponse.getName()).isEqualTo(createUserRequestBody.getName()));

        step("Verify user Job", () ->
                assertThat(createUserResponse.getJob()).isEqualTo(createUserRequestBody.getJob()));


    }

}
