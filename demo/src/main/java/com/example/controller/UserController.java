package com.example.controller;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.annotation.PostMapping;
import co.yiiu.annotation.ResponseBody;
import io.undertow.server.HttpServerExchange;

import java.util.*;

/**
 * Created by tomoya at 2019/4/9
 */
@Controller
public class UserController {

  @GetMapping("/user/{username}")
  @ResponseBody
  public Map profile(HttpServerExchange exchange, Map model) {
    Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
    Map map = new HashMap();
    map.put("username", queryParameters.get("username").getFirst());
    return map;
  }

  @GetMapping("/user/list")
  public String list(HttpServerExchange exchange, Map<String, Object> model) {
    List<String> users = Arrays.asList("tomcat", "jetty", "undertow");
    model.put("users", users);
    return "user_list";
  }

  // 测试post请求
  @PostMapping("/user/save")
  @ResponseBody
  public String save(HttpServerExchange exchange, Map<String, Object> model) {
    return "user save";
  }

}
