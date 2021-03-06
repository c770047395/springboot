# Shiro

## 1.快速开始

1. 引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-core</artifactId>
        <version>1.4.1</version>
    </dependency>

    <!-- configure logging -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
        <version>1.7.26</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.26</version>
    </dependency>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
</dependencies>
```
2. 配置文件

   - log4j的配置
   ```properties
    log4j.rootLogger=INFO, stdout
    
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m %n
    
    # General Apache libraries
    log4j.logger.org.apache=WARN
    
    # Spring
    log4j.logger.org.springframework=WARN
    
    # Default Shiro logging
    log4j.logger.org.apache.shiro=INFO
    
    # Disable verbose logging
    log4j.logger.org.apache.shiro.util.ThreadContext=WARN
    log4j.logger.org.apache.shiro.cache.ehcache.EhCache=WARN
   ```
   
   - shiro.ini配置
   ```ini
   [users]
   # user 'root' with password 'secret' and the 'admin' role
   root = secret, admin
   # user 'guest' with the password 'guest' and the 'guest' role
   guest = guest, guest
   # user 'presidentskroob' with password '12345' ("That's the same combination on
   # my luggage!!!" ;)), and role 'president'
   presidentskroob = 12345, president
   # user 'darkhelmet' with password 'ludicrousspeed' and roles 'darklord' and 'schwartz'
   darkhelmet = ludicrousspeed, darklord, schwartz
   # user 'lonestarr' with password 'vespa' and roles 'goodguy' and 'schwartz'
   lonestarr = vespa, goodguy, schwartz
   
   # -----------------------------------------------------------------------------
   # Roles with assigned permissions
   #
   # Each line conforms to the format defined in the
   # org.apache.shiro.realm.text.TextConfigurationRealm#setRoleDefinitions JavaDoc
   # -----------------------------------------------------------------------------
   [roles]
   # 'admin' role has all permissions, indicated by the wildcard '*'
   admin = *
   # The 'schwartz' role can do anything (*) with any lightsaber:
   schwartz = lightsaber:*
   # The 'goodguy' role is allowed to 'drive' (action) the winnebago (type) with
   # license plate 'eagle5' (instance specific id)
   goodguy = winnebago:drive:eagle5
   ```
   
3. 测试类
   ```java
        
    import org.apache.shiro.SecurityUtils;
    import org.apache.shiro.authc.*;
    import org.apache.shiro.config.IniSecurityManagerFactory;
    import org.apache.shiro.mgt.SecurityManager;
    import org.apache.shiro.session.Session;
    import org.apache.shiro.subject.Subject;
    import org.apache.shiro.util.Factory;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    
    
    /**
     * Simple Quickstart application showing how to use Shiro's API.
     *
     * @since 0.9 RC2
     */
    public class Quickstart {
    
        private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);
    
    
        public static void main(String[] args) {
    
            // The easiest way to create a Shiro SecurityManager with configured
            // realms, users, roles and permissions is to use the simple INI config.
            // We'll do that by using a factory that can ingest a .ini file and
            // return a SecurityManager instance:
    
            // Use the shiro.ini file at the root of the classpath
            // (file: and url: prefixes load from files and urls respectively):
            Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
            SecurityManager securityManager = factory.getInstance();
    
            // for this simple example quickstart, make the SecurityManager
            // accessible as a JVM singleton.  Most applications wouldn't do this
            // and instead rely on their container configuration or web.xml for
            // webapps.  That is outside the scope of this simple quickstart, so
            // we'll just do the bare minimum so you can continue to get a feel
            // for things.
            SecurityUtils.setSecurityManager(securityManager);
    
            // Now that a simple Shiro environment is set up, let's see what you can do:
    
            // get the currently executing user:
            Subject currentUser = SecurityUtils.getSubject();
    
            // Do some stuff with a Session (no need for a web or EJB container!!!)
            Session session = currentUser.getSession();
            session.setAttribute("someKey", "aValue");
            String value = (String) session.getAttribute("someKey");
            if (value.equals("aValue")) {
                log.info("Retrieved the correct value! [" + value + "]");
            }
    
            // let's login the current user so we can check against roles and permissions:
            if (!currentUser.isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
                token.setRememberMe(true);
                try {
                    currentUser.login(token);
                } catch (UnknownAccountException uae) {
                    log.info("There is no user with username of " + token.getPrincipal());
                } catch (IncorrectCredentialsException ice) {
                    log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                } catch (LockedAccountException lae) {
                    log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                            "Please contact your administrator to unlock it.");
                }
                // ... catch more exceptions here (maybe custom ones specific to your application?
                catch (AuthenticationException ae) {
                    //unexpected condition?  error?
                }
            }
    
            //say who they are:
            //print their identifying principal (in this case, a username):
            log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
    
            //test a role:
            if (currentUser.hasRole("schwartz")) {
                log.info("May the Schwartz be with you!");
            } else {
                log.info("Hello, mere mortal.");
            }
    
            //test a typed permission (not instance-level)
            if (currentUser.isPermitted("lightsaber:wield")) {
                log.info("You may use a lightsaber ring.  Use it wisely.");
            } else {
                log.info("Sorry, lightsaber rings are for schwartz masters only.");
            }
    
            //a (very powerful) Instance Level permission:
            if (currentUser.isPermitted("winnebago:drive:eagle5")) {
                log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                        "Here are the keys - have fun!");
            } else {
                log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
            }
    
            //all done - log out!
            currentUser.logout();
    
            System.exit(0);
        }
    }
   ```
4. 测试类解析

固定语句，通过加载配置文件添加SecurityManager
```java
Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
SecurityManager securityManager = factory.getInstance();
SecurityUtils.setSecurityManager(securityManager);
```

获取当前用户对象
```java
// 获取当前的用户对象Subject
Subject currentUser = SecurityUtils.getSubject();
```

通过当前用户拿到session，并且操作session（与http的session不同，但是使用方法差不多）
```java
// 通过当前的用户对象拿到Session
Session session = currentUser.getSession();
session.setAttribute("someKey", "aValue");
String value = (String) session.getAttribute("someKey");
if (value.equals("aValue")) {
    log.info("Subject=>session[" + value + "]");
}
```

判断用户登陆状态
```java
currentUser.isAuthenticated()
```

用户登陆
```java
UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
token.setRememberMe(true);
currentUser.login(token);
```

获取用户属性
```java
currentUser.getPrincipal()
```

判断用户有无权限
```java
currentUser.hasRole("schwartz")
currentUser.isPermitted("lightsaber:wield")
```

注销
```java
currentUser.logout();
```

## shiro的核心三大对象
- Subject：用户
- SecurityManager：管理所有用户
- Realm：连接数据

## springboot整合shiro的配置
1. 引入依赖
```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.4.1</version>
</dependency>
```
2. 编写UserRealm与ShiroConfig配置类

UserRealm.java(需要继承AuthorizingRealm并重写其中的方法)
```java
//自定义的Realm
public class UserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthorizationInfo");

        return null;
    }
}
```
ShiroConfig.java
```java
@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        return bean;
    }
    //defaultWebSecurityManager
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);

        return securityManager;
    }

    //创建Realm对象，需要自定义
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

}
```

### 拦截的实现
1. 定义静态页面

2. 添加shiro内置过滤器
```java
//添加shiro内置过滤器
/*
    anon:无需认证就可以访问
    authc：必须认证才能访问
    user：必须拥有记住我功能才能用
    perms：拥有对某个资源的权限才能访问
    role：拥有某个角色权限才能用
 */
Map<String, String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/user/add","authc");
//        filterMap.put("/user/update","authc");
filterMap.put("/user/*","authc");
bean.setFilterChainDefinitionMap(filterMap);
//设置登陆的请求
bean.setLoginUrl("/toLogin");
```

### 认证的实现
认证的实现需要通过Subject对象，在Controller中获取账号密码，在Realm中进行验证。

1. 获取账号密码
```java
@RequestMapping("/login")
public String login(String username,String password,Model model){
    //获取当前的用户
    Subject subject = SecurityUtils.getSubject();

    //封装用户的登陆数据
    UsernamePasswordToken token = new UsernamePasswordToken(username, password);

    try {
        subject.login(token);//执行登陆的方法，如果没有异常就说明ok
        return "index";
    } catch (UnknownAccountException e) {//用户名不存在
        model.addAttribute("msg","用户名错误");
        return "login";
    } catch (IncorrectCredentialsException e) {//密码不存在
        model.addAttribute("msg", "密码错误");
        return "login";
    }
}
```
其中subject.login(token)会走Realm中的授权方法，并抛出多种异常，方便处理

2. UserRealm中处理登陆
```java
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    System.out.println("执行了=>认证doGetAuthorizationInfo");

    //用户名，密码 数据库中取
    String name = "root";
    String password = "123456";

    UsernamePasswordToken userToken = (UsernamePasswordToken) token;
    if(!userToken.getUsername().equals(name)){
        return null;//抛出异常 UnknownAccountException
    }
    //密码认证，shiro做
    return new SimpleAuthenticationInfo("",password,"");
}
```

**上面使用的是静态密码认证，现在我们改成从数据库获取：**

1. 引入mybatis、druid依赖
```xml
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.21</version>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.12</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>

        </dependency>
```
2. Mybatis的配置与实体类、Mapper等的新建

3. 在UserRealm中使用调用service的方法获取用户并认证
```java
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
```

### 授权的实现
使用perms过滤器可将指定页面设置成需要某些权限才能进入,并且需要设置unauthorizedUrl，指定在试图进入无授权页面时的页面
```java
//ShiroFilterFactoryBean
@Bean
public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
    ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
    //设置安全管理器
    bean.setSecurityManager(defaultWebSecurityManager);
    Map<String, String> filterMap = new LinkedHashMap<>();
    //授权，正常情况下，进入没有授权的页面会跳转到未授权页面
    filterMap.put("/user/add","perms[user:add]");
    filterMap.put("/user/update","perms[user:update]");
    bean.setFilterChainDefinitionMap(filterMap);
    //设置登陆的请求
    bean.setLoginUrl("/toLogin");
    //设置未授权页面
    bean.setUnauthorizedUrl("/noauth");
    return bean;
}
```
授权与认证相同，都是在Realm中处理，我们可以在认真的时候将用户加入principal
```java
//return new SimpleAuthenticationInfo("",user.getPwd(),"");
return new SimpleAuthenticationInfo(user,user.getPwd(),"");
```
```java
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    System.out.println("执行了=>授权doGetAuthorizationInfo");
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    //拿到当前登陆的这个对象
    Subject subject = SecurityUtils.getSubject();
    User currentUser =(User) subject.getPrincipal();
    //设置当前用户的权限
    info.addStringPermission(currentUser.getPerms());

    return info;
}
```
这样就完成了页面的授权


### thymeleaf与shiro的整合
1. 首先还是要引入整合依赖
```xml
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```

2. 在ShiroConfig里配置注入一个Bean
```java
//整合ShiroDialect：用来整合Shiro和Thymeleaf
@Bean
public ShiroDialect getShiroDialect(){
    return new ShiroDialect();
}
```

3. Thymeleaf页面中可以使用shiro:xxx来使用shiro的一些方法
```html
<div shiro:hasPermission="user:add">
<a th:href="@{/user/add}">添加</a><br/>
</div>
<div shiro:hasPermission="user:update">
<a th:href="@{/user/update}">修改</a>
```