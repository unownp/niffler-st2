package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.Test;

public class RegistrationWebTest extends BaseWebTest {

    @Test
    void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
        Selenide.open(RegistrationPage.URL, RegistrationPage.class)
                .checkThatPageLoaded()
                .fillRegistrationForm("wdfsdasfs", "123", "12345")
                .checkErrorMessage("Passwords should be equal");

        new FriendsPage()
                .getHeader()
                .goToMainPage()
                .getHeader()
                .goToFriendsPage();
    }

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
