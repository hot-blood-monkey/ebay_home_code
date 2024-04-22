package com.homework.zhouhao.common;

import com.homework.zhouhao.model.UserInfoVO;

public class AuthUserContext {
    private static final ThreadLocal<UserInfoVO> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserInfo(UserInfoVO userInfoVO) {
        if (userInfoVO != null) {
            USER_INFO_THREAD_LOCAL.set(userInfoVO);
        } else {
            USER_INFO_THREAD_LOCAL.remove();
        }
    }

    public static UserInfoVO getUserInfo() {
        return USER_INFO_THREAD_LOCAL.get();
    }

    public static String getUserId() {
        UserInfoVO user = getUserInfo();
        if (user == null) {
            throw new BizRuntimeException(ErrorCode.GET_USER_INFO_ERROR);
        }
        return user.getUserId();
    }

    public static Boolean isAdminUser() {
        UserInfoVO user = getUserInfo();
        return RoleEnum.isAdmin(user.getRole());
    }

}
