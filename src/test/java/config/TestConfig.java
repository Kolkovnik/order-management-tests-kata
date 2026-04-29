package config;

public class TestConfig {
    private static final AppConfig config = new AppConfig("application-test.properties");

    public static final String BASE_URL = config.getString("BASE_URL");
    public static final String ADMIN_LOGIN = config.getString("TEST_ADMIN_LOGIN");
    public static final String ADMIN_PASSWORD = config.getString("TEST_ADMIN_PASSWORD");
}
