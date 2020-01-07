package co.yiiu.plugin;

import co.yiiu.annotation.Plugin;
import co.yiiu.util.PropUtil;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;

/**
 * 一个简单的DataSource实现
 * 源码来自：https://blog.51cto.com/lavasoft/265073
 *
 * @author leizhimin 2010-1-14 0:03:17
 */
@Plugin
public class DataSourcePlugin implements DataSource, IPlugin {

    private static final String driverClassName = PropUtil.getString("jdbc.driver");
    private static final String url = PropUtil.getString("jdbc.url");
    private static final String user = PropUtil.getString("jdbc.user");
    private static final String password = PropUtil.getString("jdbc.password");
    //连接池
    private static LinkedList<Connection> pool = new LinkedList<>();
    private static DataSourcePlugin instance = new DataSourcePlugin();

    @Override
    public void init() throws Exception {
        Class.forName(driverClassName);
    }

    /**
     * 获取数据源单例
     *
     * @return 数据源单例
     */
    public DataSourcePlugin instance() {
        if (instance == null) instance = new DataSourcePlugin();
        return instance;
    }

    /**
     * 获取一个数据库连接
     *
     * @return 一个数据库连接
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        synchronized (pool) {
            if (pool.size() > 0) return pool.removeFirst();
            else return makeConnection();
        }
    }

    /**
     * 连接归池
     *
     * @param conn
     */
    public static void freeConnection(Connection conn) {
        pool.addLast(conn);
    }

    private Connection makeConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
