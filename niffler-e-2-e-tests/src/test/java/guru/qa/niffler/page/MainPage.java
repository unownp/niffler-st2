package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.Toastify;
import lombok.Getter;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@Getter
public class MainPage extends BasePage<MainPage> {
    public static String URL = BASE_URL + ":" + FRONT_PORT + Config.getConfig().getMainPath();
    private final Header header = new Header();
    private final Footer footer = new Footer();

    private final SelenideElement mainContentAddSpending = $(".main-content__section.main-content__section-add-spending");
    private final SelenideElement categoryInput = mainContentAddSpending.$x(".//input[contains(@id,'react-select') and contains(@id,'-input')]");
    private final SelenideElement categoryInputPlaceHolder =
            mainContentAddSpending.$x(".//div[contains(@id,'react-select') and contains(@id,'-placeholder')]");
    private final SelenideElement categoryInputDropDown =
            mainContentAddSpending.$(".//div[contains(@id,'react-select') and contains(@id,'-listbox')]");
    private final ElementsCollection categoryInputListBoxCategories =
            $$x(".//div[contains(@id,'react-select') and contains(@id,'-option')]");
    private final SelenideElement categoryInputEmptyListBox = categoryInputDropDown.$(byText("No options"));
    private final SelenideElement categoryFormLabel = categoryInput.ancestor(".form__label");
    private final SelenideElement categoryFormLabelError = categoryFormLabel.$x("span[@class='form__error']");
    private final SelenideElement amountInput = mainContentAddSpending.$x(".//input[@class='form__input ' and @name='amount']");
    private final SelenideElement amountFormLabel = amountInput.ancestor(".form__label");
    private final SelenideElement spendDateInput = mainContentAddSpending
            .$(".react-datepicker__input-container").lastChild();
    private final SelenideElement spendDateFormLabel = spendDateInput.ancestor(".form__label");
    private final SelenideElement descriptionInput = mainContentAddSpending.$(byAttribute("name", "description"));
    private final SelenideElement descriptionFormLabel = descriptionInput.ancestor(".form__label");
    private final SelenideElement submitButton = mainContentAddSpending.$(byAttribute("type", "submit"));
    private final SelenideElement mainContentSection = $(".main-content__section.main-content__section-add-spending");
    private final SelenideElement mainContentStats = $(".main-content__section.main-content__section-stats");
    private final SelenideElement mainContentHistory = $(".main-content__section.main-content__section-history");
    private final SelenideElement mainContentHistoryName = mainContentHistory.$x("h2");
    private final SelenideElement mainContentHistoryImage = mainContentHistory.$(".spendings__img");
    private final SelenideElement spendingsContent = mainContentHistory.$(".spendings__content");
    private final SelenideElement spendingsControls = spendingsContent.$(".spendings__controls");
    private final SelenideElement spendingsButtons = spendingsContent.$(".spendings__buttons");
    private final SelenideElement spendingsControlsHeader = spendingsButtons.$(".spendings__controls-header");
    private final SelenideElement spendingsButtonToday = spendingsButtons.$(byText("Today"));
    private final SelenideElement spendingsButtonLastWeek = spendingsButtons.$(byText("Last week"));
    private final SelenideElement spendingsButtonLastMonth = spendingsButtons.$(byText("Last month"));
    private final SelenideElement spendingsButtonAllTime = spendingsButtons.$(byText("All time"));
    private final SelenideElement spendingsCurrencyButton = spendingsControls.$(".form__label");
    private final SelenideElement spendingsCurrencyVariations = $("#react-select-5-listbox");
    private final SelenideElement spendingsCurrencyVariationsAllButton = spendingsCurrencyVariations
            .$(byText("ALL"));
    private final SelenideElement spendingsRefreshFiltersButton = spendingsControls.$(".button-icon_type_close");
    private final SelenideElement spendingsBulkActions = spendingsControls.$(".spendings__bulk-actions");
    private final SelenideElement spendingsControlHeaders = spendingsBulkActions.$(byText("Actions:"));
    private final SelenideElement spendingsDeleteSelectedButton = spendingsBulkActions.$(byText("Delete selected"));
    private final SelenideElement spendingsTable = spendingsContent.$(".spendings-table");
    private final SelenideElement spendingsTableCheckBox = spendingsTable.$(byAttribute("type", "checkbox"));
    private final ElementsCollection spendingsTableRows = $$(".spendings-table>tbody>tr");
    private final SelenideElement spendingsTableDate = spendingsTable
            .$(byText("Date"));
    private final SelenideElement spendingsTableAmount = spendingsTable
            .$(byText("Amount"));
    private final SelenideElement spendingsTableCurrency = spendingsTable
            .$(byText("Currency"));
    private final SelenideElement spendingsTableCategory = spendingsTable
            .$(byText("Category"));
    private final SelenideElement spendingsTableDescription = spendingsTable
            .$(byText("Description"));
    private final SelenideElement spendingsTableEmptiness = spendingsContent
            .$(byText("No spendings provided yet!"));
    private final SelenideElement spendingsUpdatedToastifyText =
            $(byText("Spending successfully added!"));
    private final SelenideElement spendingsDeletedToastifyText = $(byText("Spendings deleted"));


    public static final String MAIN_CONTENT_SECTION_TEXT = "Add new spending";
    public static final String CATEGORY_INPUT_PLACEHOLDER_TEXT = "Choose spending category";
    public static final String CATEGORY_FORM_LABEL_TEXT = "Category\n" + " \n" + "*";
    public static final String CATEGORY_FORM_LABEL_ERROR = "Category is required";
    public static final String AMOUNT_FORM_LABEL_TEXT = "Amount\n" + " \n" + "*";
    public static final String AMOUNT_INPUT_PLACEHOLDER_TEXT = "Set Amount";
    public static final String SPEND_DATE_FORM_LABEL_TEXT = "Spend date\n" + " \n" + "*";
    public static final String DESCRIPTION_FORM_LABEL_TEXT = "Description\n ";
    public static final String DESCRIPTION_INPUT_PLACEHOLDER_TEXT = "Spending description";
    public static final String SUBMIT_BUTTON_TEXT = "Add new spending";
    public static final String MAIN_CONTENT_HISTORY_NAME_TEXT = "History of spendings";
    public static final String SPENDING_CONTROLS_TEXT = "\"All the limits are in your head\"";
    public static final String SPENDING_CONTROLS_HEADER_TEXT = "Filters:";

    @Override
    public MainPage checkThatPageLoaded() {
        header.checkThatComponentDisplayed();
        categoryInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        amountInput.shouldBe(visible);
        amountInput.shouldHave(attribute("placeholder", AMOUNT_INPUT_PLACEHOLDER_TEXT));
        spendDateInput.shouldBe(visible);
        descriptionInput.shouldBe(visible);
        descriptionInput.shouldHave(attribute("placeholder", DESCRIPTION_INPUT_PLACEHOLDER_TEXT));
        mainContentStats.shouldBe(visible);
        mainContentHistory.shouldBe(visible);
        spendingsButtonToday.shouldBe(visible);
        spendingsButtonLastWeek.shouldBe(visible);
        spendingsButtonLastMonth.shouldBe(visible);
        spendingsButtonAllTime.shouldBe(visible);
        spendingsCurrencyButton.shouldBe(visible);
        spendingsBulkActions.shouldBe(visible);
        spendingsDeleteSelectedButton.shouldBe(visible);
        spendingsTableCheckBox.shouldBe(visible);
        spendingsTableDate.shouldBe(visible);
        spendingsTableAmount.shouldBe(visible);
        spendingsTableCurrency.shouldBe(visible);
        spendingsTableCategory.shouldBe(visible);
        spendingsTableDescription.shouldBe(visible);
        footer.checkThatComponentDisplayed();
        assertAll(
                () -> assertEquals(mainContentAddSpending.$x(".//h2").getOwnText(), MAIN_CONTENT_SECTION_TEXT),
                () -> assertEquals(categoryInputPlaceHolder.getOwnText(), CATEGORY_INPUT_PLACEHOLDER_TEXT),
                () -> assertEquals(categoryFormLabel.getOwnText(), CATEGORY_FORM_LABEL_TEXT),
                () -> assertEquals(amountFormLabel.getOwnText(), AMOUNT_FORM_LABEL_TEXT),
                () -> assertEquals(spendDateFormLabel.getOwnText(), SPEND_DATE_FORM_LABEL_TEXT),
                () -> assertEquals(descriptionFormLabel.getOwnText(), DESCRIPTION_FORM_LABEL_TEXT),
                () -> assertEquals(submitButton.getOwnText(), SUBMIT_BUTTON_TEXT),
                () -> assertEquals(mainContentHistoryName.getOwnText(), MAIN_CONTENT_HISTORY_NAME_TEXT),
                () -> assertEquals(spendingsControls.$x(".//cite").getOwnText(), SPENDING_CONTROLS_TEXT),
                () -> assertEquals(spendingsControlsHeader.getOwnText(), SPENDING_CONTROLS_HEADER_TEXT)
        );
        return this;
    }

    public void checkNewSpendingCreated() {
        categoryInput.click();
        List<String> categories = getCategoriesList();
        if (categories.size() > 0) {
            Faker faker = new Faker();
            int randomIndex = faker.number().numberBetween(0, categories.size() - 1);
            String randomCategory = categories.get(randomIndex);
            System.out.println(randomCategory);
            SelenideElement randomCategoryElement =
                    $x(".//div[contains(@id,'react-select') " +
                            "and contains(@id,'-option-" + randomIndex + "')]");
            randomCategoryElement.click();
            String categoryInputOwnText = categoryFormLabel.getText();
            int randomAmount = faker.number().numberBetween(1, 10000);
            amountInput.setValue(String.valueOf(randomAmount));
            String formatPattern = "dd/MM/yyyy";
            DateFormat dateFormat = new SimpleDateFormat(formatPattern);
            long dateStart = 643900206L;
            long dateEnd = 2537356206L;
            Date randomDate = new Date(faker.number().numberBetween(dateStart, dateEnd));
            String randomFormattedDate = dateFormat.format(randomDate);
            System.out.println(randomFormattedDate);
            for (int i = 0; i < formatPattern.length(); i++) {
                spendDateInput.press(Keys.BACK_SPACE);
            }
            spendDateInput.setValue(randomFormattedDate);
            amountInput.click();
            String randomDescription = faker.lordOfTheRings().character();
            descriptionInput.setValue(randomDescription);
            submitButton.click();
            Toastify toastify = new Toastify();
            toastify.checkThatComponentDisplayed();
            spendingsUpdatedToastifyText.shouldBe(visible);
            toastify.getToastifyCloseButton().click();

            ElementsCollection cells = spendingsTableRows.last().$$("td");

            cells.get(0)
                    .$(byAttribute("type", "checkbox"))
                    .scrollTo().shouldBe(visible);
            String tableDateText = cells.get(1).$("span").getOwnText();
            DateFormat tableDateFormat = new SimpleDateFormat("dd MMM yy", new Locale("en", "EN"));
            String randomFormattedDateForTable = tableDateFormat.format(randomDate);
            String tableAmount = cells.get(2).$("span").getOwnText();
            String tableCurrency = cells.get(3).getOwnText();
            String tableCategory = cells.get(4).$("span").getOwnText();
            String tableDescription = cells.get(5).$("span").getOwnText();
            cells.get(6).$(".button-icon_type_edit").scrollTo().shouldBe(visible);
            ProfilePage profilePage = header.goToProfilePage();
            String currencyFromProfile = profilePage.getCurrencyInput().getText();
            assertAll(
                    () -> assertTrue(categoryInputOwnText.contains(randomCategory)),
                    () -> assertEquals(tableDateText, randomFormattedDateForTable),
                    () -> assertEquals(tableAmount, String.valueOf(randomAmount)),
                    () -> assertEquals(tableCategory, randomCategory),
                    () -> assertEquals(tableDescription, randomDescription),
                    () -> assertEquals(tableCurrency, currencyFromProfile)
            );
        }
    }

    public void deleteAllSpendings() {
        if (spendingsTableRows.size() == 0) {
            spendingsTableEmptiness.scrollTo().shouldBe(visible);
            spendingsDeleteSelectedButton.shouldHave(attribute("disabled"));
        } else {
            spendingsTableCheckBox.scrollTo().click();
            spendingsDeleteSelectedButton.click();
            Toastify toastify = new Toastify();
            toastify.checkThatComponentDisplayed();
            spendingsDeletedToastifyText.shouldBe(visible);
            spendingsTableEmptiness.shouldBe(visible);
            refresh();
            spendingsDeleteSelectedButton.shouldHave(attribute("disabled"));
            assertEquals(0, spendingsTableRows.size());
        }
    }

    public List<String> getCategoriesList() {
        categoryInput.click();
        return categoryInputListBoxCategories.texts();
    }

    public static Date getDateFromTable(String dateFromTableText) throws ParseException {
        DateFormat tableDateFormat = new SimpleDateFormat("dd MMM yy", new Locale("en", "EN"));
        return tableDateFormat.parse(dateFromTableText);
    }
}
