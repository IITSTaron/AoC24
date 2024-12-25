package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.toMatrix
import org.springframework.stereotype.Service

@Service("Day12")
class Day12 : Day {
    override fun handlePart1(input: String): Int {
        val matrix = input.toMatrix()
        val fieldsToPerimeters: Map<MutableList<Pair<Int, Int>>, Int> = determineFields(matrix).associateWith { determinePerimeter(matrix, it) }
        return fieldsToPerimeters.map { it.key.size * it.value }.sum()
    }

    override fun handlePart2(input: String): Int {
        val matrix = input.toMatrix()
        val fieldsToSides: Map<MutableList<Pair<Int, Int>>, Int> = determineFields(matrix).associateWith { determineSides(matrix, it) }
        return fieldsToSides.map { it.key.size * it.value }.sum()
    }

    private fun determinePerimeter(matrix: List<List<String>>, fieldCoords: MutableList<Pair<Int, Int>>): Int =
            fieldCoords.sumOf { coords ->
                val nextSteps = listOf(
                        Pair(coords.first, coords.second - 1),
                        Pair(coords.first, coords.second + 1),
                        Pair(coords.first - 1, coords.second),
                        Pair(coords.first + 1, coords.second),
                )
                nextSteps.map {
                    if (it.first !in matrix.indices || it.second !in matrix.indices || matrix[it.first][it.second] != matrix[coords.first][coords.second]) {
                        1
                    } else {
                        0
                    }
                }.sum()
            }

    private fun determineSides(matrix: List<List<String>>, fieldCoords: MutableList<Pair<Int, Int>>): Int {
        fun isOutsideBounds(y: Int, x: Int) = y !in matrix.indices || x !in matrix.indices

        val leftBounds = fieldCoords.filter { isOutsideBounds(it.first,it.second - 1) || matrix[it.first][it.second - 1] != matrix[it.first][it.second] }
        val rightBounds = fieldCoords.filter { isOutsideBounds(it.first, it.second + 1) || matrix[it.first][it.second + 1] != matrix[it.first][it.second] }
        val upperBounds = fieldCoords.filter { isOutsideBounds(it.first - 1, it.second) || matrix[it.first - 1][it.second] != matrix[it.first][it.second] }
        val lowerBounds = fieldCoords.filter { isOutsideBounds(it.first + 1, it.second) || matrix[it.first + 1][it.second] != matrix[it.first][it.second] }

        return listOf(
                leftBounds.groupBy { it.second }.map {
                    it.value.sortedBy { it.first }.zipWithNext { a, b -> b.first - a.first }.count { it > 1 } + 1
                }.sum(),
                rightBounds.groupBy { it.second }.map {
                    it.value.sortedBy { it.first }.zipWithNext { a, b -> b.first - a.first }.count { it > 1 } + 1
                }.sum(),
                upperBounds.groupBy { it.first }.map {
                    it.value.sortedBy { it.second }.zipWithNext { a, b -> b.second - a.second }.count { it > 1 } + 1
                }.sum(),
                lowerBounds.groupBy { it.first }.map {
                    it.value.sortedBy { it.second }.zipWithNext { a, b -> b.second - a.second }.count { it > 1 } + 1
                }.sum(),
        ).sum()
    }

    private fun determineFields(matrix: List<List<String>>): MutableList<MutableList<Pair<Int, Int>>> {
        val fields = mutableListOf<MutableList<Pair<Int, Int>>>()
        matrix.forEachIndexed { y, row ->
            row.forEachIndexed { x, s ->
                if (y to x !in fields.flatMap { it.toList() }) {
                    fields.add(traverseFieldsForFieldDeter(matrix, y to x, s).distinct().toMutableList())
                }
            }
        }
        return fields
    }

    fun traverseFieldsForFieldDeter(matrix: List<List<String>>, coords: Pair<Int, Int>, searchedContent: String, visited: MutableList<Pair<Int, Int>>? = null): List<Pair<Int, Int>> {
        val content = matrix[coords.first][coords.second]
        if (content != searchedContent) {
            return emptyList()
        }
        val nextSteps = listOf(
                Pair(coords.first, coords.second - 1),
                Pair(coords.first, coords.second + 1),
                Pair(coords.first - 1, coords.second),
                Pair(coords.first + 1, coords.second),
        ).filter { it.first in matrix.indices && it.second in matrix.indices && visited?.contains(it) != true }
        val results = nextSteps.flatMap {
            traverseFieldsForFieldDeter(matrix, it, searchedContent, (visited
                    ?: mutableListOf()).also { it.add(coords) })
        }.toMutableList()
        results.add(coords)
        return results
    }


}
