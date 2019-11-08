package springboot_myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot_myproject.dao.EmployeeDao;
import springboot_myproject.pojo.Employee;

import java.util.Collection;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public Collection<Employee> getAll(){
        return employeeDao.getAll();
    }
}
