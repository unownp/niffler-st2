package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BaseComponent;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class Toastify extends BaseComponent<Toastify> {
    public Toastify() {
        super($(".main-content__section").$(".table"));
    }
    private final SelenideElement toastifyCloseButton = $(".Toastify__close-button");

    public SelenideElement getToastifyCloseButton() {
        return toastifyCloseButton;
    }

    private final SelenideElement profileUpdatedToastifyProgressBar = $(".Toastify__progress-bar");
    private final SelenideElement profileUpdatedToastifyIcon = $(".Toastify__toast-icon");

    @Override
    public Toastify checkThatComponentDisplayed() {
        toastifyCloseButton.shouldBe(visible);
        profileUpdatedToastifyProgressBar.shouldBe(visible);
        profileUpdatedToastifyIcon.shouldBe(visible);
        return new Toastify();
    }

}
