package com.oshacker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oshacker.model.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class JedisAdapter {
    public static void print(int index,Object obj) {
        System.out.println(String.format("%d %s",index,obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis=new Jedis("redis://127.0.0.1:6379/1");
        jedis.flushDB();//删除1号数据库中的所有key

        //set get
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        print(1,jedis.get("newhello"));
        //hello2的过期时间为15s
        //短信验证：当需要短信通知时，就将一个短信验证码到redis中，过期数据库自动删除。登录注册验证码同理。
        //redis用作缓存：先从redis缓存中去取，缓存过期了就从MySQL数据库中取。
        jedis.setex("hello2",15,"world");

        //值加减
        jedis.set("pv","100");//用于存储pv、uv
        print(2,jedis.incr("pv"));//加1
        print(2,jedis.incrBy("pv",3));//加3
        print(2,jedis.decrBy("pv",2));//减2

        //keys *
        print(3,jedis.keys("*"));

        //list
        for (int i = 0; i <10 ; i++) {
            jedis.lpush("list1","a"+ String.valueOf(i));
        }
        print(1,jedis.lrange("list1",0,-1));
        print(4,jedis.lrange("list1",1,3));//两端闭区间
        print(4,jedis.lindex("list1",3));

        print(4,jedis.llen("list1"));
        print(4,jedis.lpop("list1"));
        print(4,jedis.llen("list1"));

        print(4,jedis.linsert("list1", BinaryClient.LIST_POSITION.AFTER,"a4","bb"));
        print(4,jedis.linsert("list1", BinaryClient.LIST_POSITION.BEFORE,"a4","aa"));
        print(4,jedis.lrange("list1",0,-1));

        //hash：适用于对象属性可能会增加/减少
        jedis.hset("user","name","zhangsan");
        jedis.hset("user","age","18");
        jedis.hset("user","phone","13777778888");
        print(5,jedis.hget("user","name"));
        print(5,jedis.hgetAll("user"));

        print(5,jedis.hdel("user","phone"));
        print(5,jedis.hgetAll("user"));

        print(5,jedis.hexists("user","email"));
        print(5,jedis.hexists("user","age"));

        print(5,jedis.hkeys("user"));
        print(5,jedis.hvals("user"));

        //hsetnx表示key不存在就存储，存在就无效
        jedis.hsetnx("user","school","xjtu");
        jedis.hsetnx("user","name","yw");
        print(5,jedis.hgetAll("user"));

        //set:天生适用于去重
        for (int i = 0; i <10 ; i++) {
            jedis.sadd("set1", String.valueOf(i));
            jedis.sadd("set2", String.valueOf(i*i));
        }
        print(6,jedis.smembers("set1"));
        print(6,jedis.smembers("set2"));

        print(6,jedis.sunion("set1","set2"));//并集
        print(6,jedis.sinter("set1","set2"));//交集，应用于共同好友
        print(6,jedis.sdiff("set1","set2"));//前边有，后边没有，即set1-set2
        print(6,jedis.sismember("set1","12"));
        print(6,jedis.sismember("set2","16"));

        jedis.srem("set1","5");
        print(6,jedis.smembers("set1"));

        jedis.smove("set2","set1","25");
        print(6,jedis.smembers("set1"));
        print(6,jedis.smembers("set2"));

        print(6,jedis.scard("set1"));//统计集合中元素个数

        print(6,jedis.srandmember("set1"));//用于抽奖
        print(6,jedis.srandmember("set1",3));

        //sorted set(优先队列)：用于排行榜
        jedis.zadd("rank1",50,"张三");
        jedis.zadd("rank1",80,"李四");
        jedis.zadd("rank1",77,"王五");
        jedis.zadd("rank1",98,"赵六");
        jedis.zadd("rank1",60,"zhouqi");
        jedis.zadd("rank1",90,"冯八");
        print(7,jedis.zcard("rank1")); //统计元素个数

        print(7,jedis.zscore("rank1","李四"));
        jedis.zincrby("rank1",2,"张三");//加2分
        print(7,jedis.zscore("rank1","张三"));
        jedis.zincrby("rank1",3,"周七");
        print(7,jedis.zscore("rank1","周七"));

        print(7,jedis.zcount("rank1",60,90));//分数在60-90(两端闭区间)
        print(7,jedis.zrange("rank1",0,100));//默认从小到大排序
        print(7,jedis.zrange("rank1",0,2));//后三名
        print(7,jedis.zrevrange("rank1",0,2));//前三名
        for (Tuple tuple:jedis.zrangeByScoreWithScores("rank1",60,90)) {
            print(7,tuple.getElement()+" "+ String.valueOf(tuple.getScore()));
        }
        print(7,jedis.zrank("rank1","张三"));//某个人的排名
        print(7,jedis.zrevrank("rank1","张三"));

        jedis.zadd("zset1",5,"a");
        jedis.zadd("zset1",5,"b");
        jedis.zadd("zset1",5,"c");
        jedis.zadd("zset1",5,"d");
        jedis.zadd("zset1",5,"e");

        //分数相同的情况下，按照字典序排序
        print(7,jedis.zrange("zset1",0,-1));
        print(7,jedis.zlexcount("zset1","-","+"));//返回zset中处于该字典区间的元素的个数
        print(7,jedis.zlexcount("zset1","[b","[d"));
        print(7,jedis.zlexcount("zset1","(b","[d"));
        jedis.zrem("zset1","b");
        print(7,jedis.zrange("zset1",0,-1));//遍历所有也可以这样
        jedis.zremrangeByLex("zset1","(c","+");//删除该字典区间内的元素
        print(7,jedis.zrange("zset1",0,-1));

        //连接池
//        JedisPool pool=new JedisPool();//默认8个jedis
//        for (int i = 0; i < 20; i++) {
//            jedis=pool.getResource();
//            print(8,jedis.get("pv"));
//            jedis.close();//用完如果不归回给连接池，那么连接被占满，程序会卡住
//        }

        //redis用作缓存
        User user=new User();
        user.setName("zhangsan");
        user.setPassword("123");
        user.setHeadUrl("xyz.png");
        user.setSalt("abcd");
        user.setId(1);
        String json=JSONObject.toJSONString(user);//序列化
        jedis.set("user", json);

        String val = jedis.get("user");
        User user1 = JSON.parseObject(val, User.class);//反序列化
        print(9,user1);
    }
}
