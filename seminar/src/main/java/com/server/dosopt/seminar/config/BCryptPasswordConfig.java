package com.server.dosopt.seminar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BCryptPasswordConfig {

	private static final int STRENGTH = 10;
	/*
	STRENGTH: 보안 강도(default가 10)
	 */

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(STRENGTH);
	}
	/*
	BCryptPasswordEncoder bean을 생성하여 Spring Security에서 사용될 수 있도록 함

	BCryptPasswordEncoder: hash 기반 암호화를 제공하는 Spring Security의 PasswordEncoder의 구현체
	 */
}
