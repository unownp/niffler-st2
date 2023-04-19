package niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import niffler.jupiter.GenerateCategory;
import niffler.jupiter.GenerateCategoryExtension;
import niffler.jupiter.GenerateSpend;
import niffler.jupiter.GenerateSpendExtension;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


@ExtendWith({GenerateCategoryExtension.class, GenerateSpendExtension.class})
public class SpendsWebTest {
    static final String USERNAME = "GEESEFETCHER";
    static final String CATEGORY = "SOMECATEGORY1";

    static {
        Configuration.browserSize = "1920x1080";
    }


    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(USERNAME);
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

    @GenerateCategory(
            username = USERNAME,
            category = CATEGORY
    )
    @GenerateSpend(
            username = USERNAME,
            description = "SOME DESCRIPTION",
            currency = CurrencyValues.USD,
            amount = 10000.00,
            category = CATEGORY
    )

    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
        ElementsCollection spendingTableCollection = $(".spendings-table tbody").$$("tr");
        spendingTableCollection
                .find(text(spend.getDescription()))
                .$$("td").first()
                .scrollTo()
                .click();
        int tableSize = spendingTableCollection.size();
        $$(".button_type_small").find(text("Delete selected"))
                .click();

        spendingTableCollection
                .shouldHave(CollectionCondition.size(tableSize - 1));
    }
}
