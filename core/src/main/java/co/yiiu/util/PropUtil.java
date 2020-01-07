package co.yiiu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tomoya at 2019/4/10
 */
public class PropUtil {

    private static Logger log = LoggerFactory.getLogger(PropUtil.class);

    private static Properties properties = new Properties();

    static {
        try {
            InputStream is = PropUtil.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(is);
        } catch (IOException e) {
            log.error("加载配置文件出错");
            e.printStackTrace();
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static Integer getInt(String key) {
        String string = getString(key);
        return string == null ? null : Integer.parseInt(string);
    }

    public static void main(String[] args) {
        System.out.println(PropUtil.getInt("port"));
    }
}
