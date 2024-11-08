package com.newsportal.newsportal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from the 'uploaded_videos' directory
        registry.addResourceHandler("/uploads/news_files/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/news_files/");
    }
}
