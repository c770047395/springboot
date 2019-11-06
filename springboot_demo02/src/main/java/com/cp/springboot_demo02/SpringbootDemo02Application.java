package com.cp.springboot_demo02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication:来标注这是一个主程序类，表明他是一个springboot应用，是创建项目自己生成的
@SpringBootApplication
public class SpringbootDemo02Application {

    public static void main(String[] args) {

        //将springboot应用执行起来
        SpringApplication.run(SpringbootDemo02Application.class, args);
    }

}
