package com.server.dosopt.seminar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/*
@Configuration: 설정 클래스임을 나타내는 어노테이션
-> spring application의 구성(configuration) 정보를 포함하고 있음을 의미함

1. bean 등록 (메서드 이름으로 bean 이름이 결정됨)
- spring container는 @Configuration이 붙어있는 클래스를 자동으로 bean으로 등록,
  @Cofiguration = @Target(ElementType.TYPE), @Retention(RetentionPolicy.RUNTIME), @Documented, @Component로 이루어져 있는데
  @Component 때문에 bean으로 등록됨
- 이후 클래스의 본문을 파싱하여 @Bean이 붙어있는 메서드도 bean으로 등록
- @Configuration 없이 @Bean만 사용하는 경우 bean으로 등록은 되지만 singleton 유지 X

추가로 bean.getClass()로 @Configuration & @Bean으로 등록한 bean을 출력해보면
클래스명 뒤에 CGLIB가 붙은 것을 확인할 수 있음
-> 클래스에 @Configuration 어노테이션이 붙으면 Spring은 해당 클래스를
   CGLIB(Code Generator Library)을 사용하여 동적으로 상속받은 proxy 클래스를 생성
   이 proxy 클래스는 원래의 @Configuration 클래스의 메서드를 오버라이드하고, bean을 생성하고 관리함

2. 외부 설정 정보 로드 등의 작업 담당
AwsConfig 클래스처럼 property

 */
@Configuration
public class AwsConfig {
	private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
	private static final String AWS_SECRET_ACCESS_KEY = "aws.secretAccessKey";

	private final String accessKey;
	private final String secretKey;
	private final String regionString;

	/*
	@Value 어노테이션을 통해 property 파일(application.yml)에서
	AWS 자격 증명 및 지역 정보(accessKey, secretKey, regionString)를 주입받아 사용
	 */
	public AwsConfig(@Value("${aws-property.access-key}") final String accessKey,
		@Value("${aws-property.secret-key}") final String secretKey,
		@Value("${aws-property.aws-region}") final String regionString) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.regionString = regionString;
	}

	@Bean
	public SystemPropertyCredentialsProvider systemPropertyCredentialsProvider() {
		/*
		SystemPropertyCredentialsProvider: system 환경변수에 access key와 secret key를 등록하여 사용
		AWS 자격 증명을 system property로 설정,
		해당 property를 사용하여 AWS 자격 증명을 제공하는
		SystemPropertyCredentialsProvider bean 생성 및 반환
		 */
		System.setProperty(AWS_ACCESS_KEY_ID, accessKey);
		System.setProperty(AWS_SECRET_ACCESS_KEY, secretKey);
		return SystemPropertyCredentialsProvider.create();
	}

	@Bean
	public Region getRegion() {
		// 주입된 지역 문자열 사용하여 Region 객체 생성 및 반환
		return Region.of(regionString);
	}

	@Bean
	public S3Client getS3Client() {
		/*
		위 두 메서드 이용하여
		AWS S3 client를 빌드하고 구성하기 위한 bean 생성
		 */
		return S3Client.builder()
			.region(getRegion())
			.credentialsProvider(systemPropertyCredentialsProvider())
			.build();
	}
}
