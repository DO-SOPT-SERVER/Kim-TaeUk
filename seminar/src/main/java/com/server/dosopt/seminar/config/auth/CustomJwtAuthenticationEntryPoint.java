package com.server.dosopt.seminar.config.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
AuthenticationEntryPoint의 구현체
Spring Security에서 발생하는 예외에 대한 커스텀 처리를 담당하는 클래스
유효한 자격 증명을 제공하지 않고 접근하려 할 때 401 Unauthorized 리턴!
 */

@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)  {
		sendError(response);
	}

	private void sendError(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}