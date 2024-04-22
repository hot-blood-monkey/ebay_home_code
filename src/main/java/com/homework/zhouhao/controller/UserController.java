package com.homework.zhouhao.controller;

import com.homework.zhouhao.common.AuthUserContext;
import com.homework.zhouhao.common.BizRuntimeException;
import com.homework.zhouhao.common.ErrorCode;
import com.homework.zhouhao.db.UserAccessMapper;
import com.homework.zhouhao.model.UserAccessRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    @Resource
    private UserAccessMapper userAccessMapper;

    @PostMapping("/admin/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserAccessRequest userAccessRequest) {
        if (!AuthUserContext.isAdminUser()) {
            throw new BizRuntimeException(ErrorCode.NOT_ADMIN_ROLE);
        }

        List<String> oldAccess = userAccessMapper.getUserAccess(userAccessRequest.getUserId());
        if (!CollectionUtils.isEmpty(oldAccess)) {
            throw new BizRuntimeException(ErrorCode.USER_EXIST_ERROR);
        }
        userAccessMapper.addUserAccess(userAccessRequest);
        return ResponseEntity.ok("User access added successfully.");
    }


    @GetMapping("/user/{resource}")
    public ResponseEntity<String> getResourceAccess(@PathVariable String resource) {
        String userId = AuthUserContext.getUserId();
        List<String> accessList = userAccessMapper.getUserAccess(userId);

        boolean isFind = accessList.stream().anyMatch(resource::equals);
        if (!isFind) {
            return ResponseEntity.status(ErrorCode.FORBIDDEN_RESOURCE.getCode())
                    .body(ErrorCode.FORBIDDEN_RESOURCE.getDesc());
        }

        return ResponseEntity.ok("User has access to resource: " + resource);
    }

}
