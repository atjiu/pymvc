package co.yiiu.handler;

import co.yiiu.annotation.*;
import co.yiiu.plugin.Beans;
import co.yiiu.plugin.JsonViewResolvePlugin;
import co.yiiu.plugin.RouterPlugin;
import co.yiiu.plugin.ViewResolvePlugin;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
public class DispatchHttpHandler {

  // 获取相关bean
  private ViewResolvePlugin viewResolvePlugin = Beans.getBean(ViewResolvePlugin.class);
  private JsonViewResolvePlugin jsonViewResolvePlugin = Beans.getBean(JsonViewResolvePlugin.class);
  private RouterPlugin routerPlugin = Beans.getBean(RouterPlugin.class);
  private RoutingHandler routes = new RoutingHandler();

  public RoutingHandler getRoutes() {
    return routes;
  }

  public DispatchHttpHandler() {
    Map<String, Map<String, Object>> getMappingMap = routerPlugin.getMappingMap(GetMapping.class);
    Map<String, Map<String, Object>> postMappingMap = routerPlugin.getMappingMap(PostMapping.class);
    Map<String, Map<String, Object>> putMappingMap = routerPlugin.getMappingMap(PutMapping.class);
    Map<String, Map<String, Object>> deleteMappingMap = routerPlugin.getMappingMap(DeleteMapping.class);
    handleRequest(getMappingMap);
    handleRequest(postMappingMap);
    handleRequest(putMappingMap);
    handleRequest(deleteMappingMap);
  }

  private void handleRequest(Map<String, Map<String, Object>> postMappingMap) {
    postMappingMap.forEach((key, value) -> routes.get(key, exchange -> {
      // 转发部分
      Map<String, Object> model = new HashMap<>();
      Object clazz = value.get("clazz");
      Object returnValue = ((Method) value.get("method")).invoke(clazz, exchange, model);

      // 响应部分
      // 判断是否有 @ResponseBody 注解
      ResponseBody responseBodyAnnotation = ((Method) value.get("method")).getAnnotation(ResponseBody.class);
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
    }));
  }

  public void handleRequest(HttpServerExchange exchange) throws Exception {
    // 获取请求路径
//    String path = exchange.getRequestPath();
//    String requestMethod = exchange.getRequestMethod().toString();
//    Map<String, Object> value = null;
//    if (requestMethod.equalsIgnoreCase("GET")) {
//      value = routerPlugin.getMappingMap(GetMapping.class).get(path);
//    } else if (requestMethod.equalsIgnoreCase("POST")) {
//      value = routerPlugin.getMappingMap(PostMapping.class).get(path);
//    }
//    if (value != null) {
//      // 转发部分
//      Map<String, Object> model = new HashMap<>();
//      Object clazz = value.get("clazz");
//      Object returnValue = ((Method) value.get("method")).invoke(clazz, exchange, model);
//
//      // 响应部分
//      // 判断是否有 @ResponseBody 注解
//      ResponseBody responseBodyAnnotation = ((Method) value.get("method")).getAnnotation(ResponseBody.class);
//      if (responseBodyAnnotation == null) {
//        viewResolvePlugin.render(exchange, (String) returnValue, model);
//      } else {
//        if (returnValue instanceof String) {
//          // 如果是String类型，则直接渲染text/plain
//          exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
//          exchange.getResponseSender().send((String) returnValue);
//        } else {
//          jsonViewResolvePlugin.render(exchange, returnValue);
//        }
//      }
//    } else {
//      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
//      exchange.getResponseHeaders().put(Headers.STATUS, 404);
//      exchange.getResponseSender().send("404");
//    }
    // 转发部分
//    Map<String, Object> model = new HashMap<>();
//    Object clazz = this.target.get("clazz");
//    Object returnValue = ((Method) this.target.get("method")).invoke(clazz, exchange, model);
//
//    // 响应部分
//    // 判断是否有 @ResponseBody 注解
//    ResponseBody responseBodyAnnotation = ((Method) this.target.get("method")).getAnnotation(ResponseBody.class);
//    if (responseBodyAnnotation == null) {
//      viewResolvePlugin.render(exchange, (String) returnValue, model);
//    } else {
//      if (returnValue instanceof String) {
//        // 如果是String类型，则直接渲染text/plain
//        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
//        exchange.getResponseSender().send((String) returnValue);
//      } else {
//        jsonViewResolvePlugin.render(exchange, returnValue);
//      }
//    }
  }

}
