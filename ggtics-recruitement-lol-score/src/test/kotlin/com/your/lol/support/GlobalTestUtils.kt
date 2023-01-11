package com.your.lol.support

import com.google.gson.reflect.TypeToken
import com.your.lol.dto.riot.MatchDto
import com.your.lol.domain.ChampionDataDto
import com.your.lol.dto.riot.StatDto
import org.assertj.core.api.Assertions
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.function.Executable


abstract class GlobalTestUtils {

    protected fun <T> assertEquals(actual: T, expected: T): Executable {
        return Executable { Assertions.assertThat(actual).isEqualTo(expected) }
    }

    protected fun <T> assertCloseTo(actual: Double, expected: Double, offset: Double): Executable {
        return Executable { Assertions.assertThat(actual).isCloseTo(expected, offset(offset)) }
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