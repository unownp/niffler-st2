package guru.qa.niffler.config;

public interface Config {

    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.INSTANCE;
        } else if ("local".equals(System.getProperty("test.env"))) {
            return LocalConfig.INSTANCE;
        } else throw new IllegalStateException("Can`t read 'test.env' System property");
    }

    String getDBHost();

    String getDBLogin();

    String getDBPassword();

    int getDBPort();

    String getBaseUrl();

    int getOauth2Port();

    int getFrontPort();

    String getRegisterPath();

    int getSpendPort();

    String getOauthPath();

    String getAuthorizePath();

    String getAuthorizedPath();

    String getLoginPath();

    String getTokenPath();

    String getMainPath();

    String getPeoplePath();

    String getRedirectPath();

    int getUserDataPort();

    String getCurrentUserPath();

    String getFriendsPath();

    int getCurrencyGrpcPort();

    String getSpendPath();

    String getFrontPath();

    String getAuthPath();

    String getCurrencyGrpcPath();

    int getCurrencyPort();

    String getInvitationsPath();

    String getCategoriesPath();

    String getStatisticPath();
}
