package demoqa.api;

import demoqa.models.LoginRequestModel;
import demoqa.models.LoginResponseModel;
import demoqa.specs.Spec;
import demoqa.tests.TestData;

import static io.restassured.RestAssured.given;

public class AuthApi {

    public static LoginResponseModel login() {
        LoginRequestModel request = new LoginRequestModel(TestData.userName,TestData.password);
        return
                given(Spec.request)
                        .body(request)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(Spec.response(200))
                        .extract().as(LoginResponseModel.class);
    }
}
