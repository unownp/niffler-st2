package guru.qa.niffler.condition;

import static java.lang.System.lineSeparator;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import javax.annotation.Nullable;

public class SpendsMismatch extends UIAssertionError {

    public SpendsMismatch(CollectionSource collection,
                          SpendJson spendJson, List<SpendJson> actualSpends,
                          @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Spends mismatch" +
                        lineSeparator() + "Actual: " + actualSpends +
                        lineSeparator() + "Expected: " + spendJson +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                spendJson, actualSpends,
                timeoutMs);
    }
}