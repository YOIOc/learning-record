# 第一章 Spring Boot入门程序

需求：使用Spring Boot开发一个Web应用，浏览器发起请求后，程序给浏览器返回字符串”Hello World~“

## 1. 创建Maven工程

![image-20240605202344235](https://github.com/YOIOc/learning-record/blob/main/image/创建Maven工程.png)

## 2. 添加Spring Boot框架支持

![image-20240605193552108](https://github.com/YOIOc/learning-record/blob/main/image/添加框架支持.png)

## 3. 编写Controller

```java
package com.itheima.springboot001.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String toHello(){
        return "Hello World~";
    }
}
```

## 4. 启动启动类主函数

**由Spring Boot框架自动生成**

![image-20240606114255012](https://github.com/YOIOc/learning-record/blob/main/image/启动主函数.png)

# 第二章 Spring Boot配置文件

## 1. 配置文件的位置与格式

**位置**：位于resources根目录下，名为application.properties / application.yml

**格式**：两种类型都可以被Spring Boot框架识别，建议使用yml格式

- .properties：使用 `.` 分隔

  ```properties
  # 数据库连接信息
  jdbc.driver=com.mysql.cj.jdbc.Dricer
  jdbc.url=jdbc:mysql://loaclhost:8080/powernode
  jdbc.username=root
  jdbc.password=CZA20030203
  
  # 学生爱好(列表类信息)
  hobbies=唱,跳,rap,篮球
  ```

- .yml：使用 `空格 + 缩进` 分隔（值前必须由空格）

  ```yaml
  # 数据库连接信息
  jdbc:
    driver: com.mysql.cj.jdbc.Dricer
    url: jdbc:mysql://loaclhost:8080/powernode
    username: root
    password: CZA20030203

  # 学生爱好(列表类信息)
  hobbies:
    - 唱
    - 跳
    - rap
    - 篮球
  ```

## 2. 配置信息的书写与获取

**书写**：在application配置文件中，按照语法格式书写配置信息（因为配置文件会被框架自动识别，所以在Java程序获取属性值的时候，不需要提前指定配置文件位置）

**获取**：

- **成员变量、方法参数**上使用 `@Value("${键名}")` 注解

  ```java
  public class DataSource{
      @Value("${jdbc.driver}")
      private String driver;
      
      @Value("${jdbc.url}")
      private String url;
      
      @Value("${jdbc.username}")
      private String username;
      
      @Value("${jdbc.password}")
      private String password;
  }
  ```

- **类**上使用 `@ConfigurationProperties(prefix="前缀")` 注解：**成员变量名与键名相同，由框架自动赋值**

  ```java
  @ConfigurationProperties(prefix="jdbc")
  public class DataSource{
      private String driver;
      
      private String url;
      
      private String username;
      
      private String password;
  }
  ```

# 第三章 Spring Boot整合MyBatis

## 1. 引入相关依赖

```xml
<!--mysql驱动依赖-->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
<!--mybatis的起步依赖-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

## 2. 编写配置文件

将 `application.properties` 修改为 `application.yml` 文件，修改配置信息格式，添加数据库连接的配置信息

注意：**Spring Boot框架会自动读取，并使用数据库的连接配置信息**

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mybatis
    username: root
    password: CZ20030203
```

## 3. 编写业务代码

- M层：POJO + Mapper + Service
- C层：Controller

## 4. 运行启动类主函数

![image-20240606114255012](https://github.com/YOIOc/learning-record/blob/main/image/启动主函数.png)

# 第四章 Bean对象的管理

## 1. Spring Boot是如何扫描Bean的

Spring Boot会自动扫描**启动类**所在包，及其子包下所有被 `@Component、@Bean...` 等注解标注的类，并将其注册并纳入IoC容器管理

## 2. 如何手动完成Bean注册

**适用场景**：当需要将第三方jar包中的类纳入IoC容器管理时，由于第三方代码文件通常都为**只读**，因此无法在类上添加 `@Component` 注解将其注册到IoC容器管理

**解决措施**：手动注册（假设User、Order为第三方Jar包中的一个类）

- 使用 `@Bean` 注解 - 【配置类位于启动类所在包，及其子包下】

  **通常会创建一个配置类，用于注册所有项目所需的第三方Bean对象**

  ```java
  @Configuration
  public class CommonConfig{
      // 使用@Bean注解注册Order对象
      @Bean
      public Order order(){
          return new Order();
      }
      
      // 使用@Bean注解注册User对象
      @Bean
      public User user(Order order){
          return new User(order);
      }
  }
  ```

  注：

  1. Bean对象的名字默认为：方法名
  2. 如果方法内部需要使用IoC容器管理中的Bean对象，只需要在参数列表中声明即可，Spring Boot框架会自动注入

- 使用 `@Import(Xxx.class)` 注解 - 【当配置类不在启动类所在包，及其子包下时，使用该注解导入】

  ```java
  @Import(CommonImportSelector.class)
  public class SpringbootRegisterApplication {
      public static void main(String[] args){
          SpringApplication.run(SpringbootRegisterApplication.class, args);
      }
  }
  ```

  注：

  1. 通常用于导入 配置类、导入 ImportSelector 接口的实现类

## 3. 注册条件

Spring Boot提供了设置注册生效条件的注解 `@Conditional` ：只有被注解的对象，满足注册条件时才会被注册

- `@ConditionalProperty(prefix="前缀", name={"属性名",...})`：配置文件中存在对应属性时，才会注册该Bean
- `@ConditionalOnMissingBean(Xxx.class)`：当不存在指定类型的Bean时，才会注册该Bean
- `@ConditionalOnClass(Xxx.class)`：只有当前环境中存在指定类型的Bean时，才会注册Bean

# 第五章 自动配置

原理：

1. 首先：Spring Boot框架会把需要自动装配的Bean对象类名都写在一个 `.import` 文件中
2. 然后：Spring Boot框架会通过一个实现了 `ImportSelector` 接口的配置类，将 `.import` 文件中的所有类名存储在一个`String[]`中
3. 最后：将 `@Import(配置类)` 注解封装进 `@SprintBootApplication` 注解中，当启动类运行→`@SprintBootApplication` 注解运行，Spring Boot自动装配 `.import` 文件中配置的所有Bean对象

![image-20240606174422570](https://github.com/YOIOc/learning-record/blob/main/image/自动配置.png)

# 第六章 自定义starter

需求：自定义mybatis的starter

步骤：

- 创建dmybatis-spring-boot-autoconfigure模块，用于自动配置

  - 提供自动配置功能 - 将需要自动装配的对象在此类中初始化，并注册

  ```java
  @AutoConfiguration // 表示当前类是一个自动装配类
  public class MyBatisAutoConfig {
      // SqlSessionFactoryBean
      @Bean
      public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
          SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
          sqlSessionFactoryBean.setDataSource(dataSource);
          return sqlSessionFactoryBean;
      }
  
      // MapperScannerConfigure
      @Bean
      public MapperScannerConfigurer mapperScannerConfigurer(BeanFactory beanFactory){
          MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
          // 扫描的包：启动类所在的包，及其子包
          List<String> packages = AutoConfigurationPackages.get(beanFactory);
          String p = packages.get(0);
          mapperScannerConfigurer.setBasePackage(p);
          // 扫描的注解
          mapperScannerConfigurer.setAnnotationClass(Mapper.class);
          return mapperScannerConfigurer;
      }
  }
  ```

  - 自定义配置文件 MATE-INF/spring/xxx.import - **格式固定**

  ```import
  # 写入自动配置类的全限定类名
  com.itheima.config.MyBatisAutoConfig
  ```

![image-20240607102910757](https://github.com/YOIOc/learning-record/blob/main/image/自动配置文件.png)

- 创建dmybatis-spring-boot-starter模块，用于依赖管理

  - 在starter中引入自动配置模块 - 官方建议一并引入 `autoconfigure` 模块中引入的依赖

  ```xml
  <dependency>
    <groupId>com.itheima</groupId>
    <artifactId>dmybatis-spring-boot-autoconfigure</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>
  ```

# 第七章 '大事件'项目实战

## 1. 环境搭建

- 准备数据库表

  ```sql
  -- 创建数据库
  create database big_event;
  
  -- 使用数据库
  use big_event;
  
  -- 用户表
  create table user (
                        id int unsigned primary key auto_increment comment 'ID',
                        username varchar(20) not null unique comment '用户名',
                        password varchar(32)  comment '密码',
                        nickname varchar(10)  default '' comment '昵称',
                        email varchar(128) default '' comment '邮箱',
                        user_pic varchar(128) default '' comment '头像',
                        create_time datetime not null comment '创建时间',
                        update_time datetime not null comment '修改时间'
  ) comment '用户表';
  
  -- 分类表
  create table category(
                           id int unsigned primary key auto_increment comment 'ID',
                           category_name varchar(32) not null comment '分类名称',
                           category_alias varchar(32) not null comment '分类别名',
                           create_user int unsigned not null comment '创建人ID',
                           create_time datetime not null comment '创建时间',
                           update_time datetime not null comment '修改时间',
                           constraint fk_category_user foreign key (create_user) references user(id) -- 外键约束
  );
  
  -- 文章表
  create table article(
                          id int unsigned primary key auto_increment comment 'ID',
                          title varchar(30) not null comment '文章标题',
                          content varchar(10000) not null comment '文章内容',
                          cover_img varchar(128) not null  comment '文章封面',
                          state varchar(3) default '草稿' comment '文章状态: 只能是[已发布] 或者 [草稿]',
                          category_id int unsigned comment '文章分类ID',
                          create_user int unsigned not null comment '创建人ID',
                          create_time datetime not null comment '创建时间',
                          update_time datetime not null comment '修改时间',
                          constraint fk_article_category foreign key (category_id) references category(id),-- 外键约束
                          constraint fk_article_user foreign key (create_user) references user(id) -- 外键约束
  )
  ```

- 使用Maven快速构建项目

  ![image-20240607113425907](https://github.com/YOIOc/learning-record/blob/main/image/快速构建项目.png)

- 在pom文件中引入有关Spring Boot-web工程的依赖

  ```xml
  <!--父工程依赖-->
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.0</version>
  </parent>
  
  <dependencies>
    <!--Web依赖-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
  
    <!--MyBatis依赖-->
    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>3.0.3</version>
    </dependency>
  
    <!--MySQL驱动依赖-->
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
    </dependency>
  </dependencies>
  ```

- 在src/main目录下创建resources目录，并添加application.yml文件，引入mybatis的配置信息

  ```yaml
  spring:
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/big_event
      username: root
      password: CZA20030203
  ```

- 创建包结构，并准备实体类

![image-20240607122347971](https://github.com/YOIOc/learning-record/blob/main/image/准备实体类.png)

- 编写Spring Boot框架的启动类

![image-20240607122203825](https://github.com/YOIOc/learning-record/blob/main/image/启动.png)

- 完善实体类

  使用 `lombok` 的子注解，为实体类自动创建set、get等方法（编译阶段生成，不再需要手动编写）

  ```xml
  <!--lombok依赖-->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
  </dependency>
  ```

  ![image-20240608164139892](https://github.com/YOIOc/learning-record/blob/main/image/完善实体类.png)

  Result实体类，用于封装部分固定格式的响应数据

  ![image-20240608165213057](https://github.com/YOIOc/learning-record/blob/main/image/封装响应数据.png)


## 2. 用户

### 2.1 注册接口

#### 2.1.1 流程步骤![image-20240608164358359](https://github.com/YOIOc/learning-record/blob/main/image/注册接口.png)

#### 2.1.2 代码编写

Controller

![image-20240611111533969](https://github.com/YOIOc/learning-record/blob/main/image/Controller层.png)

Service

![image-20240611111633379](https://github.com/YOIOc/learning-record/blob/main/image/Service层.png)

Mapper

![image-20240611111804042](https://github.com/YOIOc/learning-record/blob/main/image/Mapper层.png)

#### 2.1.3 接口测试 - PostMan

创建工作空间

![image-20240611133542170](https://github.com/YOIOc/learning-record/blob/main/image/Postman.png)

![image-20240611133859945](https://github.com/YOIOc/learning-record/blob/main/image/Postman创建工作空间1.png)

导入测试用例

![image-20240611134439753](https://github.com/YOIOc/learning-record/blob/main/image/导入测试用例.png)

开始测试

![image-20240611134759506](https://github.com/YOIOc/learning-record/blob/main/image/开始测试.png)

![image-20240611140513172](https://github.com/YOIOc/learning-record/blob/main/image/测试接口.png)

#### 2.1.4 参数校验

使用Spring Validation - 参数校验框架对注册接口的参数进行合法性校验

1.引入Spring Validation 起步依赖

```xml
<!--Validation依赖(完成参数校验)-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

2.在参数前面添加`@Pattern`注解，并在当前Controller类上添加`@Validated`注解

![image-20240611141851333](https://github.com/YOIOc/learning-record/blob/main/image/参数校验.png)

使用异常处理器，完成参数校验失败时的异常处理

![image-20240611143353470](https://github.com/YOIOc/learning-record/blob/main/image/参数异常处理.png)

### 2.2 登录接口

#### 2.2.1 代码编写

Controller

![image-20240611145106702](https://github.com/YOIOc/learning-record/blob/main/image/登录Controller.png)

#### 2.2.2 登录认证 - 令牌

令牌：是一段由服务器发送给浏览器的字符串（用户登录后），用户后续的请求访问都会携带该令牌，当服务器接受到请求后，会检查令牌的合法性，若令牌合法就允许此次请求，反之则拒绝此次请求

- 承载业务数据，减少后续请求查询数据库的次数
- 防篡改，保证信息的合法性和有效性

JWT（JSON Web Token）：定义了一种简洁的、自由含的格式，用于通信双方以json数据格式安全的传输信息（一种令牌的实现规则）

![image-20240611151529966](https://github.com/YOIOc/learning-record/blob/main/image/令牌.png)

![image-20240611151548300](https://github.com/YOIOc/learning-record/blob/main/image/令牌组成.png)

注意事项：

- JWT校验时使用的签名密钥，必须和生成JWT令牌是使用的密钥是配套的
- 如果JWT令牌解析校验时报错，则说明JWT令牌被篡改 或 失效了，令牌非法

**引入JWT依赖**

```xml
<!--JWT依赖(生成Token令牌)-->
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.4.0</version>
</dependency>
```

**编写JWTUtil工具类**

![image-20240611160811674](https://github.com/YOIOc/learning-record/blob/main/image/JWT工具.png)

**生成Token**

![image-20240611161056337](https://github.com/YOIOc/learning-record/blob/main/image/生成Token.png)

**模拟登陆验证**

创建拦截器

![image-20240611164849498](https://github.com/YOIOc/learning-record/blob/main/image/拦截器·.png)

注册拦截器

![image-20240611165709445](https://github.com/YOIOc/learning-record/blob/main/image/注册拦截器.png)

验证

![image-20240611163431476](https://github.com/YOIOc/learning-record/blob/main/image/验证.png)

#### 2.2.3 接口测试

![image-20240611145519094](https://github.com/YOIOc/learning-record/blob/main/image/登录接口测试.png)

#### 2.2.4 登陆优化 - redis

引入redis依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>3.3.2</version>
</dependency>
```

配置redis信息

![image-20240805232016710](https://github.com/YOIOc/learning-record/blob/main/image/登陆优化1.png)

登陆后将token存储到redis缓存中（redisTemplate.opsForValue()用于获取缓存的操作对象）

![image-20240805232524756](https://github.com/YOIOc/learning-record/blob/main/image/登陆优化2.png)

修改密码后需删除缓存中的token

![image-20240810051721594](https://github.com/YOIOc/learning-record/blob/main/image/登陆优化3.png)

登录拦截器检查前端发送的token与缓存中的token是否一致

![image-20240810051159281](https://github.com/YOIOc/learning-record/blob/main/image/登陆优化4.png)

### 2.3 获取用户详细信息

#### 2.3.1 代码编写

Controller

![image-20240611174618610](https://github.com/YOIOc/learning-record/blob/main/image/用户Controller.png)

#### 2.3.2 ThreadLocal优化

存在问题：控制器方法需要解析封装在Token中的用户名，再根据解析到的用户名查询该用户信息这就使得

- 需要在方法的参数列表中，声明`String token`
- 如果还有其他方法同样需要获取用户名，就也要在参数列表中声明

能否将用户名存储在一个共享的`域`中，在需要使用的时候直接获取该数据

**编写ThreadLocalUtil工具类**

![image-20240611193007693](https://github.com/YOIOc/learning-record/blob/main/image/ThreadLocal优化.png)

**选择在登录拦截器中，将线程共享信息存储到ThreadLocal中**

![image-20240611193539509](https://github.com/YOIOc/learning-record/blob/main/image/ThreadLocal优化2.png)

**在需要获取ThreadLocal中数据的地方，调用get方法**

![image-20240611194953104](https://github.com/YOIOc/learning-record/blob/main/image/ThreadLocal优化3.png)

**在线程共享数据使用完毕后，清空ThreadLocal中的数据**

![image-20240611195948907](https://github.com/YOIOc/learning-record/blob/main/image/ThreadLocal优化4.png)

#### 2.3.3 接口测试

![image-20240611191655998](https://github.com/YOIOc/learning-record/blob/main/image/ThreadLocal接口测试.png)

#### 2.3.4 相关小点

- 前端展示用户信息时不向用户展示用户密码：在User实体类的password属性上使用`@JsonIgnore`注解，忽略password属性的展示

![image-20240611191312706](https://github.com/YOIOc/learning-record/blob/main/image/用户相关.png)

- PostMan - 为当前集合下的所有请求，设置请求提交时携带Authorization数据

![image-20240611174454927](https://github.com/YOIOc/learning-record/blob/main/image/用户相关2.png)

- 解决由于**数据库字段**与**Java属性**命名规则不同，导致数据库中数据无法赋值给对应的Java属性，以至于前端限制数据为**null**

![image-20240611192526920](https://github.com/YOIOc/learning-record/blob/main/image/用户相关3.png)

### 2.4 更新用户基本信息

#### 2.4.1 代码编写

Controller

![image-20240611202453606](https://github.com/YOIOc/learning-record/blob/main/image/用户更新Controller.png)

Service

![image-20240611205648497](https://github.com/YOIOc/learning-record/blob/main/image/用户更新Service.png)

Mapper

![image-20240611205714047](https://github.com/YOIOc/learning-record/blob/main/image/用户更新Mapper.png)

#### 2.4.2 实体参数校验

- 实体类的成员变量上添加注解

![image-20240612093240457](https://github.com/YOIOc/learning-record/blob/main/image/实体参数校验.png)

- 接口方法的实体参数上添加@Validate注解

![image-20240612093359929](https://github.com/YOIOc/learning-record/blob/main/image/Validate注解.png)

#### 2.4.3 接口测试

![image-20240611205844801](https://github.com/YOIOc/learning-record/blob/main/image/用户更新测试.png)

#### 2.4.4 相关小点

- 前端发送Json格式数据，后端如何封装进对应实体类中：使用`@RequestBode`注解

![image-20240611205608804](https://github.com/YOIOc/learning-record/blob/main/image/RequestBode注解.png)

### 2.5 更新用户头像

#### 2.5.1 代码编写

Controller

![image-20240612100000462](https://github.com/YOIOc/learning-record/blob/main/image/更新用户头像Controller.png)

Service

![image-20240612100031254](https://github.com/YOIOc/learning-record/blob/main/image/更新用户头像Service.png)

Mapper

![image-20240612100054068](https://github.com/YOIOc/learning-record/blob/main/image/更新用户头像Mapper.png)

#### 2.5.2 参数校验

使用`@URL`注解，限制前端的avatarUrl参数格式，必须为合法的URL格式

![image-20240612100339090](https://github.com/YOIOc/learning-record/blob/main/image/更新用户头像参数校验.png)

#### 2.5.3 接口测试

![image-20240612100153696](https://github.com/YOIOc/learning-record/blob/main/image/更新用户头像接口测试.png)

### 2.6 更新用户密码

#### 2.6.1 代码编写

Controller

![image-20240613144434486](https://github.com/YOIOc/learning-record/blob/main/image/更新用户密码Controller.png)

Service

![image-20240613143526590](https://github.com/YOIOc/learning-record/blob/main/image/更新用户密码Servicec.png)

Mapper

![image-20240613143551079](https://github.com/YOIOc/learning-record/blob/main/image/更新用户密码Mapper.png)

#### 2.6.2 参数校验

![image-20240613144533722](https://github.com/YOIOc/learning-record/blob/main/image/更新用户密码参数校验.png)

#### 2.6.3 接口测试

![image-20240613144643764](https://github.com/YOIOc/learning-record/blob/main/image/更新用户密码接口测试.png)

#### 2.6.4 相关小点

- 前端发送Json格式数据后端没有对应实体类对象，使用Map集合接收

![image-20240613144852765](https://github.com/YOIOc/learning-record/blob/main/image/更新用户密码相关点1.png)

## 3. 文章分类

采用Restful风格

### 3.1 新增文章分类

#### 3.1.1 代码编写

Controller

![image-20240613155219627](https://github.com/YOIOc/learning-record/blob/main/image/新增文章Controller.png)

Service

![image-20240613155349688](https://github.com/YOIOc/learning-record/blob/main/image/新增文章Service.png)

Mapper

![image-20240613155416429](https://github.com/YOIOc/learning-record/blob/main/image/新增文章Mapper.png)

#### 3.1.2 参数校验

![image-20240613155526935](https://github.com/YOIOc/learning-record/blob/main/image/新增文章参数校验.png)

![image-20240613155612042](https://github.com/YOIOc/learning-record/blob/main/image/新增文章参数校验2.png)

#### 3.1.3 接口测试

![image-20240613155646774](https://github.com/YOIOc/learning-record/blob/main/image/新增文章接口测试.png)

### 3.2 文章分类列表

#### 3.2.1 代码编写

Controller

![image-20240613163534142](https://github.com/YOIOc/learning-record/blob/main/image/文章分类Controller.png)

Service

![image-20240613163559630](https://github.com/YOIOc/learning-record/blob/main/image/文章分类Service.png)

Mapper

![image-20240613163626371](https://github.com/YOIOc/learning-record/blob/main/image/文章分类Mapper.png)

#### 3.2.1 接口测试

![image-20240613163733457](https://github.com/YOIOc/learning-record/blob/main/image/文章分类接口测试.png)

#### 3.2.3 相关小点

- 将后端实体类中的日期数据，以指定的格式输出给前端

![image-20240613164624561](https://github.com/YOIOc/learning-record/blob/main/image/文章分类相关点1.png)

### 3.3 获取文章分类详情

#### 3.3.1 代码编写

Controller

![image-20240613171126323](https://github.com/YOIOc/learning-record/blob/main/image/文章分类详情相关点.png)

Service

![image-20240613171142145](https://github.com/YOIOc/learning-record/blob/main/image/文章分类详情Serviec.png)

Mapper

![image-20240613171202974](https://github.com/YOIOc/learning-record/blob/main/image/文章分类详情Mapper.png)

#### 3.3.2 接口测试

![image-20240613171233136](https://github.com/YOIOc/learning-record/blob/main/image/文章分类详情接口测试.png)

### 3.4 更新文章分类

#### 3.4.1 代码编写

Controller

![image-20240613172524993](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类Controller.png)

Service

![image-20240613172546024](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类Service.png)

Mapper

![image-20240613172622798](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类Mapper.png)

#### 3.4.2 参数校验

![image-20240613173715240](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类参数校验.png)

#### 3.4.2 接口测试

![image-20240613172655148](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类接口测试.png)

#### 3.4.3 相关小点

- 存在问题：当将Category实体类中的`id`属性用`@NotNull`注解标注后，新增接口由于前端没有传送id参数，就会导致新增接口报错

  解决措施：分组校验 - 将校验规则分组，在完成不同的功能的时候，校验指定组中的校验项

  操作步骤：

  1. 定义分组：**在实体内部定义接口**

  2. 定义该校验项校验时归属的分组：**通过group属性指定**

     ![image-20240613175351391](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类相关点1.png)

  3. 校验时指定要使用的分组：给`@Validated`注解的value属性赋值

     ![image-20240613175613913](https://github.com/YOIOc/learning-record/blob/main/image/更新文章分类相关点2.png)

### 3.5 删除文章分类

## 4. 文章管理

### 4.1 新增文章

#### 4.1.1 代码编写

Controller

![image-20240614121259082](https://github.com/YOIOc/learning-record/blob/main/image/文章管理Controller.png)

Service

![image-20240614121319412](https://github.com/YOIOc/learning-record/blob/main/image/文章管理Service.png)

Mapper

![image-20240614121337013](https://github.com/YOIOc/learning-record/blob/main/image/文章管理Mapper.png)

#### 4.1.2 接口测试

![image-20240614121109950](https://github.com/YOIOc/learning-record/blob/main/image/文章管理接口测试.png)

#### 4.1.3 自定义参数校验

- 自定义注解`Xxx`，并使用`@Documented、@Target、@Retention、@Constraint`注解标注

![image-20240614142612677](https://github.com/YOIOc/learning-record/blob/main/image/文章管理自定义参数校验.png)

- 自定义校验数据的类`XxxValidation`实现ConstranintValidation接口

![image-20240614142646405](https://github.com/YOIOc/learning-record/blob/main/image/文章管理自定义参数校验2.png)

- 在需要校验的地方使用自定义注解

![image-20240614142742970](https://github.com/YOIOc/learning-record/blob/main/image/文章管理自定义参数校验3.png)

### 4.2 文章列表

#### 4.2.1 代码编写

准备封装分页数据的实体类

![image-20240614185003199](https://github.com/YOIOc/learning-record/blob/main/image/文章列表封装数据的实体类.png)

Controller

![image-20240703154417153](https://github.com/YOIOc/learning-record/blob/main/image/文章列表Controller.png)

添加PageHelper - 分页依赖

- PageHelper是一个MyBatis的分页插件，工作原理是修改SQL语句以实现物理分页

![image-20240615181147486](https://github.com/YOIOc/learning-record/blob/main/image/添加分页依赖.png)

Service

- `PageHelper.startPage(pageNum, pageSize);`这行代码会设置一个ThreadLocal变量，存储分页参数，当MyBatis准备执行SQL语句时，PageHelper的Inteceptor会拦截SQL， 然后在SQL语句后面添加limit子句来实现分页

![image-20240615181057272](https://github.com/YOIOc/learning-record/blob/main/image/文章列表Service.png)

Mapper

![image-20240615181237388](https://github.com/YOIOc/learning-record/blob/main/image/文章列表Mapper.png)

![image-20240615181546136](https://github.com/YOIOc/learning-record/blob/main/image/文章列表Mapper2.png)

#### 4.2.2 接口测试

![image-20240615182434104](https://github.com/YOIOc/learning-record/blob/main/image/文章列表接口测试.png)

### 4.3 获取文章详情

### 4.4 更新文章

### 4.5 删除文章

## 5. 文件上传

### 5.1 本地

#### 5.1.1 代码编写

Controller

![image-20240711120832921](https://github.com/YOIOc/learning-record/blob/main/image/文件上传Controller.png)

#### 5.1.2 接口测试

![image-20240711121121173](https://github.com/YOIOc/learning-record/blob/main/image/文件上传接口测试.png)

### 5.2 云端

**阿里云**

![image-20240805075127283](https://github.com/YOIOc/learning-record/blob/main/image/阿里云.png)

#### 进入阿里云，打开控制台

![image-20240805070720483](https://github.com/YOIOc/learning-record/blob/main/image/打开阿里云控制台.png)

#### 打开 **对象存储OSS** 服务

![image-20240805071143088](https://github.com/YOIOc/learning-record/blob/main/image/打开对象存储OSS服务.png)

#### 创建项目**bucket**

Bucket：理解为逻辑容器，用于存储对象（文件、图片、视频等）包含如下特点 

​	唯一性：命名空间唯一

​	权限管理：控制谁可以访问和操作其中的对象

​	地域选择：选择用于存储对象的服务器地理位置

​	生命周期管理：自动管理对象的存储和删除

![image-20240805072616569](https://github.com/YOIOc/learning-record/blob/main/image/创建项目bucket.png)

![image-20240805074654450](https://github.com/YOIOc/learning-record/blob/main/image/创建项目bucket2.png)

#### 创建 AccessKey

AccessKey：用于访问阿里云服务的API密钥（拥有账户的完全权限）

![image-20240805075238943](https://github.com/YOIOc/learning-record/blob/main/image/创建AccessKey.png)

![image-20240805075658398](https://github.com/YOIOc/learning-record/blob/main/image/创建AccessKey2.png)

#### 参照SDK编写程序

SDK：软件开发工具包，包括开发所需的依赖，代码示例等

![image-20240805083326865](https://github.com/YOIOc/learning-record/blob/main/image/参照SDK编写程序.png)

导入所需依赖

```xml
<!--阿里云OSS所需依赖-->
<dependency>
  <groupId>com.aliyun.oss</groupId>
  <artifactId>aliyun-sdk-oss</artifactId>
  <version>3.17.4</version>
</dependency>
<dependency>
  <groupId>javax.xml.bind</groupId>
  <artifactId>jaxb-api</artifactId>
  <version>2.3.1</version>
</dependency>
<dependency>
  <groupId>javax.activation</groupId>
  <artifactId>activation</artifactId>
  <version>1.1.1</version>
</dependency>
<!-- no more than 2.3.3-->
<dependency>
  <groupId>org.glassfish.jaxb</groupId>
  <artifactId>jaxb-runtime</artifactId>
  <version>2.3.3</version>
</dependency>
```

编写阿里云OSS工具类

​	项目bucket的配置信息，可在阿里云对象存储OSS服务的Bucket列表中点击对应的Bucket中找到

![image-20240805085727568](https://github.com/YOIOc/learning-record/blob/main/image/编写阿里云OSS工具类.png)

调用阿里云OSS工具类的上传文件方法

![image-20240805091034741](https://github.com/YOIOc/learning-record/blob/main/image/调用阿里云OSS工具类.png)

#### 接口测试

![image-20240805091259582](https://github.com/YOIOc/learning-record/blob/main/image/文件上传到阿里云接口测试.png)

# 第八章 Spring Boot项目部署

## 8.1 添加打包插件

不写版本，则默认与SpringBoot父工程版本一致

```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

## 8.2 项目打包

![image-20240810053404653](https://github.com/YOIOc/learning-record/blob/main/image/项目打包.png)

## 8.3 启动项目jar包

使用命令 **java -jar jar包名**（服务器需要有jre环境）

![image-20240810053719348](https://github.com/YOIOc/learning-record/blob/main/image/在Linux上启动程序.png)

# 第九章 Spring Boot属性配置

**存在问题**：运维成员拿到项目jar包后，由于运维成员无法直接修改项目代码，当项目需要在某特定端口运行时，无法直接修改

**解决措施**：

- 方式一：命令行参数方式【高级】

  -- server.port=端口号

![image-20240813114316636](https://github.com/YOIOc/learning-record/blob/main/image/项目在指定端口运行.png)

- 环境变量方式【中级】

![image-20240813114727459](https://github.com/YOIOc/learning-record/blob/main/image/环境变量方式.png)

- 外部配置文件【低级】

![image-20240813115727521](https://github.com/YOIOc/learning-record/blob/main/image/外部配置文件方式.png)

![image-20240813115622088](https://github.com/YOIOc/learning-record/blob/main/image/编写配置.png)

**配置优先级**：由低到高

- 项目resources目录下的application.yml
- jar包所在目录下的application.yml
- 操作系统环境变量
- 命令行参数

# 第十章 Spring Boot多环境开发

**单文件配置**

1. '---' 分隔不同环境的配置
2. spring.confilg.activate.on-profile 配置所属环境
3. spring.profiles.active 激活环境

![image-20240813124318363](https://github.com/YOIOc/learning-record/blob/main/image/多环境开发1.png)

**多文件配置**

1. 通过多个文件分别配置不同环境的属性
2. 文件的名字为 application-环境名称.yml
3. 在application.yml中激活环境

![image-20240813130902735](https://github.com/YOIOc/learning-record/blob/main/image/多环境开发2.png)

**多环境开发-分组**

1. 按照配置的类别，把配置信息配置到不同的配置文件中 

   application-分类名.yml

2. 在application.yml中定义分组

   spring.profiles.group

3. 在application.yml中激活分组

   spring.profiles.active

![image-20240813130336917](https://github.com/YOIOc/learning-record/blob/main/image/多环境开发3.png)
