package com.example.zg.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author zhangguo
 * @date 2020/6/2 20:10
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService<Person> redisService;


    public static final Person PERSON1 = new Person("zhangguo", 27);
    public static final Person PERSON2 = new Person("zhangfangfang", 25);
    public static final List<Person> PERSON_LIST = Arrays.asList(PERSON1, PERSON2, PERSON2, PERSON2);

    @Test
    public void testCommon() {
        System.out.println("redisTestString-zhangguo：" + redisService.getExpire("redisTestString-zhangguo"));
        System.out.println("redisTestList-zhangguo：" + redisService.getExpire("redisTestList-zhangguo"));
        System.out.println("redisTestHash-zhangguo：" + redisService.getExpire("redisTestHash-zhangguo"));
    }

    @Test
    public void testRedisString() {
        if (redisService.hasKey("redisTestString-zhangguo")) {
            Person person = redisService.get("redisTestString-zhangguo");
//            JSONObject.toJavaObject((JSONObject) person, person.getClass());
            System.out.println(person);
        } else {
            System.out.println(redisService.set("redisTestString-zhangguo", PERSON1));
        }
    }

    @Test
    public void testRedisList() {
        if (redisService.hasKey("redisTestList-zhangguo")) {
            List<Person> personList = redisService.lGet("redisTestList-zhangguo", 0, -1);
            System.out.println(personList);
            Person person = redisService.lGetIndex("redisTestList-zhangguo", 1);
            System.out.println(person);
        } else {
            System.out.println(redisService.lSet("redisTestList-zhangguo", PERSON_LIST));
        }
    }

    @Test
    public void testRedisSet() {
        if (redisService.hasKey("redisTestSet-zhangguo")) {
            Set<Person> personSet = redisService.sGet("redisTestSet-zhangguo");
            System.out.println(personSet);
        } else {
            System.out.println(redisService.sSet("redisTestSet-zhangguo", PERSON1, PERSON2, PERSON1));
        }
    }


    @Test
    public void testRedisHash() {
//        redisService.hset("redisTestHash-zhangguo", "3", PERSON2);
        if (redisService.hasKey("redisTestHash-zhangguo")) {
            Map<String, Person> map = redisService.hmget("redisTestHash-zhangguo");
            System.out.println(map);
            Person person = redisService.hget("redisTestHash-zhangguo", "1");
            System.out.println(person);
//            System.out.println(redisService.en("redisTestHash-zhangguo", "1"));
        } else {
            Map<String, Person> map = new HashMap<>();
            map.put("1", PERSON1);
            map.put("2", PERSON2);
            System.out.println(redisService.hmset("redisTestHash-zhangguo", map));
        }
//        redisService.hdel("redisTestHash-zhangguo", "1");
        System.out.println(redisService.hHasKey("redisTestHash-zhangguo", "2"));
    }

}