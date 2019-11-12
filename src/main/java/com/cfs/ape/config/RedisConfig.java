package com.cfs.ape.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
@EnableAutoConfiguration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisAddress;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedisTemplate<String, Object> redisObjTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;

    }

    @Primary
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
                 //缓存配置对象
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L)) //设置缓存的默认超时时间：30分钟
                         .disableCachingNullValues()             //如果是空值，不缓存
                         .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))         //设置key序列化器
                         .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((valueSerializer())));  //设置value序列化器

        return RedisCacheManager
                         .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                         .cacheDefaults(redisCacheConfiguration).build();
             }
     private RedisSerializer<String> keySerializer() {
                 return new StringRedisSerializer();
             }

     private RedisSerializer<Object> valueSerializer() {
         Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

         ObjectMapper om = new ObjectMapper();
         // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
         om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
         // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
         om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
         om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
         om.registerModule(new JavaTimeModule());
         jacksonSeial.setObjectMapper(om);
         //RedisSerializer<Object> result = jacksonSeial;
         return jacksonSeial;
     }


    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissionClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://49.233.172.155:6379").setPassword("3106393");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean
    @ConditionalOnMissingBean
    public Config redissionConfig(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://49.233.172.155:6379").setPassword("3106393");
        return config;
    }



}
