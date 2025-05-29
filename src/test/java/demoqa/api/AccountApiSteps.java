package demoqa.api;

import demoqa.models.BookModel;
import demoqa.models.LoginRequestModel;
import demoqa.models.LoginResponseModel;
import demoqa.specs.Specs;
import demoqa.tests.TestData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AccountApiSteps {

    public static LoginResponseModel login() {
        LoginRequestModel request = new LoginRequestModel(TestData.userName,TestData.password);
        return
                given(Specs.request)
                        .body(request)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(Specs.response(200))
                        .extract().as(LoginResponseModel.class);
    }
    public static List<BookModel> getUserBooks(String token, String userId) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .spec(Specs.response(200))
                .extract()
                .jsonPath()
                .getList("books", BookModel.class);
    }
}
