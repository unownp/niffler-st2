package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.StartPage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainWebTest extends BaseWebTest {

    @Test
    void categoriesShouldBeSameWithProfilePage() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        ProfilePage profilePage = mainPage.getHeader().goToProfilePage();
        profilePage.checkThatPageLoaded();
        List<String> profileCategories = profilePage.getCategoriesList().texts();
        profileCategories.sort(naturalOrder());
        System.out.println(profileCategories);
        mainPage = profilePage.getHeader().goToMainPage();
        mainPage.checkThatPageLoaded();
        List<String> mainCategories = mainPage.getCategoriesList();
        mainCategories.sort(naturalOrder());
        System.out.println(mainCategories);
        assertEquals(profileCategories, mainCategories);
    }

    @Test
    void addNewSpending() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        mainPage.checkNewSpendingCreated();
    }

    @Test
    void deleteAllSpending() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        mainPage.deleteAllSpendings();
    }
}
