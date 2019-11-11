package com.cp.springboot_mybatis.mapper;

import com.cp.springboot_mybatis.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper//也可以在Application中使用MapperScan注解扫描
@Repository
public interface UserMapper {
    List<User> queryUserList();
    User queryUserById(int id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(int id);
}
