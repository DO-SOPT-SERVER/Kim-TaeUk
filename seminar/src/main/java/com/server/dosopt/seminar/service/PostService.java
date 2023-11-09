package com.server.dosopt.seminar.service;

import com.server.dosopt.seminar.domain.Member;
import com.server.dosopt.seminar.domain.Post;
import com.server.dosopt.seminar.dto.member.MemberGetResponse;
import com.server.dosopt.seminar.dto.post.PostCreateRequest;
import com.server.dosopt.seminar.dto.post.PostGetResponse;
import com.server.dosopt.seminar.dto.post.PostUpdateRequest;
import com.server.dosopt.seminar.repository.MemberJpaRepository;
import com.server.dosopt.seminar.repository.PostJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    // 이거 DI 하는 이유? member(member)해서 Member member = memberJpaRepository.findByIdOrThrow(memberId);하려고

    @Transactional
    public String create(PostCreateRequest request, Long memberId) {
        Member member = memberJpaRepository.findByIdOrThrow(memberId);
        Post post = Post.builder()
                .member(member)
                .title(request.title())
                .content(request.content())
                .build();

        Post createdPost = postJpaRepository.save(post);
        return createdPost.getPostId().toString();
    }

    public PostGetResponse getPostBypostId(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 게시글이 없습니다."));
        return PostGetResponse.of(post);
    }

    public List<PostGetResponse> getPostsBymemberId(Long memberId) {
        Member member = memberJpaRepository.findByIdOrThrow(memberId);
        List<Post> postList = postJpaRepository.findAllByMember(member);
        return postList.stream()
                .map(PostGetResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePostBypostId(Long postId) {
        Post post = postJpaRepository.findBypostIdOrThrow(postId);
        postJpaRepository.delete(post);
    }

    @Transactional
    public void updateContentBypostId(Long postId, PostUpdateRequest request) {
        Post post = postJpaRepository.findBypostIdOrThrow(postId);
        post.updateContent(request.content());
    }
}
