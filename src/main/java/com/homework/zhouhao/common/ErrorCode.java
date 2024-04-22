package com.homework.zhouhao.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST_ERROR(400, "Bad Request"),

    UNAUTHORIZED(401, "Unauthorized"),

    FORBIDDEN(403, "Forbidden"),

    FORBIDDEN_RESOURCE(403, "User does not have access to this resource."),

    NOT_FOUND(404, "Not Found"),

    REQUEST_TIMEOUT(408, "Request Timeout"),

    SERVICE_INTERNAL_ERROR(500, "Service Internal Error"),


    GET_USER_INFO_ERROR(4002, "Get user info failed"),

    NOT_HAS_PERMISSION(4003, "Not Has Admin Permission"),

    NOT_ADMIN_ROLE(4004, "Not Admin Role, Only admin can access this endpoint."),

    USER_EXIST_ERROR(4005, "User is already existed"),

    INVALID_SIGN(4010, "invalid sign"),

    PERSIST_USER_ACCESS_ERROR(4011, "Failed to flush the access info in a json file");

    private int code;
    private String desc;
}
