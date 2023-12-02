package com.mavs.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.mavs.common.interceptor.DataSourceInterceptor;

/**
 * WebMVC設定用クラス
 * 
 * インターセプター(mavs.common.interceptor)を設定する
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /** データソース用のインターセプター */
    @Autowired
    DataSourceInterceptor dataSourceInterceptor;

    /**
     * インターセプターの登録
     * 
     * @param InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dataSourceInterceptor);
    }
}
