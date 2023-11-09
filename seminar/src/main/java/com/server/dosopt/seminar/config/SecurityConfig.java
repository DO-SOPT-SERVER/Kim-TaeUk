package com.server.dosopt.seminar.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
    SecurityFilterChain bean: Spring Security의 filter chain 정의
    HTTP 요청에 대한 보안 규칙 적용
    http.csrf().disable(): CSRF 보호 비활성화
    .authorizeHttpRequests(): HTTP 요청에 대한 권한 부여 규칙 설정
    .anyRequest().permitAll(): 권한 검사 없이 어떤 요청이든 허용 - 모든 엔드포인트에 엑세스하도록 설정
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and().build();
    }

    /*
    WebMvcConfigurer bean: CORS 설정 구성
    -> addCorsMappings 메서드를 통해 모든 도메인에서의 요청 허용하도록 설정
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*");
            }
        };
    }
}
