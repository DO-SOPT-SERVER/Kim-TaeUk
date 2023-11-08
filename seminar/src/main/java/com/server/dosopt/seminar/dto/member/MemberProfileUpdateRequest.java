package com.server.dosopt.seminar.dto.member;


import com.server.dosopt.seminar.domain.Part;

public record MemberProfileUpdateRequest(
        int generation,
        Part part
) {
}