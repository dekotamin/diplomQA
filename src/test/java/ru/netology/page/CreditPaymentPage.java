package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.Card;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

public class CreditPaymentPage {
    protected SelenideElement numberCard = $("[placeholder='0000 0000 0000 0000']");
    protected SelenideElement mouth = $("[placeholder='08']");
    protected SelenideElement year = $("[placeholder='22']");
    protected SelenideElement holder = $(byText("Владелец")).parent().$(byCssSelector(".input__control"));
    protected SelenideElement cvc = $("[placeholder='999']");
    protected SelenideElement continueButton = $(byText("Продолжить"));
    protected SelenideElement notificationSuccess = $(byCssSelector(".notification_status_ok"));
    protected SelenideElement notificationError = $(byCssSelector(".notification_status_error"));
    protected SelenideElement invalidFormat = $(".input__sub");


    public CreditPaymentPage() {

        $(withText("Кредит по данным карты")).shouldBe(Condition.visible);
    }

    public void pullData(Card card) {
        numberCard.setValue(card.getNumber());
        mouth.setValue(card.getMonth());
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
