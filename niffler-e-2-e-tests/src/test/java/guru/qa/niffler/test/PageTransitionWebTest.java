package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.Test;

public class PageTransitionWebTest extends BaseWebTest {

    @Test
    void test() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        RegistrationPage registrationPage = startPage.clickRegisterButton();
        registrationPage.checkThatPageLoaded();
        LoginPage loginPage = registrationPage.clickSignInHyperLink();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        FriendsPage friendsPage = mainPage.getHeader().goToFriendsPage();
        friendsPage.checkThatPageLoaded();
        PeoplePage peoplePage = friendsPage.getHeader().goToPeoplePage();
        peoplePage.checkThatPageLoaded();
        ProfilePage profilePage = peoplePage.getHeader().goToProfilePage();
        profilePage.checkThatPageLoaded();
        startPage = profilePage.getHeader().logOut();
        startPage.checkThatPageLoaded();
    }
}
