package ax.bru.java;

import ax.bru.defs.Executable;

import java.lang.annotation.*;

/**
 * Created by alexbruckner on 24/01/2014
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String name();
    boolean main() default false;
    Class<?>[] steps();
    boolean parallel() default false;
}