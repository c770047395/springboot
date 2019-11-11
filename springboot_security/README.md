# Spring Security
1. 静态页面准备，导入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

2. 编写Config类
```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    //链式编程
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人可以访问，功能页只有有权限的人才能访问
        //请求授权的规则
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        //没有权限默认会调到登陆页面
        http.formLogin();
    }

    //认证
    //密码编码
    //在Spring Security 5.0+ 新增了很多加密方式
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("cp").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3");
    }
}

```
Spring Security的配置类需要``@EnableWebSecurity``注解，并且需要继承``WebSecurityConfigurerAdapter``类，并重写其中的方法达到认证与授权

**授权：**

- 重写``configure(HttpSecurity http)``方法，其中``http.authorizeRequests().antMatchers("path").hasRole("role")``可以添加需要授权的页面`` http.formLogin()``方法则会在没有权限且未登录时候跳转到登陆页面。


**认证：**

- 重写``configure(AuthenticationManagerBuilder auth)``方法，其中``auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("cp").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")``添加一个用户，并且赋予权限，这种用户是添加到内存的