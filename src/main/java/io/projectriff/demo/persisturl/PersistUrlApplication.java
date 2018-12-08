package io.projectriff.demo.persisturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class PersistUrlApplication {

    @Bean
    public Function<String, Boolean> printToLog() {
        return s -> {
            System.out.println("Got URL:"+s);
            return Boolean.TRUE;
        };
    }

	public static void main(String[] args) {
		SpringApplication.run(PersistUrlApplication.class, args);
	}
}
