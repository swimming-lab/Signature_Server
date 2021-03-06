package com.github.bestheroz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.github.bestheroz", exclude = {JacksonAutoConfiguration.class})
public class MyApplication {
    public static void main(final String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}


/**
 * War 패키징
 */
/*public class MyApplication extends SpringBootServletInitializer {
    public static void main(final String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
        return builder.sources(MyApplication.class);
    }
}*/
