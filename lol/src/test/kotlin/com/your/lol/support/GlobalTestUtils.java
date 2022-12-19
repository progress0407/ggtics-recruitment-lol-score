package com.your.lol.support;

import com.google.gson.reflect.TypeToken;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.statistics.ChampionDataDto;
import com.your.lol.dto.statistics.StatsDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

public abstract class GlobalTestUtils {

    @NotNull
    protected static <T> Executable assertEquals(T actual, T expected) {
        return () -> assertThat(actual).isEqualTo(expected);
    }

    @NotNull
    protected static Executable assertCloseTo(double actual, double expected, double offset) {
        return () -> assertThat(actual).isCloseTo(expected, offset(offset));
    }

    @NotNull
    protected static ChampionDataDto findChampionByName(StatsDto stats, String championName) {
        return stats.getChampionDataDtos().stream()
                .filter(it -> it.getChampionName().equals(championName))
                .findAny()
                .get();
    }

    protected List<MatchDto> createTestData(String filePath) {
        return JsonFileConverter.fromJsonFile(
                filePath,
                new TypeToken<>() {
                });
    }
}
