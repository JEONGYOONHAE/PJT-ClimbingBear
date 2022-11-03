package com.example.climbingBear.global.config;

import com.example.climbingBear.global.jwt.AccessTokenInterceptor;
import com.example.climbingBear.global.jwt.RefreshTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final AccessTokenInterceptor accessTokenInterceptor;
    private final RefreshTokenInterceptor refreshTokenInterceptor;

//    @Autowired
//    AccessTokenInterceptor accessTokenInterceptor;
//    @Autowired
//    RefreshTokenInterceptor refreshTokenInterceptor;

//    @Bean
//    public AccessTokenInterceptor accessTokenInterceptor(){
//        return new AccessTokenInterceptor();
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshTokenInterceptor).addPathPatterns("/user/access-token");
        registry.addInterceptor(accessTokenInterceptor)
                .addPathPatterns("/diary");
//                .addPathPatterns("/**/*")
//                .addPathPatterns("/**/**")
//                .excludePathPatterns("/user/**");
//        registry.addInterceptor(accessTokenInterceptor).excludePathPatterns(Arrays.asList(
//                new String[]{"/h2-console", "/swagger-ui.html","/swagger-ui.html/**", "/swagger-resources", "/swagger-resources/**", "/v3/*", "/v3", "/user/**"}));
    }

}