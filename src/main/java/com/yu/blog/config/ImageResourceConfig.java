package com.yu.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ImageResourceConfig implements WebMvcConfigurer {

    @Value("${image.storage.root:/root/images}")
    private String storageRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // -> file:/root/images/   （一定是这个，不要多一层 /images）
        String location = Paths.get(storageRoot).toAbsolutePath().normalize().toUri().toString();
        if (!location.endsWith("/")) location += "/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations(location)
                .setCachePeriod(31536000);
    }
}