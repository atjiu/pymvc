package co.yiiu.server;

import co.yiiu.annotation.Plugin;
import co.yiiu.annotation.Plugins;
import co.yiiu.handler.DispatchHttpHandler;
import co.yiiu.plugin.Beans;
import co.yiiu.util.PropUtil;
import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tomoya at 2019/4/10
 */
public class Server {

  private Logger log = LoggerFactory.getLogger(Server.class);

  private void initPlugins() {
    List<Class<?>> excludePlugins = new ArrayList<>();
    List<Object> plugin = Beans.getAnnotationBeans(Plugin.class);
    for (Object o : Beans.getAnnotationBeans(Plugins.class)) {
      Class<?>[] excludes = o.getClass().getDeclaredAnnotation(Plugins.class).exclude();
      excludePlugins.addAll(Arrays.asList(excludes));
    }
    plugin.forEach(v -> {
      try {
        if (!excludePlugins.isEmpty() && excludePlugins.contains(v.getClass())) return;
        Method[] methods = v.getClass().getMethods();
        for (Method method : methods) {
          if (method.getName().equals("init")) {
            method.invoke(v);
          }
        }
      } catch (Exception e) {
        log.error("初始化插件: {} 失败", v.getClass().getName());
        e.printStackTrace();
      }
    });
  }

  public void run(Class clazz) {
    // 扫包
    log.info("开始扫框架下的包, 包名: co.yiiu");
    Beans.init("co.yiiu");
    log.info("开始扫项目下的包, 包名: {}", clazz.getPackage().getName());
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
