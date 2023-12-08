package com.server.dosopt.seminar.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.server.dosopt.seminar.domain.Member;
import com.server.dosopt.seminar.domain.Post;
import com.server.dosopt.seminar.dto.post.PostCreateRequest;
import com.server.dosopt.seminar.exception.BusinessException;
import com.server.dosopt.seminar.external.S3Service;
import com.server.dosopt.seminar.repository.MemberJpaRepository;
import com.server.dosopt.seminar.repository.PostJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceV2 {
	private static final String POST_IMAGE_FOLDER_NAME = "post/";

	private final MemberJpaRepository memberJpaRepository;
	private final PostJpaRepository postJpaRepository;
	private final S3Service s3Service;

	@Transactional
	public String createV2(PostCreateRequest request, MultipartFile image, Long memberId) {
		try {
			final String imageUrl = s3Service.uploadImage(POST_IMAGE_FOLDER_NAME, image);
			Member member = memberJpaRepository.findByIdOrThrow(memberId);

			Post post = Post.builderWithImageUrl()
				.title(request.title())
				.content(request.content())
				.imageUrl(imageUrl)
				.member(member)
				.build();

			Post createdPost = postJpaRepository.save(post);
			return createdPost.getPostId().toString();
		} catch (RuntimeException | IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	public void deleteByIdV2(Long postId) {
		try {
			Post post = postJpaRepository.findById(postId)
				.orElseThrow(() -> new BusinessException("해당하는 게시글이 없습니다."));
			s3Service.deleteImage(post.getImageUrl());
			postJpaRepository.deleteById(postId);
		} catch (IOException | RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
