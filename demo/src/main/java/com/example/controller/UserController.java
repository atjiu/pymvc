package com.example.controller;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.annotation.PostMapping;
import co.yiiu.annotation.ResponseBody;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
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

  // 测试post请求, 接收form data参数
  // 实现上传
  @PostMapping("/user/save")
  @ResponseBody
  public Map save(HttpServerExchange exchange, Map<String, Object> model) throws IOException {
    FormDataParser parser = FormParserFactory.builder().build().createParser(exchange);
    FormData data = parser.parseBlocking();
    for (String d : data) {
      Deque<FormData.FormValue> formValues = data.get(d);
      if (formValues.getFirst().isFileItem()) {
        FormData.FileItem fileItem = formValues.getFirst().getFileItem();
        String fileName = formValues.getFirst().getFileName();

        FileOutputStream fos = new FileOutputStream(new File("/Users/hh/git/github/pymvc/" + fileName));
        Files.copy(fileItem.getFile(), fos);
        fos.close();
        model.put(d, fileName);
      } else {
        model.put(d, formValues.getFirst().getValue());
      }
    }
    return model;
  }

}
