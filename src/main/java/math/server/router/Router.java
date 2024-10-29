package math.server.router;

import math.server.common.Constants;
import math.server.dto.request.BaseRequest;
import math.server.dto.response.BaseResponse;
import math.server.service.utils.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class to manage request and response to client <p>
 * Register classes and methods to handle requests from the client based on the end point attached. <p>
 * Implements the RouterMapping interface and uses the @EndPoint annotation for the classes and methods you want to route.
 * @author dcthoai
 */
public class Router implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private final Map<String, Method> routeMap = new HashMap<>();
    private static final Router instance = new Router();

    private Router() {}

    public static Router getInstance() {
        return instance;
    }

    private void initRouter() {
        try {
            // Look in the controller package and scan through all the classes and methods
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = Constants.CONTROLLER_PACKAGE.replace('.', '/');   // Convert to window file url
            File packageDirectory = new File(Objects.requireNonNull(classLoader.getResource(path)).toURI());
            log.info("Initialize router successfully");

            if (packageDirectory.exists()) {
                log.info("Scanning controller package for router successfully");

                for (File file : Objects.requireNonNull(packageDirectory.listFiles())) {
                    if (file.getName().endsWith(".class")) {
                        String className = Constants.CONTROLLER_PACKAGE + '.' + file.getName().replace(".class", "");
                        Class<?> clazz = Class.forName(className);

                        if (RouterMapping.class.isAssignableFrom(clazz)) {  // Check if class implements RouterMapping interface
                            registerRoutes(clazz);
                            log.info("Register method for {} successfully", className);
                        }
                    }
                }
            } else {
                log.warn("No method found to register or empty package");
            }
        } catch (Exception e) {
            log.error("Router initialization failed", e);
        }
    }

    private void registerRoutes(Class<?> controller) {
        Method[] methods = controller.getDeclaredMethods(); // Get all methods of the controller class

        for (Method method : methods) {
            // Check if this method is marked with @EndPoint annotation then register it as a router handler
            if (method.isAnnotationPresent(EndPoint.class)) {
                EndPoint endpoint = method.getAnnotation(EndPoint.class);
                String route = controller.getAnnotation(EndPoint.class).value() + endpoint.value();

                routeMap.put(route, method);
            }
        }
    }

    public Object handleRequest(UserSession session, BaseRequest request) {
        Method method = routeMap.get(request.getEndPoint());

        if (Objects.nonNull(method)) {
            try {
                if (Objects.isNull(request.getAction()))
                    request.setAction(Constants.NO_ACTION); // Set default action for the request if it could not be sent attachment action

                // If the request has a valid end point, use the router to call the corresponding registered method to handle it.
                Object controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                return method.invoke(controllerInstance, session, request);
            } catch (Exception e) {
                log.error("Failed to handle request: {}", request.getEndPoint(), e);
                return new BaseResponse(Constants.INTERNAL_SERVER_ERROR, false, request.getAction(), e.getMessage());
            }
        } else {
            log.error("Could not found method to handle this request");
            return new BaseResponse(Constants.NOT_FOUND, false, request.getAction(), "Could not found method to handle this request");
        }
    }

    @Override
    public void run() {
        initRouter();
    }
}
