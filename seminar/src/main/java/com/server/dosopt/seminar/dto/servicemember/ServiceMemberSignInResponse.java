package com.server.dosopt.seminar.dto.servicemember;

public record ServiceMemberSignInResponse(
	String token
) {
	public static ServiceMemberSignInResponse of(String token) {
		return new ServiceMemberSignInResponse(token);
	}
}
