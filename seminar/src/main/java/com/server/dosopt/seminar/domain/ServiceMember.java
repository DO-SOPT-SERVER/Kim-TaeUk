package com.server.dosopt.seminar.domain;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ServiceMember {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String nickname;
	private String password;

	@Builder
	public ServiceMember(String nickname, String password) {
		this.nickname = nickname;
		this.password = password;
	}
}
