package com.server.dosopt.seminar.config.jwt;

import static com.server.dosopt.seminar.config.jwt.JwtValidationType.*;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/*
JWT(JSON Web Token)
: Header, Payload, Signature 3가지 부분으로 이루어짐

1. Header: typ과 alg로 구성
- typ: token의 type을 지정 -> JWT
- alg: hashing algorithm을 지정 -> HS256나 RS256

2. Payload: payload에는 토큰에서 사용할 정보의 조각인 claim이 담겨있음,
claim은 총 3가지, json(key-value) 형태로 다수의 정보를 넣을 수 있음

- registered claim: 토큰 정보 표현을 위해 이미 정해진 종류의 데이터들
: iss(발급자), sub(제목), aud(대상자), exp(만료 시간), nbf(활성 날짜), iat(발급 시간), jtl(식별자)
-> 등록된 claim의 사용은 모두 optional임

- public claim: 사용자 정의 claim,
공개용 정보를 위해 사용, 충돌 방지를 위해 URI 포맷을 이용

- private claim: 사용자 정의 claim,
서버와 클라이언트 사이에 임의로 지정한 정보 저장

3. Signature
: Header의 encoding 값(Base64)과 Payload의 encoding 값(Base64)을 합친 후,
secret key를 이용해 Header에서 정의한 알고리즘으로 해싱하여 생성!
-> {Header를 Base64로 인코딩한 값} + "." + {Payload를 Base64로 인코딩한 값}을
JWT_SECRET_KEY를 이용해 HS256 알고리즘으로 해싱하여 생성!

이렇게 생성된 JWT는 HTTP 통신 시 Authorization의 key의 value로 사용됨
ex)
{
	"Authorization": "Bearer {JWT 값}",
}
 */

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private static final String MEMBER_ID = "memberId";        // claim의 key
	private static final Long EXPIRED_MS = 1000 * 60 * 60L;        // 1시간으로 설정

	@Value("${jwt.secret}")
	private String JWT_SECRET_KEY;        // JWT의 signature에서 사용

	/*
	@PostConstruct: DI가 이루어진 후 초기화를 수행하는 메서드
	순서: 생성자 호출 -> 의존성 주입(@Autowired, @RequiredArgsConstructor) -> @PostConstruct

	Spring에서 bean은 ApplicationContext가 초기화될 때 발생하는데,
	bean을 필요한 경우에만 생성하고 초기화하기 때문에 모든 bean이 미리 생성되어 있지는 X

	환경 변수 설정, 외부 리소스 로딩, 초기화된 DB 연결 등의 작업은
	애플리케이션이 시작될 때 bean이 초기화되어야 한다
	-> @PostContruct를 사용함으로써 애플리케이션 라이프사이클 동안 해당 bean의 초기화를 보장할 수 있음!
	 */

	// JWT_SECRET_KEY를 Base64로 인코딩한 값이 아닌 String으로 그대로 써야 jwt.io에서 generate한 값과 같게 나옴
	// @PostConstruct
	// protected void init() {
	// 	/*
	// 	yml에서 @Value 어노테이션을 통해 주입받은 JWT_SECRET을
	// 	getBytes(StandardCharsets.UTF_8)를 통해 UTF-8로 인코딩 된 바이트 배열로 변환,
	// 	Base64.getEncoder().encodeToString()를 통해 UTF-8로 인코딩 된 바이트 배열을 Base64로 인코딩한 문자열로 변환
	// 	 */
	// 	JWT_SECRET_KEY = Base64.getEncoder()
	// 		.encodeToString(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	// }

	public String generateJwt(Authentication authentication) {
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)        // Header에서 typ: JWT 명시
			.setClaims(generateClaims(authentication))        // Claim
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)        // Signature
			.compact();
	}

	public JwtValidationType validateToken(String token) {
		try {
			final Claims claims = getClaim(token);
			return VALID_JWT;
		} catch (MalformedJwtException ex) {
			return INVALID_JWT_TOKEN;
		} catch (ExpiredJwtException ex) {
			return EXPIRED_JWT_TOKEN;
		} catch (UnsupportedJwtException ex) {
			return UNSUPPORTED_JWT_TOKEN;
		} catch (IllegalArgumentException ex) {
			return EMPTY_JWT;
		}
	}

	public Long getUserFromJwt(String token) {
		Claims claims = getClaim(token);
		return Long.valueOf(claims.get(MEMBER_ID).toString());
	}

	private Claims generateClaims(Authentication authentication) {
		// payload 중 registered claim 설정 부분
		final Claims claims = Jwts.claims()
			.setIssuer("dosopt.server.seminar.com")
			.setSubject("memberIdToken")
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRED_MS));

		/*
		payload 중 private claim 설정 부분
		: 서버 - 클라이언트 간의 데이터 교환에 사용됨

		-> 사용자의 식별 정보를 MEMBER_ID 라는 key로 클레임에 추가,
		value는 authentication.getPrincipal()로 설정

		authentication.getPrincipal(): Spring Security에서 현재 사용자의 principal(주체) 정보를 가져오는 메서드
		주로 UserDetails 객체가 principal로 사용됨
		 */
		claims.put(MEMBER_ID, authentication.getPrincipal());

		return claims;
	}

	private Claims getClaim(final String token) {
		/*
		.parserBuilder(): JWT를 파싱하기 위해 parseBuilder 객체 생성
		.setSigningKey(getSigningKey()): signature 확인 위해 signature key 설정
		.build(): 빌더 객체를 통해 파서 생성
		.parseClaimsJws(token): 주어진 JWT를 파싱하고 signature가 올바른지 확인
		-> signature가 올바르지 않는 경우 SignatureException 발생
		.getBody(): JWT의 claim 부분 반환!
		 */
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
		// JWT_SECRET_KEY를 바이트 배열로 만들고 HS256 알고리즘을 사용하여 JWT signature에 사용할 secret key 생성!
	}
}
