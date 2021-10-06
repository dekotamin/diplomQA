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


public class BuyTourTestPositive {
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
        assertEquals ("APPROVED", SQLHelper.selectCreditStatus());
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
        assertEquals("APPROVED", SQLHelper.selectBuyStatus());
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
        assertEquals("APPROVED", SQLHelper.selectCreditStatus());
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