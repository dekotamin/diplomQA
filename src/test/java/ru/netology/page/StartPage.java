package ru.netology.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {
    private SelenideElement buyButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));


    public PaymentPage goPaymentPage() {
        buyButton.click();
        return Selenide.page(PaymentPage.class);
    }

    public CreditPaymentPage goCreditPage() {
        creditButton.click();
        return Selenide.page(CreditPaymentPage.class);
    }
}