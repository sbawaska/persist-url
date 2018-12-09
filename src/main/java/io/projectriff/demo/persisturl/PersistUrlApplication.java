package io.projectriff.demo.persisturl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;

import java.util.function.Function;

@SpringBootApplication
public class PersistUrlApplication {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(
                        "my-redis-master.default.svc.cluster.local"));
        // TODO parameterize this
    }


    @Bean
    public Function<String, Boolean> printToLog() {
        return s -> {
            System.out.println("Got URL:"+s);
            writeToRedis(s);
            return Boolean.TRUE;
        };
    }

    protected void writeToRedis(@NonNull String s) {
        String[] arr = s.split(":");
        if (arr.length < 2) {
            throw new IllegalStateException("cannot split " + s);
        }
        redisTemplate.opsForValue().set(arr[0], arr[1]);
    }

    public static void main(String[] args) {
		SpringApplication.run(PersistUrlApplication.class, args);
	}
}
