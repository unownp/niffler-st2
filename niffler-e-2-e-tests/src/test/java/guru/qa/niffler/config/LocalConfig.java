package guru.qa.niffler.config;

public class LocalConfig implements Config {

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
  //  public String getBaseUrl() {
//        return "http://127.0.0.1";
//    }
    public String getBaseUrl() {
        return "localhost";
    }

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
}
