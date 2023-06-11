package guru.qa.niffler.test.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
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
    @ApiLogin(username = "GEESECATCHER", password = "12345")
    void confirmFriendRequest(String username) {
        FriendsPage friendsPage = Selenide.open(FriendsPage.URL, FriendsPage.class);
        friendsPage.checkThatPageLoaded();
        friendsPage.confirmFriendRequest(username);
    }

    @Test
    @ApiLogin(username = "GEESECATCHER", password = "12345")
    void declineFriendRequest(String username) {
        FriendsPage friendsPage = Selenide.open(FriendsPage.URL, FriendsPage.class);
        friendsPage.checkThatPageLoaded();
        friendsPage.declineFriendRequest(username);
    }
}
