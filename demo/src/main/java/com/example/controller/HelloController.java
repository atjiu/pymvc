package com.example.controller;

import co.yiiu.annotation.Autowired;
import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.domain.Model;
import com.example.service.UserService;

/**
 * Created by tomoya at 2019/4/9
 */
@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(String name, int age, Model model, boolean gender, double salary) {
        userService.sayHello();
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("gender", gender);
        model.addAttribute("salary", salary);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
