package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage extends BasePage<MainPage> {

    private final Header header = new Header();
    private final Footer footer = new Footer();

    private final SelenideElement mainContentAddSpending = $(".main-content__section.main-content__section-add-spending");
    private final SelenideElement categoryInput = mainContentAddSpending.$("#react-select-3-input");
    private final SelenideElement categoryInputPlaceHolder = mainContentAddSpending.$("#react-select-3-placeholder");
    private final SelenideElement categoryInputDropDown = mainContentAddSpending.$("#react-select-3-listbox");
    private final SelenideElement categoryInputEmptyListBox = categoryInputDropDown.$(byText("No options"));
    private final SelenideElement categoryFormLabel = categoryInput.ancestor(".form__label");
    private final SelenideElement categoryFormLabelError = categoryFormLabel.$x("span[@class='form__error']");
    private final SelenideElement amountInput = mainContentAddSpending.$x(".//input[@class='form__input ' and @name='amount']");
    private final SelenideElement amountFormLabel = amountInput.ancestor(".form__label");
    private final SelenideElement spendDateInput = mainContentAddSpending.$(".react-datepicker__input-container");
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
    private final SelenideElement spendingsTableCheckBox = spendingsTable
            .$(byAttribute("type", "checkbox"));
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


    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

    public SelenideElement getMainContentAddSpending() {
        return mainContentAddSpending;
    }

    public SelenideElement getCategoryInput() {
        return categoryInput;
    }

    public SelenideElement getCategoryInputDropDown() {
        return categoryInputDropDown;
    }

    public SelenideElement getCategoryInputEmptyListBox() {
        return categoryInputEmptyListBox;
    }

    public SelenideElement getCategoryFormLabel() {
        return categoryFormLabel;
    }

    public SelenideElement getCategoryFormLabelError() {
        return categoryFormLabelError;
    }

    public SelenideElement getSubmitButton() {
        return submitButton;
    }

    public SelenideElement getAmountInput() {
        return amountInput;
    }

    public SelenideElement getAmountFormLabel() {
        return amountFormLabel;
    }

    public SelenideElement getSpendDateInput() {
        return spendDateInput;
    }

    public SelenideElement getSpendDateFormLabel() {
        return spendDateFormLabel;
    }

    public SelenideElement getDescriptionInput() {
        return descriptionInput;
    }

    public SelenideElement getDescriptionFormLabel() {
        return descriptionFormLabel;
    }

    public SelenideElement getMainContentSection() {
        return mainContentSection;
    }

    public SelenideElement getMainContentStats() {
        return mainContentStats;
    }

    public SelenideElement getMainContentHistory() {
        return mainContentHistory;
    }

    public SelenideElement getMainContentHistoryName() {
        return mainContentHistoryName;
    }

    public SelenideElement getMainContentHistoryImage() {
        return mainContentHistoryImage;
    }

    public SelenideElement getSpendingsContent() {
        return spendingsContent;
    }

    public SelenideElement getSpendingsControls() {
        return spendingsControls;
    }

    public SelenideElement getSpendingsButtons() {
        return spendingsButtons;
    }

    public SelenideElement getSpendingsControlsHeader() {
        return spendingsControlsHeader;
    }

    public SelenideElement getSpendingsButtonToday() {
        return spendingsButtonToday;
    }

    public SelenideElement getSpendingsButtonLastWeek() {
        return spendingsButtonLastWeek;
    }

    public SelenideElement getSpendingsButtonLastMonth() {
        return spendingsButtonLastMonth;
    }

    public SelenideElement getSpendingsButtonAllTime() {
        return spendingsButtonAllTime;
    }

    public SelenideElement getSpendingsCurrencyButton() {
        return spendingsCurrencyButton;
    }

    public SelenideElement getSpendingsCurrencyVariations() {
        return spendingsCurrencyVariations;
    }

    public SelenideElement getSpendingsCurrencyVariationsAllButton() {
        return spendingsCurrencyVariationsAllButton;
    }

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
}
