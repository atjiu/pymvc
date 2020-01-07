package com.example.controller;

import co.yiiu.annotation.Controller;
import co.yiiu.annotation.GetMapping;
import co.yiiu.domain.Model;

/**
 * Created by tomoya at 2019/4/9
 */
@Controller
public class HelloController {

    @GetMapping("/")
    public String index(String name, int age, Model model, boolean gender, double salary) {
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
