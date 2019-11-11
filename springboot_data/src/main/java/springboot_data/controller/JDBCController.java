package springboot_data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JDBCController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //查询数据库的所有信息
    @GetMapping("/userList")
    public List<Map<String,Object>> userList(){
        String sql = "select * from user";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }

    @GetMapping("addUser")
    public String addUser(){
        String sql = "insert into user(name,pwd) values('小敏','123456')";
        jdbcTemplate.update(sql);
        return "add-ok";
    }

    @GetMapping("updateUser/{id}")
    public String update(@PathVariable int id){
        String sql = "update user set name=?,pwd=? where id="+id;
        Object[] objects = new Object[2];
        objects[0]="小明2";
        objects[1]="222222";
        jdbcTemplate.update(sql,objects);
        return "update-ok";
    }

    @GetMapping("deleteUser/{id}")
    public String delete(@PathVariable int id) {
        String sql = "delete from user where id = "+id;
        jdbcTemplate.update(sql);
        return "delete-ok";
    }
}
