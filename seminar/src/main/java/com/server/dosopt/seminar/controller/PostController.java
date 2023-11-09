package com.server.dosopt.seminar.controller;

import com.server.dosopt.seminar.dto.post.PostCreateRequest;
import com.server.dosopt.seminar.dto.post.PostGetResponse;
import com.server.dosopt.seminar.dto.post.PostUpdateRequest;
import com.server.dosopt.seminar.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private static final String CUSTOM_AUTH_ID = "X-AUTH-Id";
    public static final String POST_BASE_URL = "/api/post/";

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestHeader(CUSTOM_AUTH_ID) Long memberId,
                                           @RequestBody PostCreateRequest request) {
        String postId = postService.create(request, memberId);
        URI location = URI.create(POST_BASE_URL + postId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResponse> getPost(@PathVariable Long postId) {
        PostGetResponse response = postService.getPostBypostId(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostGetResponse>> getPosts(@RequestHeader(CUSTOM_AUTH_ID) Long memberId) {
        List<PostGetResponse> postGetResponseList = postService.getPostsBymemberId(memberId);
        return ResponseEntity.ok(postGetResponseList);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePostBypostId(postId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody PostUpdateRequest request) {
        postService.updateContentBypostId(postId, request);
        return ResponseEntity.noContent().build();
    }
}
