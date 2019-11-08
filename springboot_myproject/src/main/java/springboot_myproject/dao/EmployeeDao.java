package springboot_myproject.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springboot_myproject.pojo.Department;
import springboot_myproject.pojo.Employee;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Repository
public class EmployeeDao {
    //准备数据
    private static Map<Integer, Employee> employees = null;

    @Autowired
    private DepartmentDao departmentDao;
    //默认数据
    static{
        employees = new HashMap<>();
        employees.put(1001,new Employee(1001,"小明","xiaoming@qq.com",1,new Department(101,"教学部"),new Date()));
        employees.put(1002,new Employee(1002,"小z","xiaomq@qq.com",0,new Department(102,"教学部"),new Date()));
        employees.put(1003,new Employee(1003,"小x","xiaomwng@qq.com",1,new Department(103,"教学部"),new Date()));
        employees.put(1004,new Employee(1004,"小w","xiaomsg@qq.com",1,new Department(104,"教学部"),new Date()));
        employees.put(1005,new Employee(1005,"小q","xiaoxg@qq.com",0,new Department(105,"教学部"),new Date()));
    }

    //保存员工==》自增
    private static Integer initID = 1006;
    public void save(Employee employee){
        if(employee.getId() == null){
            employee.setId(initID++);
        }
        employee.setDepartment(departmentDao.getDepartment(employee.getDepartment().getId()));
        employees.put(employee.getId(),employee);
    }


    //查询所有员工
    public Collection<Employee> getAll(){
        return employees.values();
    }

    //根据id查找员工
    public Employee get(Integer id){
        return employees.get(id);
    }

    //删除一个员工
    public void delete(Integer id){
        employees.remove(id);
    }

}
