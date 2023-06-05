package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.*;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class GenerateUserExtension implements ParameterResolver, BeforeEachCallback {
    private final Config config = Config.getConfig();

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        GenerateUser annotation = context.getRequiredTestMethod()
                .getAnnotation(GenerateUser.class);
        if (annotation != null) {
            UserJson user = generateRandomUser();
            user = createNewUserViaApi(user);
            context.getStore(NAMESPACE).put("user", user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("user", UserJson.class);
    }

    public UserJson generateRandomUser() {
        Faker faker = new Faker();
        UserJson userJson = new UserJson();
        userJson.setUsername(faker.name().firstName());
        userJson.setPassword("12345");
        System.out.println(userJson);
        return userJson;
    }

    public UserJson createNewUserViaApi(UserJson userJson) {
        RestAssured.baseURI = config.getBaseUrl();
        Response response1 = given()
                .port(config.getOauth2Port())
                .get(config.getRegisterPath());
        String xsrfToken = "XSRF-TOKEN";
        String xsrf = response1.cookie(xsrfToken);
        System.out.println("NEW USER");
        System.out.println(xsrf);
        System.out.println(response1.statusCode());
        System.out.println(response1.cookies());
        System.out.println(response1.headers());

        Response response2 = given()
                .port(config.getOauth2Port())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("_csrf", xsrf)
                .formParam("username", userJson.getUsername())
                .formParam("password", userJson.getPassword())
                .formParam("passwordSubmit", userJson.getPassword())
                .cookie(xsrfToken, xsrf)
                .post(config.getRegisterPath());
        System.out.println("NEW USER");
        System.out.println(response2.statusCode());
        System.out.println(response2.cookies());
        System.out.println(response2.headers());

        Response response3 = given()
                .port(config.getUserDataPort())
                .queryParam("username", userJson.getUsername())
                .get(config.getCurrentUserPath());
        System.out.println("NEW USER");
        System.out.println(response3.statusCode());
        System.out.println(response3.cookies());
        System.out.println(response3.headers());

        userJson.setId(UUID.fromString(response3.jsonPath().get("id")));
        userJson.setCurrency(CurrencyValues.valueOf(response3.jsonPath().get("currency")));
        userJson.setFirstname(response3.jsonPath().get("firstname"));
        userJson.setSurname(response3.jsonPath().get("surname"));
        userJson.setPhoto(response3.jsonPath().get("photo"));
        return userJson;
    }
}