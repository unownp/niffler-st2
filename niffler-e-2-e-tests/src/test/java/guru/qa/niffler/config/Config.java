package guru.qa.niffler.config;

public interface Config {

    static Config getConfig() {
        if ("docker".equals(System.getProperty("env"))) {
            return new DockerConfig();
        }
        return new LocalConfig();
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
}
