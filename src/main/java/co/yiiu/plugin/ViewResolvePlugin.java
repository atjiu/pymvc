package co.yiiu.plugin;

import co.yiiu.annotation.Plugin;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.undertow.server.HttpServerExchange;

import java.io.*;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/9
 *
 * 视图解析插件
 */
@Plugin
public class ViewResolvePlugin implements IPlugin {

  private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);

  public void render(HttpServerExchange exchange, String templatePath, Map<String, Object> model) throws IOException, TemplateException {
    Template template = configuration.getTemplate(templatePath + ".ftl");
    StringWriter sw = new StringWriter();
    template.process(model, sw);
    exchange.getResponseSender().send(sw.toString());
  }

  @Override
  public void init() throws IOException {
    FileTemplateLoader templateLoader = new FileTemplateLoader(new File(ViewResolvePlugin.class.getClassLoader().getResource("templates").getPath()));
    configuration.setTemplateLoader(templateLoader);
  }
}
