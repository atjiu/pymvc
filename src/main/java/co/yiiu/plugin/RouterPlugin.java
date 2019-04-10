package co.yiiu.plugin;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.annotation.Plugin;
import co.yiiu.annotation.PostMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
@Plugin
public class RouterPlugin implements IPlugin {

  private Map<String, Map<String, Object>> getMappingMap = new HashMap<>();
  private Map<String, Map<String, Object>> postMappingMap = new HashMap<>();

  public Map<String, Map<String, Object>> getGetMappingMap() {
    return getMappingMap;
  }

  public Map<String, Map<String, Object>> getPostMappingMap() {
    return postMappingMap;
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
        // get method
        GetMapping getMappingMethod = method.getDeclaredAnnotation(GetMapping.class);
        if (getMappingMethod != null) {
          getMappingMap.put(getMappingMethod.value(), assemble(method, controller, method.getParameters()));
        }
        // post method
        PostMapping postMappingMethod = method.getDeclaredAnnotation(PostMapping.class);
        if (postMappingMethod != null) {
          postMappingMap.put(postMappingMethod.value(), assemble(method, controller, method.getParameters()));
        }
      }
    }
  }


  public Map<String, Object> assemble(Method method, Object clazz, Parameter[] parameters) {
    Map<String, Object> map = new HashMap<>();
    map.put("method", method);
    map.put("clazz", clazz);
    map.put("params", parameters);
    return map;
  }
}
