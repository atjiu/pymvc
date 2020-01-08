package co.yiiu.plugin;

import co.yiiu.annotation.Component;
import co.yiiu.annotation.Controller;
import co.yiiu.annotation.Plugin;
import co.yiiu.annotation.Plugins;
import co.yiiu.util.ReflectUtils;

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
    // 存放被注解修饰的类
    private static Map<String, List<Object>> annotationBeans = new HashMap<>();

    // 扫描包
    public static void init(String packageName) {
        try {
            ReflectUtils reflectUtils = new ReflectUtils(packageName);
            List<String> allClassFullNames = reflectUtils.getFullyQualifiedClassNameList();
            if (annotationClassNames.isEmpty()) {
                annotationClassNames = reflectUtils.getClassNameListByAnnotation(allClassFullNames, Component.class);
            }
            for (String annotation : annotationClassNames) {
                Class<?> aClass = Class.forName(annotation);
                List<String> componentClassNames = reflectUtils.getClassNameListByAnnotation(allClassFullNames, aClass);
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

    public static List<Object> getAnnotationBeans(Class clazz) {
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
            annotationBeans.put(Plugin.class.getName(), plugin);
            annotationBeans.put(Plugins.class.getName(), plugins);
            annotationBeans.put(Controller.class.getName(), controller);
        }
        return annotationBeans.get(clazz.getName());
    }

}
