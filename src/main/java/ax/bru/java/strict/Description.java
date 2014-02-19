package ax.bru.java.strict;

import java.lang.annotation.*;

/**
 * Created by alexbruckner on 18/02/2014
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value();
}
