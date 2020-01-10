package co.yiiu.plugin;

import co.yiiu.domain.Model;
import freemarker.template.TemplateException;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;

/**
 * Created by tomoya at 2019/4/9
 * <p>
 * 视图解析插件，这个插件还要拆，要抽出一个接口定义视图的渲染方法
 */
public interface ViewResolvePlugin extends IPlugin {

    void render(HttpServerExchange exchange, String templatePath, Model model) throws IOException, TemplateException;
}
