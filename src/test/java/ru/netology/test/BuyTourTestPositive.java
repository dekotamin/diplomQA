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


public class BuyTourTest {
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

    @DisplayName("1. Заполнить все поля валидными данными для покупки")
    @Test
    void shouldValidBuySuccess() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        paymentPage.pullData(cardApproved);
        paymentPage.checkOperationOk();
        assertEquals("APPROVED", SQLHelper.selectBuyStatus());

    }

    @DisplayName("2. Заполнить все поля валидными данными для покупки в кредит")
    @Test
    void shouldValidCreditSuccess() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage= startPage.goCreditPage();
        setCardApproved();
        creditPage.pullData(cardApproved);
        creditPage.checkOperationOk();
        assertEquals ("approved", SQLHelper.selectCreditStatus());
    }

    @DisplayName("3. Заполнить поле 'Имя' именем через дефис и фамилией при покупке")
    @Test
    void shouldValidNameThrowDash() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("Ivan-Ivan Ivanov");
        paymentPage.pullData(cardApproved);
        paymentPage.checkOperationOk();
    }

    @DisplayName("4. Заполнить поле 'Имя' именем через дефис и фамилией при покупке в кредит")
    @Test
    void shouldValidNameThrowDashOnCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("Ivan-Ivan Ivanov");
        creditPage.pullData(cardApproved);
        creditPage.checkOperationOk();
    }


    @DisplayName("5. Заполнить все поля данными для покупки в кредит")
    @Test
    void shouldNotValidBuyWithCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardDecline();
        creditPage.pullData(cardDecline);
        creditPage.checkOperationError();
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
    }

    @DisplayName("10. Заполнить номер карты единицами при покупке")
    @Test
    void numberCardWithOne() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setNumber("1111 1111 1111 1111");
        paymentPage.pullData(cardApproved);
        paymentPage.checkOperationError();
    }

    @DisplayName("11. Заполнить номер карты единицами при покупке в кредит")
    @Test
    void numberCreditCardWithOne() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setNumber("1111 1111 1111 1111");
        creditPage.pullData(cardApproved);
        creditPage.checkOperationError();
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
    }

    @DisplayName("16. Заполнить в поле 'Владелец' только поле 'Имя' при покупке")
    @Test
    void onlyFillInName() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("Ivan");
        paymentPage.pullData(cardApproved);
        paymentPage.checkOperationError();
    }

    @DisplayName("17. Заполнить в поле 'Владелец' только поле 'Имя' при покупке в кредит")
    @Test
    void onlyFillInNameCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("Ivan");
        creditPage.pullData(cardApproved);
        creditPage.checkOperationError();
    }

    @DisplayName("18. Заполнить поле 'Владелец' на кириллице при покупке в кредит")
    @Test
    void shouldHaveNameCreditOnABC() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("Анна Васина");
        creditPage.pullData(cardApproved);
        creditPage.checkOperationError();
        creditPage.checkOperationOk();
    }

    @DisplayName("19. Заполнить поле 'Владелец' на кириллице при покупке")
    @Test
    void shouldHaveNameOnABC() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("Анна Васина");
        paymentPage.pullData(cardApproved);
        paymentPage.checkOperationError();
    }

    @DisplayName("20.Заполнить поле 'Имя' цифрами при покупке")
    @Test
    void nameOnlyNumbers() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val paymentPage = startPage.goPaymentPage();
        setCardApproved();
        cardApproved.setHolder("1234 56789012");
        paymentPage.pullData(cardApproved);
        paymentPage.checkOperationError();
    }

    @DisplayName("21.Заполнить поле 'Имя' цифрами при покупке в кредит")
    @Test
    void nameOnlyNumbersOnCredit() {
        val startPage = open("http://localhost:8080", StartPage.class);
        val creditPage = startPage.goCreditPage();
        setCardApproved();
        cardApproved.setHolder("1234 56789012");
        creditPage.pullData(cardApproved);
        creditPage.checkOperationError();
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