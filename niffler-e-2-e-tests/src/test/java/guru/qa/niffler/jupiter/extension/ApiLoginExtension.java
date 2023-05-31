package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static io.restassured.RestAssured.given;

public class ApiLoginExtension implements BeforeEachCallback {
    public static String USERNAME;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLogin != null) {
            doLogin(apiLogin.username(), apiLogin.password());
            USERNAME=apiLogin.username();
        }
    }

    private void doLogin(String username, String password) throws NoSuchAlgorithmException {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        Response response = given()
                .redirects().follow(false)
                .queryParam("response_type", "code")
                .queryParam("client_id", "client")
                .queryParam("scope", "openid")
                .queryParam("redirect_uri", "http://127.0.0.1:3000/authorized")
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .get("http://127.0.0.1:9000/oauth2/authorize");
        String jsessionId = "JSESSIONID";
        String jsession = response.cookie(jsessionId);
        System.out.println(response.statusCode());
        System.out.println(response.cookies());
        System.out.println(response.headers());

        Response response1 = given()
                .cookie(jsessionId, jsession)
                .get("http://127.0.0.1:9000/login");
        String xsrfToken = "XSRF-TOKEN";
        String xsrf = response1.cookie(xsrfToken);
        System.out.println(xsrf);
        System.out.println(jsession);
        System.out.println(response1.statusCode());
        System.out.println(response1.cookies());
        System.out.println(response1.headers());

        Response response2 = given()
                .redirects().follow(false)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("_csrf", xsrf)
                .formParam("username", username)
                .formParam("password", password)
                .cookie(jsessionId, jsession)
                .cookie(xsrfToken, xsrf)
                .post("http://127.0.0.1:9000/login");
        jsession = response2.cookie(jsessionId);
        System.out.println(response2.statusCode());
        System.out.println(response2.cookies());
        System.out.println(response2.headers());

        Response response3 =
                given()
                        .redirects().follow(false)
                        .cookie(jsessionId, jsession)
                        .queryParam("response_type", "code")
                        .queryParam("client_id", "client")
                        .queryParam("scope", "openid")
                        .queryParam("redirect_uri", "http://127.0.0.1:3000/authorized")
                        .queryParam("code_challenge", codeChallenge)
                        .queryParam("code_challenge_method", "S256")
                        .queryParam("continue", "")
                        .get("http://127.0.0.1:9000/oauth2/authorize");
        System.out.println(response3.statusCode());
        System.out.println(response3.cookies());
        System.out.println(response3.headers());
        Response response4 =
                given()
                        .cookie(jsessionId, jsession)
                        .get(response3.header("location"));
        System.out.println(response4.statusCode());
        System.out.println(response4.cookies());
        System.out.println(response4.headers());

        String code = response3.header("location").substring(response3.header("location").indexOf("code") + 5);
        System.out.println(code);
        Response response5 =
                given()
                        .redirects().follow(false)
                        .queryParam("client_id", "client")
                        .queryParam("redirect_uri", "http://127.0.0.1:3000/authorized")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("code", code)
                        .queryParam("code_verifier", codeVerifier)
                        .header("Authorization", "Basic Y2xpZW50OnNlY3JldA==")
                        .post("http://127.0.0.1:9000/oauth2/token");
        System.out.println(response5.statusCode());
        System.out.println(response5.cookies());
        System.out.println(response5.headers());
        String token = response5.body().jsonPath().get("access_token");
        System.out.println(token);
//        Response response6 =
//                given()
//                        .cookie(jsessionId, jsession)
//                        .header("Authorization", "Bearer " + token)
//                        .get("http://127.0.0.1:8090/allCurrencies");
//        System.out.println(response6.body().asString());
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        Selenide.sessionStorage().setItem("id_token", token);
        Selenide.sessionStorage().setItem("codeChallenge", codeChallenge);
        Selenide.sessionStorage().setItem("codeVerifier", codeVerifier);
        Cookie jssesionIdCookie = new Cookie(jsessionId, jsession);
        WebDriverRunner.getWebDriver().manage().addCookie(jssesionIdCookie);

    }

    static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}