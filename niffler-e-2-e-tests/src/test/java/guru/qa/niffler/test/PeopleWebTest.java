package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

public class PeopleWebTest extends BaseWebTest {

    @ApiLogin(username = "GEESECATCHER", password = "12345")
    @Test
    void addNewFriend()  {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class);
        peoplePage.checkThatPageLoaded();
        peoplePage.addNewFriend();
    }

    @ApiLogin(username = "GEESECATCHER", password = "12345")
    @Test
    void confirmFriendRequest() {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class);
        peoplePage.checkThatPageLoaded();
        peoplePage.confirmFriendRequest();
    }

    @ApiLogin(username = "GEESECATCHER", password = "12345")
    @Test
    void declineFriendRequest() {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class);
        peoplePage.checkThatPageLoaded();
        peoplePage.declineFriendRequest();
    }
}
