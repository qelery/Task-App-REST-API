package com.qelery.TaskRestApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Allows for case-insensitive RequestParam to Enum matching
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PriorityEnumConverter());
        registry.addConverter(new StatusEnumConverter());
    }
}