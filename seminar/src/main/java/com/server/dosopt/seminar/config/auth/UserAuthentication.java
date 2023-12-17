package com.server.dosopt.seminar.config.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/*
Spring Security에서 사용자를 인증하기 위한 커스텀한 Authentication 객체를 정의한 클래스
 */

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

	// 사용자 인증 객체 생성
	public UserAuthentication(
		Object principal,
		Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}
}