package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.second
import com.marvin.aoc24.ListUtil.third
import org.springframework.stereotype.Service

@Service("Day3")
class Day3 : Day {
    override fun handlePart1(input: String): Int =
        renderMultiplications(input, Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")).sumOf {
            it.first * it.second
        }

    override fun handlePart2(input: String): Int {
        val mulRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        val disabledRegex = Regex("don't\\(\\).*?(do\\(\\)|\$)")
        val cleanedInput = input.replace("\n", "").replace("\r", "").replace(disabledRegex, "")
        return renderMultiplications(cleanedInput, mulRegex).sumOf {
            it.first * it.second
        }
    }

    fun renderMultiplications(input: String, regex: Regex): List<Pair<Int, Int>> = regex.findAll(input).map {
        it.groupValues.let {
            it.second().toInt() to it.third().toInt()
        }
    }.toList()
}
