package com.server.dosopt.seminar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.dosopt.seminar.domain.ServiceMember;

public interface ServiceMemberJpaRepository extends JpaRepository<ServiceMember, Long> {
	Optional<ServiceMember> findByNickname(String nickname);
}