package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.toMatrix
import org.springframework.stereotype.Service

@Service("Day15")
class Day15 : Day {
    override fun handlePart1(input: String): Long {
        val matrix = input.lines().filter { it.contains("#") }.joinToString("\n").toMatrix().map { it.toMutableList() }.toMutableList()
        val movements = input.lines().filter { !it.contains("#") }.joinToString("").trim()
        val warehouse = Warehouse(matrix, movements)
        warehouse.simulateMovements()
        return warehouse.calcGPSSum()
    }

    override fun handlePart2(input: String): Long {
        var relevantMatrixStr = input.lines().filter { it.contains("#") }.joinToString("\n")
        relevantMatrixStr = relevantMatrixStr.replace("#", "##")
        relevantMatrixStr = relevantMatrixStr.replace("O", "[]")
        relevantMatrixStr = relevantMatrixStr.replace(".", "..")
        relevantMatrixStr = relevantMatrixStr.replace("@", "@.")
        val matrix = relevantMatrixStr.toMatrix().map { it.toMutableList() }.toMutableList()
        val movements = input.lines().filter { !it.contains("#") }.joinToString("").trim()
        val warehouse = Warehouse(matrix, movements)
        warehouse.simulateMovements()
        return warehouse.calcGPSSum()
    }

    class Warehouse(
            private val matrix: MutableList<MutableList<String>>,
            private val movements: String,
    ) {
        private var robotPos: Pair<Int, Int>

        init {
            robotPos = findRobotPos()
        }

        companion object {
            val MOVEMENT_VECTORS = mapOf(
                    "<" to (0 to -1),
                    ">" to (0 to 1),
                    "^" to (-1 to 0),
                    "v" to (1 to 0)
            )
        }

        private fun findRobotPos(): Pair<Int, Int> {
            for (y in 0..matrix.lastIndex) {
                for (x in 0..matrix[0].lastIndex) {
                    if (matrix[y][x] == "@") {
                        return y to x
                    }
                }
            }
            error("Robot was not found!")
        }

        fun simulateMovements() {
            movements.forEachIndexed { index, c ->
                println("---$index($c)---")
                robotPos.move(MOVEMENT_VECTORS[c.toString()]!!)
                printWarehouse()
            }
        }

        private fun printWarehouse() {
            for (y in 0..matrix.lastIndex) {
                for (x in 0..matrix[0].lastIndex) {
                    print(matrix[y][x])
                }
                println()
            }
        }

        fun calcGPSSum(): Long {
            return (0..matrix.lastIndex).flatMap { y ->
                (0..matrix[0].lastIndex).map { x ->
                    if (matrix[y][x] in listOf("O", "[")) {
                        100L * y + x
                    } else {
                        0L
                    }
                }
            }.sum()
        }

        private fun Pair<Int, Int>.move(vector: Pair<Int, Int>): Boolean {
            val targetPos = (first + vector.first) to (second + vector.second)
            fun doMovement() {
                if (matrix[first][second] == "@") {
                    robotPos = targetPos.first to targetPos.second
                }
                matrix[targetPos.first][targetPos.second] = matrix[first][second]
                matrix[first][second] = "."
            }

            val movementAllowed = when (matrix[targetPos.first][targetPos.second]) {
                "#" -> false
                "." -> {
                    true
                }

                "O" -> {
                    targetPos.move(vector)
                }

                "[" -> {
                    if (vector.first == 0) {
                        //we move sidewards - no need to obey box width
                        targetPos.move(vector)
                    } else {
                        //Check right neighbor as it's part of the box and we move vertical
                        targetPos.move(vector) && (targetPos.first to targetPos.second + 1).move(vector)
                    }
                }

                "]" -> {
                    if (vector.first == 0) {
                        //we move sidewards - no need to obey box width
                        targetPos.move(vector)
                    } else {
                        //Check left neighbor as it's part of the box and we move vertical
                        targetPos.move(vector) && (targetPos.first to targetPos.second - 1).move(vector)
                    }
                }

                else -> error("Character ${matrix[targetPos.first][targetPos.second]} is not allowed")
            }
            if (movementAllowed) {
                doMovement()
            }
            return movementAllowed
        }
    }

}