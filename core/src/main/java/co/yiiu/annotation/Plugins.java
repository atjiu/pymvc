package co.yiiu.annotation;

import java.lang.annotation.*;

/**
 * Created by tomoya at 2019/4/8
 * 如果不想让某个插件在启动时加载，可使用这个注解标明启动时排除指定插件不加载
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Plugins {

  Class<?>[] exclude() default {};

}
