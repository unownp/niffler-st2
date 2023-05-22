package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BaseComponent;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class PeopleTable extends BaseComponent<PeopleTable> {
    public SelenideElement getPeopleTable() {
        return peopleTable;
    }

    public SelenideElement getPeopleTableAvatar() {
        return peopleTableAvatar;
    }

    public SelenideElement getPeopleTableUsername() {
        return peopleTableUsername;
    }

    public SelenideElement getPeopleTableName() {
        return peopleTableName;
    }

    public SelenideElement getPeopleTableActions() {
        return peopleTableActions;
    }

    public ElementsCollection getPeople() {
        return people;
    }

    public SelenideElement getPeopleFriendButton() {
        return peopleFriendButton;
    }

    private final SelenideElement peopleTable = $(".main-content__section").$(".table");
    private final SelenideElement peopleTableAvatar = peopleTable
            .$(byText("Avatar"));
    private final SelenideElement peopleTableUsername = peopleTable
            .$(byText("Username"));
    private final SelenideElement peopleTableName = peopleTable
            .$(byText("Name"));
    private final SelenideElement peopleTableActions = peopleTable
            .$(byText("Actions"));
    private final ElementsCollection people = $$x("//tbody/tr");
    private final SelenideElement peopleFriendButton = peopleTable
            .$(byAttribute("data-tooltip-id", "remove-friend"));
    public static final String youAreFriends = "You are friends";


    public PeopleTable() {
        super($(".main-content__section").$(".table"));
    }

    @Override
    public PeopleTable checkThatComponentDisplayed() {
        peopleTableAvatar.shouldBe(visible);
        peopleTableUsername.shouldBe(visible);
        peopleTableName.shouldBe(visible);
        peopleTableActions.shouldBe(visible);
        return this;
    }
}
