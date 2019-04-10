PYMVC是一个基于`undertow`封装的支持插件的mvc框架，功能逐渐完善中...

## 目标

- [x] 注解开发，路由全注解开发
- [x] Freemarker模板支持
- [x] 插件支持
- [x] 返回json格式文本
- [ ] 路由支持通配符
- [ ] 拦截器
- [ ] 支持热加载

**想到了继续加...**

## 简单使用

插件

1. 创建类，继承 `IPlugin` 接口，并添加上 `Plugin` 注解，表示这是一个插件类
2. 将你的插件业务写在 `init()` 方法里

```java
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
```

路由

```java
@Controller
public class HelloController {

  @GetMapping("/")
  public String index(HttpServerExchange exchange, Map<String, Object> model) {
    model.put("name", "world");
    return "index";
  }
}
```

启动服务

```java
public class Application {

  public static void main(final String[] args) {
    new Server().start(Application.class);
  }
}
```

## 贡献

欢迎大家提交PR，有问题请在issue里提问

## 协议

Apache-2.0
