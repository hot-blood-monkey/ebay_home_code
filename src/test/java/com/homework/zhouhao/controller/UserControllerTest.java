package com.homework.zhouhao.controller;

import com.homework.zhouhao.common.AuthInterceptor;
import com.homework.zhouhao.common.ErrorCode;
import com.homework.zhouhao.common.JacksonUtils;
import com.homework.zhouhao.common.RoleEnum;
import com.homework.zhouhao.db.UserAccessMapper;
import com.homework.zhouhao.model.UserInfoVO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccessMapper userAccessMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void addUser() throws Exception {
        UserInfoVO userInfoVO = buildUserInfoVO(true);
        String jsonStr = JacksonUtils.toJson(userInfoVO);
        String encodeAuthStr = encodeAuthorizationHeader(jsonStr);

        Mockito.when(userAccessMapper.getUserAccess(Mockito.anyString()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AuthInterceptor.AUTH_HEADER_KEY, encodeAuthStr)
                .content("{\"userId\":123456,\"endpoint\":[\"resourceA\",\"resourceB\",\"resourceC\"]}"))
                .andExpect(status().isOk());
    }

    @Test
    public void addUserErrorWithNoAdminRole() throws Exception {
        UserInfoVO userInfoVO = buildUserInfoVO(false);
        String jsonStr = JacksonUtils.toJson(userInfoVO);
        String encodeAuthStr = encodeAuthorizationHeader(jsonStr);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AuthInterceptor.AUTH_HEADER_KEY, encodeAuthStr)
                .content("{\"userId\":123456,\"endpoint\":[\"resourceA\",\"resourceB\",\"resourceC\"]}"))
                .andExpect(status().is(ErrorCode.NOT_ADMIN_ROLE.getCode()));
    }

    @Test
    public void addUserErrorWithExistUser() throws Exception {
        UserInfoVO userInfoVO = buildUserInfoVO(true);
        String jsonStr = JacksonUtils.toJson(userInfoVO);
        String encodeAuthStr = encodeAuthorizationHeader(jsonStr);

        List<String> accessList = Lists.newArrayList("resourceA", "resourceB");
        Mockito.when(userAccessMapper.getUserAccess(Mockito.anyString()))
                .thenReturn(accessList);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AuthInterceptor.AUTH_HEADER_KEY, encodeAuthStr)
                .content("{\"userId\":123456,\"endpoint\":[\"resourceA\",\"resourceB\",\"resourceC\"]}"))
                .andExpect(status().is(ErrorCode.USER_EXIST_ERROR.getCode()));
    }

    @Test
    public void getResourceAccess() throws Exception {
        UserInfoVO userInfoVO = buildUserInfoVO(false);
        String jsonStr = JacksonUtils.toJson(userInfoVO);
        String encodeAuthStr = encodeAuthorizationHeader(jsonStr);

        List<String> accessList = Lists.newArrayList("resourceA", "resourceB");
        Mockito.when(userAccessMapper.getUserAccess(Mockito.anyString()))
                .thenReturn(accessList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/resourceA")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AuthInterceptor.AUTH_HEADER_KEY, encodeAuthStr))
                .andExpect(status().isOk());

    }

    @Test
    public void getResourceAccessErrorWithForbidden() throws Exception {
        UserInfoVO userInfoVO = buildUserInfoVO(false);
        String jsonStr = JacksonUtils.toJson(userInfoVO);
        String encodeAuthStr = encodeAuthorizationHeader(jsonStr);

        List<String> accessList = Lists.newArrayList("resourceA", "resourceB");
        Mockito.when(userAccessMapper.getUserAccess(Mockito.anyString()))
                .thenReturn(accessList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/resourceC")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AuthInterceptor.AUTH_HEADER_KEY, encodeAuthStr))
                .andExpect(status().is(ErrorCode.FORBIDDEN_RESOURCE.getCode()));

    }

    @Test
    public void getResourceAccessErrorWithUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/resourceC")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AuthInterceptor.AUTH_HEADER_KEY, ""))
                .andExpect(status().is(ErrorCode.UNAUTHORIZED.getCode()));
    }


    private String encodeAuthorizationHeader(String jsonStr) {
        return new String(Base64.getEncoder().encode(jsonStr.trim().getBytes()));
    }

    private UserInfoVO buildUserInfoVO(boolean isAdmin) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserId("123456");
        if (isAdmin) {
            userInfoVO.setRole(RoleEnum.ADMIN.getRole());
        } else {
            userInfoVO.setRole(RoleEnum.USER.getRole());
        }
        userInfoVO.setAccountName("XX");
        return userInfoVO;
    }
}