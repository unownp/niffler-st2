package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.PeopleTable;

public class FriendsPage extends BasePage<FriendsPage> {

    private final Header header = new Header();
    private final PeopleTable peopleTable = new PeopleTable();
    private final Footer footer = new Footer();

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
    public FriendsPage checkThatPageLoaded() {
        header.checkThatComponentDisplayed();
        footer.checkThatComponentDisplayed();
        peopleTable.checkThatComponentDisplayed();
        return this;
    }
}
