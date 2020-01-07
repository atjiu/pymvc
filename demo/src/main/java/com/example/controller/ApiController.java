package com.example.controller;

import co.yiiu.annotation.*;
import co.yiiu.domain.Model;
import com.example.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomoya at 2019/4/10
 */
@Controller
public class ApiController {

    // 返回string demo
    @GetMapping("/api/index")
    @ResponseBody
    public String index(Model model) {
        return "hello world";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public Object list() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hello world");
        map.put("age", 20);
        map.put("email", "py2qiuse@gmail.com");
        return map;
    }

    @PostMapping("/api/user/add")
    @ResponseBody
    public Object add(@RequestBody User user) {
        System.out.println(user.toString());
        return user;
    }
}
