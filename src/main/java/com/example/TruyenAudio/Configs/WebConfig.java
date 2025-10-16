package com.example.TruyenAudio.Configs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL /uploads/** -> thư mục vật lý upload.path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + (uploadPath.endsWith("/") ? uploadPath : uploadPath + "/"));
    }
}
