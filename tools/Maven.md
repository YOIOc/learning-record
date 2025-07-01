## 1.Maven介绍

- #### 1.1 什么是Maven

  - Maven是一个基于POM（项目对象模型）的项目管理工具

- #### 1.2 Maven的作用

  - **依赖管理**：可以自动处理项目的依赖关系
  - **项目构建**：Maven使用一种标准的方式来构建项目
  - **项目信息管理**：Maven可以生成关于项目的各种信息（项目的依赖关系，使用的插件等）
  - **跨项目的共享**：Maven的仓库机制可以让你在不同的项目之间共享JAR文件，插件等
  - **集成与持续集成**：Maven可以与各种持续集成工具（如Jenkins）集成，实现项目的自动构建和测试

- #### 1.3 仓库的概念与配置

  - **概念**：(小)本地仓库 → 私服仓库 → 中央仓库(大)

    - 本地仓库：本地计算机上的一个目录（当运行Maven命令时，如果本地仓库中没有项目运行所需的依赖，Maven会从远程仓库（中央仓库或私服仓库）下载依赖到本地仓库，以便下次使用）
    - 私服仓库：自己设置的远程仓库（用来存储私有项目的依赖、或作为中央仓库的镜像，以提高下载依赖的速度）
    - 中央仓库：Maven社区的公共仓库（包含了全球绝大多数开源的jar包）https://mvnrepository.com

  - **配置**：

    - 本地仓库的默认位置：C:\Users\用户名\.m2\repository（"用户名\.m2"文件 会在DOS窗口运行 "mvn" 命令时出现）

    - 修改本地仓库的位置：

      - 打开MAVEN_HOME\conf\settings.xml文件，修改配置

      ```xml
      <!-- 在localRepository注释下添加以下标签 -->
      <localRepository>指定的本地仓库路径</localRepository>
      ```

    - 配置私服（镜像）仓库：

      - 打开MAVEN_HOME\conf\settings.xml文件，修改配置

      ```xml
      <!-- 在mirrors标签中添加以下标签 -->
      <mirror>
        <id>nexus-aliyun</id>        镜像仓库标识[镜像仓库名]
        <mirrorOf>central</mirrorOf> 所替换的仓库标识[替换仓库名]
        <name>Nexus aliyun</name>    镜像名称
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>  镜像URL
      </mirror>
      ```

- #### 1.4 坐标的概念与组成

  - **概念**

    - 坐标用于唯一标识一个项目或者一个依赖（每个Maven项目或者依赖都有一个坐标），坐标由以下四部分组成

  - **组成**

    - **groupId**：项目属于哪个组（通常是组织/公司的域名反转）【例如：com.mycompany】

    - **artifactId**：项目的名字【例如：MyBatis】

    - **version**：项目的版本【例如：1.0.0】

    - **packaging**：项目的打包类型【例如：jar、war】

## 2. Maven的下载与配置

- 下载地址：https://maven.apache.org/download.cgi
- 环境配置：
  - 添加环境变量MAVEN_HOME：D:\apache-maven-3.9.6
  - 在Path中添加：%MAVEN_HOME%\bin

## 3. Maven的基本项目结构

```
project(项目根)
	|-- pom.xml(项目对象模型文件)
	|-- src
    	|-- main  (项目源代码)
    	|   |-- java     (程序目录)
   		|   |-- resources(资源目录)
    	|-- test  (测试源代码)
        	|-- java     (程序目录)
        	|-- resources(资源目录)
```

## 4. Maven命令

- 编译：编译源代码，并存放到自动生成的`target`目录中

  ```text
  mvn compile
  ```

- 清理：删除自动生成的`target`目录

  ```text
  mvn clean
  ```

- 测试：运行项目的单元测试(包含`compile`)

  ```text
  mvn test
  ```

- 打包：把项目打包为 .jar/.war(包含`compile`、`test`命令)

  ```text
  mvn package
  ```

- 安装到本地仓库：把项目安装到本地的Maven仓库(包含`compile`、`test`命令)

  ```text
  mvn install
  ```


## 5. 使用插件创建Maven工程

- 创建java工程

  ```text
  mvn archetype:generate        // 指令名(用于生成工程)
  	-DgroupId=com.itheima     // 项目包名
  	-DartifactId=java-project // 项目名
  	-DarchetypeArtifactId=maven-archetype-quickstart // 模板来源
  	-Dversion=0.0.1-snapshot  // 项目版本
  	-DinteractiveMode=false   // 是否开启交互模式
  ```

- 创建web工程

  ```text
  mvn archetype:generate 
  	-DgroupId=com.itheima
  	-DartifactId=web-project
  	-DarchetypeArtifactId=maven-archetype-webapp
  	-Dversion=0.0.1-snapshot
  	-DinteractiveMode=false
  ```

## 6. 使用idea创建Maven工程

- 获取Maven工具
  - 下载与当前idea适配的Maven版本，查找发布时间在IDEA版本之前的版本(http://maven.apache.org/docs/history.html)
  - 使用idea自带的Maven工具
- 修改Maven的setting文件（详见1.3 仓库配置）
  - idea自带Maven工具的setting文件存放位置：{IDEA安装目录}\plugins\maven\lib\maven3\conf\settings.xml
- **打开idea，创建一个空项目，并为项目配置JDK**
- 配置idea中的Maven设置 文件→设置→搜索maven
  - 配置Maven主路径
  - 配置Maven的setting文件路径
- **创建Maven模块**
  - 新建Maven模块
  - 设置模块名
  - 选择模块原型（Archetype）
  - 可在高级设置中设置：组ID、工件ID、版本
- **设置模块的目录类型**
  - 打开项目结构，选择所要设置的模块，在源中进行设置
  - src\main\java → 源代码【蓝色】        src\main\java\resources → 资源（没有则新建，设置）
  - src\main\test → 测试【绿色】            src\main\java\resources → 测试资源（没有则新建，设置）

## 7. 构建Maven项目

- #### 7.1 快捷方式

  - 点击idea右侧边栏的Maven按钮，在弹出页面的`生命周期`、`插件`中双击想要使用的命令

- #### 7.2 手动配置 -- 此方式可以添加断点**调式**

  - 在idea工具右上方绿色小锤子的旁边有一个`当前文件`的小窗口，点击这个小窗口→`编辑配置`
  - 在弹出页面的左上方添加Maven的运行配置
  - 编辑配置
    - 名称：功能名称
    - 命令：选择执行该功能的命令
    - 文件：选择命令执行的对应文件

## 8. pom.xml文件

- 依赖以junit为例、插件以Tomcat为例（对于依赖、插件会在后面详细说明）

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  
  <modelVersion>4.0.0</modelVersion><!-- pom的模型版本 -->

  
  <groupId>com.itheima</groupId>      <!-- 组织ID(项目报名) -->
  <artifactId>project-web</artifactId><!-- 项目ID(项目名) -->
  <packaging>war</packaging>          <!-- 打包方式：war(web程序)，jar(java程序) -->
  <version>1.0-SNAPSHOT</version>     <!-- 版本号：release(完成版), snapshot(开发板) -->

  <!-- 设置当前工程的所有依赖 -->
  <dependencies>
    <!-- 具体依赖 -->
    <dependency>
      <groupId>junit</groupId>      <!-- 依赖的组织ID -->
      <artifactId>junit</artifactId><!-- 依赖的项目ID -->
      <version>3.8.1</version>      <!-- 依赖的版本号 -->
      <scope>test</scope>           <!-- 依赖的作用范围 -->
    </dependency>
  </dependencies>

  <!-- 构建 -->
  <build>
    <!-- 设置当前工程的所有插件 -->
    <plugins>
      <!-- 具体插件 -->
      <plugin>
          <!-- 插件坐标-->
          <groupId>org.apache.tomcat.maven</groupId>   <!-- 插件的组织ID -->
          <artifactId>tomcat7-maven-plugin</artifactId><!-- 插件的项目ID -->
          <version>2.1</version>                       <!-- 插件的组织ID -->
          <!-- 插件配置-->
          <configuration>
            <port>8080</port><!-- 插件的端口 -->
            <path>/</path>   <!-- 插件的根路径 -->
          </configuration>
      </plugin>
    </plugins>
  </build>

</project>
```

## 9. 生命周期

- **清理工作**
- **核心工作**【compile → test-compile → test → package → install】
- **产生报告**-**发布站点**

## 10. 依赖

- #### 10.1 添加依赖

  - 在pom.xml文件中进行配置

  ```xml
  <!-- 在<dependencies>标签下添加插件 -->
  <dependencies>
    <!-- 具体依赖 -->
    <dependency>
      <groupId>junit</groupId>      <!-- 依赖的组织ID -->
      <artifactId>junit</artifactId><!-- 依赖的项目ID -->
      <version>4.12</version>       <!-- 依赖的版本号 -->
      <scope>test</scope>           <!-- 依赖的作用范围 -->
    </dependency>
  </dependencies>
  ```

- #### 10.2 查看依赖

  - 在idea右边栏的Maven中，若页面的`依赖项`中有添加的依赖则添加成功（若没有出现，点击页面左上角`重新加载所有Maven项目`）

- #### 10.3  依赖的作用范围

  - 项目分区

    - 主程序：main文件
    - 测试程序：test文件

  - 四个范围【compile、test、provided、runtime】

    ![image-20240321173814266](https://github.com/YOIOc/learning-record/blob/main/image/依赖作用范围.png)

  - 设置依赖范围

    ```xml
    <!-- 在<dependency>中，使用<scope>标签设置依赖范围 -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    ```

- #### 10.4 依赖的传递

  - 当前项目 ← 直接依赖 ← 间接依赖（间接依赖是直接依赖的直接依赖）

- #### 10.5 依赖冲突

  - 两大原则
    - 非同级依赖，层级越浅，优先级越高
    - 同级依赖，配置顺序越靠前，优先级越高（若不在同一个pom.xml文件，则向上推，优先级同理）

- #### 10.6 隐藏指定依赖

  - 作用：对外隐藏，当前项目使用的依赖

  ```xml
  <!-- 在<optional>标签中设置是否隐藏 -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <optional>true</optional>
  </dependency>
  ```

- #### 10.7 忽视指定依赖

  - 作用：对内隐藏，忽视直接依赖所使用的依赖

  ```xml
  <!-- 在<exclusions>下的<exclusion>标签中设置忽视的标签(需要所忽视依赖的版本号) -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
      <exclusions>
        <exclusion>
          <groupId>org.hamcrest</groupId>
          <artifaciId>hamcrest-core</artifaciId>
        </exclusion>
      </exclusions>
  </dependency>
  ```

## 11. 插件

- #### 11.1 插件的作用

  - 在项目指定的生命阶段中，执行特定的任务
  - 为项目提供额外的构建功能

- #### 11.2 插件的配置

  ```xml
  <!-- 在<plugins>标签下添加插件 -->
  <plugin>
    <!-- 插件坐标 -->
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-source-plugin</artifactId>
    <version>2.2.1</version>
     <!-- 插件的配置 --> 
    <executions>
      <!-- 插件的执行阶段与目标 --> 
      <execution>
        <goals>
          <goal>jar</goal><!-- 插件目标 -->
        </goals>
        <phase>generate-test-resources</phase><!-- 插件执行阶段 -->
      </execution>
    </executions>
  </plugin>
  ```

## 12. 安装Tomcat插件、启动web项目

- #### 12.1 安装Tomcat插件

  - 在中央仓库中搜索Tomcat maven，找到org.apache.tomcat.maven包点击进入

  - 跳转后页面的第一项就是Tomcat插件，点击进入并选择插件版本

  - 点击对应版本后，在跳转后页面中的maven窗口中，复制Tomcat插件的坐标

  - 在pom.xml文件中配置Tomcat插件

    ```xml
    <build>
    <plugins>
      <plugin>
          <groupId>org.apache.tomcat.maven</groupId>
          <artifactId>tomcat7-maven-plugin</artifactId>
          <version>2.1</version>
          <configuration>
            <port>8080</port>
            <path>/</path>
          </configuration>
      </plugin>
    </plugins>
    </build>
    ```

- #### 12.2 启动web项目

  - 将Tomcat的运行命令(Tomcat7:run)添加到运行配置中（详见7.2 手动配置）

## 13. 分模块开发与设计

1. 打开项目
2. 新建模块
3. 拷贝原始项目中对应的相关内容到新建模块中
   - Java类 / 其他执行文件 - 实现具体功能
   - resource文件 - 添加模块所需资源文件
   - pom文件 - 导入模块所需依赖

## 14. 聚合

- #### 14.1 作用

  - 用于快速构建maven工程，一次性构建多个项目/模块

- #### 14.2 操作方式

  - 创建一个空的maven模块，在pom文件中，将打包类型定义为pom - 以后统称为root模块

    ```xml
    <packaging>pom</packaging>
    ```

  - 定义当前模块进行构建操作时关联的其他模块名称

    ```xml
    <modules>
    	<module>../bank_pojo</module>
        <module>../bank_utils</module>
        <module>../bank_dao</module>
        <module>../bank_exceptions</module>
        <module>../bank_service</module>
        <module>../bank_control</module>
    </modules>
    ```

- #### 14.3 注意

  - 参与聚合操作的模块最终执行顺序与模块间的依赖关系有关，与配置顺序无关

## 15. 继承

- #### 15.1 作用

  - 通过继承可以实现在子工程中沿用父工程中的配置（与java中的继承相似）

- #### 15.2 操作方式

  - 在root模块的pom.xml文件中配置项目所需的所有依赖/插件

    ```xml
    <!-- 依赖管理 -->
    <dependencyManagement>
    	<dependencies>
        	... ...
        </dependencies>
    </dependencyManagement>
    
    <!-- 插件管理 -->
    <build>
    	<pluginManagement>
        	<plugins>
            	... ...
            </plugins>
        </pluginManagement>
    </build>
    ```

  - 在子工程中声明其父工程的坐标，与pom文件的位置

    ```xml
    <!-- 定义该工程的父工程 -->
    <parent>
        <!-- 填写父工程的坐标 -->
        <groupId>com.bjpowernode</groupId>
        <artifactId>bank</artifactId>
        <version>1.0-SNAPSHOT</version>
        <!-- 填写父工程的pom文件 -->
        <relativePath>../bank/pom.xml</relativePath>
    </parent>
    ```

  - 删除子工程中的 \<groupId>与 \<version> 标签

    ```xml
    <artifactId>bank_service</artifactId>
    <packaging>jar</packaging>
    ```

  - 子工程中添加工程所需依赖时，无需指定依赖版本

    ```xml
    <dependencies>
        <dependency>
          <groupId>jakarta.servlet</groupId>
          <artifactId>jakarta.servlet-api</artifactId>
          <scope>provided</scope>
        </dependency>
    </dependencies>
    ```

## 16. 聚合与继承的异同

- #### 16.1 作用

  - 聚合用于快速构建项目
  - 继承用于快速配置

- #### 16.2 相同点

  - 聚合与继承的pom.xml文件打包方式均为pom，可以将两种关系制作到同一个pom文件中
  - 聚合与继承均属于设计型模块，并无实际的模块内容

- #### 16.3 不同点

  - 聚合是在当前模块中配置关系，聚合可以感知到参与于聚合的模块有哪些
  - 继承是在子模块中配置关系，父模块无法感知哪些子模块继承了自己

## 17. 属性

- #### 17.1 作用

  - 配置在root模块的pom.xml文件中，等同于定义变量，方便统一维护

- #### 17.2 属性类别

  - 自定义属性 - 说明如下
  - 内置属性
    - 作用：maven的内置属性，便于配置
    - 调用：${属性名}
  - Setting属性
    - 作用：maven的setting.xml文件中的标签属性，用于动态配置
    - 调用：${setting.属性名}
  - Java系统属性
    - 作用：java系统属性
    - 调用：${user.属性名}
  - 环境变量属性
    - 作用：maven的setting.xml文件中的标签属性，用于动态配置
    - 调用：${env.属性名}

- #### 17.3 定义格式 - 自定义

  ```xml
  <!-- 定义自定义属性 -->
  <properties>
      <servlet.version>6.0.0</servlet.version>
      <jsp.version>3.1.1</jsp.version>
  </properties>
  ```

- #### 17.4 调用格式 

  ```xml
  <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <version>${servlet.version}</version>
      <scope>provided</scope>
  </dependency>
  ```

## 18. 版本管理

- #### 18.1 工程版本

  - SNAPSHOT（快照版本）
    - 开发版本（用于进行开发、测试）
  - RELEASE（发布版本）
    - 发布的稳定版本（即便进行功能的后续开发，也不会改变当前发布版本内容）

- #### 18.2 工程版本号约定

  - 约定规范
    - <主版本>.<次版本>.<增量版本>.<里程碑版本>
    - 主版本：项目重大架构的变更升级
    - 次版本：有较大的功能增加，或全面系统的修复漏洞
    - 增量版本：有重大漏洞的修复
    - 里程碑版本：一个版本的里程碑

## 19. 资源配置

- #### 19.1 作用

  - 统一在pom文件中管理资源配置

- #### 19.2 调用格式

  - 在root模块的pom.xml文件中添加属性

  ```xml
  <properties>
      <jdbc.driver>com.mysql.cj.jdbc.Driver</jdbc.driver>
      <jdbc.url>jdbc:mysql://localhost:3306/mvc</jdbc.url>
      <jdbc.username>root</jdbc.username>
      <jdbc.password>CZA20030203</jdbc.password>
  </properties>
  ```

  - 在root模块的pom.xml文件中，设置加载此pom中属性的目录

  ```xml
  <!-- 配置采用的资源文件 -->
  <build>
  	<resources>
          <resource>
              <!-- 设定配置文件对应的位置目录，支持使用属性，动态设定路径 -->
              <directory>${project.basedir}/src/main/resources</directory>
              <!-- 开启对配置文件的资源加载过滤 -->
              <filtering>true</filtering>
          </resource>
      </resources>
      
      <testResources>
          <testResource>
              <!-- 设定配置文件对应的位置目录，支持使用属性，动态设定路径 -->
              <directory>${project.basedir}/src/test/resources</directory>
              <!-- 开启对配置文件的资源加载过滤 -->
              <filtering>true</filtering>
          </testResource>
      </testResources>
  </build>
  ```

  - 打开开启了资源加载过滤的目录下的文件，使用配置的属性

  ```properties
  dirver=${jdbc.driver}
  url=${jdbc.url}
  username=${jdbc.username}
  password=${jdbc.password}
  ```

## 20. 多环境配置

- #### 20.1 作用

  - 允项目在不同的环境（如开发环境、测试环境、生产环境等）定义不同的配置

- #### 20.2 语法

  ```xml
  <!-- 创建多环境 -->
  <profiles>
      <!-- 定义具体的环境：开发环境 -->
      <profile>
          <!-- 定义环境对应的唯一名称 -->
          <id>dev_env</id>
          <!-- 定义环境中专用的属性值 -->
          <properties>
              <maven.compiler.source>21</maven.compiler.source>
              <maven.compiler.target>21</maven.compiler.target>
              <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  
              <servlet.version>6.0.0</servlet.version>
              <jsp.version>3.1.1</jsp.version>
  
              <jdbc.driver>com.mysql.cj.jdbc.Driver</jdbc.driver>
              <jdbc.url>jdbc:mysql://localhost:3306/mvc</jdbc.url>
              <jdbc.username>root</jdbc.username>
              <jdbc.password>CZA20030203</jdbc.password>
          </properties>
          <!-- 设置默认启动 -->
          <activation>
              <activeByDefault>true</activeByDefault>
          </activation>
      </profile>
      
      <!-- 定义具体的环境：生产环境 -->
      <profile>
          <id>pro_env</id>
      </profile>
  </profiles>
  ```

- ### 20.3 环境切换

  ![image-20240402162111768](https://github.com/YOIOc/learning-record/blob/main/image/环境切换.png)

## 21. 跳过测试

- #### 21.1 UI操作

  ![image-20240402160712388](https://github.com/YOIOc/learning-record/blob/main/image/跳过测试的UI操作.png)

- #### 21.2 配置文件

  ```xml
  <!-- Maven中所有生命周期的执行都是依靠插件执行的，故可以在plugin便签中关闭指定插件 -->
  <bulid>
  	<plugins>
          <plugin>
              <groupId>org.apache.maven</groupId>
              <artifactId>maven-surefire-plugin</artifactId>
              <version>2.22.1</version>
              <configuration>
                  <!-- 仅执行指定测试 -->
                  <includes>
                      <include>测试类的文件路径</include>
                  </includes>
                  <!-- 排除指定测试的执行 -->
                  <excludes>
                      <excludes>测试类的文件路径</excludes>
                  </excludes>
              </configuration>
          </plugin>
      </plugins>
  </bulid>
  ```

## 22. 私服

![image-20240403093613151](https://github.com/YOIOc/learning-record/blob/main/image/私服.png)

- #### 22.1 仓库分类

  - 宿主仓库hosted - 保存无法从中央仓库获取的资源
    - 自主研发
    - 第三方非开源项目
  - 代理仓库prixy - 代理远程仓库
  - 仓库组 - 将若干个仓库组成一个群组，简化配置（设计型仓库）

- #### 22.2 本地仓库访问私服 - 下载

  - 用nexus搭建私服，在私服中创建宿主仓库[heima-release、heima-snapshots]，并将宿主仓库添加到仓库组[maven-public]中

  - 配置本地仓库访问私服的权限（在maven的setting.xml文件中配置）

    ```xml
    <servers>
    	<server>
        	<id>heima-release</id>    <!--设置宿主仓库的专属ID-->
            <username>admin</username><!--私服的用户名-->
            <password>admin</password><!--私服的用户密码-->
        </server>
        
        <server>
        	<id>heima-snapshots</id>
            <username>admin</username>
            <password>admin</password>
        </server>
    </servers>
    ```

  - 配置本地仓库资源来源（在maven的setting.xml文件中配置）

    ```xml
    <mirrors>
    	<mirror>
        	<id>nexus-heima</id>  <!--设置私服仓库的专属ID-->
            <mirrorOf>*</mirrorOf><!--指定该私服替代哪些仓库-->
            <url>http://localhost:8081/repository/maven-public</url><!--私服仓库组的URL-->
        </mirror>
    </mirrors>
    ```

- #### 22.3 idea访问私服 - 上传

  - 配置当前项目访问私服上传资源的保存位置（在root模块的pom.xml文件中配置）

    ```xml
    <distributionManagement>
    	<repository>
            <id>heima-release</id>   <!-- 生产仓库ID -->
            <url>http://localhost:8081/repository/heima-release</url><!-- 生产仓库url -->
        </repository>
        <snapshotRepository>
            <id>heima-snapshots</id> <!-- 开发仓库ID -->
            <url>http://localhost:8081/repository/heima-snapshots</url><!-- 开发仓库url -->
        </snapshotRepository>
    </distributionManagement>
    ```

  - 发布资源到私服命令

    ```bat
    mav deploy
    ```

