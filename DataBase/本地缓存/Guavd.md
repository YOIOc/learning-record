# 1. 本地缓存

> ​	在实际的项目中,我们通常会将一些热点数据存储到Redis或Memcached 这类缓存中间件中,只有当缓存的访问没有命中时再查询数据库。在提升访问速度的同时,也能降低数据库的压力
>
> ​	随着不断的发展,这一架构也产生了改进,在一些场景下可能单纯使用Redis类的远程缓存已经不够了,还需要进一步配合本地缓存使用,例如Guava等，从而再次提升程序的响应速度与服务性能。于是,就产生了使用本地缓存作为一级缓存,再加上远程缓存作为二级缓存的两级缓存架构

- **在不考虑并发等复杂问题情况下的访问流程**

![二级缓存的访问流程](https://github.com/YOIOc/learning-record/blob/main/image/二级缓存的访问流程.png)

- **本地缓存的适用场景以及优缺点**
  - 适用场景：数据查询频率高、查询耗时；数据大小不会超过内存总量
  - 优点：空间换时间
  - 缺点：缓存并未持久化到硬盘；占用内存空间；多个应用实例出现缓存不一致的情况

# 2. Guava Cache介绍

> ​	Guava是一套非常完善的本地缓存机制

- Guava的优点
  - 缓存过期和淘汰机制：通过设置Key的过期时间（包括访问过期和创建过期），Guava在缓存容量达到指定大小时，采用LRU（Least Recently Used-最近最少使用）策略来移除不常使用的键值
  - 并发处理能力：Guava是线程安全的，使得缓存支持并发的写入和读取
  - 更新锁定：通常情况下，当缓存中查询的某个key不存在时，则查源数据，并回填缓存，但在高并发的情况下，多次查源并重复回填缓存,可能会造成源的宕机(DB)，Guava可以在方法中加以控制，对同一个key，只让一个请求去读源并回填缓存，其他请求阻塞等待

# 3. 缓存的三种创建方式

**导入依赖**

```xml
<!--引入guava缓存-->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>25.0-jre</version>
</dependency>
```

## 3.1 LoadingCache对象的get方法，在创建缓存对象时，设置缓存加载逻辑

```java
// 使用CacheBuilder创建一个LoadingCache缓存对象并设置相关参数
	// initialCapacity(5)：初始容量为5
	// maximumSize(100)：最大容量为100，超过最大容量会触发缓存淘汰
	// expireAfterWrite(5000, TimeUnit.MILLISECONDS)：写入后5秒过期
	// // .concurrencyLevel(1)：设置缓存的并发级别，即允许多少个线程同时访问缓存
	// removalListener：监听缓存移除事件，当缓存被移除（显示移除或容量超限）时会触发回调（使用RemovalListener接口实现移除监听器，并重写onRemoval方法打印被移除的键和值，格式为： key移除了,value[value]）
	// build：缓存加载逻辑，当缓存中不存在某个键时，触发load方法加载数据（使用CacheLoader接口实现加载器，并重写CacheLoader方法，使调用cache.get(key)缓存中不存在key时，自动将key加载到缓存中，value就是方法的返回值）

// 使用LoadingCache实现的缓存，必须在build()参数中设置CacheLoader缓存加载逻辑
```

```java
private static void method1() throws ExecutionException, InterruptedException {
    // 使用CacheBuilder创建一个LoadingCache缓存对象并设置相关参数
    LoadingCache<String, String> cacheLoadInit = CacheBuilder.newBuilder()
            .initialCapacity(5)
            .maximumSize(100)
            .expireAfterWrite(5000, TimeUnit.MILLISECONDS)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    System.out.println(removalNotification.getKey() + "移除了,value" + removalNotification.getValue());
                }
            })
            .build(
                    new CacheLoader<String, String>() {
                        @Override
                        public String load(String key) throws Exception {
                            System.out.println("first init.....");
                            return key;
                        }
                    });
    
    //由于CacheLoader可能抛出异常,LoadingCache.get(K)也声明为抛出ExecutionException异常
    String key1 = cacheLoadInit.get("first-name"); // first init.....
    String key2 = cacheLoadInit.get("first-name");
    System.out.println(key);                       // first-name
    TimeUnit.MILLISECONDS.sleep(5000);
    
    //设置了5s的过期时间,5s后会重新加载缓存
    String keySecond = cacheLoadInit.get("first-name");  // first init.....
    System.out.println("5s后重新加载缓存数据" + keySecond); // 5s后重新加载缓存数据first-name
}
```

## 3.2 Cache对象的put方法直接插入

```java
private static void method2(){
    // 使用CacheBuilder创建一个Cache缓存对象并设置相关参数
    Cache<String, String> cache = CacheBuilder.newBuilder()
            .initialCapacity(5)
            .maximumSize(100)
        	.expireAfterWrite(5000, TimeUnit.MILLISECONDS)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    System.out.println(notification.getKey()+"移除了,value:"+notification.getValue());
                }
            })
            .build();
    
    // getIfPresent(key)：从现有的缓存中获取,如果缓存中有key,则返回value,如果没有则返回null
    System.out.println(cache.getIfPresent("java金融1")); // null
    if(StringUtils.isEmpty(key1)){
        cache.put("java金融1","value - java金融1");
        System.out.println(cache.getIfPresent("java金融1")); // value - java金融1
    }
}
```

## 3.3 Cache对象的get方法，传入一个Callable实例，自定义缓存加载逻辑

```java
// 所有类型的Guava Cache，不管有没有自动加载功能，都支持get(K, Callable<V>)方法。这个方法返回缓存中相应的值,或者用给定的Callable运算并把结果加入到缓存中。在整个加载方法完成前,缓存项相关的可观察状态都不会更改。这个方法简便地实现了"如果有缓存则返回；否则运算、缓存、然后返回"

// 如果缓存中不包含key对应的记录,Guava会启动一个线程执行Callable对象中的call方法,call方法的返回值会作为key对应的值被存储到缓存中,并且被get方法返回

// Guava可以保证当有多个线程同时访问Cache中的一个key时,如果key对应的记录不存在,Guava只会启动一个线程执行get方法中Callable参数对应的任务加载数据存到缓存。当加载完数据后,任何线程中的get方法都会获取到key对应的值
```

```java
private static void method3() throws ExecutionException {
    // 使用CacheBuilder创建一个Cache缓存对象并设置相关参数
    Cache<String, String> cache = CacheBuilder.newBuilder()
            .initialCapacity(5)
        	.maximumSize(100)
            .expireAfterWrite(5000, TimeUnit.MILLISECONDS)
            .build();
    
    // 使用get方法获取获取缓存数据，并自定义当缓存不存在时，添加缓存的运算逻辑
    cache.get("key1",new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "value1";
        }
    });
    System.out.println(cache.getIfPresent("key1")); // value1
    
    // 手动清除缓存"
    cache.invalidateAll();
    System.out.println(cache.getIfPresent("key1")); // null
}
```

# 4. 缓存的四种回收方式

> ​	GuavaCache构建的缓存不会"自动"执行清理和回收工作，不会在某个缓存项过期后马上清理，GuavaCache是在每次进行缓存操作的时候，惰性删除 如get()或者put()的时候，判断缓存是否过期

## 4.1 容量回收 - 默认

> ​	CacheBuilder的默认回收策略时，当缓存数据超过缓存最大容量后，Cache采用LRU（优先移除最近最少使用的元素，若都没有被使用，则移除最早添加的元素）淘汰策略，移除那些最近没有被访问的元素，添加新元素

## 4.2 定时回收

1. expireAfterAccess(long, TimeUnit)：缓存项在给定时间内没有被读/写访问,则回收（这种缓存的回收顺序和容量回收一样，因为经常使用的元素会经常更新访问时间）
2. expireAfterWrite(long, TimeUnit)：缓存项在给定时间内没有被写访问创建或覆盖,则回收（如果认为缓存数据总是在固定时候后变得陈旧不可用,这种回收方式是可取的）

```java
private static void expireAfterAccessMethod() throws InterruptedException {
    Cache<String, String> build = CacheBuilder.newBuilder()
            //访问缓存3s后失效(访问缓存数据相当于更新缓存时间)
            .expireAfterAccess(3000, TimeUnit.MILLISECONDS)
            .build();
    
    // 添加缓存数据
    build.put("1","1 - value");
    
    // 阻塞1s后访问数据1(没有过期)
    Thread.sleep(1000);
    build.getIfPresent("1");
    
    // 阻塞2s后访问数据1(在1s时更新了访问时间，故依然没有过期)
    Thread.sleep(2100);
    System.out.println(build.getIfPresent("1"));
    
    // 阻塞3s后访问数据(过期了)
    Thread.sleep(3000);
    System.out.println(build.getIfPresent("1"));
}

private static void expireAfterWriteMethod() throws ExecutionException, InterruptedException {
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            // 创建缓存3s后失效(修改缓存数据相当于更新缓存时间)
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return s + " - value";
                }
            });
    
    // 添加缓存数据
    cache.get("1");
    
    // 阻塞1s后访问数据1(没有过期)
    Thread.sleep(1000);
    cache.getIfPresent("1");
    
    // 阻塞2s后访问数据1(在1s时虽然访问了数据1，但距离数据1的创建已经过去了3s，所以过期了)
    Thread.sleep(2100);
    System.out.println(cache.getIfPresent("1"));
}
```

## 4.3 引用回收

> ​	基于Reference-based Eviction(通过使用弱引用的键、或弱引用的值、或软引用的值，Guava Cache可以把缓存设置为允许垃圾回收）
>
> - 强引用：Object obj = new Object();垃圾回收器不会回收该对象
>
> - 弱引用：WeakReference<Object> weakRef = new WeakReference<>(new Object());对象被WeakReference封装，垃圾回收器会在下一次垃圾回收时直接回收该对象
>
> - 软引用：SoftReference<Object> softRef = new SoftReference<>(new Object());对象被SoftReference封装，垃圾回收器会在内存不足时回收该对象

1. CacheBuilder.weakKeys()：使用弱引用存储键。当键没有其它强或软引用时，缓存项可以被垃圾回收，值的比较是使用 `==` 比较内存地址而不是 `equals` 比较值

   ```java
   // 创建一个使用弱引用存储键的缓存
   Cache<String, String> cache = CacheBuilder.newBuilder()
           .weakKeys() // 使用弱引用存储键
           .build();
   
   // 强引用指向键(虽然缓存设置了弱引用存储，但由于缓存键被强引用，所以该缓存项不会被垃圾回收)
   String strongKey = "strongKey";
   cache.put(strongKey, "value");
   ```

2. CacheBuilder.weakValues()：使用弱引用存储值。当值没有其它强或软引用时，缓存项可以被垃圾回收，值的比较是使用 `==` 比较内存地址而不是 `equals` 比较值

3. CacheBuilder.softValues()：使用软引用存储值。软引用只有在响应内存需要时，才按照全局最近最少使用的顺序回收（软引用的回收具有不可预测性，所以在缓存设计中，建议使用容量回收，直接限定缓存大小可以更好地预测性能）值的比较是使用 `==` 比较内存地址而不是 `equals` 比较值

```java
public class GuavaExpireDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建缓存对象
        Cache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .weakValues()
                .build();
        
        // 添加缓存数据
        Person value = new Person(1,"tang");
        cache.put("1",value);
        System.out.println(cache.getIfPresent("1")); // Person(id=1, name=tang)
        
        // value不再指向Person(1,"tang")，缓存中key为1的缓存的value失去强引用，该缓存此时可以被垃圾回收
        value=new Person(2,"yang");
        
        // 强制垃圾回收
        System.gc();
        System.out.println(cache.getIfPresent("1")); // null
    }
}
```

## 4.4 显式清除-主动

1. 清除单个

   ```java
   Cache.invalidate(key)
   ```

2. 批量清除

   ```java
   // 将key=1和2的删除：cache.invalidateAll(Arrays.asList(“1”,“2”));
   Cache.invalidateAll(keys)
   ```

3. 清除所有缓存项

   ```java
   Cache.invalidateAll()
   ```

# 5. 移除监听器

- 通过CacheBuilder.removalListener(RemovalListener)，可以声明一个监听器，以便在缓存项被移除时做一些额外操作
- 缓存项被移除时，RemovalListener会获取移除通知[RemovalNotification]（移除原因[RemovalCause]，键和值）

```java
public class removalListenerDemo {
    public static void main(String[] args) {
        // 创建缓存对象
        Cache cache = CacheBuilder.newBuilder()
                .initialCapacity(1)
                .maximumSize(3)
                .concurrencyLevel(1)
                .removalListener(new RemovalListener() {
                    @Override
                    public void onRemoval(RemovalNotification removalNotification) {
                        System.out.println("removalListener => key:" + removalNotification.getKey() + ",value:" + removalNotification.getValue());
                    }
                })
                .build();
        cache.put("key - 1","value - 1");
        System.out.println(cache.getIfPresent("key - 1")); // value - 1
        cache.invalidate("key - 1");                       // removalListener => key:key - 1,value:value - 1
        System.out.println(cache.getIfPresent("key - 1")); // null

    }
}
```

# 6. 查看缓存信息

> ​	通过CacheBuilder.recordStats()方法启动cache的数据收集
>
> 缓存记录的数据
>
> - hitCount：缓存命中次数
> - missCount：缓存未命中次数
> - loadSuccessCount：成功加载值的次数（通常通过CacheLoader加载）
> - loadExceptionCount：加载时发生异常的次数
> - totalLoadTime：加载值所花费的总时间
> - evictionCount：缓存中被驱逐的条目数（例如因容量限制被移除的条目）

```java
public class recordStatsDemo {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Integer, Integer> cache = CacheBuilder.newBuilder()
            // 开启缓存统计
            .recordStats()
            .maximumSize(5)
            .build(new CacheLoader<Integer, Integer>() {
                @Override
                public Integer load(Integer id) throws Exception {
                    System.out.println("mock query db...."); // 模拟查询数据库
                    if (id % 2 == 0) {                       // 如果id是偶数则抛出异常
                        Thread.sleep(100);
                        throw new RuntimeException();        // 如果id是基数，睡眠0.2秒(模拟业务操作)，并返回id*10
                    } else {
                        Thread.sleep(200);
                        return id * 10;
                    }
                }
            });
        
        // 预先添加一条缓存数据
        cache.put(1, 100);
        //CacheStats{hitCount=0, missCount=0, loadSuccessCount=0, loadExceptionCount=0, totalLoadTime=0, evictionCount=0}
        System.out.println(cache.stats());

        // 缓存命中,hitCount加1
        System.out.println(cache.get(1));
        //CacheStats{hitCount=1, missCount=0, loadSuccessCount=0, loadExceptionCount=0, totalLoadTime=0, evictionCount=0}
        System.out.println(cache.stats());

        // 没有命中缓存, missCount和loadSuccessCount加1，并增加totalLoadTime(纳秒为单位)
        System.out.println("get data====" + cache.get(3));
        //....CacheStats{hitCount=1, missCount=1, loadSuccessCount=1, loadExceptionCount=0, totalLoadTime=214873400, evictionCount=0}
        System.out.println(cache.stats());

        // 没有命中缓存, missCount和loadExceptionCount加1,并增加totalLoadTime(纳秒为单位)
        try {
            System.out.println("get data====" + cache.get(4));
        } catch (Exception e) {
            // ....CacheStats{hitCount=1, missCount=2, loadSuccessCount=1, loadExceptionCount=1, totalLoadTime=318706100, evictionCount=0}
            System.out.println("...." + cache.stats());
        }

        // 直接操作缓存底层数据(Map)，如果查找的key不存在，不会触发CacheLoader加载缓存，并且不影响统计信息(通过map添加数据，会影响evictionCount)
        System.out.println("get data====" + cache.asMap().get(1));
        cache.invalidateAll();// 清空缓存
        //....CacheStats{hitCount=1, missCount=2, loadSuccessCount=1, loadExceptionCount=1, totalLoadTime=318706100, evictionCount=0}
        System.out.println("...." + cache.stats());

        // 添加7条缓存数据,由于最大数目是5,所以evictionCount=2
        System.out.println("size===" + cache.size());
        cache.put(1, 100);
        cache.put(2, 100);
        cache.put(3, 100);
        cache.put(4, 100);
        cache.put(5, 100);
        cache.put(6, 100);
        cache.put(7, 100);
        //....CacheStats{hitCount=1, missCount=2, loadSuccessCount=1, loadExceptionCount=1, totalLoadTime=318706100, evictionCount=2}
        System.out.println("...." + cache.stats());
    }
}
```

# 7. asMap视图

> ​	asMap是本地缓存的底层数据结构实现，有以下几点需要注意
>
> - cache.asMap()包含当前所有加载到缓存的项，因此cache.asMap().keySet()包含当前所有已加载的键
> - asMap().get(key)实质上等同于cache.getIfPresent(key)，但不会引起缓存项的加载
> - 所有读写操作都会重置相关缓存项的访问时间,包括Cache.asMap().get(Object)方法和Cache.asMap().put(K, V)方法，但Cache.asMap().containsKey(Object)、Cache.asMap()的集合视图操作不会修改缓存项的访问时间

```java
public class GuavaAsMapDemo {
    public static void main(String[] args) throws ExecutionException {
        // 创建缓存对象
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return "value:" + key;
                    }
                });
        
        // 添加缓存数据
        cache.get("1");
        cache.get("2");
        cache.get("3");
        
        // 获取asMap操作对象
        ConcurrentMap<String, String> map = cache.asMap();
        
        // 获取到cache所有的key:[1, 3, 2]
        Set<String> strings = map.keySet();
        System.out.println("获取到cache所有的key:"+strings);
        
        // 获取到cache所有的value:[value:1, value:3, value:2]
        Collection<String> values = map.values();
        System.out.println("获取到cache所有的value:"+values); 
        
        // 获取Map集合的视图操作对象，并遍历
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> stringStringEntry : set) {
            System.out.println(stringStringEntry.getKey()+"<====>"+stringStringEntry.getValue());
        }
        
        // 获取缓存数据
        map.get("4");
        System.out.println("从缓存中获取key4:"+cache.getIfPresent("4"));
    }
}
```

# 8. 异步锁定

> ​	异步锁定指的是在缓存加载数据时，使用异步的方式来处理锁定操作，以避免阻塞调用线程。具体来说，数据写入缓存后开始计时，超过刷新时间后数据不会被立即删除仍可被正常读取，当有新的请求访问该数据时，系统会在后台异步触发数据刷新，客户端会立即获得当前值（可能过时的）缓存数据，刷新完成后新数据覆盖旧

## 8.1 单独使用refreshAfterWrite

**缺陷**：如很长一段时间内没有请求，再次请求有可能会得到一个旧值这个旧值可能来自于很长时间之前，这将会引发问题。可以使用**expireAfterWrite和refreshAfterWrite搭配使用**解决这个问题

```java
//	使用CacheBuilder.refreshAfterWrites()，需要实现CacheLoader的reload方法。在方法中需要创建一个ListenableFutureTask，然后将这个task提交给线程池去异步执行。这样的话，缓存失效后重新加载就变成了异步，加载期间尝试获取取缓存的线程也不会被阻塞。而是获取到加载之前的值。加载完毕之后,各个线程就能取到最新的值。
 
//	总结:refreshAfterWrites是异步去刷新缓存的方法，使用过期的旧值快速响应。而expireAfterWrites缓存失效后线程需要同步等待加载结果，可能会造成请求大量堆积的问题。
```

```java
@SuppressWarnings("all")
public class TestGuavaCache {
    // 定义大小为5的线程池，用于异步任务的执行，确保异步操作不会阻塞主线程
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    // 创建缓存对象
    static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        // 指定缓存刷新时间
        .refreshAfterWrite(2, TimeUnit.SECONDS)
        .build(new CacheLoader<String, String>() {
            //同步加载缓存
            @Override
            public String load(String key) throws Exception {
                return "value:" + key;
            }
            
            //异步加载缓存
            @Override
            public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                //定义任务(模拟耗时操作，打印日志并返回“曹操”)，creat括号中的就是要执行的异步操作(这里使用了lambda表达式)
                ListenableFutureTask<String> futureTask = ListenableFutureTask.create(() -> {
                    Thread.sleep(1000);
                    return "曹操";
                });
                //异步执行任务
                executorService.execute(futureTask);
                return futureTask;
            }
        });

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 添加缓存数据，并获取缓存
        cache.put("name", "李白");
        System.out.println(cache.get("name")); // 李白
        
        // 睡眠2s后再次获取，此时缓存失效，异步的加载缓存，但是线程是立即返回“旧结果”
        Thread.sleep(2000);
        System.out.println(cache.get("name")); // 李白
        
        // 睡眠1s后再次获取，此时缓存已经更新
        Thread.sleep(1000);
        System.out.println(cache.get("name")); // 曹操
    }
}
```

## 8.2  expireAfterWrite和refreshAfterWrite搭配使用

1. expireAfterWrite写入后过期：表示缓存项从写入到过期的时间间隔。一旦缓存项被写入，经过指定的时间后，它将被视为过期并从缓存中移除
2. refreshAfterWrite写入后刷新：表示缓存项在写入后，多久后自动刷新。这个参数可以让你保持缓存项的新鲜度,确保在一段时间后自动更新缓存数据
3. 一起使用的情况:
   假设你有一个缓存用于存储某个网络请求的响应数据，你希望这个响应数据在写入后的一段时间内保持有效，但是你也希望在一段时间后自动刷新这个响应数据，以获取更新的信息。这时，你可以同时使用expireAfterWrite和refreshAfterWrite

```markdown
假设你设置了如下参数:expireAfterWrite:10分钟、 refreshAfterWrite:5分钟

示例:
时间点0:你第一次请求数据,从网络获取了响应,并将其写入缓存。缓存项的写入时间被记录。
时间点5分钟:缓存自动刷新,系统检测到缓存项已过期,触发一个刷新操作,从网络获取最新的数据,并更新缓存。同时,刷新时间被记录。
时间点10分钟:缓存项再次过期,但不会触发刷新操作。如果此时有新的请求,系统会从网络获取最新数据并更新缓存。
时间点15分钟:如果在此时有新的请求,系统会从网络获取最新数据,并刷新缓存。

这个示例中,expireAfterWrite和refreshAfterWrite的组合使得缓存项在被写入后的10分钟内保持有效,但在5分钟后会自动刷新以保持数据的新鲜度。
```

```java
 CacheBuilder.newBuilder()
     // 设置缓存的刷新时间为20分钟(缓存写入20分钟后如果有请求访问该缓存，会触发刷新操作)
     .refreshAfterWrite(20, TimeUnit.MINUTES)
     // 设置缓存的过期时间为30分钟(缓存写入30分钟后该缓存会被移除)
     .expireAfterWrite(30, TimeUnit.MINUTES)
     .maximumSize(1)
     .build(new CacheLoader<String, List<Map<String, Long>>>() {
         // 同步加载逻辑
         @Override
         public List<Map<String, Long>> load(String s) throws Exception {
             return queryData();
         }
         
         // 异步加载逻辑
         @Override
         public ListenableFuture<List<Map<String, Long>>> reload(String key, List<Map<String, Long>> oldValue)throws Exception {
             ListenableFutureTask<List<Map<String, Long>>> task = ListenableFutureTask
                 .create(() -> queryData());
             // 异步执行任务
             executorService.execute(task);
             // 返回异步任务的结果
             return task;
         }
     });
```

## 8.3 不用reload加载

> 不会获取到旧值，会重新同步刷新缓存

```java
public class TestGuavaCache {
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);


    static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        // 设置缓存的刷新时间为2s
        .refreshAfterWrite(2, TimeUnit.SECONDS)
        .build(new CacheLoader<String, String>() {
            //同步加载缓存
            @Override
            public String load(String key) throws Exception {
                return "曹操";
            }
        });
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
		// 添加缓存数据，并获取缓存
        cache.put("name", "李白");
        System.out.println(cache.get("name")); // 李白
        
        //睡眠2s后再次获取，此时缓存失效，重新加载缓存
        Thread.sleep(2000);
        System.out.println("从缓存取数据:"+cache.get("name")); // 曹操
    }
}
```

