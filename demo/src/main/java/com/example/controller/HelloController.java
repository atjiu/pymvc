package com.example.controller;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import io.undertow.server.HttpServerExchange;

import java.util.Deque;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/9
 */
@Controller
public class HelloController {

  @GetMapping("/")
  public String index(HttpServerExchange exchange, Map<String, Object> model) {
    Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
    Deque<String> name = queryParameters.get("name");
    model.put("name", name == null ? null : name.getFirst());
    return "index";
  }

  @GetMapping("/about")
  public String about(HttpServerExchange exchange, Map<String, Object> model) {
    return "about";
  }
}
