package springboot_myproject.dao;

import org.springframework.stereotype.Repository;
import springboot_myproject.pojo.Department;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Repository
public class DepartmentDao {
    //准备数据
    private static Map<Integer, Department> departments = null;
    //默认数据
    static{
        departments = new HashMap<>();
        departments.put(101,new Department(101,"教学部"));
        departments.put(102,new Department(102,"教研部"));
        departments.put(103,new Department(103,"市场部"));
        departments.put(104,new Department(104,"人力部"));
        departments.put(105,new Department(105,"后勤部"));
    }

    //获得所有部门
    public Collection<Department> getDepartments(){
        return departments.values();
    }

    //根据id获取部门
    public Department getDepartment(int id){
        return departments.get(id);
    }
}
