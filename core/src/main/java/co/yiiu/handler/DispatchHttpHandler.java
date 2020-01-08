package co.yiiu.handler;

import co.yiiu.annotation.*;
import co.yiiu.domain.Model;
import co.yiiu.plugin.Beans;
import co.yiiu.plugin.JsonViewResolvePlugin;
import co.yiiu.plugin.RouterPlugin;
import co.yiiu.plugin.ViewResolvePlugin;
import co.yiiu.util.IOUtils;
import co.yiiu.util.JsonUtils;
import freemarker.template.TemplateException;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.util.Headers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.Map;

import static io.undertow.Handlers.resource;

/**
 * Created by tomoya at 2019/4/10
 */
public class DispatchHttpHandler {

    // 获取相关bean
    private ViewResolvePlugin viewResolvePlugin = Beans.getBean(ViewResolvePlugin.class);
    private JsonViewResolvePlugin jsonViewResolvePlugin = Beans.getBean(JsonViewResolvePlugin.class);
    private RoutingHandler routes = new RoutingHandler();

    public RoutingHandler getRoutes() {
        return routes;
    }

    public DispatchHttpHandler() {
        RouterPlugin routerPlugin = Beans.getBean(RouterPlugin.class);
        Map<String, Map<String, Object>> getMappingMap = routerPlugin.getMappingMap(GetMapping.class);
        Map<String, Map<String, Object>> postMappingMap = routerPlugin.getMappingMap(PostMapping.class);
        Map<String, Map<String, Object>> putMappingMap = routerPlugin.getMappingMap(PutMapping.class);
        Map<String, Map<String, Object>> deleteMappingMap = routerPlugin.getMappingMap(DeleteMapping.class);
        getMappingMap.forEach((key, value) -> routes.get(key, exchange -> forward(exchange, value)));
        postMappingMap.forEach((key, value) -> routes.post(key, exchange -> forward(exchange, value)));
        putMappingMap.forEach((key, value) -> routes.put(key, exchange -> forward(exchange, value)));
        deleteMappingMap.forEach((key, value) -> routes.delete(key, exchange -> forward(exchange, value)));

        ClassPathResourceManager classPathManager = new ClassPathResourceManager(DispatchHttpHandler.class.getClassLoader(), "/static/");
        routes.get("/static", resource(classPathManager).setDirectoryListingEnabled(true));
        routes.setFallbackHandler(exchange -> {
            String requestPath = exchange.getRequestPath();
            exchange.setStatusCode(404);
            exchange.getResponseSender().send("<h1>404 Not Found</h1><p>" + requestPath + "</p>");
        });
    }

    private void forward(HttpServerExchange exchange, Map<String, Object> value) throws InvocationTargetException, IllegalAccessException, IOException, TemplateException {
        // 转发部分
        Model model = new Model();
        Object clazz = value.get("clazz");
        Method method = ((Method) value.get("method"));
        Parameter[] parameters = method.getParameters();
        Object[] paramsObj = new Object[parameters.length];
        Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
        Map<String, Deque<String>> pathParameters = exchange.getPathParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 获取加了 @RequestBody 注解的参数
            Parameter parameter = parameters[i];
            RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (requestBody != null) {
                // !!! 我这是将请求的输入流转成json字符串，然后再将json字符串通过fastjson转成对象，总感觉这样不是最优解，有大佬看到这时恳请优化 !!!
                String text = IOUtils.inputStreamToString(exchange.getInputStream());
                paramsObj[i] = JsonUtils.jsonToObject(text, parameter.getType());
            } else if (pathVariable != null) {
                // 为毛这种被 @PathVariable 修饰的字段不是从 exchange.getPathParameters() 里获取的数据??
                Deque<String> strings = queryParameters.get(parameter.getName());
                paramsObj[i] = strings == null ? getDefaultValue(parameter.getType()) : strings.getFirst();
            } else {
                if (parameter.getType() == HttpServerExchange.class) {
                    paramsObj[i] = exchange;
                } else if (parameter.getType() == Model.class) {
                    paramsObj[i] = model;
                } else {
                    Deque<String> strings = queryParameters.get(parameter.getName());
                    paramsObj[i] = strings == null ? getDefaultValue(parameter.getType()) : strings.getFirst();
                }
            }
        }
        Object returnValue = method.invoke(clazz, paramsObj);
        viewRender(exchange, returnValue, model, method);
    }

    private Object getDefaultValue(Class<?> clazz) {
        if ("int".equals(clazz.getName())) {
            return 0;
        } else if ("boolean".equals(clazz.getName())) {
            return false;
        } else if ("double".equals(clazz.getName())) {
            return 0.0;
        } else if ("float".equals(clazz.getName())) {
            return 0.0;
        }
        return null;
    }

    private void viewRender(HttpServerExchange exchange, Object returnValue, Model model, Method method) throws IOException, TemplateException {
        // 响应部分
        // 判断是否有 @ResponseBody 注解
        ResponseBody responseBodyAnnotation = method.getAnnotation(ResponseBody.class);
        if (responseBodyAnnotation == null) {
            viewResolvePlugin.render(exchange, (String) returnValue, model);
        } else {
            if (returnValue instanceof String) {
                // 如果是String类型，则直接渲染text/plain
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send((String) returnValue);
            } else {
                jsonViewResolvePlugin.render(exchange, returnValue);
            }
        }
    }

}
