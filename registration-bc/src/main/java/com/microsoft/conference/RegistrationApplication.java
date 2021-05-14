package com.microsoft.conference;

import com.microsoft.conference.common.config.ConferenceDataSourceConfiguration;
import com.microsoft.conference.common.config.WebMvcConfiguration;
import org.enodeframework.spring.EnableEnode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEnode
@Import(value = {
    ConferenceDataSourceConfiguration.class,
    WebMvcConfiguration.class,
})
public class RegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistrationApplication.class, args);
    }
}