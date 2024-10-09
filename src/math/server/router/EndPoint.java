package math.server.router;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EndPoint {

    String value();

//    Constants.HttpMethod method() default Constants.HttpMethod.GET;
}
