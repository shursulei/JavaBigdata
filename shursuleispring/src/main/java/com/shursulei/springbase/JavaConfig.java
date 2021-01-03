package com.shursulei.springbase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
@Configuration
public class JavaConfig {
    @Bean
    public  FunctionService functionService(){
        return new FunctionService();
    }
}
