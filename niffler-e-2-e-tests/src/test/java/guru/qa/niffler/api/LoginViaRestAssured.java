package guru.qa.niffler.api;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.MainPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class LoginViaRestAssured {
    public static final String ID_TOKEN_SESSION_STORAGE_NAME = "id_token";
    public static final String CODE_VERIFIER_SESSION_STORAGE_NAME = "codeVerifier";
    public static final String CODE_CHALLENGE_SESSION_STORAGE_NAME = "codeChallenge";
    public static final String JSESSION_ID_COOKIE_NAME = "JSESSIONID";
    public static final String XSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";
    public final Config config=Config.getConfig();

    public HashMap<String, String> getTokenHashMap(String username, String password) throws NoSuchAlgorithmException {
        RestAssured.baseURI= config.getBaseUrl();
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
                .port(config.getOauth2Port())
                .get(config.getAuthorizePath());

        String jsession = response.cookie(JSESSION_ID_COOKIE_NAME);
        System.out.println(response.statusCode());
        System.out.println(response.cookies());
        System.out.println(response.headers());

        Response response1 = given()
                .cookie(JSESSION_ID_COOKIE_NAME, jsession)
                .port(config.getOauth2Port())
                .get(config.getLoginPath());
        String xsrf = response1.cookie(XSRF_TOKEN_COOKIE_NAME);
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
                .cookie(JSESSION_ID_COOKIE_NAME, jsession)
                .cookie(XSRF_TOKEN_COOKIE_NAME, xsrf)
                .port(config.getOauth2Port())
                .post(config.getLoginPath());
        jsession = response2.cookie(JSESSION_ID_COOKIE_NAME);
        System.out.println(response2.statusCode());
        System.out.println(response2.cookies());
        System.out.println(response2.headers());

        Response response3 =
                given()
                        .redirects().follow(false)
                        .cookie(JSESSION_ID_COOKIE_NAME, jsession)
                        .queryParam("response_type", "code")
                        .queryParam("client_id", "client")
                        .queryParam("scope", "openid")
                        .queryParam("redirect_uri", baseURI+":"+config.getFrontPort()+config.getAuthorizedPath())
                        .queryParam("code_challenge", codeChallenge)
                        .queryParam("code_challenge_method", "S256")
                        .queryParam("continue", "")
                        .port(config.getOauth2Port())
                        .get(config.getAuthorizePath());
        System.out.println(response3.statusCode());
        System.out.println(response3.cookies());
        System.out.println(response3.headers());
        Response response4 =
                given()
                        .cookie(JSESSION_ID_COOKIE_NAME, jsession)
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
                        .queryParam("redirect_uri", baseURI+":"+config.getFrontPort()+config.getAuthorizedPath())
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("code", code)
                        .queryParam("code_verifier", codeVerifier)
                        .header("Authorization", "Basic Y2xpZW50OnNlY3JldA==")
                        .port(config.getOauth2Port())
                        .post(config.getTokenPath());
        System.out.println(response5.statusCode());
        System.out.println(response5.cookies());
        System.out.println(response5.headers());
        String token = response5.body().jsonPath().get("access_token");
        System.out.println(token);
        HashMap<String, String> tokenHashMap = new HashMap<>();
        tokenHashMap.put(ID_TOKEN_SESSION_STORAGE_NAME, token);
        tokenHashMap.put(CODE_CHALLENGE_SESSION_STORAGE_NAME, codeChallenge);
        tokenHashMap.put(CODE_VERIFIER_SESSION_STORAGE_NAME, codeVerifier);
        tokenHashMap.put(JSESSION_ID_COOKIE_NAME, jsession);
//        Response response6 =
//                given()
//                        .cookie(jsessionId, jsession)
//                        .header("Authorization", "Bearer " + token)
//                        .get("http://127.0.0.1:8090/allCurrencies");
//        System.out.println(response6.body().asString());
        return tokenHashMap;
    }


    /** @noinspection ResultOfMethodCallIgnored*/
    public void doLogin(HashMap<String, String> tokenHashMap) {
        Selenide.open(MainPage.URL, MainPage.class);
        Selenide.sessionStorage().setItem(ID_TOKEN_SESSION_STORAGE_NAME, tokenHashMap.get(ID_TOKEN_SESSION_STORAGE_NAME));
        Selenide.sessionStorage().setItem(CODE_CHALLENGE_SESSION_STORAGE_NAME, tokenHashMap.get(CODE_CHALLENGE_SESSION_STORAGE_NAME));
        Selenide.sessionStorage().setItem(CODE_VERIFIER_SESSION_STORAGE_NAME, tokenHashMap.get(CODE_VERIFIER_SESSION_STORAGE_NAME));
        Cookie jsessionIdCookie = new Cookie(JSESSION_ID_COOKIE_NAME, tokenHashMap.get(JSESSION_ID_COOKIE_NAME));
        WebDriverRunner.getWebDriver().manage().addCookie(jsessionIdCookie);
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
