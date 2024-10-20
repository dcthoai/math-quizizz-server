package main.java.math.server.router;

import main.java.math.server.common.Constants;
import main.java.math.server.dto.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Router {

    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private final Map<String, Method> routeMap = new HashMap<>();
    private static final Router instance = new Router();

    private Router() {
        initRouter();
    }

    public static Router getInstance() {
        return instance;
    }

    private void initRouter() {
        log.info("Initialize router successfully");

        try {
            log.info("Scanning controller package");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = Constants.CONTROLLER_PACKAGE.replace('.', '/');
            File packageDirectory = new File(Objects.requireNonNull(classLoader.getResource(path)).toURI());

            if (packageDirectory.exists()) {
                for (File file : Objects.requireNonNull(packageDirectory.listFiles())) {
                    if (file.getName().endsWith(".class")) {
                        String className = Constants.CONTROLLER_PACKAGE + '.' + file.getName().replace(".class", "");
                        Class<?> clazz = Class.forName(className);

                        if (RouterMapping.class.isAssignableFrom(clazz)) {
                            registerRoutes(clazz);
                            log.info("Register method for {} successfully", className);
                        }
                    }
                }
            } else {
                log.warn("No method found to register");
            }
        } catch (Exception e) {
            log.error("Router initialization failed. " + e.getMessage());
        }
    }

    private void registerRoutes(Class<?> controller) {
        Method[] methods = controller.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(EndPoint.class)) {
                EndPoint endpoint = method.getAnnotation(EndPoint.class);
                String route = controller.getAnnotation(EndPoint.class).value() + endpoint.value();

                routeMap.put(route, method);
            }
        }
    }

    public Object handleRequest(String endpoint, String request) {
        Method method = routeMap.get(endpoint);

        if (Objects.nonNull(method)) {
            try {
                Object controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                return method.invoke(controllerInstance, request);
            } catch (Exception e) {
                log.error("Failed to handle request: " + e.getMessage());
                return new BaseResponse<>(500, false, e.getMessage());
            }
        } else {
            log.error("Could not found method to handle this request");
            return new BaseResponse<>(400, false, "Could not found method to handle this request");
        }
    }
}
