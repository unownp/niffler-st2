package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

public class PeopleWebTest extends BaseWebTest {

    @ApiLogin(username = "GEESECATCHER", password = "12345")
    @Test
    void addNewFriend(String username)  {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class);
        peoplePage.checkThatPageLoaded();
        peoplePage.addNewFriend(username);
    }

    @ApiLogin(username = "GEESECATCHER", password = "12345")
    @Test
    void confirmFriendRequest(String username) {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class);
        peoplePage.checkThatPageLoaded();
        peoplePage.confirmFriendRequest(username);
    }

    @ApiLogin(username = "GEESECATCHER", password = "12345")
    @Test
    void declineFriendRequest(String username) {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class);
        peoplePage.checkThatPageLoaded();
        peoplePage.declineFriendRequest(username);
    }
}
