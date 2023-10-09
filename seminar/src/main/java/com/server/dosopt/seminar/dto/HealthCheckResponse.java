package com.server.dosopt.seminar.dto;

import lombok.Getter;

@Getter
public class HealthCheckResponse {
    private static final String OK = "OK";
    private String status;

    public HealthCheckResponse() {
        this.status = OK;
    }
}
