package com.server.dosopt.seminar.external;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.server.dosopt.seminar.config.AwsConfig;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3Service {

	private final String bucketName;
	private final AwsConfig awsConfig;
	private static final List<String> IMAGE_EXTENSIONS =
		Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
	private static final Long MAX_FILE_SIZE = 5 * 1024 * 1024L;

	public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, AwsConfig awsConfig) {
		this.bucketName = bucketName;
		this.awsConfig = awsConfig;
	}

	public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
		validateExtension(image);
		validateFileSize(image);

		final String key = directoryPath + generateImageFileName();
		final S3Client s3Client = awsConfig.getS3Client();

		/*
		S3에 업로드할 버킷 이름 설정

		S3 버킷 내에서 객체 고유하게 식별하는 키 설정
		-> key = 이미지 파일 경로 + UUID 값 concat

		업로드된 이미지의 콘텐츠 타입을 업로드할 객체의 콘텐츠 타입으로 설정

		콘텐츠의 표시 방식은 inline으로
		 */
		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(key)
			.contentType(image.getContentType())
			.contentDisposition("inline")
			.build();

		/*
		MultipartFile에서 byte 배열을 추출하고 RequestBody로 변환
		RequestBody에는 S3에 업로드할 이미지의 실제 데이터를 담고 있음
		 */
		RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
		s3Client.putObject(request, requestBody);
		return key;
	}

	public void deleteImage(String key) throws IOException {
		final S3Client s3Client = awsConfig.getS3Client();

		s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
			builder.bucket(bucketName)
				.key(key)
				.build()
		);
	}

	private String generateImageFileName() {
		return UUID.randomUUID().toString() + ".jpg";
	}

	private void validateExtension(MultipartFile image) {
		String contentType = image.getContentType();
		if (!IMAGE_EXTENSIONS.contains(contentType)) {
			throw new RuntimeException("이미지 확장자는 jpg, png, webp만 가능합니다!");
		}
	}

	private void validateFileSize(MultipartFile image) {
		if (image.getSize() > MAX_FILE_SIZE) {
			throw new RuntimeException("이미지 사이즈는 5MB를 넘을 수 없습니다!");
		}
	}
}
