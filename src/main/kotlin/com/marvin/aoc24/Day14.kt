package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.second
import org.springframework.stereotype.Service

@Service("Day14")
class Day14 : Day {
    companion object {
        const val MATRIX_X_SIZE = 101
        const val MATRIX_Y_SIZE = 103
    }
    override fun handlePart1(input: String): Long {
        val robots = renderRobots(input)
        repeat(100000) { i ->
            robots.forEach { it.move() }
            if(robots.map { it.currentPos }.distinct().size == robots.size) {
                println("---${i+1}---")
                visualize(robots.map { it.currentPos })
                println("---------")
            }

        }
        val endPositions = robots.map { it.currentPos }
        return calcQuadrantSecurityScore(endPositions)
    }

    override fun handlePart2(input: String): Int = 0

    fun renderRobots(input: String): MutableList<Robot> = input.lines().filter { it.isNotEmpty() }.map { line ->
        line.split(" ").map { it.split("=").second() }.let {
            fun String.toPos(): Pair<Int, Int> = split(",").let { it.second().toInt() to it.first().toInt() }
            Robot(it.first().toPos(), it.second().toPos())
        }
    }.toMutableList()

    fun calcQuadrantSecurityScore(positions: List<Pair<Int, Int>>): Long {
        val midX = MATRIX_X_SIZE / 2
        val midY = MATRIX_Y_SIZE / 2
        val quadrants = listOf(
                (0..<midY) to (0..<midX),
                (0..<midY) to (midX + 1..<MATRIX_X_SIZE),
                (midY + 1..<MATRIX_Y_SIZE) to (0..<midX),
                (midY + 1..<MATRIX_Y_SIZE) to (midX + 1..<MATRIX_X_SIZE),
        )
        return quadrants.map { quadrant -> positions.count { it.first in quadrant.first && it.second in quadrant.second } }.fold(1) { acc, i -> acc * i }
    }

    fun visualize(positions: List<Pair<Int, Int>>) {
        for(y in 0..MATRIX_Y_SIZE) {
            for(x in 0..MATRIX_X_SIZE) {
                print(positions.count { it == y to x }.let { if(it == 0) "." else it })
            }
            println()
        }
    }

    class Robot(
            startingPos: Pair<Int, Int>,
            private val movingVector: Pair<Int, Int>,
    ) {
        var currentPos: Pair<Int, Int>

        init {
            currentPos = startingPos
        }

        fun move() {
            var x = currentPos.second
            var y = currentPos.first
            x = (x + movingVector.second) % MATRIX_X_SIZE
            y = (y + movingVector.first) % MATRIX_Y_SIZE
            if (x < 0) x += MATRIX_X_SIZE
            if (y < 0) y += MATRIX_Y_SIZE

            currentPos = y to x
        }
    }
}
