package guru.qa.niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.StartPage;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

//@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

    @Disabled
    @AllureId("102")
    @Test
    void friendsShouldBeVisible0(@User(userType = WITH_FRIENDS) UserJson user) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }

    @Disabled
    @AllureId("103")
    @Test
    void friendsShouldBeVisible1(@User(userType = INVITATION_SENT) UserJson user) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr").find(Condition.text("Pending invitation"))
                .should(Condition.visible);
    }

    @Test
    void confirmFriendRequest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        FriendsPage friendsPage = mainPage.getHeader().goToFriendsPage();
        friendsPage.checkThatPageLoaded();
        friendsPage.confirmFriendRequest();
    }

    @Test
    void declineFriendRequest() {
        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signIn();
        mainPage.checkThatPageLoaded();
        FriendsPage friendsPage = mainPage.getHeader().goToFriendsPage();
        friendsPage.checkThatPageLoaded();
        friendsPage.declineFriendRequest();
    }
}
