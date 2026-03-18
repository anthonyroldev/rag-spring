package com.anthonyroldev.ragapi.config;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenTextSplitterBean {
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return TokenTextSplitter.builder().withChunkSize(1000).build();
    }
}
