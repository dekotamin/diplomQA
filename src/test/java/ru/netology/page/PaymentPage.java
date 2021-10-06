package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.Card;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

public class PaymentPage {

    private SelenideElement numberCard = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement month = $("[placeholder='08']");
    private SelenideElement year = $("[placeholder='22']");
    private SelenideElement holder = $(byText("Владелец")).parent().$(byCssSelector(".input__control"));
    private SelenideElement cvc = $("[placeholder='999']");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement notificationSuccess = $(byCssSelector(".notification_status_ok"));
    private SelenideElement notificationError = $(byCssSelector(".notification_status_error"));
    private SelenideElement invalidFormat = $(".input__sub");

    public PaymentPage() {
        $(withText("Купить")).shouldBe(Condition.visible);
    }

    public void pullData(Card card) {
        numberCard.setValue(card.getNumber());
        month.setValue(card.getMonth());
        year.setValue(card.getYear());
        holder.setValue(card.getHolder());
        cvc.setValue(card.getCvc());
        continueButton.click();
    }
    public void checkOperationOk() {

        notificationSuccess.shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    public void checkOperationError() {

        notificationError.shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    public boolean invalidNameFormatMessage() {
        invalidFormat.shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        return true;
    }

    public boolean invalidCardFormatMessage() {
        invalidFormat.shouldHave(Condition.exactText("Неверный формат"));
        return true;
    }

    public boolean yearExpiredErrorMessage() {
        invalidFormat.shouldHave(Condition.exactText("Истёк срок действия карты"));
        return true;
    }

    public boolean yearTermErrorMessage() {
        invalidFormat.shouldHave(Condition.exactText("Неверно указан срок действия карты"));
        return true;
    }
}