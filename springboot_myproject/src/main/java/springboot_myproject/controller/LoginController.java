package springboot_myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;
import springboot_myproject.pojo.Employee;
import springboot_myproject.service.EmployeeService;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class LoginController {
    @Autowired
    private EmployeeService employeeService;



    @PostMapping("/user/login")
    public String login(HttpSession session,String username, String password, Model model){
        System.out.println("接收到的参数:"+username+":"+password);
        //伪造登录
        if(!StringUtils.isEmpty(username) && password.equals("123456")){
            //登录成功,重定向到主页面
            session.setAttribute("username",username);
            return "redirect:/main.html";
        }else{
            //告诉用户登录失败，用户名密码错误
            model.addAttribute("msg","用户名或者密码错误");
            return "index";
        }
    }
    @RequestMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }

    @RequestMapping("/user/list")
    public String list(HttpSession session){

        Collection<Employee> all = employeeService.getAll();
        session.setAttribute("all",all);
        return "redirect:/list.html";
    }

}
