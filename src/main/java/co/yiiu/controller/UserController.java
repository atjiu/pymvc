package co.yiiu.controller;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import io.undertow.server.HttpServerExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/9
 */
@Controller
public class UserController {

  @GetMapping("/user/list")
  public String list(HttpServerExchange exchange, Map<String, Object> model) {
    List<String> users = Arrays.asList("tomcat", "jetty", "undertow");
    model.put("users", users);
    return "user_list";
  }

}
