# 1. SpringBoot集成Memcached

## 1.1 Memcached介绍

> ​	Memcached 是一个高性能的**分布式内存对象缓存系统**，用于减轻数据库负载。Memcached基于键/值对的hashmap存储缓存

## 1.2 整合Memcached

### 1.2.1 pom依赖

```xml
<dependency>
    <groupId>net.spy</groupId>
    <artifactId>spymemcached</artifactId>
    <version>2.12.2</version>
</dependency>
```

### 1.2.2 配置文件

```properties
memcache.ip=127.0.0.1
memcache.port=11211
```

### 1.2.3 设置配置对象

​	`@ConfigurationProperties(prefix = "memcache")` ：以 `memcache.*` 为开头将对配置文件中对应的属性加载进来

```java
package org.example.memcached.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "memcache")
public class MemcachedSource {
    private String ip;

    private int port;
}
```

### 1.2.4 初始化 MemcachedClient

> ​	CommandLineRunner是Spring Boot提供的一个接口，用于在应用启动后执行一些特定的代码，实现该接口的类会在Spring容器初始化完成后被调用，通常用于执行一些初始化逻辑，例如加载数据、配置资源或执行任务

利用 `CommandLineRunner` 在项目启动的时候配置好 `MemcachedClient `

```java
package org.example.memcached.config;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;

@Component
public class MemcachedRunner implements CommandLineRunner {
    // 通过LoggerFactory创建日志记录器Logger
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // MemcacheSource是一个配置类，负责读取application.properties 文件中的 memcache.ip 和 memcache.port 配置信息
    @Resource
    private MemcacheSource memcacheSource;

    //  MemcachedClient对象用于存储Memcached 客户端实例，字段可以用于与 Memcached 服务器进行交互
    private MemcachedClient client = null;

    // 重写run方法，该方法会在SpringBoot应用启动完成后自动执行
    @Override
    public void run(String... args) throws Exception {
        try {
            // new MemcachedClient：创建 MemcachedClient 实例；
            // new InetSocketAddress(memcacheSource.getIp(), memcacheSource.getPort())：InetSocketAddress类用于表示IP地址和端口号的组合
            client = new MemcachedClient(new InetSocketAddress(memcacheSource.getIp(), memcacheSource.getPort()));
        } catch (IOException e) {
            logger.error("inint MemcachedClient failed ", e);
        }
    }

    public MemcachedClient getClient() {
        return client;
    }
}
```

### 1.2.5 启动类

```java
package org.example.memcached;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemcachedApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemcachedApplication.class, args);
    }

}
```

### 1.2.6 测试

```java
package org.example.memcached.repository;

import org.example.memcached.config.MemcachedRunner;
import net.spy.memcached.MemcachedClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

// 指定使用SpringRunner作为测试运行器，集成了Spring TestContext框架，支持Spring Boot的测试功能
@RunWith(SpringRunner.class)
// 表示这是一个Spring Boot测试类，会加载Spring应用上下文
@SpringBootTest
public class RepositoryTests {

    @Resource
    private MemcachedRunner memcachedRunner;

    @Test
    public void testSetGet() {
        // 获取已初始化的 MemcachedClient
        MemcachedClient memcachedClient = memcachedRunner.getClient();
        
        // 添加缓存数据：key为testkey，过期时间1000秒，value为666666
        memcachedClient.set("testkey", 1000, "666666");
        
        // 读取缓存数据：key为testkey
        System.out.println(memcachedClient.get("testkey").toString());
    }
}
```

