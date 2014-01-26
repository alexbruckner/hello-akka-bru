package ax.bru.java;

import java.lang.annotation.*;

/**
 * Created by alexbruckner on 24/01/2014
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Step {
    String name();
}