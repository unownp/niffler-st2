package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.StartPage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest {

    @Test
    void profileUpdateTest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        ProfilePage profilePage = mainPage.getHeader().goToProfilePage();
        profilePage.checkThatPageLoaded();
        profilePage.checkUpdateProfile();
    }

    @Test
    void categoryUpdateTest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        ProfilePage profilePage = mainPage.getHeader().goToProfilePage();
        profilePage.checkThatPageLoaded();
        profilePage.checkCategoryUpdate();
    }

    @Test
    void avatarUploadTest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        ProfilePage profilePage = mainPage.getHeader().goToProfilePage();
        profilePage.checkThatPageLoaded();
        profilePage.checkAvatarUploaded();
    }
}
