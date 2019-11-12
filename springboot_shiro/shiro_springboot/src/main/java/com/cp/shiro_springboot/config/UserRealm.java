package com.cp.shiro_springboot.config;

import com.cp.shiro_springboot.pojo.User;
import com.cp.shiro_springboot.service.UserService;
import com.cp.shiro_springboot.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的Realm
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthorizationInfo");

        //用户名，密码 数据库中取
//        String name = "root";
//        String password = "123456";


        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        User user = userService.queryUserByName(userToken.getUsername());
        if(user==null){
            return null;//抛出异常 UnknownAccountException
        }
//        if(!userToken.getUsername().equals(name)){
//            return null;//抛出异常 UnknownAccountException
//        }
        //密码认证，shiro做
        return new SimpleAuthenticationInfo("",user.getPwd(),"");
    }
}
