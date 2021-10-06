package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.Card;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BuyTourTestNegative {
    Card cardApproved = new Card();

    @BeforeEach
    void cleanTables() {
        SQLHelper.cleanTables();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @DisplayName("6. Не полностью заполнить номер карты при покупке")
    @Test
    void shouldInvalidCardNumber() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setNumber("4444 4444 4444");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("8. Не заполнять поле месяц при покупке")
    @Test
    void shouldEmptyMonth() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setMonth("");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }


    @DisplayName("10. Заполнить номер карты единицами при покупке")
    @Test
    void numberCardWithOne() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setNumber("1111 1111 1111 1111");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }


    @DisplayName("12. Заполнить поле месяц несуществующим номером при покупке")
    @Test
    void monthShouldError() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setMonth("13");
        paymentPage.pullData(cardApproved);
        paymentPage.yearTermErrorMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }


    @DisplayName("14. Заполнить поле на 2 года раньше текущей даты при покупке")
    @Test
    void earlyYearShouldErrorMessage() {
        val startPage= open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setYear(DataHelper.setEarlyYear());
        paymentPage.pullData(cardApproved);
        paymentPage.yearExpiredErrorMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }


    @DisplayName("16. Заполнить в поле 'Владелец' только поле 'Имя' при покупке")
    @Test
    void onlyFillInName() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("Ivan");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("19. Заполнить поле 'Владелец' на кириллице при покупке")
    @Test
    void shouldHaveNameOnABC() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("Анна Васина");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("20.Заполнить поле 'Имя' цифрами при покупке")
    @Test
    void nameOnlyNumbers() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("1234 56789012");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("22.Отправить пустую анкету при покупке")
    @Test
    void sendEmptyForm() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setNumber("");
        cardApproved.setMonth("");
        cardApproved.setYear("");
        cardApproved.setHolder("");
        cardApproved.setCvc("");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("28.Не заполнить поле 'Имя' при покупке")
    @Test
    void withoutName() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder(" ");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("29.Не заполнить поле 'Номер карты' при покупке")
    @Test
    void withoutNumbers() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setNumber(" ");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("30.Не заполнить поле 'Месяц' при покупке")
    @Test
    void withoutMonth() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setMonth(" ");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("31.Не заполнить поле 'Год' при покупке")
    @Test
    void withoutYear() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setYear(" ");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }

    @DisplayName("32.Не заполнить поле 'CVC' при покупке")
    @Test
    void withoutCvc() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setCvc(" ");
        paymentPage.pullData(cardApproved);
        paymentPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectBuyStatus());
    }


    /////

    void setCardApproved() {
        cardApproved.setNumber(DataHelper.approvedCardNumber());
        cardApproved.setMonth(DataHelper.validMonth());
        cardApproved.setYear(DataHelper.validYear());
        cardApproved.setHolder(DataHelper.name());
        cardApproved.setCvc(DataHelper.validCvc());
    }
}
