package guru.qa.niffler.config;

public class DockerConfig implements Config {

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
    return null;
  }

  @Override
  public int getOauth2Port() {
    return 0;
  }

  @Override
  public int getFrontPort() {
    return 0;
  }

  @Override
  public String getRegisterPath() {
    return null;
  }

  @Override
  public int getSpendPort() {
    return 0;
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

  @Override
  public int getCurrencyGrpcPort() {
    return 0;
  }
}
