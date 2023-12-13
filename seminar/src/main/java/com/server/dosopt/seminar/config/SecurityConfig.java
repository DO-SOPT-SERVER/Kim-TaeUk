package com.server.dosopt.seminar.config;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.server.dosopt.seminar.config.auth.CustomAccessDeniedHandler;
import com.server.dosopt.seminar.config.auth.CustomJwtAuthenticationEntryPoint;
import com.server.dosopt.seminar.config.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/*
EnableWebSecurity: 이거만 적어주면 endpoint에서 401에러(Unauthorized)가 뜸
-> 적절한 처리를 해줘야 함
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	/*
	sign-up과 sign-in은 인증/인가되지 않아도 접근 가능한 endpoint여야 하기 때문에
	AUTH_WHITELIST에 넣어두고
	 */
	private static final String[] AUTH_WHITELIST = {
		"api/users/sign-up",
		"api/users/sign-in"
	};

	/*
	SecurityFilterChain bean: Spring Security의 filter chain 정의

	.csrf().disable(): CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
	.httpBasic().disable(): Http Basic 인증 비활성화
	.formLogin().disable(): Form 기반 로그인 기능 비활성화
	.sessionManagement().sessionCreationPolicy(STATELESS): stateless 기반 세션 관리 사용하도록 설정, JWT 기반 토큰 사용 -> 세션 유지 X

	.exceptionHandling(): 예외 처리 설정
	.authenticationEntryPoint(customJwtAuthenticationEntryPoint)
	: 인증되지 않은 사용자가 보호된 endpoint에 접근하려 할 때 호출(로그인 페이지로 리다이렉션 or 인증 에러 처리하는 등의 방식을 정의하기도 함)
	.accessDeniedHandler(customAccessDeniedHandler)
	: 인증은 되어 있지만 사용자가 특정 리소스에 대한 권한이 없을 때 호출(403 에러와 함께 적절한 에러 페이지 등을 반환하기도 함)

	.authorizeHttpRequests(): HTTP 요청에 대한 권한 설정
	.requestMatchers(AUTH_WHITELIST).permitAll(): 특정 요청에 대한 권한 검사를 하지 않고 모든 사용자에게 허용
	-> sign-up, sign-in은 모두 허용해줘야 하니까 permitAll로 열어놓음!
	.anyRequest().authenticated(): 그 외 모든 요청은 인증된 사용자만 허용

	.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	: custom으로 만든 jwtAuthenticationFilter(JWT 기반 사용자 인증 필터)를
	UsernamePasswordAuthenticationFilter 앞에 두고 사용자 인증을 처리함!
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf().disable()
			.httpBasic().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(STATELESS)
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(customJwtAuthenticationEntryPoint)
			.accessDeniedHandler(customAccessDeniedHandler)
			.and()
			.authorizeHttpRequests()
			.requestMatchers(AUTH_WHITELIST).permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
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

