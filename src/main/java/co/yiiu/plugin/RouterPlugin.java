package co.yiiu.plugin;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.annotation.Plugin;
import co.yiiu.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
@Plugin
public class RouterPlugin implements IPlugin {

  private Map<String, Map<String, Object>> methodMap = new HashMap<>();

  public Map<String, Map<String, Object>> getMethodMap() {
    return methodMap;
  }

  private List<Object> getController() {
    List<Object> controllers = new ArrayList<>();
    Map<String, Object> beans = Beans.getBeans();
    beans.forEach((key, value) -> {
      Annotation declaredAnnotation = value.getClass().getDeclaredAnnotation(Controller.class);
      if (declaredAnnotation != null) {
        controllers.add(value);
      }
    });
    return controllers;
  }

  @Override
  public void init() throws Exception {
    List<Object> controllers = getController();
    for (Object controller : controllers) {
      Method[] methods = controller.getClass().getMethods();
      for (Method method : methods) {
        GetMapping declaredAnnotationMethod = method.getDeclaredAnnotation(GetMapping.class);
        if (declaredAnnotationMethod != null) {
          String url = declaredAnnotationMethod.value();
          Map<String, Object> map = new HashMap<>();
          map.put("method", method);
          map.put("clazz", controller);
          map.put("params", method.getParameters());
          methodMap.put(url, map);
        }
      }
    }
  }
}
