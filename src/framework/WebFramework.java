package framework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class WebFramework {

    private Map<String, Method> routeMethods;

    public WebFramework(Class[] classesWithRouteAnnotation) {
        routeMethods = new HashMap();
        for(Class classWithRoute : classesWithRouteAnnotation) {
            for (Method method : classWithRoute.getDeclaredMethods()) {
                WebRoute webRouteAnnotation = method.getAnnotation(WebRoute.class);
                if(webRouteAnnotation != null) {
                    routeMethods.put(webRouteAnnotation.value(), method);
                }

            }
        }
    }

    public void startServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
            server.createContext("/", this::handle);
            server.start();
        } catch (IOException e) {
            System.out.println("Unable to start the server. " + e);
        }
    }

    private void handle(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();

        Method currentMethod = routeMethods.get(path);

        if (currentMethod == null) {
            System.out.println("Cannot find handler for route " + path);
            return;
        }

        String response;
        try {
            response = (String) currentMethod.invoke(httpExchange);
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            System.out.println("Method " + currentMethod.getName() + " has wrong signature! " + e);
            return;
        } catch (InvocationTargetException e) {
            System.out.println("Method " + currentMethod.getName() + " threw an exception " + e.getTargetException());
            return;
        }
        if (response == null) {
            System.out.println("WARNING: Method " + currentMethod.getName() + " did not return anything.");
            return;
        }
        try {
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            System.out.println("Error while trying to send reply " + e);
        }
    }

}

