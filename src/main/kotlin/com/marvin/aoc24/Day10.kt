package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.toMatrix
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service("Day10")
class Day10 : Day {

    override fun handlePart1(input: String): Int {
        val matrix = input.toMatrix()
        val trailheads = determineTrailheads(matrix)
        return trailheads.sumOf { traverseTrailsUnique(matrix, it).distinct().size }
    }

    override fun handlePart2(input: String): Int {
        val matrix = input.toMatrix()
        val trailheads = determineTrailheads(matrix)
        return trailheads.sumOf { traverseTrailsAbs(matrix, it) }
    }

    fun traverseTrailsAbs(matrix: List<List<String>>, coords: Pair<Int, Int>, height: Int = 0, lastVisited: Pair<Int, Int>? = null): Int {
        val content = matrix[coords.first][coords.second].toInt()
        val nextSteps = listOf(
                Pair(coords.first, coords.second - 1),
                Pair(coords.first, coords.second + 1),
                Pair(coords.first - 1, coords.second),
                Pair(coords.first + 1, coords.second),
        ).filter { it.first in matrix.indices && it.second in matrix.indices && it != lastVisited }
        return when {
            content != height -> 0
            content == 9 -> 1
            content == height -> nextSteps.sumOf { traverseTrailsAbs(matrix, it, height + 1, coords) }
            else -> error("Should not happen")
        }
    }

    fun traverseTrailsUnique(matrix: List<List<String>>, coords: Pair<Int, Int>, height: Int = 0, lastVisited: Pair<Int, Int>? = null): List<Pair<Int, Int>?> {
        val content = matrix[coords.first][coords.second].toInt()
        val nextSteps = listOf(
                Pair(coords.first, coords.second - 1),
                Pair(coords.first, coords.second + 1),
                Pair(coords.first - 1, coords.second),
                Pair(coords.first + 1, coords.second),
        ).filter { it.first in matrix.indices && it.second in matrix.indices && it != lastVisited }
        return when {
            content != height -> listOf(null)
            content == 9 -> listOf(coords)
            content == height -> nextSteps.flatMap { traverseTrailsUnique(matrix, it, height + 1, coords) }.filterNotNull()
            else -> error("Should not happen")
        }
    }

    fun determineTrailheads(matrix: List<List<String>>): List<Pair<Int, Int>> =
            matrix.flatMapIndexed { y, row ->
                row.mapIndexedNotNull { x, c ->
                    if (c == "0") Pair(y, x) else null
                }
            }
}
