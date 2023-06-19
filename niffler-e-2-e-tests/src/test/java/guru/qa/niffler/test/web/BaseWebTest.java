package guru.qa.niffler.test.web;


import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.WebTest;
import org.junit.jupiter.api.BeforeAll;

@WebTest
public abstract class BaseWebTest {

    @BeforeAll
    static void setUp() {
        Configuration.browser = "Firefox";
        Configuration.browserSize = "1500x1500";
//        Configuration.holdBrowserOpen=true;
    }

}
