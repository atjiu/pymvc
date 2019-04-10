package co.yiiu.plugin;

import co.yiiu.annotation.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
public class Register {

  private static Map<String, Object> plugins = new HashMap<>();

  public static Map<String, Object> getPlugins() {
    Map<String, Object> beans = Beans.getBeans();
    beans.forEach((key, value) -> {
      Plugin declaredAnnotation = value.getClass().getDeclaredAnnotation(Plugin.class);
      if (declaredAnnotation != null && declaredAnnotation.active()) {
        plugins.put(value.getClass().getName(), value);
      }
    });
    return plugins;
  }

  public static void add(Object plugin) {
    plugins.put(plugin.getClass().getName(), plugin);
  }

  public static Object get(Object obj) {
    return plugins.get(obj.getClass().getName());
  }
}
