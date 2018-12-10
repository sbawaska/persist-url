package io.projectriff.demo.persisturl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
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
        // TODO parameterize this
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(
                        "my-redis-master.default.svc.cluster.local"));
    }


    @Bean
    public Function<String, Boolean> persistUrl() {
        return s -> {
            System.out.println("Got URL:"+s);
            writeToRedis(s);
            return Boolean.TRUE;
        };
    }

    protected void writeToRedis(@NonNull String s) {
        int splitIndex = s.indexOf(':');
        if (splitIndex < 0 || splitIndex == s.length() + 1 ) {
            // delimiter not found, or delimiter is last
            throw new IllegalArgumentException("expected form key:value but was "+s);
        }
        redisTemplate.opsForValue().set(s.substring(0, splitIndex), s.substring(splitIndex + 1));
    }

    public static void main(String[] args) {
		SpringApplication.run(PersistUrlApplication.class, args);
	}
}
