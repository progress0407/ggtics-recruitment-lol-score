package com.your.lol.support

import com.google.gson.reflect.TypeToken
import com.your.lol.dto.riot.MatchDto
import com.your.lol.domain.ChampionDataDto
import com.your.lol.domain.dto.PreProcessedData
import com.your.lol.dto.riot.StatDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.function.Executable
import kotlin.streams.toList


abstract class GlobalTestUtils {

    companion object {
        fun List<PreProcessedData>.findChampionsByName(championName: String): List<PreProcessedData> {
            return this.stream()
                    .filter { preProcessData -> preProcessData.championName == championName }
                    .toList()
        }
    }

    protected fun <T> assertEquals(actual: T, expected: T): Executable {
        return Executable { assertThat(actual).isEqualTo(expected) }
    }

    protected fun <T> assertCloseTo(actual: Double, expected: Double, offset: Double): Executable {
        return Executable { assertThat(actual).isCloseTo(expected, offset(offset)) }
    }

    protected fun findChampionByName(statDto: StatDto, championName: String): ChampionDataDto {
        return statDto.championDataDtos.stream()
                .filter { championDataDto -> championDataDto.championName == championName }
                .findAny()
                .get()
    }

    protected fun createTestData(filePath: String): List<MatchDto> {
        return JsonFileConverter.fromJsonFile(filePath, object : TypeToken<List<MatchDto>>() {})
    }
}
