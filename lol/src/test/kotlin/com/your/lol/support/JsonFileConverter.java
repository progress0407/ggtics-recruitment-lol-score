package com.your.lol.support;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonFileConverter {

    private static final Gson gson = new Gson();

    private JsonFileConverter() {
    }

    /**
     * 일반 제네릭의 경우 아래 메서드를 사용을 권장합니다.
     */
    public static <T> T fromJsonFile(String filePath, Class<T> type) {
        BufferedReader bufferedReader = read(filePath);
        return gson.fromJson(bufferedReader, type);
    }

    /**
     * 복합 타입을 가진 제네릭의 경우 (슈퍼 타입 토큰) 아래 메서드 사용을 권장합니다.
     */
    public static <T> T fromJsonFile(String filePath, TypeToken<T> typeToken) {
        BufferedReader bufferedReader = read(filePath);
        return gson.fromJson(bufferedReader, typeToken);
    }

    @NotNull
    private static BufferedReader read(String filePath) {
        try {
            File jsonFile = new ClassPathResource(filePath).getFile();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile));
            return bufferedReader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
