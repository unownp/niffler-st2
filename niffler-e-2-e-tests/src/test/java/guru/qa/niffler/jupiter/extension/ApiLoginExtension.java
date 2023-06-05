package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.LoginViaRestAssured;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;

public class ApiLoginExtension implements ParameterResolver, BeforeEachCallback {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(ApiLoginExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLogin != null) {
            LoginViaRestAssured loginViaRestAssured = new LoginViaRestAssured();
            loginViaRestAssured.doLogin(loginViaRestAssured.getTokenHashMap(apiLogin.username(), apiLogin.password()));
            String username = apiLogin.username();
            context.getStore(NAMESPACE).put("username", username);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("username", String.class);
    }
}