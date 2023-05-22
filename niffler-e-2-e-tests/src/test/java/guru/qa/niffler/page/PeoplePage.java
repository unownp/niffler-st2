package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.PeopleTable;

import static com.codeborne.selenide.Selectors.byAttribute;

public class PeoplePage extends BasePage<PeoplePage> {

    private final Header header = new Header();
    private final PeopleTable peopleTable = new PeopleTable();
    private final Footer footer = new Footer();
    private final SelenideElement addFriendButton = peopleTable.getPeopleTable()
            .$(byAttribute("data-tooltip-id", "add-friend"));

    public PeopleTable getPeopleTable() {
        return peopleTable;
    }

    public Footer getFooter() {
        return footer;
    }

    public Header getHeader() {
        return header;
    }

    @Override
    public PeoplePage checkThatPageLoaded() {
        header.checkThatComponentDisplayed();
        peopleTable.checkThatComponentDisplayed();
        footer.checkThatComponentDisplayed();
        return this;
    }
}
