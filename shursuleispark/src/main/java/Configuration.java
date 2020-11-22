import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private Properties propertie;
    private InputStream inputFile;
    private OutputStream outputFile;

    /**
     * 初始化Configuration类
     */
    public Configuration() {
        propertie = new Properties();
    }

    /**
     * 初始化Configuration类
     *
     * @param filePath 要读取的配置文件的路径+名称
     */
    public Configuration(String filePath) {
        propertie = new Properties();
        try {
            inputFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            propertie.load(inputFile);
            inputFile.close();
        } catch (FileNotFoundException ex) {
            log.info("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在", ex);
        } catch (IOException ex) {
            log.info("装载文件--->失败!", ex);
        }
    }

    public Configuration(InputStream inputFile) {
        this.inputFile = inputFile;
        propertie = new Properties();
        try {
            propertie.load(inputFile);
            inputFile.close();
        } catch (FileNotFoundException ex) {
            log.info("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在", ex);
        } catch (IOException ex) {
            log.info("装载文件--->失败!", ex);
        }
    }

    public static Properties getPropertiesFromFile(String filePath) {
        Properties properties = new Properties();
        if (filePath == null || filePath.trim().length() == 0) {
            return null;
        }
        try {
            InputStream propertyIS = new FileInputStream(filePath);
            properties.load(propertyIS);
            propertyIS.close();
        } catch (FileNotFoundException ex) {
            log.info("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在. filePath=" + filePath, ex);
        } catch (IOException ex) {
            log.info("装载文件--->失败!", ex);
        }
        return properties;
    }

    /**
     * 重载函数，得到key的值
     *
     * @param key 取得其值的键
     * @return key的值
     */
    public String getValue(String key) {
        if (propertie.containsKey(key)) {
            return propertie.getProperty(key);// 得到某一属性的值
        } else
            return "";
    }

    /**
     * 重载函数，得到key的值
     *
     * @param fileName properties文件的路径+文件名
     * @param key      取得其值的键
     * @return key的值
     */
    public String getValue(String fileName, String key) {
        try {
            String value = "";
            inputFile = new FileInputStream(fileName);
            propertie.load(inputFile);
            inputFile.close();
            if (propertie.containsKey(key)) {
                value = propertie.getProperty(key);
                return value;
            } else {
                return value;
            }
        } catch (FileNotFoundException e) {
            log.info("", e);
            return "";
        } catch (IOException e) {
            log.info("", e);
            return "";
        } catch (Exception ex) {
            log.info("", ex);
            return "";
        }
    }

    /**
     * 清除properties文件中所有的key和其值
     */
    public void clear() {
        propertie.clear();
    }

    /**
     * 改变或添加一个key的值，当key存在于properties文件中时该key的值被value所代替， 当key不存在时，该key的值是value
     *
     * @param key   要存入的键
     * @param value 要存入的值
     */
    public void setValue(String key, String value) {
        propertie.setProperty(key, value);
    }

    /**
     * 将更改后的文件数据存入指定的文件中，该文件可以事先不存在。
     *
     * @param fileName    文件路径+文件名称
     * @param description 对该文件的描述
     */
    public void saveFile(String fileName, String description) {
        try {
            outputFile = new FileOutputStream(fileName);
            propertie.store(outputFile, description);
            outputFile.close();
        } catch (FileNotFoundException e) {
            log.info("", e);
        } catch (IOException ioe) {
            log.info("", ioe);
        }
    }
}
