package com.homework.zhouhao.common;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FileUtil {

    public static List<String> readFile(String filePath) {
        List<String> list = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            log.error("Read json file to string error", e);
        }

        return list;
    }

    public static boolean appendToFile(String filePath, String content) {
        boolean result = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
        } catch (IOException e) {
            log.error(ErrorCode.PERSIST_USER_ACCESS_ERROR.getDesc(), e);
            result = false;
        }
        return result;
    }
}
