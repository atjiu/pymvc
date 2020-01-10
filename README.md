PYMVC是一个基于`undertow`封装的支持插件的mvc框架，功能逐渐完善中...

## 目标

- [x] 注解开发，路由全注解开发
- [x] Freemarker模板支持
- [x] 插件支持
- [x] 返回json格式文本
- [x] 路由支持通配符, RESTFUL风格路由支持
- [x] 支持Form Data数据解析，上传文件等
- [x] 支持热加载，代码有变动，IDEA里build一下项目，会自动重启加载
- [ ] 拦截器
- [ ] ORM(原生jdbc实现 || 通过动态代理实现一个简易的hibernate)
- [x] 增加自动注入变量功能 @Autowired
- [ ] AOP

**想到了继续加...**

## 简单使用

插件

1. 创建类，继承 `IPlugin` 接口，并添加上 `Plugin` 注解，表示这是一个插件类
2. 将你的插件业务写在 `init()` 方法里
3. 注意，如果要想让插件生效，一定要将 `active` 赋值为 `true`

```java
@Plugin(active=true)
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

路由风格：

- 配合注解 `@PathVariable` 注解，可传路径参数  `/user/{username}`
- 支持 `application/json` 请求格式的参数，参数格式为 `json`
- 支持 `application/x-www-form-urlencoded` 请求格式的参数，请求参数直接写在路由方法中即可，跟springmvc用法完全一致
- 添加 `@ResponseBody` 注解，可返回 json 数据，不添加，返回模板文件的路径，默认路径在 `src/main/resources/templates` 下

```java
@Controller
public class HelloController {
    
  @Autowired
  private UserService userService;

  @GetMapping("/")
  public String index(Model model) {
    userService.sayHello();
    model.addAttribute("name", "world");
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

## 运行demo

项目里有两个模块

- core 框架的核心代码
- demo 依赖core模块开发的网站

直接运行demo模块下的 Application.java 类中的main方法即可启动，访问 http://localhost:8080

## 贡献

欢迎大家提交PR，有问题请在issue里提问

## 协议

Apache-2.0
