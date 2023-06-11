package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.users.NifflerUsersDAO;
import guru.qa.niffler.db.dao.users.NifflerUsersDAOHibernate;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Disabled
public class LoginNewUserTest extends BaseWebTest {

    private static final Faker faker = new Faker();
    private final NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();
    private UserEntity ue;

    private static final String TEST_PWD = "12345";

    @BeforeEach
    void createUserForTest() {
        ue = new UserEntity();
        ue.setUsername("valentin3");
        ue.setPassword(TEST_PWD);
        ue.setEnabled(true);
        ue.setAccountNonExpired(true);
        ue.setAccountNonLocked(true);
        ue.setCredentialsNonExpired(true);
        ue.setAuthorities(Arrays.stream(Authority.values()).map(
                a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(ue);
                    return ae;
                }
        ).toList());
        usersDAO.createUser(ue);
    }

    @AfterEach
    void cleanUp() {
        usersDAO.removeUser(ue);
    }

    @Test
    void loginTest() {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(ue.getUsername());
        $("input[name='password']").setValue(TEST_PWD);
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }

}
