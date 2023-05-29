package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.StartPage;
import org.junit.jupiter.api.Test;

public class PeopleWebTest extends BaseWebTest {

    @Test
    void addNewFriend() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        PeoplePage peoplePage = mainPage.getHeader().goToPeoplePage();
        peoplePage.checkThatPageLoaded();
        peoplePage.addNewFriend();
    }

    @Test
    void confirmFriendRequest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        PeoplePage peoplePage = mainPage.getHeader().goToPeoplePage();
        peoplePage.checkThatPageLoaded();
        peoplePage.confirmFriendRequest();
    }

    @Test
    void declineFriendRequest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        PeoplePage peoplePage = mainPage.getHeader().goToPeoplePage();
        peoplePage.checkThatPageLoaded();
        peoplePage.declineFriendRequest();
    }
}
