package com.marvin.aoc24

import org.springframework.stereotype.Service
import kotlin.math.abs

@Service("Day1")
class Day1 : Day {
    override fun handlePart1(input: String): Int {
        val (left, right) = renderLists(input)
        val distances = mutableListOf<Int>()
        left.sorted().forEachIndexed { index, item ->
            distances.add(abs(item - right.sorted()[index]))
        }
        return distances.sum()
    }

    override fun handlePart2(input: String): Int {
        val (left, right) = renderLists(input)
        val scores = mutableListOf<Int>()
        left.forEach { litem ->
            scores.add(litem * right.count { it == litem  })
        }
        return scores.sum()
    }

    fun renderLists(input: String): Pair<List<Int>, List<Int>> {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        input.lines().filter { it.isNotBlank() }.forEach {
            it.split(" ").filter { it.isNotBlank() }.let {
                left.add(it[0].toInt())
                right.add(it[1].toInt())
            }
        }
        return (Pair(left, right))
    }

}
