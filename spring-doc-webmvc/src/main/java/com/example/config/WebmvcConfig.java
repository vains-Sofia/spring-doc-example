package com.example.config;

import com.example.config.converter.EnumConverterFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 添加自定义枚举转换配置
 *
 * @author vains
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
public class WebmvcConfig implements WebMvcConfigurer {

    private final EnumConverterFactory<?, ?> enumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConverterFactory);
    }
}