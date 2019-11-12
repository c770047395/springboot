package com.cp.shiro_springboot.service;

import com.cp.shiro_springboot.pojo.User;

public interface UserService {
    public User queryUserByName(String name);
}
