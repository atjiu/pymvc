package co.yiiu.plugin;

import co.yiiu.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
@Plugin
public class RouterPlugin implements IPlugin {

  private Logger log = LoggerFactory.getLogger(RouterPlugin.class);

  private Map<String, Map<String, Map<String, Object>>> mappingMap = new HashMap<>();

  private void initMappingMap() {
    if (mappingMap.isEmpty()) {
      List<Object> controllers = Beans.getAnnotationBeans(Controller.class);
      Map getMappingMap = new HashMap();
      Map postMappingMap = new HashMap();
      Map putMappingMap = new HashMap();
      Map deleteMappingMap = new HashMap();
      for (Object controller : controllers) {
        Method[] methods = controller.getClass().getMethods();
        for (Method method : methods) {
          // get method
          GetMapping getMappingMethod = method.getDeclaredAnnotation(GetMapping.class);
          if (getMappingMethod != null) {
            String url = getMappingMethod.value();
            log.info("[GET] {} {}", url, controller.getClass().getName());
            getMappingMap.put(url, assemble(method, controller, method.getParameters()));
          }
          // post method
          PostMapping postMappingMethod = method.getDeclaredAnnotation(PostMapping.class);
          if (postMappingMethod != null) {
            String url = postMappingMethod.value();
            log.info("[POST] {} {}", url, controller.getClass().getName());
            postMappingMap.put(url, assemble(method, controller, method.getParameters()));
          }
          // put method
          PutMapping putMappingMethod = method.getDeclaredAnnotation(PutMapping.class);
          if (putMappingMethod != null) {
            String url = putMappingMethod.value();
            log.info("[PUT] {} {}", url, controller.getClass().getName());
            putMappingMap.put(url, assemble(method, controller, method.getParameters()));
          }
          // delete method
          DeleteMapping deleteMappingMethod = method.getDeclaredAnnotation(DeleteMapping.class);
          if (deleteMappingMethod != null) {
            String url = deleteMappingMethod.value();
            log.info("[PUT] {} {}", url, controller.getClass().getName());
            deleteMappingMap.put(url, assemble(method, controller, method.getParameters()));
          }
        }
      }
      mappingMap.put(GetMapping.class.getName(), getMappingMap);
      mappingMap.put(PostMapping.class.getName(), postMappingMap);
      mappingMap.put(PutMapping.class.getName(), putMappingMap);
      mappingMap.put(DeleteMapping.class.getName(), deleteMappingMap);
    }
  }

  public Map<String, Map<String, Object>> getMappingMap(Class clazz) {
    return mappingMap.get(clazz.getName());
  }

  @Override
  public void init() throws Exception {
    initMappingMap();
  }

  public Map<String, Object> assemble(Method method, Object clazz, Parameter[] parameters) {
    Map<String, Object> map = new HashMap<>();
    map.put("method", method);
    map.put("clazz", clazz);
    map.put("params", parameters);
    return map;
  }

}
