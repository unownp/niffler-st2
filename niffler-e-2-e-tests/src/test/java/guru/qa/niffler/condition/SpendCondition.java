package guru.qa.niffler.condition;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.page.MainPage.getDateFromTable;

public class SpendCondition {

    public static CollectionCondition newSpendIsVisible(SpendJson spendJson) {
        return new CollectionCondition() {

            @Override
            public void fail(@Nonnull CollectionSource collection, @Nullable List<WebElement> elements,
                             @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    throw new ElementNotFound(collection, toString(), timeoutMs, lastError);
                }
//                else if (elements.size() < expectedSpends.length) {
//                    throw new SpendsSizeMismatch(collection,
//                            Arrays.asList(expectedSpends),
//                            elements.stream().map(SpendCondition::convertWebElementToSpendJson).toList(),
//                            explanation,
//                            timeoutMs);
//                }
                else {
                    throw new SpendsMismatch(
                            collection,
                            spendJson,
                            elements.stream().map(SpendCondition::convertWebElementToSpendJson).toList(),
                            explanation,
                            timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> webElements) {
                WebElement row = webElements.get(webElements.size() - 1);
                System.out.println("TEXT ROW:"+row.getText());
                return spendRowEqualsSpend(row, spendJson);
            }

        };

    }

    private static boolean spendRowEqualsSpend(WebElement row, SpendJson expectedSpend) {
    SpendJson jsonFromRow=convertWebElementToSpendJson(row);
        Instant instant = expectedSpend.getSpendDate().toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        localDateTime = localDateTime.toLocalDate().atStartOfDay();
        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        expectedSpend.setSpendDate(date);
        expectedSpend.setUsername(null);
        expectedSpend.setId(null);
//        System.out.println("webElement to JSON:\n" + jsonFromRow);
//        System.out.println(expectedSpend);
    return (jsonFromRow.equals(expectedSpend));
    }

    private static SpendJson convertWebElementToSpendJson(WebElement row) {
        SpendJson sj = new SpendJson();
        try {
            sj.setSpendDate(getDateFromTable(
                    row.findElements(By.cssSelector("td")).get(1).getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sj.setAmount(Double.valueOf(row.findElements(By.cssSelector("td")).get(2).getText()));
        sj.setCurrency(CurrencyValues.valueOf(row.findElements(By.cssSelector("td")).get(3).getText()));
        sj.setCategory(row.findElements(By.cssSelector("td")).get(4).getText());
        sj.setDescription(row.findElements(By.cssSelector("td")).get(5).getText());
        return sj;
    }

}