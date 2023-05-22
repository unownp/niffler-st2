package guru.qa.niffler.page.component;

import guru.qa.niffler.page.BaseComponent;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Footer extends BaseComponent<Footer> {
    public Footer() {
        super($(".footer"));
    }

    @Override
    public Footer checkThatComponentDisplayed() {
        self.shouldHave(text("Study project for QA Automation Advanced. 2023"));
        return this;
    }
}
