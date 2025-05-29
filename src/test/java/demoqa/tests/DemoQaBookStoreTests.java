package demoqa.tests;

import demoqa.api.AccountApiSteps;
import demoqa.api.BookStoreApiSteps;
import demoqa.helpers.WithLogin;
import demoqa.models.BookModel;
import demoqa.models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static demoqa.tests.TestData.isbn;
import static demoqa.tests.TestData.userName;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


public class DemoQaBookStoreTests extends TestBase {

    @Test
    @WithLogin
    @DisplayName("Удаление книги из профиля")
    void deleteBookTest() {
        LoginResponseModel auth = step("Авторизация пользователя через API", AccountApiSteps::login);
        String token = auth.getToken();
        String userId = auth.getUserId();

        step("Удаляем корзину пользователя", () ->
                BookStoreApiSteps.deleteAllBooks(token, userId)
        );

        step("Добавляем книгу через Api", () ->
                BookStoreApiSteps.addBook(token, userId, isbn)
        );

        step("Проверяем добавленную книгу через Api", () -> {
            List<BookModel> books = AccountApiSteps.getUserBooks(token, userId);
            assertThat(books).extracting(BookModel::getIsbn).contains(isbn);
        });

        step("Переходим на страницу профиля", () -> {
            open("/profile");
            $("#userName-value").shouldHave(text(userName));
        });

        step("Удаляем книгу через API", () ->
            BookStoreApiSteps.deleteBook(token, userId, isbn)
        );

        step("Проверка через API, что книга удалена", () -> {
            List<BookModel> books = AccountApiSteps.getUserBooks(token, userId);
            assertThat(books).extracting(BookModel::getIsbn).doesNotContain(isbn);
        });
    }
}