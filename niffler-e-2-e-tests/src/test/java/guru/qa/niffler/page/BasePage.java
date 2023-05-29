package guru.qa.niffler.page;

import guru.qa.niffler.config.Config;

public abstract class BasePage<T extends BasePage<T>> {
    public static final String BASE_URL = Config.getConfig().getBaseUrl();
    public static final String OAUTH2_PORT = Config.getConfig().getOauth2Port();
    public static final String FRONT_PORT = Config.getConfig().getFrontPort();
    public static final String USERNAME = "ELGATO";


    public abstract T checkThatPageLoaded();


}
