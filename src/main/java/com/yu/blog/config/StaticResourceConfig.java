// com.yu.blog.config.StaticResourceConfig
package com.yu.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${image.storage.root}")
    private String root; // 例如：C:/Users/.../Blog/data/image-store

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 指向 root 下的 images 子目录
        String location = Paths.get(root).toUri().toString(); // 自动是 file:///C:/.../image-store/images/
        registry.addResourceHandler("/images/**")
                .addResourceLocations(location)
                .setCachePeriod(31536000);
    }
}
