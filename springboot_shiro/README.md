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

