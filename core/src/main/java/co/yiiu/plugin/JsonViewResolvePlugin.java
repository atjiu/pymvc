package co.yiiu.plugin;

import co.yiiu.annotation.Plugin;
import com.google.gson.Gson;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * Created by tomoya at 2019/4/10
 */
@Plugin
public class JsonViewResolvePlugin implements IPlugin {

  private static Gson gson;

  public <T> T fromJson(String json, Class<T> clazz) {
    return gson.fromJson(json, clazz);
  }

  public String toJson(Object object) {
    return gson.toJson(object);
  }

  @Override
  public void init() throws Exception {
    gson = new Gson();
  }

  public void render(HttpServerExchange exchange, Object returnValue) {
    String json = this.toJson(returnValue);
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
    exchange.getResponseSender().send(json);
  }
}
