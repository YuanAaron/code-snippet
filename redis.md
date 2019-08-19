## Redis简介

redis是key-value数据库，支持主从同步，数据存在内存，性能卓越。

**参考资料**
+ 英文官网：https://redis.io/
+ 中文官网：http://www.redis.cn/
+ 书籍推荐：《redis设计与实现》

**Redis配置**

<!-- the behaviour will be to save after 900 sec (15 min) if at least 1 key changed -->
save 900 1

dbfilename dump.rdb

**AOF和RDB的比较**

RDB: 保存执行完所有命令后的最终结果。
AOF: 不保存最终结果，只保存执行的所有命令。这样，重新执行一遍这些命令就可以得到原来的结果。

更多详细比较可以参考：https://redis.io/topics/persistence 或 http://www.redis.cn/topics/persistence.html

## Redis基础功能

set	单一数值

setex 适用于短信验证码，注册登录验证码，缓存 

incr 适用于PV

## List

双向列表，适用于最新列表（假设列表总共10个元素，每次添加就加入到列表，超过10个就删除最后一个元素，保证10个元素一直是最新的），关注列表（关注我的都有谁）
+ lpush
+ lpop
+ blpop 
+ lindex	
+ lrange 
+ lrem	
+ linsert	
+ lset 
+ rpush

## Hash

适用于对象属性数不定
+ hset	
+ hget	
+ hgetAll 	
+ hexists	
+ hkeys	
+ hvals

## Set

适用于无顺序的集合（具有天然去重的特点），点赞点踩（把点赞的人放到点赞这样一个集合中），已读，抽奖，共同好友（集合的交集）
+ sdiff	
+ smembers	
+ sinter	
+ scard	

## SortedSet

优先队列，适用于排行榜
+ zadd	
+ zscore	
+ zrange	
+ zcount	
+ zrank	
+ zrevrank

## Redis在牛客的应用
+ PV(帖子的浏览数)
+ 点赞（Set）
+ 关注（List都有谁关注了张三）
+ 排行榜（SortedSet）
+ 验证码(带有超时时间的KV)
+ 缓存(序列化与反序列化，还可以设置超时时间，找不到就去Mysql数据库找)
+ 异步队列
+ 判题队列

最后两个是通过redis做中间层，把数据异步化，即用一个队列去维护，同步的话可能会卡死，比如突然100个人同时提交，同步的话需要100判题线程，非常的卡。如果有一个队列（每个提交都先进入队列）将其异步掉，这个队列有好几个服务器在判题，判完后再将结果回调，这样的话就能比较流畅的看到判题的结果。

## geo

附近的人等功能的实现。MongoDB也支持地理位置索引，实现原理自己Google。

## 哨兵
主从同步指主从服务器数据保持一致。现有几台同步的redis服务器，如果其中一台挂掉了，那么就会从其他几台中选出一台作为主机，这就是哨兵的概念。
