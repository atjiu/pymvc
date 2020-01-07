package co.yiiu.annotation;

import java.lang.annotation.*;

/**
 * Created by tomoya at 2019/4/8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface GetMapping {

    String value() default "";

}
