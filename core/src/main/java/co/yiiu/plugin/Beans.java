package co.yiiu.plugin;

import co.yiiu.annotation.*;
import co.yiiu.util.ReflectUtils;
import co.yiiu.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
public class Beans {

    private static List<String> allClassFullNames = new ArrayList<>();
    private static List<String> annotationClassNames = new ArrayList<>();
    private static Map<String, Object> beans = new HashMap<>();
    // 存放被注解修饰的类
    private static Map<String, List<Object>> annotationBeans = new HashMap<>();

    // 初始化容器
    public static void init(String packageName) {
        try {
            ReflectUtils reflectUtils = new ReflectUtils(packageName);
            List<String> fullyQualifiedClassNameList = reflectUtils.getFullyQualifiedClassNameList();
            allClassFullNames.addAll(fullyQualifiedClassNameList);
            if (annotationClassNames.isEmpty()) {
                annotationClassNames = reflectUtils.getClassNameListByAnnotation(fullyQualifiedClassNameList, Component.class);
            }
            for (String annotation : annotationClassNames) {
                Class<?> aClass = Class.forName(annotation);
                List<String> componentClassNames = reflectUtils.getClassNameListByAnnotation(fullyQualifiedClassNameList, aClass);
                for (String componentClassName : componentClassNames) {
                    Class<?> bClass = Class.forName(componentClassName);
                    String classSimpleName = bClass.getSimpleName();
                    if (beans.containsKey(classSimpleName)) throw new RuntimeException("实例化Bean: " + classSimpleName + " 时发现有名字重复!");
                    beans.put(classSimpleName, bClass.newInstance());
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    // 给类中属性注入
    public static void inject() {
        try {
            // 注入变量
            for (String allClassFullName : allClassFullNames) {
                Class<?> aClass = Class.forName(allClassFullName);
                Object target = Beans.getBean(aClass.getSimpleName());
                if (target != null) {
                    Field[] fields = target.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        Autowired annotation = field.getAnnotation(Autowired.class);
                        if (annotation != null) {
                            String className = annotation.value().equals("") ? StringUtils.captureName(field.getName()) : StringUtils.captureName(annotation.value());
                            Object bean = Beans.getBean(className);
                            field.setAccessible(true);
                            if (bean != null) field.set(target, bean);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getBeans() {
        return beans;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz.getSimpleName());
    }

    public static Object getBean(String name) {
        return beans.get(name);
    }

    public static List<Object> getAnnotationBeans(Class<?> clazz) {
        if (annotationBeans.isEmpty()) {
            List<Object> plugins = new ArrayList<>();
            List<Object> plugin = new ArrayList<>();
            List<Object> controller = new ArrayList<>();
            Beans.getBeans().forEach((key, value) -> {
                if (value.getClass().getDeclaredAnnotation(Plugins.class) != null) {
                    plugins.add(value);
                }
                if (value.getClass().getDeclaredAnnotation(Plugin.class) != null) {
                    plugin.add(value);
                }
                if (value.getClass().getDeclaredAnnotation(Controller.class) != null) {
                    controller.add(value);
                }
            });
            annotationBeans.put(Plugin.class.getSimpleName(), plugin);
            annotationBeans.put(Plugins.class.getSimpleName(), plugins);
            annotationBeans.put(Controller.class.getSimpleName(), controller);
        }
        return annotationBeans.get(clazz.getSimpleName());
    }

}
