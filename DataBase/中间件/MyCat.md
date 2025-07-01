# 1.MyCat

## 1.1 什么是MyCat

​	MyCAT是分布式数据库中间插件，是一个实现了MySQL协议的服务器，其核心功能是**分表分库**，即将一个大表水平分割为N个小表，存储在后端MySQL服务器里或者其他数据库里。

## 1.2 MyCat的作用

- 能满足数据库数据大量存储；
- 提高了查询性能；
- 实现读写分离，分库分表；

## 1.3MyCat的原理

​	它拦截用户发送过来的SQL语句，并对SQL语句做了一些特定的分析，如分片分析、路由分析、读写分离分析、缓存分析等，然后将此SQL发往后端的真实数据库，并将返回的结果做适当处理，最终返回给用户。

# 2.MyCat名词解释

- **分库分表**：按照一定规则把数据库中的表拆分为多个带有数据库实例、物理库、物理表访问路径的分表
- **逻辑库**：数据库代理中的数据库，它可以包含多个逻辑表
- **逻辑表**：数据库代理中的表，它可以映射代理连接的数据库中的表(物理表)
- **物理库**：数据库代理连接的数据库中的库
- **物理表**：数据库代理连接的数据库中的表
- **拆分键**：即分片键，描述拆分逻辑表的数据规则的字段
- **分区键**：当使用等值查询的时候，能直接映射一个分区的拆分键
- **物理分表**：指已经进行数据拆分的，在数据库上面的物理表，是分片表的一个分区
- **物理分库**：一般指包含多个物理分表的库
- **分库**：一般指通过多个数据库拆分分片表，每个数据库一个物理分表，物理分库名字相同
- **分片表、水平分片表**：按照一定规则把数据拆分成多个分区的表，在分库分表语境下，它属于逻辑表的一种
- **全局表(广播表)**：每个数据库实例都冗余全量数据的逻辑表，它通过表数据冗余，使分片表的分区与该表的数据在同一个数据库实例里，达到JOIN运算能够直接在该数据库实例里执行。它的数据一致一般是通过数据库代理分发SQL实现，也有基于集群日志的实现
- **集群**：多个数据节点组成的逻辑节点，在MyCAT2里，它是把对多个数据源地址视为一个数据源地址(名称)，并提供自动故障恢复、转移、即实现高可用、负载均衡的组件
- **数据源**：连接后端数据库的组件，它是数据库代理中连接后端数据库的客户端
- **schema(库)**：在MyCAT2中配置表逻辑，视图等的配置

# 3.使用MyCat

## 3.1 配置物理库地址

- MyCat启动之前需要配置物理库的地址，不然MyCat启动会报错，配置文件地址：mycat/conf/datasources/prototypeDs.datasource.json

- 打开配置文件

  ```shell
  vim prototypeDs.datasource.json
  ```

  - 修改配置文件

    - 数据库的连接密码："password"
    - 数据库的连接地址："url"
    - 数据库用户名："user"

    ```json
    {
    	"dbType":"mysql",
    	"idleTimeout":60000,
    	"initSqls":[],
    	"initSqlsGetConnection":true,
    	"instanceType":"READ_WRITE",
    	"maxCon":1000,
    	"maxConnectTimeout":3000,
    	"maxRetryCount":5,
    	"minCon":1,
    	"name":"prototypeDs",
    	"password":"CZA20030203",
    	"type":"JDBC",
    	"url":"jdbc:mysql://127.0.0.1:3306?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8",
    	"user":"root",
    	"weight":0
    }
    ```

    在MyCat中，配置数据源时的UTL并不需要指定数据库名称，因为MyCat的设计理念是作为数据库中间件，它会根据SQL查询的内容动态地将请求路由到具体的数据库或分片上

    ## 3.2 启动MyCat

    ```shell
    cd /data/mycat/bin
    # 启动
    ./mycat start
    # 停止
    ./mycat stop
    # 前台运行
    ./mycat console
    # 添加到系统自动启动
    ./mycat install
    # 取消随系统自动启动
    ./mycat remove
    # 重启
    ./mycat restart
    # 暂停
    ./mycat pause
    # 查看启动状态
    ./mycat status
    ```

    ## 3.3 连接MyCat

    ​	我们在外面看MyCat，认为MyCat就是一个MySQL
    <img src="https://github.com/YOIOc/learning-record/blob/main/image/连接MyCat.png" alt="连接MyCat" style="zoom:50%;" />

# 4.MyCat配置文件

## 4.1 用户相关

### 4.1.1 创建用户

```sql
/*+ mycat:createUser{
  "username":"[用户名]",
  "password":"[登陆密码]",
  "ip":"[URI]",
  "transactionType":"xa"
} */
```

### 4.1.2 删除用户

```sql
/*+ mycat:dropUser{
  "username":"[用户名]"} */
```

### 4.1.3 显示用户

```sql
/*+ mycat:showUsers */
```

## 4.2 数据源相关

### 4.2.1 创建数据源

```sql
/*+ mycat:createDataSource{
  "dbType":"mysql",
  "idleTimeout":60000,
  "initSqls":[],
  "initSqlsGetConnection":true,
  "instanceType":"READ_WRITE",
  "maxCon":1000,
  "maxConnectTimeout":3000,
  "maxRetryCount":5,
  "minCon":1,
  "name":"[数据源名称]",
  "password":"[登陆密码]",
  "type":"JDBC",
  "url":"[URL(jdbc:mysql://127.0.0.1:3306?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8)]",
  "user":"[登录用户]",
  "weight":0
} */;
```

### 4.2.2 删除数据源

```sql
/*+ mycat:dropDataSource{
  "dbType":"mysql",
  "idleTimeout":60000,
  "initSqls":[],
  "initSqlsGetConnection":true,
  "instanceType":"READ_WRITE",
  "maxCon":1000,
  "maxConnectTimeout":3000,
  "maxRetryCount":5,
  "minCon":1,
  "name":"[数据源名称]",
  "type":"JDBC",
  "weight":0
} */;
```

### 4.2.3 显示数据源

```sql
/*+ mycat:showDataSources{} */
```

## 4.3 集群相关

### 4.3.1 创建集群

```sql
/*! mycat:createCluster{
  "clusterType":"MASTER_SLAVE",
  "heartbeat":{
    "heartbeatTimeout":1000,
    "maxRetry":3,
    "minSwitchTimeInterval":300,
    "slaveThreshold":0
  },
  "masters":[
    "[主数据源名称]" //主节点
  ],
  "maxCon":2000,
  "name":"[集群名称]",
  "readBalanceType":"BALANCE_ALL",
  "replicas":[
    "[从数据源名称]" //从节点
  ],
  "switchType":"SWITCH"
} */;
```

### 4.3.2 删除集群

```sql
/*! mycat:dropCluster{
  "name":"[集群名称]"
} */;
```

### 4.3.3 显示集群

```sql
/*+ mycat:showClusters{} */
```

# 5.MySQL的主从搭建

​	因为MyCat只能路由、分发，不能把多个数据库里面的数据进行同步，所以要数据同步必须还要使用MySQL的读写分离，主从复制

## 5.1 什么是主从

​	A与B两台机器做主从后，在A上写数据另外一台B也会跟着写数据，实现数据实时同步

- MySQL主从基于binlog，主上需开启binlog才能进行主从
- 主将更改操作记录到binlog中 → 从将主的binlog事件(SQL语句)同步到本机并记录在relaylog里 → 从根据relaylog里面的SQL语句按顺序执行

## 5.2 主从的作用

- 实时灾备，用于故障切换
- 读写分离，提高业务效率
- 备份，避免影响业务

## 5.3 主从复制配置步骤

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
  docker run --name M1 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 从数据库 M1S1 端口3308
  docker run --name M1S1 -p 3308:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  ```

- 修改配置文件

  在root目录下创建mysqlms目录存放主、从数据库的配置文件

  ```shell
  cd /root
  mkdir mysqlms
  
  # 复制主数据库的配置文件到mysqlms目录下
  docker cp M1:/etc/my.cnf m1.cnf
  
  # 复制从数据库的配置文件到mysqlms目录下
  docker cp M1S1:/etc/my.cnf m1s1.cnf
  ```

  配置主数据库的配置文件，并复制到主数据库中

  ```shell
  vim m1.cnf
  
  # 以下内容追加到m1.cnf配置文件中
  	#server-id=1：为服务器标识
  	#log-bin=master.bin：打开当前数据库的日志功能，并将日志文件命名为master.bin
  	#read-only=0：是否只读，1代表只读，0代表读写
  [mysqld]
  server-id=1
  read-only=0
  log-bin=master.bin
  
  # 将修改后的配置文件复制替换到主数据库中
  docker cp m1.cnf M1:/etc/my.cnf
  ```

  配置从数据库的配置文件，并复制到主数据库中

  ```shell
  vim m1s1.cnf
  
  # 以下内容追加到m1.cnf配置文件中
  	#server-id=2：为服务器标识
  [mysqld]
  server-id=2
  read-only=1
  
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
  change master to master_host="192.168.127.129",master_port=3307,master_user="M1_cza",master_password="123456",master_log_file="[show master status命令查看]",master_log_pos=[show master status命令查看];
  
  # 启动主从(从库里面执行)
  start slave;
  
  # 查询主从的状态(从库里面执行)
  	# 当Slave_IO_Running与Slave_SQL_Running的状态同时为Yes时，即代表主从启动成功
  show slave status \G;
  ```

## 5.4 MySQL主从规范

- 只能在主机里执行DML语句
- 在从机执行查询语句

# 6.MyCat的主从搭建

​	前提：先搭建好MySQL的主从配置，登录MyCat

## 6.1 搭建MyCat主从的优势

- 读写分离：主库(Master)负责处理写操作、从库(slave)负责处理读操作
- 负载均衡：MyCat可以根据配置将请求分发到多个从库，从而实现负载均衡，减轻主库的压力
- 高可用性：如果主库或从库发生故障，MyCat可以通过心跳检测和切换机制自动将请求切换到可用的数据库节点

## 6.2 搭建步骤

- **创建数据源**

  添加主数据源

  ```sql
  /*+ mycat:createDataSource{
    "dbType":"mysql",
    "idleTimeout":60000,
    "initSqls":[],
    "initSqlsGetConnection":true,
    "instanceType":"[主机为READ_WRITE]",
    "maxCon":1000,
    "maxConnectTimeout":3000,
    "maxRetryCount":5,
    "minCon":1,
    "name":"[主数据源名称]",
    "password":"[登陆密码]",
    "type":"JDBC",
    "url":"[URL(jdbc:mysql://127.0.0.1:3307?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8)]",
    "user":"[登录用户]",
    "weight":0
  } */;
  ```

  添加从数据源

  ```sql
  /*+ mycat:createDataSource{
    "dbType":"mysql",
    "idleTimeout":60000,
    "initSqls":[],
    "initSqlsGetConnection":true,
    "instanceType":"[从机为READ]",
    "maxCon":1000,
    "maxConnectTimeout":3000,
    "maxRetryCount":5,
    "minCon":1,
    "name":"[从数据源名称]",
    "password":"[登陆密码]",
    "type":"JDBC",
    "url":"[URL(jdbc:mysql://127.0.0.1:3308?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8)]",
    "user":"[登录用户]",
    "weight":0
  } */;
  ```

- **查看数据源**

  ```sql
  /*+ mycat:showDataSources{} */
  ```

- **创建集群**

  ```json
  /*! mycat:createCluster{
    "clusterType":"MASTER_SLAVE",
    "heartbeat":{
      "heartbeatTimeout":1000,
      "maxRetry":3,
      "minSwitchTimeInterval":300,
      "slaveThreshold":0
    },
    "masters":[
      "[主数据源名称]"
    ],
    "maxCon":2000,
    "name":"[集群名称]",
    "readBalanceType":"BALANCE_ALL",
    "replicas":[
      "[从数据源名称]"
    ],
    "switchType":"SWITCH"
  } */;
  ```

- **查看集群**

  ```sql
  /*+ mycat:showClusters{} */
  ```

- **创建逻辑库**

  ```sql
  create database db1 default character set utf8mb4 collate utf8mb4_general_ci;
  ```

- **修改逻辑库的数据源**

  ```shell
  # 进入逻辑库配置文件
  vim /data/mycat/conf/schemas/db1.schema.json
  ```

  在里面添加属性

  ```json
  "targetName":"[集群名称]"
  ```

- **重启MyCat**

  ```shell
  ./bin/mycat restart
  ```

# 7.MySQL搭建集群(双主双从)

## 7.1 搭建思路

<img src="https://github.com/YOIOc/learning-record/blob/main/image/集群关系图.png" alt="集群关系图" style="zoom:50%;" />

## 7.2 搭建步骤

- 使用Docker创建M1S2、M2、M2S1数据库

  ```shell
  # 数据库 M1S2 端口3309
  docker run --name M1S2 -p 3309:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 数据库 M2 端口3310
  docker run --name M2 -p 3310:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 数据库 M2S1 端口3311
  docker run --name M2S1 -p 3311:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  ```

- 修改数据库配置文件

  ```shell
  cd /root/mysqlms
  
  # 复制M1S2数据库的配置文件到mysqlms目录下
  docker cp M1S2:/etc/my.cnf m1s2.cnf
  
  # 复制M2数据库的配置文件到mysqlms目录下
  docker cp M2:/etc/my.cnf m2.cnf
  
  # 复制M2S1数据库的配置文件到mysqlms目录下
  docker cp M2S1:/etc/my.cnf m2s1.cn
  ```

  ```shell
  # 配置M1S2数据库的配置文件，并复制到M1S2数据库中
  vim m1s2.cnf
  # 以下内容追加到m1s2.cnf配置文件中
  [mysqld]
  server-id=3
  read-only=1
  # 将修改后的配置文件复制替换到M1S2数据库中
  docker cp m1s2.cnf M1S2:/etc/my.cnf
  
  # 配置M2数据库的配置文件，并复制到M2数据库中
  vim m2.cnf
  # 以下内容追加到m2.cnf配置文件中
  [mysqld]
  server-id=4
  read-only=1
  log_slave_updates=1
  # 将修改后的配置文件复制替换到M2数据库中
  docker cp m2.cnf M2:/etc/my.cnf
  
  # 配置M2S1数据库的配置文件，并复制到M2S1数据库中
  vim m2s1.cnf
  # 以下内容追加到m2s1.cnf配置文件中
  [mysqld]
  server-id=5
  read-only=1
  # 将修改后的配置文件复制替换到M2S1数据库中
  docker cp m2s1.cnf M2S1:/etc/my.cnf
  ```

  ​	**注：M2会从M1复制数据，但并不会记录复制的数据，所以M2S1里面没有数据！我们需要打开M2的级联复制功能，让M2也能记录M1里面复制的数据**

- 重启数据库

  ```shell
  docker restart M1S2 M2 M2S1
  ```

- 在M2数据库中创建一个同步账户授权给M2S1数据库使用

  ```shell
  # 进入M2数据库的交互终端
  docker exec -it M2 bash
  ```

  ```sql
  # 登录M2数据库
  mysql -uroot -p123456
  
  # 创建用户
  create user 'M2_cza'@'%' identified by '123456';
  
  # 给该用户授权
  grant replication slave on *.* to 'M2_cza'@'%';
  
  #刷新权限
  flush privileges;
  ```

  查询主数据库的binlog文件名和位置

  ```sql
  show master status;
  ```

- 为M1S2、M2、M2S1数据库关联主数据库并分配同步账户

  ```sql
  # 进入M1S2数据库的交互终端
  docker exec -it M1S2 bash
  
  # 登录从数据库
  mysql -uroot -p123456
  
  # 为M1S2数据库关联主数据库M1
  change master to master_host="192.168.127.129",master_port=3307,master_user="M1_cza",master_password="123456",master_log_file="[show master status命令查看]",master_log_pos=[show master status命令查看];
  
  # 启动主从(M1S2库里面执行)
  start slave;
  
  # 查询主从的状态(M1S2库里面执行)
  show slave status \G;
  ```

  ```sql
  # 进入M2数据库的交互终端
  docker exec -it M2 bash
  
  # 登录从数据库
  mysql -uroot -p123456
  
  # 为M2数据库关联主数据库M1
  change master to master_host="192.168.127.129",master_port=3307,master_user="M1_cza",master_password="123456",master_log_file="[show master status命令查看]",master_log_pos=[show master status命令查看];
  
  # 启动主从(M2库里面执行)
  start slave;
  
  # 查询主从的状态(M2库里面执行)
  show slave status \G;
  ```

  ```sql
  # 进入M2S1数据库的交互终端
  docker exec -it M2S1 bash
  
  # 登录从数据库
  mysql -uroot -p123456
  
  # 为M2S1数据库关联主数据库M2
  change master to master_host="192.168.127.129",master_port=3310,master_user="M2_cza",master_password="123456",master_log_file="[show master status命令查看]",master_log_pos=[show master status命令查看];
  
  # 启动主从(M2S1库里面执行)
  start slave;
  
  # 查询主从的状态(M2S1库里面执行)
  show slave status \G;
  ```

# 8.MyCat搭建集群(双主双从)

## 8.1 搭建步骤

- 添加数据源

  ```sql
  # 添加M1S2库的数据源
  /*+ mycat:createDataSource{
    "dbType":"mysql",
    "idleTimeout":60000,
    "initSqls":[],
    "initSqlsGetConnection":true,
    "instanceType":"[从机为READ]",
    "maxCon":1000,
    "maxConnectTimeout":3000,
    "maxRetryCount":5,
    "minCon":1,
    "name":"M1S2",
    "password":"123456",
    "type":"JDBC",
    "url":"jdbc:mysql://127.0.0.1:3309?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root,
    "weight":0
  } */;
  ```

  ```sql
  # 添加M2库的数据源
  /*+ mycat:createDataSource{
    "dbType":"mysql",
    "idleTimeout":60000,
    "initSqls":[],
    "initSqlsGetConnection":true,
    "instanceType":"READ_WRITE",
    "maxCon":1000,
    "maxConnectTimeout":3000,
    "maxRetryCount":5,
    "minCon":1,
    "name":"M2",
    "password":"123456",
    "type":"JDBC",
    "url":"jdbc:mysql://127.0.0.1:3310?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root",
    "weight":0
  } */;
  ```

  ```sql
  # 添加M2S1库的数据源
  /*+ mycat:createDataSource{
    "dbType":"mysql",
    "idleTimeout":60000,
    "initSqls":[],
    "initSqlsGetConnection":true,
    "instanceType":"READ",
    "maxCon":1000,
    "maxConnectTimeout":3000,
    "maxRetryCount":5,
    "minCon":1,
    "name":"M2S1",
    "password":"123456",
    "type":"JDBC",
    "url":"jdbc:mysql://127.0.0.1:3311?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root",
    "weight":0
  } */;
  ```

- 配置集群

  ```sql
  /*! mycat:createCluster{
    "clusterType":"MASTER_SLAVE",
    "heartbeat":{
      "heartbeatTimeout":1000,
      "maxRetry":3,
      "minSwitchTimeInterval":300,
      "slaveThreshold":0
    },
    "masters":[
      m1","m2"
    ],
    "maxCon":2000,
    "name":"colony01",
    "readBalanceType":"BALANCE_ALL",
    "replicas":[
      "m1s1","m1s2","m2s1"
    ],
    "switchType":"SWITCH"
  } */;
  ```

- 重启MyCat

  ```shell
  cd /data/mycat/bin
  ./mycat restart
  ```

# 9.MyCat分库分表

## 9.1 分库分表的作用

- 减轻单库/单表的压力，**提高读写性能**
- 多个库/表可以分布在不同服务器上，**提升系统吞吐量，提高并发能力**
- 分库分表后，可以突破单库/表的存储限制，**支持更大的数据量**
- 不同业务模块的数据分配到不同的库/表中，**便于管理和扩展，达到业务隔离**

## 9.2 切分方式

- 垂直切分：基于表/字段切分(按业务切分)，切分后不同表的表结构也不同
- 水平切分：基于数据切分，切分后不同表的表结构相同

## 9.3 分库分表的环境搭建

- 准备四个数据库-两组主从(docker)

  | 名称 | port | ip              |
  | ---- | ---- | --------------- |
  | dw0  | 3307 | 192.168.127.129 |
  | dr0  | 3308 | 192.168.127.129 |
  | dw1  | 3309 | 192.168.127.129 |
  | dr1  | 3310 | 192.168.127.129 |

  ```shell
  # 0号主数据库
  docker run --name dw0 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 0号从数据库
  docker run --name dr0 -p 3308:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 1号主数据库
  docker run --name dw1 -p 3309:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  
  # 1号从数据库
  docker run --name dr1 -p 3310:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --lower_case_table_names=1
  ```

- 配置两组主从

  0号主从配置

  ```shell
  #修改主从配置文件
  	#进入mysqlms目录
  cd /mysqlms
  docker cp dw0:/etc/my.cnf dw0.cnf
  docker cp dr0:/etc/my.cnf dr0.cnf
  	#修改dw0.cnf
  [mysqld]
  server-id=1
  read-only=0
  log-bin=master.bin
  	#修改dr0.cnf
  [mysqld]
  server-id=2
  read-only=1
  	#将修改好的配置文件，复制到容器里
  docker cp dw0.cnf dw0:/etc/my.cnf
  docker cp dr0.cnf dr0:/etc/my.cnf
  
  #重启主从数据库
  docker restart dw0 dr0
  
  #进入dw0为dr0创建用户，并查询状态
  	#进入dw0
  docker exec -it dw0 bash
  mysql -uroot -p123456
  	#创建用户
  create user 'dw0_rep'@'%' identified by '123456';
  	#授予权限
  grant replication slave on *.* to 'dw0_rep'@'%';
  	#刷新权限
  flush privileges;
  	#查询状态
  show master status;
  
  #进入dr0关联dw0，并启动主从
  	#进入dr0
  docker exec -it dr0 bash
  mysql -u root -p123456
  	#关联dw0
  change master to master_host="192.168.127.129",master_port=3307,master_user="dw0_rep",master_password="123456",master_log_file="master.000001",master_log_pos=745;
  	#启动主从
  start slave;
  	#查询状态
  show slave status \G;
  ```

  1号主从配置

  ```shell
  #修改主从配置文件
  	#进入mysqlms目录
  cd /mysqlms
  docker cp dw1:/etc/my.cnf dw1.cnf
  docker cp dr1:/etc/my.cnf dr1.cnf
  	#修改dw0.cnf
  [mysqld]
  server-id=1
  read-only=0
  log-bin=master.bin
  	#修改dr0.cnf
  [mysqld]
  server-id=2
  read-only=1
  	#将修改好的配置文件，复制到容器里
  docker cp dw1.cnf dw1:/etc/my.cnf
  docker cp dr1.cnf dr1:/etc/my.cnf
  
  #重启主从数据库
  docker restart dw1 dr1
  
  #进入dw0为dr0创建用户，并查询状态
  	#进入dw0
  docker exec -it dw1 bash
  mysql -uroot -p123456
  	#创建用户
  create user 'dw1_rep'@'%' identified by '123456';
  	#授予权限
  grant replication slave on *.* to 'dw1_rep'@'%';
  	#刷新权限
  flush privileges;
  	#查询状态
  show master status;
  
  #进入dr0关联dw0，并启动主从
  	#进入dr0
  docker exec -it dr1 bash
  mysql -u root -p123456
  	#关联dw0
  change master to master_host="192.168.127.129",master_port=3309,master_user="dw1_rep",master_password="123456",master_log_file="master.000001",master_log_pos=745;
  	#启动主从
  start slave;
  	#查询状态
  show slave status \G;
  ```

- 配置MyCat集群

  配置数据源

  ```sql
  #数据源dw0
  /*+ mycat:createDataSource{
    "name":"dw0",
    "password":"123456",  "url":"jdbc:mysql://127.0.0.1:3307?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root",} */;
    
  #数据源dw0
  /*+ mycat:createDataSource{
    "name":"dr0",
    "password":"123456",  "url":"jdbc:mysql://127.0.0.1:3308?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root",} */;
    
  #数据源dw1
  /*+ mycat:createDataSource{
    "name":"dw1",
    "password":"123456",  "url":"jdbc:mysql://127.0.0.1:3309?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root",} */;
    
  #数据源dr1
  /*+ mycat:createDataSource{
    "name":"dr1",
    "password":"123456",  "url":"jdbc:mysql://127.0.0.1:3310?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8",
    "user":"root",} */;
  ```

  配置集群

  - 自动分片**默认要求集群名字以c为前缀**，数字为后缀
  - c0就是第一个分片节点，c1就是第二个分片节点

  ```sql
  #0号集群
  /*! mycat:createCluster{
    "name":"c0",
    "masters":[
      "dw0"
    ],
    "replicas":[
      "dr0"
    ]
  } */;
  
  #1号集群
  /*! mycat:createCluster{
    "name":"c1",
    "masters":[
      "dw1"
    ],
    "replicas":[
      "dr1"
    ]
  } */;
  ```

## 9.4 全局表

- 什么是全局表：**每个分片(数据库实例)中都有一份完整的副本**，用来存储一些**全局性、共享性的数据**
- 全局表的使用场景
  - 系统配置：如系统参数，全局设置等
  - 常量表：状态码，类型码等

### 9.4.1 将表设为全局

**broadcase**就是全局表的标识：MyCat会自动将有broadcase标识的表，添加到所有数据源中

```sql
# 创建数据库
CREATE DATABASE db1 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# 创建全局表
use db1;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_type` int  ,
  `dict_name` varchar(100) DEFAULT NULL,
  `dict_value` int ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 BROADCAST;
```

## 9.5 分片表

前提：集群c0、c1的数据源配置没有问题

### 9.5.1 将表分片

- dbpartition BY mod_hash(CUSTOMER_ID)：使用**哈希算法取模**按照**CUSTOMER_ID字段的值**进行**分库**
- tbpartition BY mod_hash(CUSTOMER_ID)：使用**哈希算法取模**按照**CUSTOMER_ID字段的值**进行**分表**
- dbpartitions 2：数据分散到**2**个数据库中
- tbpartitions 1：每个数据库中只有**1**个分表

```sql
CREATE TABLE orders(
   ID BIGINT NOT NULL AUTO_INCREMENT,
   ORDER_TYPE INT,
   CUSTOMER_ID INT,
   AMOUNT DECIMAL(10,2),
   PRIMARY KEY(ID)
) ENGINE=INNODB  DEFAULT CHARSET=utf8mb4
dbpartition BY mod_hash(CUSTOMER_ID) tbpartition By mod_hash(CUSTOMER_ID) 
tbpartitions 1 dbpartitions 2
```



## 9.6 ER表

在MyCat2.0中，有关联的数据不必须放到相同的库中，MyCat会自动优化

```sql
CREATE TABLE orders_detail(
  ID BIGINT AUTO_INCREMENT,
  detail VARCHAR(2000),
  order_id BIGINT,
  PRIMARY KEY(ID)
) ENGINE=INNODB  DEFAULT CHARSET=utf8mb4
dbpartition BY mod_hash(order_id) tbpartition By mod_hash(order_id) 
tbpartitions 1 dbpartitions 2
```

在物理库中存在订单数据与订单详情数据不配套的情况，但在MyCat2.0中，仍可以使用简单的连接查询，来查询数据

```sql
select * from orders o inner join orders_detail od on(o.id=od.order_id)
```

## 9.7 分片算法

### 9.7.1 取模哈希分片 mod_hash

- 分片规则：如果分片值是字符串则先对字符串进行hash转换为数值并求和

  - 分库键和分表键是同键

    分表下标=分片值%(分库数量*分表数量)

    分库下标=分表下标/分表数量

  - 分库键和分表键不同键

    分表下标=分片值%分表数量

    分库下标=分片值%分库数量

  ```sql
  #都以id为切分键，分库分表数量都为6
  create table travelrecord (
   ....
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 
  dbpartition by MOD_HASH (id) dbpartitions 6
  tbpartition by MOD_HASH (id) tbpartitions 6;
  ```

### 9.7.1 字符串哈希分片 uni_hash

- 特点：计算更复杂，但分布更均匀
- 分片规则：与取模哈希类似，但字符串的哈希值并不相加

### 9.7.2 范围哈希分片 range_hash

- 分片语法：RANGE_HASH(字段1, 字段2, 截取开始下标)

- 分片规则：优先使用字段1分片，当字段1不可用时，使用字段2，当字段是字符串类型时，从指定下标开始截取字符串进行哈希计算

  ```sql
  #先以id分片，当id不存在时使用user_id分片，如果分片字段是字符串类型，则从第3个字符开始截取
  create table travelrecord(
  ...
  )ENGINE=InnoDB DEFAULT CHARSET=utf8 
  dbpartition by RANGE_HASH(id,user_id,3) dbpartitions 3
  tbpartition by RANGE_HASH(id,user_id,3) tbpartitions 3;
  ```

### 9.7.3 日期哈希分片 YYYYDD

- 特点：仅用于分库

- 分库规则：分库下标=(年份*366+天数)%分库数

  ```sql
  create table travelrecord (
   ....
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 
  dbpartition by YYYYDD(go_date) dbpartitions 8
  tbpartition by mod_hash(user_id) tbpartitions 12;
  ```

# 10.MyCat生成全局ID

## 10.1 为什么要使用全局ID

​	在复杂的分布式系统中，数据分库分表后需要有一个唯一ID来标识一条消息或数据，防止分表合并后主键ID不是唯一

## 10.2 雪花算法

​	引入时间戳的ID保持自增的分布式ID生成算法，总体成递增趋势

- Snowflake的结构如下
  - 第1位：符号位，1表示负数，0标识正数，ID不可能为负数
  - 第2~41位：时间戳
  - 第42~52位：机器码，前5位是机器ID，后5位是工作ID
  - 第53~64位：毫秒计数器，每毫秒产生4096个ID序列

![雪花算法](https://github.com/YOIOc/learning-record/blob/main/image/雪花算法.png)

- 雪花算法的优点
  - 整个ID都是趋势递增的
  - 不依赖数据库等第三方系统，稳定性更高，生成ID的性能也非常高
- 雪花算法的缺点
  - 强依赖机器时钟，如果机器上时钟回拨，会导致发号重复

## 10.3 使用雪花算法

前提：使用雪花算法生成ID的字段被auto_increment标注，或字段为主键字段

- 在插入数据时，不再指定该字段，也不给该字段赋值(以主键字段为例)

  ```sql
  #不再指定Id字段，也不给Id字段赋值，MyCat会自动给主键ID赋值
  insert into
  	orders(order_type, customer_id, amount)
  values
  	(103, 102, 102102);
  ```

  
