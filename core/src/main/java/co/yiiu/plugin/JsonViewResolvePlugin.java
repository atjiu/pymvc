package co.yiiu.plugin;

import co.yiiu.annotation.Plugin;
import co.yiiu.util.JsonUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * Created by tomoya at 2019/4/10
 */
@Plugin
public class JsonViewResolvePlugin implements IPlugin {

    @Override
    public void init() throws Exception {

    }

    public void render(HttpServerExchange exchange, Object returnValue) {
        String json = JsonUtils.objectToJson(returnValue);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(json);
    }
}
