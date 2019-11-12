package com.cp.swagger_demo.controller;


import com.cp.swagger_demo.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @ApiOperation("获取helloworld")
    @GetMapping("/hello")
    public String hello(@ApiParam("用户名") String username){
        return "hello,"+username;
    }

    //只要接口中存在实体类，就会加入到Swagger的Model中
    @PostMapping("/user")
    public User user(){
        return new User();
    }
}
