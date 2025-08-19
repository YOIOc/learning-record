# 1. 什么是微服务

> ​	微服务是一种软件架构风格，它是以专注于单一职责的很多小型项目为基础，组合出复杂的大型应用
>
> ​	SpringCloud集成了各种微服务功能组件，并基于SpringBoot实现了这些组件的自动装配，从而提供了良好的开箱即用体验

# 2. 微服务拆分

## 2.1 拆分服务

1. 创建一个新的微服务模块

   ![新建模块](D:\A\image\新建模块.png)

2. 将hm-service中与商品管理相关依赖、配置文件、业务代码拷贝到新建的模块中（其他模块拆分类似）

   ![项目拆分-item](D:\A\image\项目拆分-item.png)

## 2.2 微服务之间的远程调用

> Spring给我们提供了一个RestTemplate工具，可以方便的实现Http请求的发送--以解决某一服务需要其他模块支持的情况

1. 注入RestTemplate到Spring容器

   ```java
   @Bean
   public RestTemplate restTemplate(){
       return new RestTemplate();
   }
   ```

1. 发起远程调用

   ```java
   public <T> ResponseEntity<T> exchange(
   	String url, // 请求路径
       HttpMethod method, //请求方式
       @Nullable HttpEntity<?> requestEntity, //请求实体，可以为空
       Class<T> responseType, // 返回值类型
       Map<String, ?> uriVariables // 请求参数
   )
   ```

**代码实现** -- 以查询购物车为例，方法需要知道购物车中商品的最新价格，因此需要商品模块支持

![远程调用1](D:\A\image\远程调用1.png)

![远程调用2](D:\A\image\远程调用2.png)

# 3. Nacos

## **未使用Nacos所面临的问题**

​	在未使用Nacos前，微服务间的远程调用需要在代码中写死远程调用的URL来访问接口。而在实际开发中，每个微服务通常会多实例部署，此时将某一个实例的URL写死在代码中，就失去了微服务多实例部署的意义，这时候就需要使用注册中心的思想来解决上述问题了

## 3.1 注册中心原理

- 服务提供者（上个例子中的item-service）：服务提供者在注册中心中注册服务信息

  ​	注册中心中同一服务的提供者可能有多个，所有提供者都需要定期向注册中心发送心跳，以告知自己处于正常运行状态，若某一提供者没有心跳续约，那么注册中心就会删除该提供者的信息，并告知订阅了该服务的服务调用者

- 服务调用者（上个例子中的cart-service）：向注册中心订阅并获取所需服务的服务信息

  ​	服务调用者采用负载均衡算法，远程调用某一服务提供者

## 3.2 Nacos注册中心

- **服务注册** - 提供服务

  引入nacos discovery依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

​	配置Nacos地址

```yaml
spring:
  application:
    name: item-service #服务名称
  cloud:
    nacos:
      server-addr: 8.156.77.246:8848 #nacos地址
```

- **服务发现** - 调用服务

  > 消费者需要连接nacos以拉取和订阅服务，因此服务发现的前两步与服务注册是一样的，后面再加上服务调用即可

​	引用nacos discovery依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

​	配置nacos地址

```yaml
spring:
  application:
    name: item-service #服务名称
  cloud:
    nacos:
      server-addr: 8.156.77.246:8848 #nacos地址
```

​	服务发现

```java
private final DiscoveryClient discoveryClient;

private void handleCartItems(List<CartVO> vos){
    // 1.根据服务名称，拉取服务的实例列表
    List<ServiceInstance> instances = discoveryClient.getInstances("item-service");
    // 2.负载均衡，挑选一个实例
    ServiceInstance instance = instances.get(RandomUtil.randomInt(instances.size()));
    // 3.获取实例的IP和端口
    URI uri = instance.getUri();
    // ...略
}
```

# 4. OpenFeign

> Openfeign是一个声明式的HTTP客户端，是SpringCloud在Eureka公司开源的Feign基础上改造而来，其作用就是基于SpringMVC的常见注解，帮我们优雅的实现http请求的发送

## **未使用OpenFeign所面临的问题**

​	在未使用OpenFeign之前，远程调用微服务接口需要编写繁琐的代码：

1. 通过指定调用的服务名称，发现Nacos注册中心中需要调用的服务的列表

2. 通过负载均衡算法，挑选一个服务列表中的实例

3. 获取服务实例的URL，通过SpringCloud提供的restTemplate发送Http请求，调用方法

   而在使用了OpenFeign后，可以使用OpenFeign提供的注解并通过编写FeignClient的方式简化代码，简化后后端在远程调用时，只需要调用FeignClient即可

## 4.1 使用流程

- **创建公共的API模块**：该模块下编写各种用于提供服务的Feign客户端，服务调用者通过导入该模块依赖，即可使用其中的各种服务

  公共模块引入Openfeign、负载均衡依赖提供远程服务支持，当服务调用者导入该模块后，也就同样引入了这两个依赖

  ```xml
  <!--Openfeign-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
  
  <!--负载均衡-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-loadbalancer</artifactId>
  </dependency>
  ```

  ![Feign的最佳实践1](D:\A\image\Feign的最佳实践1.png)

  在公共api模块的client包下，编写需要向其他模块提供的服务接口

  ![Feign最佳实践2](D:\A\image\Feign最佳实践2.png)

- **服务提供者模块**：引入nacos依赖，向nacos注册中心注册服务

  pom文件引入nacos依赖

  ```xml
  <!--nacos 服务注册发现-->
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
  </dependency>
  ```

  Application文件配置nacos连接

  ```yaml
  spring:
    cloud:
      nacos:
        server-addr: 8.156.77.246:8848
  ```

- **服务调用者模块**：引入nacos、公共api模块、OKHttp依赖来使用远程服务

  pom文件引入所需依赖

  ```xml
  <!--nacos 服务注册发现-->
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
  </dependency>
  
  <!--hm-api-->
  <dependency>
      <groupId>com.heima</groupId>
      <artifactId>hm-api</artifactId>
      <version>1.0.0</version>
  </dependency>
  ```

  Application文件配置nacos连接

  ```yaml
  spring:
    cloud:
      nacos:
        server-addr: 8.156.77.246:8848
  ```

  调用者模块启动项使用@EnableFeignClients依赖，来启用OpenFeign功能，并指定调用的客户端的所属包名以便Spring可以扫描到客户端Bean

  ```java
  @EnableFeignClients(basePackages = "com.hmall.api.client")
  ```

  ![Feign的最佳实践3](D:\A\image\Feign的最佳实践3.png)

  注入需要使用的Feign客户端，在需要的位置调用客户端提供的方法

  ![Feign的最佳实践4](D:\A\image\Feign的最佳实践4.png)

## 4.2 优化-连接池

>  OpenFeign对Http请求做了优雅的伪装，不过其底层发起http请求依赖其他的框架，这些框架可以自己选择，包括以下三招那个
>
>  - HttpURLConnection：默认实现，不支持连接池
>  - Apache HttpClient：支持连接池
>  - OKHttp：支持连接池

**OpenFeign整合OKHttp**

- 服务调用者引入依赖

  ```xml
  <!--ok-http-->
  <dependency>
      <groupId>io.github.openfeign</groupId>
      <artifactId>feign-okhttp</artifactId>
  </dependency>
  ```

- 开启连接池功能

  ```yaml
  feign:
    okhttp:
      enable: true #开启OKHttp连接池支持
  ```

  ![使用OKHttp连接池](D:\A\image\使用OKHttp连接池.png)

## 4.3 优化 - 记录日志

- 要输出Feign日志，需要声明一个类型为Logger.Level的Bean，在其中定义日志级别

  ![配置Feign的日志输出级别1](D:\A\image\配置Feign的日志输出级别1.png)

  ```java
  public class DefaultFeignConfig {
      @Bean
      public Logger.Level feignLogLevel(){
          return Logger.Level.Full;
      }
  }
  ```

- 此时这个Bean并未生效，要想配置某个FeignClient的日志，可以在@FeignClient注解中声明

  ```java
  @FeignClient(value = "item-service", configuration = DefaultFeignConfig.class)
  ```

  如果想要全局配置，让所有FeignClient都按照这个日志配置，则需要在@EnableFeignClients注解中声明

  ```java
  @EnableFeignClients(defaultConfiguration = DefaultFeignConfig.class)
  ```

  ![配置Feign的日志输出级别2](D:\A\image\配置Feign的日志输出级别2.png)

# 5. Ribbon - 负载均衡

>   Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡工具

## 5.1 负载均衡

>   负载均衡是从多个服务中根据某个策略选择一个进行访问，常见的负载均衡分两种
>
> - **客户端负载均衡**：在客户端就进行负载均衡算法分配，客户端会有一个服务器地址列表，在发送请求前通过负载均衡算法选择一个服务器，然后进行访问
> - **服务端负载均衡**：在消费者和服务提供方中间使用独立的代理方式进行负载，先发送请求，然后通过Nginx的负载均衡算法，在多个服务器之间选择一个进行访问

## 5.2 使用Ribbon

1. 由于spring-cloud体系下的大多数产品都整合了Ribbon，如nacos、feign等，因此在使用Ribbon时可以不再引入Ribbon依赖

2. 使用Ribbon时只需要添加@LoadBalanced注解即可，代表当前请求拥有了负载均衡的能力

   1. 为RestTemplate添加@LoadBalanced注解

      ```java
      @Configuration
      public class RestConfig {
          @Bean
          @LoadBalanced
          public RestTemplate restTemplate() {
              return new RestTemplate();
          }
      }
      ```

   2. 使用RestTemplate进行远程调用，此次调用有负载均衡效果

      ```java
      @Autowired
      private RestTemplate restTemplate;
      
      @RequestMapping(value = "/findOrderByUserId/{id}")
      public R findOrderByUserId(@PathVariable("id") Integer id) {
      
          String url = "http://order/findOrderByUserId/"+id;
          R result = restTemplate.getForObject(url,R.class);
      
          return result;
      }
      ```

# 6. 网关

>  网关：就是网络关口，负责请求的路由、转发、身份校验

## 未使用网关所面临的问题

​	在一个完整微服务项目中，每个微服务的ip和port可能都不相同，因此前端在进行页面跳转时可能不知道该向哪里发送请求，而且某些服务需要登录身份验证，如果在每个微服务中都编写验证逻辑，显然会出现代码重复等问题，因此我们需要一个角色，来统一管理资源的跳转与身份校验

![什么是网关](D:\A\image\什么是网关.png)

## 6.1 路由、转发

1. 创建新模块

   ![搭建网关1](D:\A\image\搭建网关1.png)

2. 引入网关依赖

   ```xml
   <dependencies>
       <!--网关-->
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-gateway</artifactId>
       </dependency>
       <!--nacos discovery-->
       <dependency>
           <groupId>com.alibaba.cloud</groupId>
           <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
       </dependency>
       <!--负载均衡-->
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-loadbalancer</artifactId>
       </dependency>
   </dependencies>
   ```

3. 编写启动类

   ![搭建网关2](D:\A\image\搭建网关2.png)

4. 配置路由规则

   ```yaml
   spring:
     cloud:
       gateway:
         routes:
           - id: item-service              # 路由规则id，自定义，唯一
             uri: lb://item-service        # 路由目标微服务，lb代表负载均衡
             predicates:                   # 路由断言，判断请求是否符合规则，符合则路由到目标
               - Path=/items/**,/search/** # 以请求路径做判断，以/items、/search开头则符合
   ```

## 6.2 登陆验证

### **网关实现身份校验的思路**

​	前端通过浏览器携带token发送Http请求，请求到达网关后，网关通过过滤器对token进行JWT身份校验，校验通过放行，反之拦截。当请求通过网关JWT校验后，网关将解析token得到的用户信息保存到请求头，将请求转发到对应的微服务中，微服务的拦截器先获取请求头中的用户信息，再将用户信息保存到ThreadLocal中，方便业务获取用户信息

![网关与微服务之间传递用户信息](D:\A\image\网关与微服务之间传递用户信息.png)

### 6.2.1 身份校验

- 为网关模块添加身份校验所需要的文件

  ![添加登陆校验前需要的必备文件](D:\A\image\添加登陆校验前需要的必备文件.png)

- 网关模块下编写全局过滤器，在过滤器中编写身份校验代码

  ![全局过滤器](D:\A\image\全局过滤器.png)

### 6.2.2 用户信息的传递 网关→微服务

- 在hm-gateway.filter下将用户信息添加到新的请求头中

  ![将网关中的用户信息发送给微服务](D:\A\image\将网关中的用户信息发送给微服务1.png)

- 在hm-common模块的interceptors下编写SpringMVC拦截器，这样微服务只需要引入依赖即可生效，无需重复编写

  ![将网关中的用户信息发送给微服务2](D:\A\image\将网关中的用户信息发送给微服务2.png)

- 在hm-common模块的config下编写SpringMVC的配置类，将自定义的拦截器注册到SpringMC中，并指定只有存在SpringMVC核心依赖(DispatcherServlet.class)的模块才添加该配置类

  ![将网关中的用户信息发送给微服务3](D:\A\image\将网关中的用户信息发送给微服务3.png)

- 在hm-common模块resources.META-INF包下的文件中，添加该配置类

  ![将网关中的用户信息发送给微服务4](D:\A\image\将网关中的用户信息发送给微服务4.png)

### 6.2.3 用户信息的传递 微服务→微服务

- 微服务之间的请求跳转，是通过OpenFeign客户端发送的，而OpenFeign提供了一个拦截器接口，所有由OpenFeign发起的请求都会先调用拦截器处理请求，因此可以在hm-gateway模块config包下的DefaultFeignConfig配置文件中，定义匿名内部类，来将当前线程ThreadLocal中保存的用户信息保存到转发的请求头中

  ![微服务之间用户信息的传递1](D:\A\image\微服务之间用户信息的传递1.png)

- 在服务调用者模块的启动类上用@EnableFeignClients注解的defaultConfiguration属性指定OpenFeign的配置类

  ![微服务之间用户信息的传递2](D:\A\image\微服务之间用户信息的传递2.png)

## 6.3 总结

![微服务登录校验](D:\A\image\微服务登录校验.png)

# 7. 配置管理

## 7.1 目前微服务架构存在的问题

![配置管理](D:\A\image\配置管理.png)

## 7.2 配置共享

### 7.2.1 打开项目Nacos页面，将共享配置添加到Nacos的配置管理中

![配置管理1](D:\A\image\配置管理1.png)

![配置管理2](D:\A\image\配置管理2.png)

### 7.2.2 将共享配置中不一致的配置信息，写到微服务项目自身的application.yaml文件中

![配置管理5](D:\A\image\配置管理5.png)

### 7.2.3 拉取共享配置

​	基于Nacos共享配置后，微服务在启动时会先加载Nacos中的共享配置信息，然后加载微服务自己配置文件中的配置信息，最后启动项目，但由于Nacos的服务地址写在微服务的配置文件中，而微服务启动又要先知道Nacos的服务地址，在这种矛盾情况下，就要使用SpringCloud提供的bootstrap.yam配置文件(引导配置文件)，使用后微服务在启动前会优先读取该配置文件中的内容

![配置管理3](D:\A\image\配置管理3.png)

- 引入配置管理所需依赖

  ```xml
  <!--使服务可以读取nacos配置文件-->
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
  </dependency>
  <!--创建、读取bootstrop文件-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-bootstrap</artifactId>
  </dependency>
  ```

- 编写bootstrap.yaml

  ![配置管理4](D:\A\image\配置管理4.png)

## 7.2 配置热更新

>  配置热更新：当修改配置文件中的配置时，微服务无需重启即可使配置生效

前提条件：

1. nacos中要有一个与微服务名有关的配置文件

   ```
   [spring.application.name-spring.profile.active].[file-extension]
   ```

2. 微服务中要以特定方式读取需要热更新的配置属性

   使用@ConfigurationProperties注解 

   ```java
   @Data
   @ConfigurationProperties(prefix = "hm.cart")
   public class CartProperties {
       private int maxItems;
   }
   ```

# 8. 服务保护

## 当前微服务存在的问题

- 雪崩问题产生的原因是什么
  - 微服务相互调用，服务提供者出现故障或阻塞
  - 服务调用者没有做好异常处理，导致自身故障
  - 调用链中的所有服务级联失败，导致整个集群故障
- 解决问题的思路有哪些
  - **请求限流**：限制访问微服务的请求并发量，避免服务因流量激增出现故障
  - **线程隔离**：也叫舱壁模式，通过限定每个业务能使用的线程数量而将故障业务隔离，避免故障扩散
  - **服务熔断**：由断路器统计请求的异常比例或慢调用比例，如果超出阈值则会熔断该业务，则拦截该接口的请求，熔断期间，所有请求快速失败，全都走fallback逻辑

## 8.1 服务保护技术 - Sentinel

-  什么是Sentinel

  >  Sentinel是阿里巴巴开源的一款微服务流量控制组件

- 安装Sentinel

  1. 下载jar包：https://github.com/alibaba/Sentinel/releases

  2. 运行：将jar包放在任意非中文、不包含特殊字符的目录下，然后在该目录下运行如下命令

     ```bash
     java '-Dserver.port=8090' '-Dcsp.sentinel.dashboard.server=localhost:8090' '-Dproject.name=sentinel-dashboard' '-jar' sentinel-dashboard.jar
     ```

  3. 访问[http://localhost:8090](http://localhost:8080)页面，进入sentinel的控制台，账号密码默认都是：sentinel

- 微服务整合

  1. 引入sentinel依赖

     ```xml
     <!--sentinel-->
     <dependency>
         <groupId>com.alibaba.cloud</groupId>
         <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
     </dependency>
     ```

  2. 配置控制台 - application.yaml

     ```yaml
     spring:
       cloud:
         sentinel:
           transport:
             dashboard: localhost:8090
     ```

- 簇点链路

  >    簇点链路，就是单机调用链路。是一次请求进入服务后经过的每一个被Sentinel监控的资源链。默认Sentinel会监控SpringMVC的每一个Endpoint(http接口)。限流、熔断等都是针对簇点链路中的资源设置的。而资源名默认就是接口的请求路径

- 并发线程数、QPS
  - **QPS：**每秒请求数，即在不断向服务器发送请求的情况下，服务器每秒能够处理的请求数量。
  - **并发线程数**：每秒线程数。例如：允许每秒同时5个并发线程，如果单线程QPS为2，则5个线程的总QPS为10

## 8.2 请求限流

- 在簇点链路后面点击流控按钮，即可对其作限流配置

  ![请求限流1](D:\A\image\请求限流1.png)

  ![请求限流2](D:\A\image\请求限流2.png)

- 限流测试

  ![限流测试2](D:\A\image\限流测试2.png)

  ![限流测试3](D:\A\image\限流测试3.png)

  ![限流测试1](D:\A\image\限流测试1.png)

- 测试结果

  ![限流测试4](D:\A\image\限流测试4.png)

## 8.3 线程隔离

- 在sentinel控制台中，会出现Feign接口的簇点资源，点击后面的流控按钮，即可配置线程隔离

  ![线程隔离1](D:\A\image\线程隔离1.png)

  ![线程隔离2](D:\A\image\线程隔离2.png)

- 隔离测试

  ![隔离测试2](D:\A\image\隔离测试2.png)

  ![隔离测试3](D:\A\image\隔离测试3.png)

  ![隔离测试1](D:\A\image\隔离测试1.png)

- 测试结果

  由于查询购物车，需要调用查询商品信息的业务，所以当查询商品信息业务出现异常(ThreadSleep模拟)时，查询购物车业务就会受到影响，如果对购物车服务不进行线程隔离，那么购物车服务中的其它方法，例如删除购物车中商品等方法，也会受到影响，但如果对购物车服务进行线程隔离，设置服务中不同业务允许的最大线程数，就能保证，虽然查询购物车业务收到影响，但购物车服务中的其他业务不受牵连

## 8.4 服务降级（Fallback）

​	由于资源的远程调用依靠由OpenFeign编写的客户端，那么当调用的客户端出现异常时，我们仅需再编写一套备用返回方案(Fallback)，作为异常时的返回即可

1. 将FeignClient作为Sentinel的簇点资源（将Feign客户端添加到Sentine簇点链路中）

   ```yaml
   feign:
     sentinel:
       enabled: true
   ```

2. 编写Fallback逻辑 - 继承FallbackFactory接口（以ItemClient为例）

   ```java
   @Slf4j
   public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {
       @Override
       public ItemClient create(Throwable cause) {
           return new ItemClient() {
               @Override
               public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
                   log.error("查询商品信息失败", cause);
                   return CollUtils.emptyList();
               }
   
               @Override
               public void deductStock(List<OrderDetailDTO> items) {
                   log.error("扣减商品失败", cause);
                   throw new RuntimeException(cause);
               }
           };
       }
   }
   ```

3. 将定义的ItemClientFactory注册为一个Bean

   ```java
   @Bean
   public ItemClientFallbackFactory itemClientFallbackFactory(){
       return new ItemClientFallbackFactory();
   }
   ```

4. 在ItemClient接口中使用ItemClientFactory

   ```java
   @FeignClient(value = "item-service", fallbackFactory = ItemClientFallbackFactory.class)
   public interface ItemClient {
       @GetMapping("/items")
       List<ItemDTO> queryItemByIds(@RequestParam("ids") Collection<Long> ids);
   
       @PutMapping("/items/stock/deduct")
       void deductStock(@RequestBody List<OrderDetailDTO> items);
   }
   ```

## 8.5 服务熔断

>   熔断降级是解决雪崩问题的重要手段，思路是由断路器统计服务调用的异常比例、慢请求比例，如果超出阈值则会熔断该服务，即拦截访问该服务的一切请求；而当服务恢复时，断路器会放行访问该服务的请求

### 8.5.1 断路器中三种状态的切换

![断路器三种状态的切换](D:\A\image\断路器三种状态的切换.png)

### 8.5.2 开启服务熔断

- 在Sentine控制台中，点击簇点资源后的熔断按钮，即可配置熔断策略

  ![服务熔断1](D:\A\image\服务熔断1.png)

  ![服务熔断2](D:\A\image\服务熔断2.png)

# 9.分布式事务

>   在分布式系统中，如果一个业务需要多个服务合作完成，而且每一个服务都有事务，多个事务必须同时成功或失败，这样的事务就是分布式事务。其中的每个服务的事务就是一个分支事务。整个业务成为全局事务

分布式事务解决思路：解决分布式事务，各个子事务之间必须能感知到彼此的事务状态，才能保证状态一致

![分布式事务解决思路](D:\A\image\分布式事务解决思路.png)

## 9.1 初始Seata

>   Seata是2019年1月蚂蚁金服和阿里巴巴共同开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务，为用户打造一站式的分布式解决方案

Seata事务管理中有三个重要的角色

- **TC (Transaction Coordinator) - 事务协调者：**维护全局和分支事务的状态，协调全局事务提交或回滚。 
-  **TM (Transaction Manager) -** **事务管理器：**定义全局事务的范围、开始全局事务、提交或回滚全局事务。 
-  **RM (Resource Manager) -** **资源管理器：**管理分支事务，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。 

![Seata工作架构](D:\A\image\Seata工作架构.png)

## 9.2 部署TC

### 9.2.1 准备数据库表

Seata支持多种存储模式，但考虑到持久化的需要，我们一般选择基于数据库存储

![seata1](D:\A\image\seata1.png)

### 9.2.2 准备seata的运行配置文件

将整个seata文件夹拷贝到虚拟机/root目录下

![seata2](D:\A\image\seata2.png)

### 9.2.3 Docker部署

确保nacos、mysql都在hm-net网络下，如果某个容器不再hm-net网络，可以参考下面的命令将某容器加入指定网络

```shell
docker network connect [网络名] [容器名]
```

在虚拟机的/root目录下执行下面的命令

```shell
docker run --name seata \
-p 8099:8099 \
-p 7099:7099 \
-e SEATA_IP=8.156.77.246 \
-v ./seata:/seata-server/resources \
--privileged=true \
--network hm-net \
-d \
seataio/seata-server:1.5.2
```

## 9.3 微服务集成Seata

### 9.3.1 引入Seata依赖

由于Seata的相关配置慧写在nacos配置中心中作为共享配置，因此使用Seata的模块除了需要引入Seata依赖还需要引入nacos和boosstrap依赖

```xml
<!--统一配置管理-->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<!--读取bootstrap文件-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
<!--seata-->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
```

### 9.3.2 改造配置

以trade模块为例，item、cart模块类似

- 在nacos中添加Seata的共享配置

```yml
seata:
  registry: # TC服务注册中心的配置，微服务根据这些信息去注册中心获取tc服务地址
    type: nacos # 注册中心类型 nacos
    nacos:
      server-addr: 8.156.77.246:8848 # nacos地址
      namespace: "" # namespace，默认为空
      group: DEFAULT_GROUP # 分组，默认是DEFAULT_GROUP
      application: seata-server # seata服务名称
      username: nacos
      password: nacos
  tx-service-group: hmall # 事务组名称
  service:
    vgroup-mapping: # 事务组与tc集群的映射关系
      hmall: "default"
```

- 在使用Seata服务的模块的bootstrap.yaml文件中添加读取共享的seata配置

```yaml
spring:
  application:
    name: trade-service # 服务名称
  profiles:
    active: dev
  cloud:
    nacos:
      server-addr: 8.156.77.246 # nacos地址
      config:
        file-extension: yaml # 文件后缀名
        shared-configs: # 共享配置
          - dataId: shared-jdbc.yaml # 共享mybatis配置
          - dataId: shared-log.yaml # 共享日志配置
          - dataId: shared-swagger.yaml # 共享日志配置
          - dataId: shared-seata.yaml # 共享seata配置
```

- 修改使用Seata服务的模块的application.yaml

```yaml
server:
  port: 8085
feign:
  okhttp:
    enabled: true # 开启OKHttp连接池支持
  sentinel:
    enabled: true # 开启Feign对Sentinel的整合
hm:
  swagger:
    title: 交易服务接口文档
    package: com.hmall.trade.controller
  db:
    database: hm-trade
```

### 9.3.3 添加数据库表

  由于Seata的客户端在解决分布式事务时需要记录一些中间数据，保存在数据库中，因此需要在使用分布式事务的模块对应的数据库中添加专门的数据库表

![微服务整合Seata1](D:\A\image\微服务整合Seata1.png)

## 9.4 XA模式

>   XA规范是一套分布式事务处理标准，该标准描述了全局的TM与局部的RM之间的接口

![XA模式](D:\A\image\XA模式.png)

**XA模式的优点**

- 事务的强一致性，满足ACID原则
- 常用数据库都支持，实现简单，并且没有代码入侵

**XA模式的缺点**

- 因为一阶段需要锁定数据库资源，等待二阶段结束才释放，性能较差
- 依赖关系型数据库实现事务

**实现XA模式**

1. 修改application.yml(每个参与事务的微服务)，开启XA模式

   ```yaml
   seata:
     data-source-proxy-mode: XA
   ```

2. 给发起全局事务的入口方法添加@GlobalTransactional注解

   ```java
   @GlobalTransactional
   public Long createOrder(OrderFormDTO order) {
       // ...创建订单
       // ...清理购物车
       // ...扣减库存
       return order.getId();
   }
   ```

## 9.5 AT模式

>   AT模式是Saeta主推的模式，AT模式同样是分阶段提交的事务模型，不过弥补了XA模型中资源锁定周期过长的缺陷

![AT模式](D:\A\image\AT模式.png)

**AT模式的优点**

- AT模式一阶段直接提交，不锁定资源

**AT模式的缺点**

- 过程中存在数据短暂不一致的情况，但可以保证事务最终一致

**实现AT模式**

1. 首先，为每个事务模块对应的数据库添加用于保存数据快照的数据表

   ```sql
   CREATE TABLE IF NOT EXISTS `undo_log`
   (
       `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
       `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
       `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
       `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
       `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
       `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
       `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
       UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
   ) ENGINE = InnoDB
     AUTO_INCREMENT = 1
     DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';
   ```

2. 修改application.yml文件，将事务模式修改为AT模式

   ```yaml
   seata:
     data-source-proxy-mode: AT
   ```

3. 给发起全局事务的入口方法添加@GlobalTransactional注解

   ```java
   @GlobalTransactional
   public Long createOrder(OrderFormDTO order) {
       // ...创建订单
       // ...清理购物车
       // ...扣减库存
       return order.getId();
   }
   ```
