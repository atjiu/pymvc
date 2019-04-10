package co.yiiu.handler;

import co.yiiu.annotation.ResponseBody;
import co.yiiu.plugin.Beans;
import co.yiiu.plugin.JsonViewResolvePlugin;
import co.yiiu.plugin.RouterPlugin;
import co.yiiu.plugin.ViewResolvePlugin;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
public class DispatchHttpHandler implements HttpHandler {

  private ViewResolvePlugin viewResolvePlugin = (ViewResolvePlugin) Beans.getBean(ViewResolvePlugin.class);
  private JsonViewResolvePlugin jsonViewResolvePlugin = (JsonViewResolvePlugin) Beans.getBean(JsonViewResolvePlugin.class);
  private RouterPlugin routerPlugin = (RouterPlugin) Beans.getBean(RouterPlugin.class);

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    // 获取请求路径
    String path = exchange.getRequestPath();
    Map<String, Object> value = routerPlugin.getMethodMap().get(path);
    if (value != null) {
      Map<String, Object> model = new HashMap<>();
      Object clazz = value.get("clazz");
      Object returnValue = ((Method) value.get("method")).invoke(clazz, exchange, model);
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
    } else {
      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
      exchange.getResponseHeaders().put(Headers.STATUS, 404);
      exchange.getResponseSender().send("404");
    }
  }
}
