package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseComponent<T extends BaseComponent<T>> {

  protected final SelenideElement self;

  public BaseComponent(SelenideElement self) {
    this.self = self;
  }

  public abstract T checkThatComponentDisplayed();
}
