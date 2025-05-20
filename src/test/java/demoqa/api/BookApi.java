package demoqa.api;

import demoqa.models.BookModel;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookApi {

    public static void addBook(String token, String userId, String isbn) {
        String requestBody = String.format(
                "{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                userId, isbn);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .statusCode(201);
    }

    public static void deleteBook(String token, String userId, String isbn) {
        String requestBody = String.format(
                "{\"isbn\":\"%s\",\"userId\":\"%s\"}",
                isbn, userId);

        given()
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .log().all()
                .statusCode(204);
    }

    public static void deleteAllBooks(String token, String userId) {
        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .statusCode(204);
    }

    public static List<BookModel> getUserBooks(String token, String userId) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("books", BookModel.class);
    }
}
