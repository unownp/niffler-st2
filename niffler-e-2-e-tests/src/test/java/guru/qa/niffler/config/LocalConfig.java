package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {
    static final LocalConfig INSTANCE = new LocalConfig();

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    private LocalConfig() {
    }


    @Override
    public String getDBHost() {
        return "localhost";
    }

    @Override
    public String getDBLogin() {
        return "postgres";
    }

    @Override
    public String getDBPassword() {
        return "secret";
    }

    @Override
    public int getDBPort() {
        return 5432;
    }

    @Override
    public String getBaseUrl() {
        return "http://127.0.0.1";
    }
//    public String getBaseUrl() {
//        return "localhost";
//    }

    @Override
    public int getOauth2Port() {
        return 9000;
    }

    @Override
    public int getFrontPort() {
        return 3000;
    }

    @Override
    public String getRegisterPath() {
        return "/register";
    }

    @Override
    public int getSpendPort() {
        return 8093;
    }

    @Override
    public String getOauthPath() {
        return "/oauth2";
    }

    @Override
    public String getAuthorizePath() {
        return getOauthPath() + "/authorize";
    }

    @Override
    public String getAuthorizedPath() {
        return "/authorized";
    }

    @Override
    public String getLoginPath() {
        return "/login";
    }

    @Override
    public String getTokenPath() {
        return getOauthPath() + "/token";
    }

    @Override
    public String getMainPath() {
        return "/main";
    }

    @Override
    public String getPeoplePath() {
        return "/people";
    }

    @Override
    public String getRedirectPath() {
        return "/redirect";
    }

    @Override
    public int getUserDataPort() {
        return 8089;
    }

    @Override
    public String getCurrentUserPath() {
        return "/currentUser";
    }

    @Override
    public String getFriendsPath() {
        return "/friends";
    }

    @Override
    public int getCurrencyGrpcPort() {
        return 8092;
    }

    @Override
    public String getSpendPath() {
        return "/spends";
    }

    @Override
    public String getFrontPath() {
        return null;
    }

    @Override
    public String getAuthPath() {
        return null;
    }

    @Override
    public String getCurrencyGrpcPath() {
        return null;
    }

    @Override
    public int getCurrencyPort() {
        return 0;
    }

    @Override
    public String getInvitationsPath() {
        return "/invitations";
    }

    @Override
    public String getCategoriesPath() {
        return "/categories";
    }

    @Override
    public String getStatisticPath() {
        return "/statistic";
    }
}
