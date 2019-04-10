package com.example.controller;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.annotation.ResponseBody;
import io.undertow.server.HttpServerExchange;

import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
@Controller
public class ApiController {

  // 返回string demo
  @GetMapping("/api/index")
  @ResponseBody
  public String index(HttpServerExchange exchange, Map model) {
    return "hello world";
  }

  @GetMapping("/api/list")
  @ResponseBody
  public Map list(HttpServerExchange exchange, Map map) {
    map.put("name", "hello world");
    map.put("age", 20);
    map.put("email", "py2qiuse@gmail.com");
    return map;
  }
}
