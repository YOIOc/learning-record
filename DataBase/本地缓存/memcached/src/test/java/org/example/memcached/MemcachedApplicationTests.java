package org.example.memcached;

import net.spy.memcached.MemcachedClient;
import org.example.memcached.config.MemcachedRunner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MemcachedApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private MemcachedRunner memcachedRunner;

    @Test
    public void  testSetGet(){
        MemcachedClient memcachedclient = memcachedRunner.getClient();
        memcachedclient.set("testKey", 1000, "666666");
        System.out.println(memcachedclient.get("testKey").toString());
    }

}
