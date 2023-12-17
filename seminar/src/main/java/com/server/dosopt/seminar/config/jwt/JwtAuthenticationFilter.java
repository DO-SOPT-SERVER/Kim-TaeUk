package com.server.dosopt.seminar.config.jwt;

import static com.server.dosopt.seminar.config.jwt.JwtValidationType.*;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.server.dosopt.seminar.config.auth.UserAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	/*
	실제 filter의 동작을 정의하는 메서드:
	request에서 JWT를 추출하고 valid한 token인지 확인,
	valid한 token이라면 memberId를 추출하여 인증 객체(authenticationToken) 만들고,
	Spring Security의 SecurityContextHolder에 인증 객체 저장
	나머지 FilterChain 수행할 수 있게 doFilter(request, response)를 호출
	 */
	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		try {
			final String token = getJwtFromRequest(request);

			if (jwtProvider.validateToken(token) == VALID_JWT) {
				Long memberId = jwtProvider.getUserFromJwt(token);

				// authentication 객체 생성 -> principal에 유저정보를 담는다.
				UserAuthentication userAuthentication =
					new UserAuthentication(memberId.toString(), null, null);
				userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userAuthentication);
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
		filterChain.doFilter(request, response);
	}

	/*
	보통 client에서 JWT를 Http request 헤더에 Authorization: "Bearer {JWT 값}" 형식으로 전송하므로
	slicing하여 JWT 값을 추출 함
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring("Bearer ".length());
		}
		return null;
	}
}