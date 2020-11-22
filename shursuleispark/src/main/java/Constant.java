
public class Constant {
    private static final String PROPERTY_FILE = "resource.properties";

    private static final Configuration CONF = new Configuration(PROPERTY_FILE);

    public static final String MYSQL_DRIVER = CONF.getValue("mysql.driver");
    public static final String MYSQL_URL = CONF.getValue("mysql.url");
    public static final String MYSQL_USERNAME = CONF.getValue("mysql.username");
    public static final String MYSQL_PASSWORD = CONF.getValue("mysql.password");
}
