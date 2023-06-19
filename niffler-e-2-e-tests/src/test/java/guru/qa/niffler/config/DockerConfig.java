package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {
    static final DockerConfig INSTANCE = new DockerConfig();

    static {
        Configuration.browser = "chrome";
        Configuration.browserVersion = "110.0";
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.browserSize = "1920x1080";
    }

    private DockerConfig() {
    }


    @Override
    public String getDBHost() {
        return "niffler-all-db";
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
        return "niffler-";
    }

    @Override
    public int getOauth2Port() {
        return 9000;
    }

    @Override
    public String getAuthPath() {
        return "auth";
    }

    @Override
    public String getCurrencyGrpcPath() {
        return "currency";
    }

    @Override
    public int getCurrencyPort() {
        return 8091;
    }

    @Override
    public String getInvitationsPath() {
        return null;
    }

    @Override
    public String getCategoriesPath() {
        return null;
    }

    @Override
    public String getStatisticPath() {
        return null;
    }

    @Override
    public int getCurrencyGrpcPort() {
        return 8092;
    }

    @Override
    public int getFrontPort() {
        return 0;
    }

    @Override
    public String getFrontPath() {
        return "frontend";
    }

    @Override
    public String getRegisterPath() {
        return null;
    }

    @Override
    public int getSpendPort() {
        return 8093;
    }

    @Override
    public String getSpendPath() {
        return "spend";
    }


    @Override
    public String getOauthPath() {
        return null;
    }

    @Override
    public String getAuthorizePath() {
        return null;
    }

    @Override
    public String getAuthorizedPath() {
        return null;
    }

    @Override
    public String getLoginPath() {
        return null;
    }

    @Override
    public String getTokenPath() {
        return null;
    }

    @Override
    public String getMainPath() {
        return null;
    }

    @Override
    public String getPeoplePath() {
        return null;
    }

    @Override
    public String getRedirectPath() {
        return null;
    }

    @Override
    public int getUserDataPort() {
        return 0;
    }

    @Override
    public String getCurrentUserPath() {
        return null;
    }

    @Override
    public String getFriendsPath() {
        return null;
    }


}
