package ax.bru.java;

import java.lang.annotation.*;

/**
 * Created by alexbruckner on 14/01/2014.
 */

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    String value();
}

