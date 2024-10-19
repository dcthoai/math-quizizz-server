package main.java.math.server.router;

import main.java.math.server.common.Constants;
import main.java.math.server.dto.response.BaseResponse;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Router {

    private final Map<String, Method> routeMap = new HashMap<>();

    public Router() {
        try {
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
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public Object handleRequest(String endpoint, String jsonRequest) {
        Method method = routeMap.get(endpoint);

        if (method != null) {
            try {
                Object controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();

                return method.invoke(controllerInstance, jsonRequest);
            } catch (Exception e) {
                e.printStackTrace();

                return new BaseResponse<>(500, false, e.getMessage());
            }
        }

        return new BaseResponse<>(400, false, "Not found method to handle this request");
    }
}
