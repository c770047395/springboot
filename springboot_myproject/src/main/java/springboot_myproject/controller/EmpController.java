package springboot_myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springboot_myproject.dao.DepartmentDao;
import springboot_myproject.dao.EmployeeDao;
import springboot_myproject.pojo.Department;
import springboot_myproject.pojo.Employee;

import java.util.Collection;

@Controller
public class EmpController {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private EmployeeDao employeeDao;
    @RequestMapping("toAddPage")
    public String toAddPage(Model m){
        Collection<Department> departments = departmentDao.getDepartments();
        m.addAttribute("departments",departments);
        return "emp/add";
    }

    @RequestMapping("addemp")
    public String addemp(Employee employee, Model m){
        System.out.println("测试信息--》》"+employee);
        //保存
        employeeDao.save(employee);

        return "redirect:/user/list";
    }

    @RequestMapping("toUpdatePage/{id}")
    public String toUpdatePage(@PathVariable Integer id, Model m){
        Collection<Department> departments = departmentDao.getDepartments();
        Employee employee = employeeDao.get(id);
        m.addAttribute("departments",departments);
        m.addAttribute("e",employee);
        return "emp/update";
    }

    @RequestMapping("delete/{id}")
    public String delete(@PathVariable Integer id, Model m){
        System.out.println("删除--》》"+id);
        //保存
        employeeDao.delete(id);

        return "redirect:/user/list";
    }




}
