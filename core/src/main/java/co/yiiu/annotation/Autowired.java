package co.yiiu.annotation;

import java.lang.annotation.*;

/**
 * Created by tomoya at 2019/4/8
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Autowired {

    String value() default "";

}
