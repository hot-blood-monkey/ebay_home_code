package com.homework.zhouhao.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.homework.zhouhao.common.BizRuntimeException;
import com.homework.zhouhao.common.ErrorCode;
import com.homework.zhouhao.common.FileUtil;
import com.homework.zhouhao.common.JacksonUtils;
import com.homework.zhouhao.model.UserAccessRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserAccessMapper {
    private static final String SPLIT_CHAR = "###";
    private static final String COMMIT_CHAR = "commit";
    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final String FILE_PATH = CURRENT_DIR + File.separator + "UserAccessDBFile";
    private static Map<String, List<String>> CACHE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
        };
        // load file
        List<String> list = FileUtil.readFile(FILE_PATH);
        // check row and put k-v into cache map
        list.stream().forEach(s -> {
            String[] entities = s.split(SPLIT_CHAR);
            if (entities.length != 3) {
                return;
            }
            if (!COMMIT_CHAR.equals(entities[2])) {
                return;
            }
            String key = entities[0];
            List<String> value = JacksonUtils.fromJson(entities[1], typeReference);
            CACHE_MAP.put(key, value);
        });
    }

    public List<String> getUserAccess(String userId) {
        return CACHE_MAP.getOrDefault(userId, Collections.EMPTY_LIST);
    }

    /**
     * add user access info to cache map and persist to local file
     * @param userAccessRequest
     */
    public synchronized void addUserAccess(UserAccessRequest userAccessRequest) {
        String context = userAccessRequest.getUserId() + SPLIT_CHAR
                + JacksonUtils.toJson(userAccessRequest.getEndpoint()) + SPLIT_CHAR
                + COMMIT_CHAR + "\n";
        boolean isSuc = FileUtil.appendToFile(FILE_PATH, context);
        if (!isSuc) {
            throw new BizRuntimeException(ErrorCode.PERSIST_USER_ACCESS_ERROR);
        }
        CACHE_MAP.put(userAccessRequest.getUserId(), userAccessRequest.getEndpoint());
    }


}
