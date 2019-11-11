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

**注销：**

将http中的logout开启，并指定注销成功后跳转的页面
```java
//开启注销功能
http.logout().logoutSuccessUrl("/");
```

**权限控制：**

Thymeleaf还不支持高等级的SpringBoot，所以我们使用Springboot2.0.9进行测试


如果要设置某些模块的权限，则需要将thymeleaf和Spring Security整合，首先需要引入整合依赖
```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity4</artifactId>
    <version>3.0.4.RELEASE</version>
</dependency>
```
然后在template中使用sec:标签控制：（添加命名空间``xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4"``）
```html
<!--如果未登-->
<div sec:authorize="!isAuthenticated()">
    <a href="/toLogin">登陆</a>
</div>
<!--已登陆-->
<div sec:authorize="isAuthenticated()">
    <p>用户名：<span sec:authentication="name"></span></p>
    <a href="/logout">注销</a>
</div>

<!--判断有无权限-->
<div sec:authorize="hasRole('vip1')">
    <h3>level1</h3>
    <p><a href="/level1/1">level1</a></p>
    <p><a href="/level1/2">level2</a></p>
    <p><a href="/level1/3">level3</a></p>
</div>
```
低版本的security中，登出有做防止csrf，需要在配置中手动关闭
```java
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
    //开启注销功能
    http.logout().logoutSuccessUrl("/");
    //关闭csrf拦截
    http.csrf().disable();
}
```