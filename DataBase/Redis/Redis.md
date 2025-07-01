# 1.Redis的五大基本数据结构

​	Redis本身是一个Map，其中所有的数据都采用key:value的形式存储，即key永远是String，Reids中的数据结构指的是value的数据类型

## 1.1 字符串

​	Redis是使用C语言开发的，所以Redis的String类型实际更像是一个ArrayList列表，其内存分配机制为

当长度小于1MB时，加倍扩容；反之每次扩容1MB。最大长度为512MB

```c
// Redis中字符串的数据结构(使用泛型是因为Redis会根据长度不同，采取不同的数据类型)
struct SDS{
	T capacity;     //数组容量
    T len;          //实际长度
    byte flages;    //标志位，第三位表示类型
    byte[] content; //数组内容
}
```

​	**String的应用**

1. 值缓存：将一些经常访问但不经常更改的数据存储在Redis中，以提高系统的性能和响应速度
2. 计数器(如访问量统计)：使用set初始化计数器的值为0，每次访问时使用incr命令将计数器的值自增1，使用get命令获取计数器的值
3. 分布式系统全局ID：定义一个Redis(数值)键记录当前ID，使用incrby命令一次拿取多个ID作为备用(例如1000个)，每次执行写入操作时，就使用服务内部的备用ID，并使备用ID减一，当备用ID用完时，再向Redis拿取备用ID
4. 分布式session：核心思想是将用户的会话数据存储在Redis中，而不是存储在单个应用服务器内存中，从而实现会话数据的共享和高可用

​	**Redis中的字符串与C语言中的字符串之间的主要区别**

1. Redis的字符串长度是：字符串长度 + 未分配空间长度；C的字符串长度是：字符串长度 + 1
2. Redis记录自身长度，所以len的复杂度是O(1)；C不记录自身长度，所以len的复杂度是O(n)
3. Redis记录自身长，所以在插入新字符之前会检查是否会造成缓冲区溢出；C不记录自身长度，容易造成缓冲区溢出
4. Redis数组的长度不一定是字符串的数量，所以在修改字符串时不一定要重新分配内存；C由于没有空闲空间所以每次字符串在修改时都要重新分配内存空间
5. Redis的字符串是二进制安全的，也就是说Redis可以存储空字符串，中文等；C字符串不可以
6. Redis只兼容部分C的字符串函数；C兼容所有的C字符串函数

- 创建

  ```c
  set key value
  
  // 创建键值对，并设置过期时间
  setex key 秒数 value
      
  // 仅当该键不存在时创建，反之不做操作
  setnx key value
  ```

- 删

  ```c
  del key
      
  // 删全部
  flushall
  ```

- 查

  ```c
  get key
  ```

- 检查是否存在

  ```c
  exists key
  ```

- 设置过期时间

  ```c
  expire key 秒数
  ```

- 存储数值型数据时

  ```c
  // 自增1(key的value应为数值型，以下同理)
  incr key
  // 自增X
  incrby key X
      
  // 自减1
  decr key
  // 自减X
  decrby key X
  ```

## 1.2 列表

​	**Redis的列表又叫quicklist，是一个双向的链表(linkedlist)，每个节点是一个ziplist**

1. linkedlist：存储头，尾节点和长度等信息其内存空间不连续，每个节点存储指向上一个节点和下一个节点的指针以及数据
2. ziplist：是一种为了节省内存而开发的一种压缩列表的数据结构(时间换空间)，内存空间连续
3. 如果ziplist中的节点过少，极端情况下只有1时，此时quicklist退化成普通的linkedlist
4. 如果ziplist中的节点过多，因为ziplist本身就是以时间换空间，所以会影响列表对象的读写性能

​	**Redis中的下标**：其中0表示第一个元素，1表示第二个元素，以此类推；-1表示最后一个元素，-2表示倒数第二个元素，以此类推

​	**列表的应用**

1. 时间轴：有人发布信息，用lpush加入时间轴，展示新的列表信息

**有序**

- 创建

  ```c
  // 头部插入
  lpush 列表名 元素1 元素2...
      
  // 尾部插入
  rpush 列表名 元素1 元素2...
  ```

- 删除

  ```c
  // 头部删除
  lpop 列表名 [删除的元素个数, 缺省为1个]
      
  // 尾部删除
  rppop 列表名 [删除的元素个数, 缺省为1个]
      
  // 删除指定范围(闭区间)外(起始索引是0，-1表示到最后一个元素)
  ltrim 列表名 起始位置 结束位置
  ```

- 查找

  ```c
  lrange 列表名 起始位置 结束位置
  ```

- 长度

  ```c
  llen 列表名
  ```

## 1.3 集合

​	**Redis的Set是String类型的无序集合，重复添加会被忽略**

1. 由于是哈希表实现，所以添加、删除、查找的复杂度都是O(1)
2. 最大成员数是：2^32 - 1
3. 支持集合间的操作：交、并、差集

​	**集合的应用**

1. 标签：给用户、消息添加标签，将与用户有相同标签的信息推荐给用户
2. 点赞、点踩、收藏等，可以放到set中实现

- 创建

  ```c
  sadd 集合名 元素1 元素2...
  ```

- 删除

  ```c
  srem 集合名 元素
  ```

- 查找

  ```c
  smembers 集合名
  ```

- 是否存在

  ```c
  sismember 集合名 元素1 元素2...
  ```

## 1.4 有序集合

​	Redis的SortedSet是通过在Set的基础上为每个元素添加score属性来排序的，底层有ziplist和skiplist两种数据结构支持

1. 当元素数小于128个且所有元素的长度小于64字节时，使用ziplist；反之使用skiplist
2. skiplist：为每个节点随机生成一个层数，使得新插入的节点不会影响其他节点的层数，降低了插入的复杂度，同时查找由于多层链表的存在也更加高效

<img src="https://github.com/YOIOc/learning-record/blob/main/image/skiplist1.png" alt="skiplist1" style="zoom:80%;" />

<img src="https://github.com/YOIOc/learning-record/blob/main/image/skiplist2.png" alt="skiplist2" style="zoom:80%;" />

​	**有序集合的应用**

1. 排行榜：有序集合经典使用场景。例如小说视频等网站需要对用户上传的小说视频做排行榜，榜单可以按照用户关注数，更新时间，字数等打分，做排行

- 创建

  ```c
  zadd 集合名 元素1分数 元素1 元素2分数 元素2...
  ```

- 删除

  ```c
  zrem 集合名 元素名
  ```

- 查找

  ```c
  // 查看集合内元素
  zrange 集合名 起始位置 结束位置 [withscores-同时输出分数]
      
  // 查看元素的分数
  zscore 集合名 元素名
      
  // 查看元素下标(从小到大)
  zrank 集合名 元素名
      
  // 查看元素下标(从大到小)
  zrerank 集合名 元素名
  ```


## 1.5 哈希表

​	**Redis的Hash是一个String类型的field(字段)和value(值)的映射表，Hash特别适合用于存储对象**

1. 最对可以存储 2^32 -1 个键值对
2. 底层使用ziplist / hashtable实现，当所有键，值长度加起来小于64字节且键值对数小于512个时，使用ziplist实现，反之使用hashtable实现

​		**哈希表的应用**

1. 缓存：更直观，相比String更节省空间，也能维护缓存信息如：用户信息，视频信息等

- 创建

  ```c
  hset key 属性名 属性值
  ```

- 删除

  ```c
  hdel key 属性名
  ```

- 查找

  ```c
  hget key 属性名
      
  // 查所有属性
  hgatall key
      
  // 查所有属性名
  hkeys 属性名
      
  // 查键值对数量
  hlen key
  ```

- 是否存在

  ```c
  hexists key 属性名
  ```

# 2.Redis中的复杂数据结构

## 2.1 地理空间 Geospatial

- 添加地理位置

  ```c
  geoadd [地理位置信息的名字] [经度] [纬度] [地理位置的名字]
  ```

- 查看地理位置

  ```c
  // 查看地理位置的经纬度
  geopos [地理位置信息的名字] [地理位置的名字]
      
  // 查看两地之间的距离(默认单位是m，若要km显示则最后添加"KM")
  geodist [地理位置信息的名字] [地理位置1的名字] [地理位置2的名字]
      
  // 查看范围之内有哪些城市(默认单位是m，若要km显示则最后添加"KM")
  geosearch [地理位置信息的名字] frommember [中心地理位置的名字] byradius [距离半径]
  ```

## 2.2 HyperLogLog

- 基数：集合中不重复元素的个数
- 个数：集合中元素的个数

基数统计的应用场景

1. 统计某个词的搜索次数
2. 统计在线用户数

- 添加指定元素到HyperLogLog中

  ```c
  pfadd key 元素 [元素...]
  ```

- 返回给定HyperLogLog的基数估算值

  ```c
  pfcount key
  ```

- 合并多个HyperLogLog

  ```c
  pfmerge 合并后的key名称 key1 [key2...]
  ```

## 2.3 位图Bitmap

​	位图是字符串类型的扩展，可以理解为使用String类型模拟Bit数组，数组的下标就是偏移量，值只有零和一，支持位运算

- 添加

  ```c
  setbit key 偏移量 值
  ```

- 获取

  ```c
  getbit key 偏移量
  ```

- 统计

  ```c
  // 某个key中有多少位Bit是1
  bitcount key
      
  // 某个key中第一个出现0或1的位置
  bitpos key 0/1 [start [end]]
  ```

## 2.4 位域 Bitfield

​	位域能够将很多小的整数存储到一个较大的位图中(这样可以更好地利用内存)

- 设置指定偏移处的值

  ```c
  // bitfifld mybitfifld set u8 0 1：表示创建一个名为mybitfifld的位图，其中第0位设置一个无符号长度为8位的整数，值位1
  bitfifld  key set <type> 偏移量 value
  ```

- 获取指定偏移处的值

  ```c
  bitfifld key get <type> 偏移量
  ```

- 将指定偏移处的值增加/减少指定值

  ```c
  bitfifld key incrby <type> 偏移量 value
  ```

## 2.5 消息队列 Stream

- 向消息队列中添加消息

  ```c
  // 时间戳-序列号：该部分作为消息的ID，其中时间戳和序列号都要为整数，也可用'*'代替自动生成序列号
  xadd 消息队列名 时间戳-序列号 key value
  ```

- 查看消息队列

  ```c
  // 查看队列中的消息条数
  xlen 消息队列名
      
  // 查看队列中的内容(- +表示：队列消息ID最小到最大 = 所有消息)
  xrange 消息队列名 - +
      
  // 获取队列中的消息
      // 等待时间：当队列中的消息数<获取的消息数量时，触发等待时间单位是毫秒
      // 读取位置：通常使用0，表示从头开始读取；>表示最新消息
  xread count [获取数量] block [等待时间] streams 消息队列名 [读取位置]
  ```

- 删除消息队列中的消息

  ```c
  xdel 消息队列名 消息ID
      
  // 删除队列中的所有消息
  xtrim 消息队列名 maxlen 0
  ```

- 消费者组

  ```c
  // 创建消费者组(0表示从头读；$表示从尾读)
  xgroup create 消息队列名 消费者组名称 消费者组的读取位置
      
  // 查看队列中的消费者组
  xinfo groups 消息队列名
      
  // 向消费者组中添加消费者
  xgroup createconsumer 消息队列名 消费者组名称 消费者名称
      
  // 消费者组获取消息
  xreadgroup group 消费者组名称 消费者名称 count [获取数量] block [等待时间] streams 消息队列名 [读取位置]
  ```

# 3.Redis在Spring Boot中的使用

## 3.1 创建一个依赖Redis的SpringBoot工程

- 启动Redis服务 - **redis-server**

  ```shell
  #-v /home/redis/data:/data：将Redis存储的文件挂载到主机/home/redis/data文件下
  #-v /home/redis/conf/redis.conf:/etc/redis/redis.conf：将Redis的配置文件挂载到主机/home/redis/conf/redis.conf下
  #-d redis:bullseye：后台启动本地镜像版本的redis
  #redis-server /etc/redis/redis.conf：设置redis-server每次使用/etc/redis/redis.conf这个配置
  #--appendonly 打开Redis持久化配置
  docker run --name redis -p 6379:6379 -v /home/redis/data:/data -v /home/redis/conf/redis.conf:/etc/redis/redis.conf -d redis:bullseye redis-server /etc/redis/redis.conf --appendonly yes
  ```

![添加redis服务器](https://github.com/YOIOc/learning-record/blob/main/image/添加redis服务器.png) f

![连接redis服务器](https://github.com/YOIOc/learning-record/blob/main/image/连接redis服务器.png)

- 创建Spring Boot项目

<img src="https://github.com/YOIOc/learning-record/blob/main/image/SpringBoot整合redis.png" alt="SpringBoot整合redis" style="zoom:50%;" />

- 添加项目所需依赖

<img src="https://github.com/YOIOc/learning-record/blob/main/image/SpringBoot整合Redis2.png" alt="SpringBoot整合Redis2" style="zoom:50%;" />

- 创建实体类（创建bean子包，在子包下创建实体类）

<img src="https://github.com/YOIOc/learning-record/blob/main/image/SpringBoot整合redis3.png" alt="SpringBoot整合redis3" style="zoom: 107%;" />

- 创建仓库接口（创建repository子包，在子包下创建PersonRepository接口）

![SpringBoot整合Redis4](https://github.com/YOIOc/learning-record/blob/main/image/SpringBoot整合Redis4.png)

- 在全局配置文件中配置Redis属性

![配置Redis的配置信息](https://github.com/YOIOc/learning-record/blob/main/image/配置Redis的配置信息.png)

- 测试类中编写测试方法

| 框架已实现的操作方法                                         |
| ------------------------------------------------------------ |
| save(T t)                                                              -----→                     T |
| saveAll(Iterable\<T> iterable)                           -----→                     Iterable\<T> |
| delete(T t)                                                           -----→                     void |
| deleteAll()                                                           -----→                     void |
| deleteById(String id)                                         -----→                     void |
| findAll()                                                               -----→                     Iterable\<T> |
| findById(String id)                                             -----→                     Optional\<T> |
| findAllById(Iterable\<String> iterable)            -----→                     Iterable\<T> |
| count()                                                                 -----→                     long |
| existsById(String id)                                          -----→                     boolean |

  **自定义个性化查找方法**

![自定义个性化查找方法](https://github.com/YOIOc/learning-record/blob/main/image/自定义个性化查找方法.png)

```java
// 以'findByLastName'为例
public void testFindPersonByLastName(){
    // 创建分页器
    Sort.Direction sort = Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(0, 2, sort, "id");

    // 获取页面
    Page<Person> page = personRepository.findPersonByLastName("张", pageable);

    // 输出页面内容
    for (Person person : page.getContent()){
        System.out.println(person);
    }
}

// 以'findByAddress_City'为例
public void testFindByAddress_City() {
    List<Person> persons = personRepository.findByAddress_City("上海");
    for (Person person : persons) {
        System.out.println(person);
    }
}
```

## 3.2 Spring中Redis的核心工具RedisTemplate的常见方法

### 3.1.1RedisTemplate的介绍

> ​	RedisTemplate是Spring Data Redis提供的核心工具类，用于简化Redis的交互操作，它封装了底层连接管理、数据序列化、异常处理等细节，支持多种数据结构的操作，并于Spring生态无缝集成

### 3.1.2使用前提
- 导入相关依赖(Redisson)

  ```xml
  <!--Redisson是一个基于Redis的Java客户端，提供了分布式锁的实现--> 
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  ```

- 配置Redisson

  ```properties
  spring.redis.port=<redis-port>
  spring.redis.host=<redis-host>
  ```

### 3.1.3操作不同数据结构时的用法

- 字符串

  ```c++
  // 写入
  redisTemplate.opsForValue().set("user:1", "Alice");
  redisTemplate.opsForValue().setIFAbsent("lock:ProductId:001", "线程UUID") // key不存在时，才创建键值对
  redisTemplate.opsForValue().set("num", 100, Duration.ofMinutes(10)); // 设置过期时间
  
  // 读取
  String value = (String) redisTemplate.opsForValue().get("user:1");
  Integer num = (Integer) redisTemplate.opsForValue().get("num");
  ```

- 列表

  ```c++
  // 左侧插入
  redisTemplate.opsForList().leftPush("tasks", "tasks1");
  redisTemplate.opsForList().leftPushAll("tasks", "tasks2", "tasks3");
  
  // 右侧弹出(队列消费)
  String task = (String) redisTemplate.opsForList().rightPop("tasks");
  
  // 获取范围元素(闭区间)
  List<Object> firstTwoTasks = redisTemplate.opsForList().range("taskes", 0, 1);
  ```

- 集合

  ```c++
  // 添加
  redisTemplate.opsForSet().add("tages", "java", "redis", "spring");
  
  // 判断元素是否存在
  boolean exists = redisTemplate.opsForSet().isMember("tags", "redis");
  
  // 获取所有成员
  Set<Object> tags = redisTemplate.opsForSet().members("tags");
  ```

- 有序集合

  ```c++
  // 添加元素(带分数)
  redisTemplate.opsForZSet().add("leaderboard", "PlayerA", 100);
  redisTemplate.opsForZSet().add("leaderboard", "PlayerA", 200);
  
  // 按分数范围查询(闭区间)
  Set<Object> topPlayers = redisTemplate.opsForZSet().reverseRange("leaderboard", 0, 2);
  ```

- 哈希

  ```c++
  // 写入单个字段
  redisTemplate.opsForHash().put("user:1000", "name", "Bob");
  redisTemplate.opsForHash().put("user:1000", "age", "25");
  
  // 批量写入
  Map<String, Object> userData = new HashMap<>();
  userData.put("email", "bob@example.com");
  userData.put("city", "New York");
  redisTemplate.opsForHash().putAll("user:1000", userData);
  
  // 读取字段
  String name = (String) redisTemplate.opsForHash().get("user:1000", "name");
  Map<Object, Object> allFields = redisTemplate.opsForHash().entries("user:1000");
  ```

# 4.Redis消息队列

> ​	消息队列是指一种用于在分布式系统中实现异步通信的机制，它通过消息生产者和消息消费者之间引入一个队列，解耦了两者的直接依赖，从而提高系统的可扩展性，可靠性和灵活性

​	在Redis中通常有三种方法实现消息队列

- List数据结构
- 发布订阅模式
- stream数据结构

## 4.1基于List实现

- **实现逻辑**

  ​	定义一个List列表，生产者通过lpush命令向列表左侧添加消息，消费者通过brpop命令从列表右侧获取消息

  ```c++
  生产者：lpush 列表名 消息内容
  消费者：brpop 列表名 阻塞时间
  ```

- **为什么消费消息使用brpop(阻塞右侧弹出)而不是rpop(右侧弹出)？**

  ​        由于消费者要时刻关注消息队列中是否有新的消息进入，那么此时消费者就要循环执行rpop(右侧弹出)命令，当没有新消息时，Redis返回nil；有新消息则获取。这种处理方式势必会增加服务器负担，所以我们选择使用brpop(阻塞右侧弹出)，当没有新消息时，阻塞消费者直到有新的消息被推入列表，或达到超时时间；有新消息则获取

- **适用场景**

  ​        多个消费者竞争的去抢同一个任务，不支持消费组的实现（即当多个消费者同时阻塞想要获取消息时，生产者向队列推入消息后，只有一个消费者会拿到消息）

## 4.2基于发布订阅实现

- **什么是发布订阅模式**

  ​	发布订阅模式是一种消息传递模式，订阅者监听自己喜欢的频道，发布者把消息发送到频道上，一旦消息发送成功，所有订阅者都将收到消息

- **实现逻辑**

  ​        线程1订阅频道，线程2向这个频道publist数据，一旦他publist到这个频道上面，那么线程1就会收到这个消息(支持多个线程订阅同一频道)

  ```c++
  生产者：publist 频道名 消息内容
  消费者：subscribe 频道名
  ```

- **缺陷**

  ​        基于发布订阅模式实现的消息队列没有存储功能，即如果生产者向频道发布消息后，如果该频道此时没有订阅者，那么发布的该条消息就丢失了

## 4.3基于Stream实现

- **消费者组**

  ​	对于基于Stream实现的消息队列，要先创建消费者组才能消费队列中的消息

  ```c++
  // 创建消费者组
  xgroup creat 消息队列名 消费者组名称 消费者组的读取位置
  ```

- **实现逻辑**

  ​        生产者通过xadd命令向消息队列中添加消息，消费者通过xreadgroup命令加入指定的消费者组，并自动阻塞的取消息队列中的消息(一个消息只会被组内的一个消费者获取)

  ​        如果业务需要一条消息被多个消费者消费，那么此时就要额外创建一个新的消费者组，并执行xreadgroup命令添加消费者，此时消息队列中新加的消息后，不同的消费者组都会读取到这条新消息，从而实现一条消息多个消费者(不同消费组)消费

  ```c++
  // 时间戳-序列号用来保证消息的顺序
  xadd 消息队列名 时间戳-序列号 key value
      
  // 最后的 > 表示从未被消费的消息开始读取
  xreadgroup group 消费者组名称 消费者名称 block <阻塞时间> streams 消息队列名 >
  ```

## 4.4消息确认机制

**实现逻辑**

​	消费者组会维护一个 `last delivered ID` 代表这个消费者组最后一个读过的消息ID，同时还维护一个`pendingList` 表示正在处理的消息ID(还没被确认的消息ID)，基于pendingList我们可以实现消息重试机制，他会从pendingList中查询长时间没有被确认的消息ID(消费者处理完读取到的消息后会像Redis返回ACK命令确认消息，Redis收到ACK后，会将该消息从pendingList中移除)，进行一个消息重新处理

- **消息被即时确认**

​	消息队列中进来了一条001数据，客户端1收到消息后，会将该消费者组中的**last delivered ID和pendingList**属性标记为001，后续消费者组会根据last delivered ID属性来确保读取消息的顺序性，客户端1处理完001数据后，会让Redis确认这条消息(发送ACK)，消息确认后，pendingList中001就会被移除

- **消息没有被即时确认**

​	消息队列中又进来了一条002数据，客户端2收到消息后，将该消费者组中的**last delivered ID和pendingList**属性修改为002，客户端2由于处理时间过长挂掉了，SpringBoot的补偿服务就会定期去该客户端所对应的消费者组中查看pendingList，有没有很久没有被确认的消息，有的话就把这条消息取出来处理，处理完成后再要求Redis确认该条消息，后面挂掉的服务恢复后重新处理002这条消息，处理完成后客户端2又叫Redis去帮他确认这条消息，但是这条消息已经被确认了，所以这条消息属于被重复确认了，但是问题不大，我们的业务一定会做一个幂等处理(在分布式系统中用来确保统一操作被多次执行后，结构仍是一样的)

# 5.事务

​	使用Redis事务的两个特性

1. 所有任务按顺序执行，并且不会被其他客户端发送来的命令请求打断
2. Redis的事务是要么全部执行要么全部不执行，而不是要么全部成功要么全部不成功(是否执行与是否执行成功是两个概念)

- 开启事务

  ```c
  multi
  ```

- 提交事务

  ```c
  exec
  ```

# 6.持久化

​	Redis持久化的两种方式

1. RDB：在指定时间间隔内，将内存中的数据快照写入磁盘
2. AOF：Redis在执行命令时，同时还会将命令写入到一个追加文件(AOF文件)中，以日志的形式记录每一个操作，当Redis重启时，就会重新执行AOF文件中的命令，将数据持久化

- RDB：当服务器宕机后，宕机后的所有数据都将丢失，因此该持久化方式更适用于每天凌晨执行数据备份

  自动：通过配置文件中的save参数配置

  ```c
  // save 3600 1：表示每1小时内，有1次修改，就执行一次快照
  save 秒数 修改次数
  ```

  手动：手动输入命令

  ```c
  // bgsave相较于save更加安全，原因是使用save持久化时，Redis处于阻塞状态不能接受任何请求，而bgsave会单独创建一个子进程来持久化，主进程依然可以处理请求，但在创建子进程时还是不能处理任何请求，且有一定的性能损耗，所以RDB没有办法做到秒级快照
  bgsave
  ```

- AOF：

  自动：通过配置文件中的appendonly参数配置

  ```c
  appendonly yes/no
  ```

# 7.主从复制

环境搭建：

- 创建两个redis实例

```shell
# 创建redis主节点
docker run --name redis_M -p 6379:6379 -v /home/redis/data:/data -v /home/redis/conf/redis.conf:/etc/redis/redis.conf -d redis:bullseye redis-server /etc/redis/redis.conf --appendonly yes

# 创建redis从节点
docker run --name redis_S -p 6380:6379 -v /home/redis/data:/data -v /home/redis/conf/redis.conf:/etc/redis/redis.conf -d redis:bullseye redis-server /etc/redis/redis.conf --appendonly yes
```

配置步骤

- 主节点

  - 不需要任何配置，因为默认配置就是主节点

- 从节点

  - 连接主节点

    ```c
    // 进入从节点终端
    docker exec -it redis_S bash
    
    // 设置主节点IP和port
    replicaof 主节点IP 主节点port
    ```

  - 查看从节点的主从状态

    ```c
    // 查看Relication：
    		// role:slave
        	// master_host:127.0.0.1
        	// master_port:6379
    info replication
        
    // 退出从节点
    exit
    ```

  - 查看主节点的主从状态

    ```c
    // 进入主节点终端
    docker exec -it redis_M bash
        
    // 查看Relication：
    		// role:master
        	// connected_slave:1
    info replication
        
    // 退出主节点
    exit
    ```

# 8.哨兵模式

><p>哨兵是一个独立的进程，运行在redis集群中，用于监控Redis集群中的各个节点是否运行正常</p>

**哨兵的功能**

- 监控：通过不断地发送命令，来检查Redis节点是否正常
- 通知：如果发现某个节点出现问题，哨兵就会通过发布订阅模式，来通知其他节点
- 自动故障转移：当主节点不能正常工作时，哨兵会开始自动故障转移，将一个从节点升级为新的主节点，其他从节点重新指向新的主节点

**如何启动哨兵模式**

- 创建哨兵的配置文件

  ```shell
  # 到根目录下
  cd ~
  
  # 创建配置文件
  vim sentinel.conf
  
  # 配置文件中添加如下内容
  	#sentinel monitor：哨兵配置文件的固定格式
  	#master：监控的主节点名称(自己定义)
  	#127.0.0.1 6379：主节点的IP和port
  	#1：只需要一个哨兵节点同意就可以进行故障转移
  sentinel monitor master 127.0.0.1 6379 1
  ```

- 启动哨兵

  ```shell
  #redis-sentinel命令后为哨兵配置文件的路径
  redis-sentinel sentinel.conf
  ```
