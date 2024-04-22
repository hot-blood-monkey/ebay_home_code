package com.homework.zhouhao.model;

import lombok.Data;

import java.util.List;

@Data
public class UserAccessRequest {
    private String userId;
    private List<String> endpoint;
}
