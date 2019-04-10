package co.yiiu.plugin;

import co.yiiu.annotation.Component;
import co.yiiu.util.ReflectUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
public class Beans {

  private static List<String> annotationClassNames = new ArrayList<>();
  private static Map<String, Object> beans = new HashMap<>();

  // 扫描包
  public static void init(String packageName) {
    try {
      ReflectUtil reflectUtil = new ReflectUtil(packageName);
      List<String> allClassFullNames = reflectUtil.getFullyQualifiedClassNameList();
      if (annotationClassNames.isEmpty()) {
        annotationClassNames = reflectUtil.getClassNameListByAnnotation(allClassFullNames, Component.class);
      }
      for (String annotation : annotationClassNames) {
        Class<?> aClass = Class.forName(annotation);
        List<String> componentClassNames = reflectUtil.getClassNameListByAnnotation(allClassFullNames, aClass);
        for (String componentClassName : componentClassNames) {
          Class<?> bClass = Class.forName(componentClassName);
          beans.put(bClass.getName(), bClass.newInstance());
        }
      }
    } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
  }

  public static Map<String, Object> getBeans() {
    return beans;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(Class<T> clazz) {
    return (T) beans.get(clazz.getName());
  }

}
