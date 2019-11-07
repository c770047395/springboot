package com.cp.springboot_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello,springboot";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/data")
    public String data(Model model){
        model.addAttribute("msg","<h1>hello,Thymeleaf</h1>");
        model.addAttribute("users", Arrays.asList("cp","ww"));
        return "data";
    }

}
