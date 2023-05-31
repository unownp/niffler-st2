package guru.qa.niffler.test;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static io.restassured.RestAssured.given;

public class RestAssuredAuthorizationTest {
    @Test
    void test() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallange(codeVerifier);

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
                .formParam("username", "ELGAT0")
                .formParam("password", "12345")
                .cookie(jsessionId, jsession)
                .cookie(xsrfToken, xsrf)
                .post("http://127.0.0.1:9000/login");

        System.out.println(response2.statusCode());
        System.out.println(response2.cookies());
        System.out.println(response2.headers());
    }
    String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    String generateCodeChallange(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
