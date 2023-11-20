package com.server.dosopt.seminar.repository;

import com.server.dosopt.seminar.domain.Member;
import com.server.dosopt.seminar.domain.Post;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    default Post findBypostIdOrThrow(Long postId) {
        return findById(postId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
    }

    List<Post> findAllByMember(Member member);
}
