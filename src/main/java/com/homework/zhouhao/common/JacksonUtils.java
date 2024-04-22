package com.homework.zhouhao.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collections;

@Slf4j
public class JacksonUtils {

    private final static ObjectMapper om = new ObjectMapper();

    static {
        om.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public static String toJson(Object obj) {
        if (obj == null) {
            obj = Collections.EMPTY_MAP;
        }
        try {
            return om.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String content, Class<T> valueType) {
        try {
            return om.readValue(content, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String content, TypeReference<T> typeReference) {
        try {
            return om.readValue(content, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(File file, TypeReference<T> typeReference) {
        try {
            return om.readValue(file, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object obj, Class<T> valueType) {
        try {
            return om.convertValue(obj, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T fromObject(Object obj, TypeReference<T> typeReference) {
        try {
            return om.convertValue(obj, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
