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

public class BuyTourTestNegativeCredit {

    Card cardApproved = new Card();
    Card cardDecline = new Card();

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

    @DisplayName("5. Заполнить все поля данными для покупки в кредит")
    @Test
    void shouldNotValidBuyWithCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardDecline();
        creditPage.pullData(cardDecline);
        creditPage.checkOperationError();
        assertEquals("DECLINE", SQLHelper.selectCreditStatus());
    }

    @DisplayName("24. Не заполнить поле 'Имя' для покупки в кредит")
    @Test
    void shouldNotValidNameWithCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder(" ");
        creditPage.pullData(cardApproved);
        creditPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("25. Не заполнить поле 'Номер карты' для покупки в кредит")
    @Test
    void shouldNotValidNumberWithCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setNumber(" ");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }


    @DisplayName("26. Не заполнить поле 'Год' для покупки в кредит")
    @Test
    void shouldNotValidYearWithCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setYear(" ");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("27. Не заполнить поле 'CVC' для покупки в кредит")
    @Test
    void shouldNotValidCVCWithCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setCvc(" ");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("7. Не полностью заполнить номер карты при покупке в кредит")
    @Test
    void shouldInvalidCardNumberForCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setNumber("4444 4444 4444");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("9. Не заполнять поле месяц при покупке в кредит")
    @Test
    void shouldEmptyMonthForCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setMonth("");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("11. Заполнить номер карты единицами при покупке в кредит")
    @Test
    void numberCreditCardWithOne() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setNumber("1111 1111 1111 1111");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("13. Заполнить поле месяц несуществующим номером при покупке в кредит")
    @Test
    void monthShouldErrorCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setMonth("13");
        creditPage.pullData(cardApproved);
        creditPage.yearTermErrorMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("15. Заполнить поле на 2 года раньше текущей даты при покупке в кредит")
    @Test
    void earlyYearCreditShouldError() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setYear(DataHelper.setEarlyYear());
        creditPage.pullData(cardApproved);
        creditPage.yearExpiredErrorMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("17. Заполнить в поле 'Владелец' только поле 'Имя' при покупке в кредит")
    @Test
    void onlyFillInNameCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("Ivan");
        creditPage.pullData(cardApproved);
        creditPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("18. Заполнить поле 'Владелец' на кириллице при покупке в кредит")
    @Test
    void shouldHaveNameCreditOnABC() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("Анна Васина");
        creditPage.pullData(cardApproved);
        creditPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("21.Заполнить поле 'Имя' цифрами при покупке в кредит")
    @Test
    void nameOnlyNumbersOnCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("1234 56789012");
        creditPage.pullData(cardApproved);
        creditPage.invalidNameFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    @DisplayName("23.Отправить пустую анкету при  в кредит")
    @Test
    void sendEmptyFormOnCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setNumber("");
        cardApproved.setMonth("");
        cardApproved.setYear("");
        cardApproved.setHolder("");
        cardApproved.setCvc("");
        creditPage.pullData(cardApproved);
        creditPage.invalidCardFormatMessage();
        assertEquals("", SQLHelper.selectCreditStatus());
    }

    /////

    void setCardApproved() {
        cardApproved.setNumber(DataHelper.approvedCardNumber());
        cardApproved.setMonth(DataHelper.validMonth());
        cardApproved.setYear(DataHelper.validYear());
        cardApproved.setHolder(DataHelper.name());
        cardApproved.setCvc(DataHelper.validCvc());
    }

    void setCardDecline() {
        cardDecline.setNumber(DataHelper.declinedCardNumber());
        cardDecline.setMonth(DataHelper.validMonth());
        cardDecline.setYear(DataHelper.validYear());
        cardDecline.setHolder(DataHelper.name());
        cardDecline.setCvc(DataHelper.validCvc());
    }
}
