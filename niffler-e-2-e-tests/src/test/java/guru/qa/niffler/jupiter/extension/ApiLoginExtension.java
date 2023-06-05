package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.LoginViaRestAssured;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiLoginExtension implements BeforeEachCallback {
    public static String USERNAME;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLogin != null) {
            LoginViaRestAssured loginViaRestAssured = new LoginViaRestAssured();
            loginViaRestAssured.doLogin(loginViaRestAssured.getTokenHashMap(apiLogin.username(), apiLogin.password()));
            USERNAME = apiLogin.username();
        }
    }

}