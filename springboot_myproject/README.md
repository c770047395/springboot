## 第一个springbootweb项目
1. 导入依赖（Thymeleaf,web依赖）
2. 导入静态资源(前端页面)
3. 数据库操作（dao,pojo）
4. 首页映射
5. 国际化
   1. 编辑国际化文件
   2. 看如何配置，编辑国际化配置文件的目录``spring.messages.basename=i18n.login``
   3. 在html页面使用#{...}来获取国际化资源
   4. 编写处理的localeResolver
   ```java
    //处理国际化问题，写一个自己的LocaleResolver处理器
    public class MyLocaleResolver implements LocaleResolver {
        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String language = request.getParameter("l");
            Locale locale = Locale.getDefault();
    
            //判断，如果请求的连接不为空
            if (!StringUtils.isEmpty(language)){
                //拿到请求的参数，分割请求参数['en','US']
                String[] split = language.split("_");
                locale = new Locale(split[0],split[1]);
            }
    
            return locale;
        }
    
        @Override
        public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
    
        }
    }
   ```
   5. 将他放在WebMvc拓展配置文件的bean中,命名为localeResolver
   ```java
        //扩展自己的国际化组件
        @Bean
        public LocaleResolver localeResolver(){
            return new MyLocaleResolver();
        }
   ```

6. 登录
   1. 编写前端页面
        action,method
   2. 处理请求
        controller
   3. 数据回显
        model
   4. 跳转后资源问题
        重定向
   5. 回显前端如何显示
        th:if
   6. 不登录无法进入主页设置\[ 拦截器 \]
        实现HandlerInterceptor接口
   7. 登录成功显示用户名
        th:text
   8. 注销功能
        controller,注销session
        
7. 员工展示：查询
   1. 编写前端a链接跳转的请求
   2. 编写对应的Controller
   3. 前端页面展示数据
   4. 优化页面，增加按钮
   5. 发现问题：侧边栏和顶部公用问题
      1. 提取公共页面
         1. 提取公共部分： th:fragment="xxx"
         2. 在需要的地方插入： th:insert="~{template::selector}"
      2. 优化公共页面：使用th:insert带参数将激活标签激活