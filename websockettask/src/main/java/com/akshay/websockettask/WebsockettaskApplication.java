package com.akshay.websockettask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebsockettaskApplication {
	public static void main(String[] args) {
        SpringApplication.run(WebsockettaskApplication.class, args);

    }

}
