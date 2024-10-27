package math.server.router;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used with the RouterMapping interface to mark the classes and control methods that will be scanned and routed by the Router.
 * @author dcthoai
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EndPoint {

    String value() default "";
}
