package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.Toastify;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.*;

public class ProfilePage extends BasePage<ProfilePage> {
    private final Header header = new Header();
    private final Footer footer = new Footer();
    private final SelenideElement avatarSection = $(".main-content__section-avatar");
    private final SelenideElement avatarImage = avatarSection.$(".profile__avatar");
    private final SelenideElement userName = avatarSection.$x(".//figcaption");
    private final SelenideElement profileInfoContainer = avatarSection.$(".profile__info-container");
    private final SelenideElement nameInput = profileInfoContainer
            .$(byAttribute("name", "firstname"));
    private final SelenideElement surnameInput = profileInfoContainer
            .$(byAttribute("name", "surname"));
    private final SelenideElement currencyInput = profileInfoContainer
            .$(".select-wrapper");
    private final SelenideElement submitButton = avatarSection
            .$(byText("Submit"));
    private final SelenideElement nameFormLabel = nameInput.parent();
    private final SelenideElement surnameFormLabel = surnameInput.parent();
    private final SelenideElement currencyFormLabel = currencyInput.ancestor(".form__label");
    private final ElementsCollection currencyVariations = currencyInput
            .$$x(".//div[contains(@id,'react-select') and contains(@id,'-option')]");
    private final SelenideElement addCategorySection = $(".main-content__section-add-category");
    private final SelenideElement addCategorySectionHeader = addCategorySection.$x(".//h2");
    private final ElementsCollection addCategoryInfo = addCategorySection.$$(".add-category__info");
    private final SelenideElement addCategoryInput = addCategorySection
            .$(byAttribute("name", "category"));
    private final SelenideElement addCategoryInputFormLabel = addCategoryInput.parent();
    private final SelenideElement addCategoryCreateButton = addCategorySection.$(byText("Create"));
    private final SelenideElement categoriesSection = $(".main-content__section-categories");
    private final SelenideElement allSpendingCategoriesHeader = categoriesSection.$x("h3");
    private final ElementsCollection categoriesList = categoriesSection.$$(".categories__item");
    private final SelenideElement categoriesSectionNoCategoriesMessage = categoriesSection.$x("span");
    private final SelenideElement profileUpdatedToastifyText = $(byText("Profile updated!"));
    private final SelenideElement categoryUpdatedToastifyText = $(byText("New category added!"));
    private final SelenideElement categoryMaxSizeReachedToastifyText = $(byText("Can not add new category"));
    private final SelenideElement closeEditAvatarButton = avatarSection.$(".button-icon_type_close");
    private final SelenideElement editAvatarHeader = avatarSection.$(".edit-avatar__header");
    private final SelenideElement editAvatarInput = avatarSection.$(".edit-avatar__input");

    public static final String NAME_TEXT = "Name";
    public static final String SURNAME_TEXT = "Surname";
    public static final String CURRENCY_TEXT = "Currency";
    public static final String ADD_CATEGORY_SECTION_HEADER_TEXT = "Add new category";
    public static final List<String> categoryInfoTexts =
            new ArrayList<>(Arrays.asList("Note, that number of categories is limited!",
                    "You can add not more than 8 different categories"));
    public static final String ADD_CATEGORY_INPUT_FORM_LABEL_TEXT = "Category name";
    public static final String ALL_SPENDING_CATEGORIES_HEADER_TEXT = "All your spending categories";
    public static final String CATEGORIES_SECTION_NO_CATEGORIES_TEXT = "No spending categories yet!";
    public static final String EDIT_AVATAR_HEADER_TEXT = "Update Avatar";
    private String name;
    private String surname;
    private String newCategory;

    @Override
    public ProfilePage checkThatPageLoaded() {
        header.checkThatComponentDisplayed();
        avatarImage.shouldBe(visible);
        nameInput.shouldBe(visible);
        nameInput.shouldHave(attribute("placeholder", "Set your name"));
        surnameInput.shouldBe(visible);
        surnameInput.shouldHave(attribute("placeholder", "Set your surname"));
        currencyInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        addCategoryInput.shouldBe(visible);
        addCategoryInput.shouldHave(attribute("placeholder", "Add new category"));
        addCategoryCreateButton.shouldBe(visible);
        footer.checkThatComponentDisplayed();
        List<String> addCategoryInfoActualList = addCategoryInfo.texts();
        addCategoryInfoActualList.sort(naturalOrder());
        assertAll(
                () -> assertTrue(nameFormLabel.getOwnText().contains(NAME_TEXT)),
                () -> assertTrue(surnameFormLabel.getOwnText().contains(SURNAME_TEXT)),
                () -> assertTrue(currencyFormLabel.getOwnText().contains(CURRENCY_TEXT)),
                () -> assertEquals(addCategorySectionHeader.getOwnText(), ADD_CATEGORY_SECTION_HEADER_TEXT),
                () -> assertEquals(addCategoryInfoActualList, categoryInfoTexts),
                () -> assertTrue(addCategoryInputFormLabel.getOwnText().contains(ADD_CATEGORY_INPUT_FORM_LABEL_TEXT)),
                () -> assertEquals(allSpendingCategoriesHeader.getOwnText(), ALL_SPENDING_CATEGORIES_HEADER_TEXT)
        );
        return this;
    }

    public Header getHeader() {
        return header;
    }

    public void checkUpdateProfile() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        int randomCurrencyIndex = faker.number().numberBetween(0, 3);
        currencyInput.click();
        String currency = currencyVariations.get(randomCurrencyIndex).getOwnText();
        currencyVariations.get(randomCurrencyIndex).click();
        nameInput.setValue(name);
        surnameInput.setValue(surname);
        submitButton.scrollTo().click();
        this.name = name;
        this.surname = surname;
        submitButton.click();
        Toastify toastify = new Toastify();
        toastify.checkThatComponentDisplayed();
        profileUpdatedToastifyText.shouldBe(visible);
        toastify.getToastifyCloseButton().click();
        //  Arrays.stream(CurrencyValues.values()).toList().contains(currency);
        assertAll(
                () -> assertEquals(nameInput.getAttribute("value"), name),
                () -> assertEquals(surnameInput.getAttribute("value"), surname),
                () -> assertEquals(currencyInput.getText(), currency)
        );
    }

    public void checkCategoryUpdate() {
        Faker faker = new Faker();
        String newCategory = faker.pokemon().name();
        addCategoryInput.setValue(newCategory);
        addCategoryCreateButton.click();
        List<String> categories = categoriesList.texts();
        Toastify toastify = new Toastify();
        toastify.checkThatComponentDisplayed();
        if (categories.size() < 8) {
            categoryUpdatedToastifyText.shouldBe(visible);
            this.newCategory = newCategory;
            assertTrue(categoriesList.texts().contains(newCategory));
        } else
            categoryMaxSizeReachedToastifyText.shouldBe(visible);
        toastify.getToastifyCloseButton().click();
    }

    public void checkAvatarUploaded() {
        avatarImage.click();
        closeEditAvatarButton.shouldBe(visible);
        editAvatarHeader.shouldBe(visible);
        File file = new File("niffler-e-2-e-tests/src/test/resources/220x330.jpg");
        assertEquals(editAvatarHeader.getOwnText(), EDIT_AVATAR_HEADER_TEXT);
        editAvatarInput.uploadFile(file);
        submitButton.scrollTo().click();
        refresh();
        avatarImage.shouldBe(visible);
    }

    public ElementsCollection getCategoriesList() {
        return categoriesList;
    }

    public SelenideElement getCurrencyInput() {
        return currencyInput;
    }
}
