package com.marvin.aoc24

import org.springframework.stereotype.Service
import kotlin.math.abs

@Service("Day2")
class Day2 : Day {
    override fun handlePart1(input: String): Int {
        val reports = renderLists(input)
        return reports.count { it.isSafe1() }
    }

    override fun handlePart2(input: String): Int {
        val reports = renderLists(input)
        return reports.count { it.isSafe2() }
    }

    fun renderLists(input: String): List<List<Int>> =
        input.lines().filter { it.isNotBlank() }.map { it.split(" ").map { it.toInt() } }

    fun List<Int>.isSafe1(): Boolean {
        if (sorted() != this && sortedDescending() != this) return false
        return mapIndexed { index, level ->
            if (index == this.lastIndex) return true
            val next = this[index + 1]
            val diff = abs(level - next)
            if (diff !in (1..3)) return false
        }.all { true }
    }

    fun List<Int>.isSafe2(): Boolean {
        forEachIndexed { index, item ->
            val listWoItem = toMutableList()
            listWoItem.removeAt(index)
            if(listWoItem.isSafe1()) return true
        }
        return false
    }

}
