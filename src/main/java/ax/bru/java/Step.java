package ax.bru.java;

import java.lang.annotation.*;

/**
 * Created by alexbruckner on 24/01/2014
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Step {
    int order() default 0;
    String name();
}