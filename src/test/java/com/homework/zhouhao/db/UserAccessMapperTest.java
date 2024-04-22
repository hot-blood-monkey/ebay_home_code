package com.homework.zhouhao.db;

import com.homework.zhouhao.common.BizRuntimeException;
import com.homework.zhouhao.common.ErrorCode;
import com.homework.zhouhao.common.FileUtil;
import com.homework.zhouhao.model.UserAccessRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserAccessMapperTest {

    @Spy
    private UserAccessMapper userAccessMapper;

    @Test
    void addUserAccess() {

        List<String> endpointList = Lists.newArrayList("resourceA", "resourceB");
        UserAccessRequest request = new UserAccessRequest();
        request.setUserId("123456");
        request.setEndpoint(endpointList);

        userAccessMapper.addUserAccess(request);
    }

    @Test()
    void addUserAccessErrorWithAppendLogFailed() {
        try (MockedStatic mockStatic = Mockito.mockStatic(FileUtil.class)) {
            mockStatic.when(() -> FileUtil.appendToFile(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(false);
            List<String> endpointList = Lists.newArrayList("resourceA", "resourceB");
            UserAccessRequest request = new UserAccessRequest();
            request.setUserId("123456");
            request.setEndpoint(endpointList);

            BizRuntimeException exception = assertThrows(BizRuntimeException.class, ()-> userAccessMapper.addUserAccess(request));

            assertEquals(ErrorCode.PERSIST_USER_ACCESS_ERROR.getCode(), exception.getStatusCode());
            assertEquals(ErrorCode.PERSIST_USER_ACCESS_ERROR.getDesc(), exception.getMessage());
        }
    }
}