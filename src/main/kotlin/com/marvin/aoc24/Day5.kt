package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.second
import org.springframework.stereotype.Service

@Service("Day5")
class Day5 : Day {
    override fun handlePart1(input: String): Int {
        val rules = renderRules(input)
        val updates = renderUpdates(input)
        val validUpdates = updates.filter { it.isValid(rules) }

        return validUpdates.sumOf { it[it.size/2] }
    }

    override fun handlePart2(input: String): Int {
        val rules = renderRules(input)
        val updates = renderUpdates(input)
        val invalidUpdates = updates.filter { it.isValid(rules).not() }
        val fixedUpdates = invalidUpdates.map { it.fixViolations(rules) }

        return fixedUpdates.sumOf { it[it.size/2] }
    }

    fun List<Int>.isValid(rules: List<Pair<Int, Int>>): Boolean {
        forEachIndexed { index, item ->
            val violates1 = index != 0 && rules.any { it.first == item && it.second in subList(0, index) }
            val violates2 =
                index != lastIndex && rules.any {
                    it.second == item && it.first in subList(
                        index + 1,
                        lastIndex
                    )
                }
            if (violates1 || violates2) {
                return false
            }
        }
        return true
    }

    fun List<Int>.fixViolations(rules: List<Pair<Int, Int>>): List<Int> {
        val fixedVersion = this.toMutableList()
        forEachIndexed { index, _ ->
            if(index != 0) {
                val sublistToScan = fixedVersion.subList(0, index - 1)
                val violatedRule = rules.find { it.first == fixedVersion[index] && it.second in sublistToScan }
                if(violatedRule != null) {
                    fixedVersion[fixedVersion.indexOf(violatedRule.second)] = violatedRule.first
                    fixedVersion[index] = violatedRule.second

                }
            }
            if(index != lastIndex) {
                val sublistToScan = fixedVersion.subList(index + 1, fixedVersion.size)
                val violatedRule = rules.find { it.second == fixedVersion[index] && it.first in sublistToScan }
                if(violatedRule != null) {
                    fixedVersion[fixedVersion.indexOf(violatedRule.first)] = violatedRule.second
                    fixedVersion[index] = violatedRule.first
                }
            }
        }
        return if(fixedVersion.isValid(rules)) fixedVersion else fixedVersion.fixViolations(rules)
    }

    fun renderRules(input: String): List<Pair<Int, Int>> = input.lines().filter { it.contains("|") }.map {
        it.trim().split("|").let { it.first().toInt() to it.second().toInt() }
    }

    fun renderUpdates(input: String): List<List<Int>> =
        input.lines().filter { it.contains(",") }.map { it.split(",").map { it.toInt() } }
}
