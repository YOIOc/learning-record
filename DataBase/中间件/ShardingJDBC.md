# 1.ShardingJDBC

## 1.1 什么是ShardingJDBC

​	Sharding-JDBC是一个分布式数据库中间件，提供**分库分表**、**读写分离**、**分布式事务**、**弹性扩展**等功能

# 2.MySQL的主从搭建

## 2.1 什么是主从

​	A与B两台机器做主从后，在A上写数据另外一台B也会跟着写数据，实现数据实时同步

- MySQL主从基于binlog，主上需开启binlog才能进行主从
- 主将更改操作记录到binlog中 → 从将主的binlog事件(SQL语句)同步到本机并记录在relaylog里 → 从根据relaylog里面的SQL语句按顺序执行

## 2.2 主从的作用

- 实时灾备，用于故障切换
- 读写分离，提高业务效率
- 备份，避免影响业务

## 2.3 主从复制配置步骤

- 确保从数据库与主数据库里的数据一致

  使用Docker启动两个原始的MySQL数据库

  ```shell
  # 主数据库 M1 端口3307
  	#docker run：运行一个新的容器
  	#--name M1：新容器名称为 M1
  	#-p 3307:3306：配置端口映射 主机使用3307端口访问docker 3306端口的服务
  	#-e MYSQL_ROOT_PASSWORD=123456：设置环境变量 MySQL root用户的密码为123456
  	#-d：后台运行容器，容器启动后不会占用当前终端
  	#mysql:5.7：指定使用MySQL5.7镜像
  	#--lower_case_table_name=1：这是一个MySQL的配置选项，设置表名大小写不敏感
  docker run --name M1 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 从数据库 M1S1 端口3308
  docker run --name M1S1 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  ```

- 修改配置文件

  在root目录下创建mysqlms目录存放主、从数据库的配置文件

  ```shell
  # 创建mysqlms目录用于存储配置文件
  cd /root
  mkdir mysqlms
  
  # 复制主数据库的配置文件到mysqlms目录下
  	#docker cp：如果目标路径不存在，则创建并复制；反之则覆盖目标文件或目录
  docker cp M1:/etc/my.cnf m1.cnf
  
  # 复制从数据库的配置文件到mysqlms目录下
  docker cp M1S1:/etc/my.cnf m1s1.cnf
  ```

  修改主数据库的配置文件，并复制到主数据库中

  ```shell
  vim m1.cnf
  
  # 以下内容追加到m1.cnf配置文件中
  	#server-id=1：服务器唯一标识
  	#log-bin=master.bin：打开当前数据库的日志功能，并将日志文件命名为master.bin
  	#read-only=0：是否只读，1代表只读，0代表读写
  [mysqld]
  server-id=1
  read-only=0
  log-bin=master.bin
  
  # 将修改后的配置文件复制替换到主数据库中
  docker cp m1.cnf M1:/etc/my.cnf
  ```

  配置从数据库的配置文件，并复制到从数据库中

  ```shell
  vim m1s1.cnf
  
  # 以下内容追加到m1.cnf配置文件中
  	#server-id=2：为服务器标识
  	#relay_log=slave.bin：打开当前数据库的中继日志功能，并将中继日志文件命名为slave.bin
  [mysqld]
  server-id=2
  read-only=1
  relay_log=slave.bin
  
  # 将修改后的配置文件复制替换到主数据库中
  docker cp m1s1.cnf M1S1:/etc/my.cnf
  ```

  重启主从数据库

  ```shell
  docker restart M1 M1S1
  ```

- 在主数据库中创建一个同步账户授权给从数据库使用

  ```shell
  # 进入主数据库的交互终端
  	#docker exec：在运行中的容器内执行命令
  	#-it：交互模式运行(-i)，为容器分配一个终端，用于用户与容器交互(-t)
  	#M1：容器名称
  	#bash：在容器中执行Bash Shell
  docker exec -it M1 bash
  ```

  ```sql
  # 登录主数据库
  mysql -uroot -p123456
  
  # 创建用户
  	#create user：创建新用户
  	#'M1_cza'@'%'：新用户名为M1_cza，其可在任意主机(%代表任意主机)上登陆
  	#identified by '123456'：设置用户密码为123456
  create user 'M1_cza'@'%' identified by '123456';
  
  # 给该用户授权
  	#grant：授予用户权限
  	#replication slave：允许用户执行与主从复制相关操作的权限
  	#on *.*：表示权限适用于所有库(第一个*)和所有表(第二个*)
  	#to 'M1_cza'@'%'：权限赋给用户M1_cza，其可在任意主机(%代表任意主机)上使用该权限
  grant replication slave on *.* to 'M1_cza'@'%';
  
  #刷新权限
  flush privileges;
  ```

  查询主数据库的binlog文件名和位置

  ```sql
  show master status;
  ```

- 为从数据库关联主数据库并分配同步账户

  ```shell
  docker exec -it M1S1 bash
  ```

  ```sql
  # 登录从数据库
  mysql -uroot -p123456
  
  # 从库设置
  	#change master to：配置从库连接主库的配置信息
  	#master_host=""：主库的IP地址
  	#master_port=：主库的端口号
  	#master_user=""：从库连接主库时使用的用户名
  	#master_password=""：从库连接主库时使用的密码
  	#master_log_file=""：从库开始复制时使用的文件
  	#master_log_pos=：从库开始复制时的位置
  # 其中master_log_file与master_log_pos的值不是固定不变的，需要去主库执行show master status命令查看
  change master to master_host="47.109.203.48",master_port=3306,master_user="M1_cza",master_password="123456",master_log_file="[show master status命令查看]",master_log_pos=[show master status命令查看];
  ```

- 启动主从复制(从节点执行)

  ```sql
  start slave;
  ```

- 查看主从同步状态

  ```sql
  # 当Slave_IO_Running与Slave_SQL_Running的状态同时为Yes时，即代表主从启动成功
  show slave status\G;
  ```

## 2.4 MySQL主从规范

- 只能在主机里执行DML语句
- 在从机执行查询语句

# 3. ShardingJDBC配置读写分离

## 3.1 创建SpringBoot工程

## 3.2 设置maven

## 3.3 设置SpringBoot版本

## 3.4 启动SpringBoot

- 找不到测试中使用的org.junit.jupiter.api.Test类时，尝试右键将此类添加到类路径中
- lombok注解不生效时，进入设置的"注解处理器"，勾选项目的“启用注解处理”、“从项目类路径获取处理器”

## 3.5 导入所需依赖

- 注意版本兼容问题

```xml
<!--Web依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--lombok依赖(生成样板代码，提供日志支持)-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
</dependency>

<!--mybatis依赖与mysql驱动-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>

<!--测试依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>RELEASE</version>
    <scope>test</scope>
</dependency>

<!--ShardingSphere依赖-->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-core</artifactId>
    <version>5.4.1</version>
</dependency>

<!--druid数据源-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.24</version>
</dependency>
```

## 3.6 连接数据库

## 3.7 Mybatis-Plus生成项目骨架

## 3.8 编写项目配置

```yml
#配置springboot服务的启动端口
server:
  port: 8085

spring:
  main:
    allow-bean-definition-overriding: true #允许覆盖Spring容器中已有的Bean定义

  shardingsphere:
    props:
      sql:
        show: true #显示sql日志

    datasource:
      names: M1,M1S1,M1S2
      M1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://47.109.203.48:3306/db1?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT
        username: root
        password: 123456
        maxPoolSize: 100
        minPoolSize: 5
      M1S1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://47.109.203.48:3307/db1?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT
        username: root
        password: 123456
        maxPoolSize: 100
        minPoolSize: 5
      M1S2:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://47.109.203.48:3308/db1?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT
        username: root
        password: 123456
        maxPoolSize: 100
        minPoolSize: 5

    sharding:
      default-data-source-name: M1 #默认数据源(如果不配置那么就会把三个节点都当作从节点，导致DML出错)，主要用于写(一定要配置读写分离)

    masterslave: #配置主从(读写分离)
      name: MS
      master-data-source-name: M1
      slave-data-source-names: M1S1,M1S2
      load-balance-algorithm-type: round_robin #负载均衡均衡策略，采用轮询机制

# 整合mybatis的配置XXXXX
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.xuexiangban.shardingjdbc.entity #todo
```

## 3.9 完成业务需求

# 4. ShardingJDBC分库分表

## 4.1 分片算法

### 4.1.1 数值分片(inline)

```yaml
spring:
  shardingsphere:
    sharding:
      tables:
        # 分片的表名(逻辑)
        user:
          actual-data-nodes: db$->{0..1}.user$->{0..1} #分库分表结构
          database-strategy:
            inline: 
		      sharding-column: sex #分库字段
		      algorithm-expression: db$->{sex%2} #分库逻辑
		  table-strategy:
			inline:
			  sharding-column: age #分表字段
			  algorithm-expression: user$->{age%2} #分表逻辑
```

### 4.1.2 日期分片(standard)

规则配置文件

```yaml
spring:
  shardingsphere:
    sharding:
      tables:
      	# 分片的表名(逻辑)
        user:
      	  actual-data-nodes: db$->{0..1}.user$->{0..1}
      	    database-strategy:
      		  standard:
      		    sharding-column: birthday
      			preciseAlgorithmClassName: [分片规则类的全限定包名]
      		table-strategy:
      		  inline:
      		    sharding-column: age
			  	algorithm-expression: user$->{age%2}
```

策略实现类

```java
package org.example.shardingjdbc.algorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import sun.util.resources.cldr.CalendarData;
import java.util.*;

// 规则类要实现PreciseShardingAlgorithm<>接口，泛型为分片字段的数据类型
public class BirthdayAlgorithm implements PreciseShardingAlgorithm<Date> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        // 获取属性数据库的值(所有用户的生日)
        Date date = preciseShardingValue.getValue();
        // 获取数据源的名称信息列表(db1, db2)
        Iterator<String> iterator = collection.iterator();
        if(日期规则){
            // 返回db1
        }
        // 返回db2
    }
}
```

## 4.2 按年月分库分表

配置文件

```yaml
spring:
  shardingsphere:
    sharding:
      tables:
        user_order:
        #collect{t ->t.toString().padLeft(2, '0')}：对集合中的每个元素进行操作并生成一个新的集合，padLeft(2, '0')表示将字符串左侧填充"0"，使其长度至少为2
          actual-data-nodes:db1.user_order_$->{2024..2025}${1..12}.collect{t ->t.toString().padLeft(2, '0')}
          table_strategy:
            shardingColumn: yearmonth
            preciseAlgorithmClassName: org.example.shardingjdbc.algorithm.YearMonthShardingAlgorithm
```

策略实现类

```java
package org.example.shardingjdbc.algorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import sun.util.resources.cldr.CalendarData;
import java.util.*;

public class BirthdayAlgorithm implements PreciseShardingAlgorithm {
    private static final String SPLITTER = "_";
    
    // availableTargetNames：可用目标合集(例如user_order_202401等等)
    // shardingValue：分片值对象，包含逻辑表名、分片字段名、分片字段值
    // doSharding的执行逻辑：当该方法返回的返回值，存在于availableTargetNames中时，则路由成功否则失败
    @Override
    public String doSharding(Collection availableTargetNames, PreciseShardingValue shardingValue) {
        String tbName = shardingValue.getLogicTargetNames() + "_" + shardingValue.getValue();
        return tbName;
    }
}
```

# 5. ShardingSphere全局ID

## 5.1 如何使用全局ID

```yaml
spring:
  shardingsphere:
    sharding:
      tables:
        # 使用全局ID的表名(逻辑)
        user：
          key-generator:
            column: id #使用全局ID的字段
            type: SNOWFLAKE #生成算法
```

