package com.example.controller;

import co.yiiu.annotation.*;
import co.yiiu.domain.Model;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by tomoya at 2019/4/9
 */
@Controller
public class UserController {

    @GetMapping("/user/{username}")
    @ResponseBody
    public Object profile(@PathVariable String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        return map;
    }

    @GetMapping("/user/list")
    public String list(Model model) {
        List<String> users = Arrays.asList("tomcat", "jetty", "undertow");
        model.addAttribute("users", users);
        return "user_list";
    }

    // 测试post请求, 接收form data参数
    // 实现上传
    @PostMapping("/user/save")
    @ResponseBody
    public Map save(HttpServerExchange exchange) throws IOException {
        Map<String, Object> map = new HashMap<>();
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
                map.put(d, fileName);
            } else {
                map.put(d, formValues.getFirst().getValue());
            }
        }
        return map;
    }

}
