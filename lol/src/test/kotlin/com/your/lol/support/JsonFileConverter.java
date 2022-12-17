package com.your.lol.support;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.your.lol.dto.riot.MatchDto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;

public class JsonFileConverter {

    private static final Gson gson = new Gson();

    private JsonFileConverter() {
    }

    public static <T> T fromJsonFile(String filePath, Class<T> type) {
        BufferedReader bufferedReader = read(filePath);
        return gson.fromJson(bufferedReader, type);
    }

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
