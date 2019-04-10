package co.yiiu.server;

import co.yiiu.handler.DispatchHttpHandler;
import co.yiiu.plugin.Beans;
import co.yiiu.plugin.Register;
import co.yiiu.util.PropUtil;
import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
public class Server {

  private Logger log = LoggerFactory.getLogger(Server.class);

  private void initPlugins() {
    Map<String, Object> plugins = Register.getPlugins();
    plugins.forEach((key, value) -> {
      try {
        Method[] methods = value.getClass().getMethods();
        for (Method method : methods) {
          if (method.getName().equals("init")) {
            method.invoke(value);
          }
        }
      } catch (Exception e) {
        log.error("初始化插件: {} 失败", key);
        e.printStackTrace();
      }
    });
  }

  public void start(Class clazz) {
    // 扫包
    log.info("开始扫包");
    Beans.init(clazz.getPackage().getName());

    // 初始化插件
    log.info("初始化插件");
    initPlugins();

    // 启动服务
    log.info("创建服务");
    Integer port = PropUtil.getInt("server.port");
    Undertow server = Undertow.builder()
        .addHttpListener(port == null ? 8080 : port, "localhost")
        .setHandler(new DispatchHttpHandler()).build();

    log.info("启动服务");
    server.start();

    log.info("服务启动成功，端口: {}", port);
  }
}
