package com.waduclay.multiauthentication;

import com.waduclay.multiauthentication.config.UserConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(UserConfigurationProperties.class)
public class MultiAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiAuthenticationApplication.class, args);
    }

}
