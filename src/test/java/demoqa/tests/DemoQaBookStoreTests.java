package demoqa.tests;

import demoqa.api.AuthApi;
import demoqa.api.BookApi;
import demoqa.helpers.WithLogin;
import demoqa.models.BookModel;
import demoqa.models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


public class DemoQaBookStoreTests extends TestBase {

    @Test
    @WithLogin
    @DisplayName("Удаление книги из профиля")
    void deleteBookTest() {
        LoginResponseModel auth = step("Авторизация", () -> {
            LoginResponseModel response = AuthApi.login();
            assertThat(response.getToken()).isNotEmpty();
            return response;
        });

        step("Удаляем корзину пользователя", () ->
                BookApi.deleteAllBooks(auth.getToken(), auth.getUserId())
        );

        step("Добавляем книгу через Api", () ->
                BookApi.addBook(auth.getToken(), auth.getUserId(), TestData.isbn)
        );

        step("Проверяем добавленную книгу через Api", () -> {
            List<BookModel> books = BookApi.getUserBooks(auth.getToken(), auth.getUserId());
            assertThat(books).extracting(BookModel::getIsbn).contains(TestData.isbn);
        });

        step("Переходим на страницу профиля", () -> {
            open("/profile");
            refresh();
            $("#userName-value").shouldHave(text(auth.getUsername()));
            $(".rt-tbody").shouldBe(visible);
        });

        step("Удаляем книгу через API", () ->
                BookApi.deleteBook(auth.getToken(), auth.getUserId(), TestData.isbn)
        );

        step("Проверка через API, что книга удалена", () -> {
            List<BookModel> books = BookApi.getUserBooks(auth.getToken(), auth.getUserId());
            assertThat(books).extracting(BookModel::getIsbn).doesNotContain(TestData.isbn);
        });
    }
}