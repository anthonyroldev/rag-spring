package com.anthonyroldev.ragapi;

import com.anthonyroldev.ragapi.config.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(S3Properties.class)
public class RagapiApplication {

    static void main(String[] args) {
        SpringApplication.run(RagapiApplication.class, args);
    }

}
