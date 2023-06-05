package guru.qa.niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.condition.SpendCondition.newSpendIsVisible;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
public class SpendsWebTest extends BaseWebTest {
    @Disabled
    @GenerateSpend(
        username = "dima",
        description = "QA GURU ADVANCED VOL 2",
        currency = CurrencyValues.RUB,
        amount = 52000.00,
        category = "Обучение"
    )
    @AllureId("101")
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
        $(".spendings-table tbody").$$("tr")
            .find(text(spend.getDescription()))
            .$$("td").first()
            .scrollTo()
            .click();

        $$(".button_type_small").find(text("Delete selected"))
            .click();

        $(".spendings-table tbody")
            .$$("tr")
            .shouldHave(CollectionCondition.size(0));
        throw new IllegalStateException();
    }

    @GenerateSpend(
            username = "ELGATO",
            description = "someDescription",
            currency = CurrencyValues.RUB,
            amount = 52000.00,
            category = "Electrode"
    )
    @ApiLogin(username = "ELGATO", password = "12345")
    @AllureId("101")
    @Test
    void spendInTableShouldBeEqualToGiven(SpendJson spend) {
        refresh();
        MainPage mainPage=new MainPage();
        mainPage.checkThatPageLoaded();
        mainPage.getSpendingsTableRows().shouldHave(newSpendIsVisible(spend));
    }

    @Test
    @GenerateUser
    void test(UserJson userJson){
        System.out.println(userJson);
    }
}
