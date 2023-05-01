package niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOHibernate;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginNewUserTest extends BaseWebTest {

  private static Faker faker = new Faker();
  private NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();
//  private NifflerUsersDAO usersDAO = new NifflerUsersDAOSpringJdbc();
  private UserEntity ue;

  private static final String TEST_PWD = "1234567";

  @BeforeEach
  void createUserForTest() {
    ue = new UserEntity();
    ue.setUsername("muchogusto1");
    ue.setPassword(TEST_PWD);
    ue.setEnabled(false);
    ue.setAccountNonExpired(false);
    ue.setAccountNonLocked(true);
    ue.setCredentialsNonExpired(false);
    ue.setAuthorities(Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          ae.setUser(ue);
          return ae;
        }
    ).toList());
    usersDAO.updateUser(ue);
  }

  @AfterEach
  void cleanUp() {
  //  usersDAO.removeUser(ue);
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
