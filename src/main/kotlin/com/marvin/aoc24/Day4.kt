package com.marvin.aoc24

import org.springframework.stereotype.Service

@Service("Day4")
class Day4 : Day {
    override fun handlePart1(input: String): Int {
        return renderText(input).sumOf { it.split("XMAS").size - 1 }
    }

    override fun handlePart2(input: String): Int {
        val matrix = input.lines().map { it.split("").filter { it.isNotBlank() } }.filter { it.isNotEmpty() }
        var masCounter = 0
        for (y in matrix.indices) {
            for (x in matrix.indices) {
                if (matrix[y][x] == "A") {
                    runCatching {
                        val tl = matrix[y - 1][x - 1]
                        val tr = matrix[y - 1][x + 1]
                        val bl = matrix[y + 1][x - 1]
                        val br = matrix[y + 1][x + 1]
                        val tlBrCrit = (tl == "M" && br == "S") || (tl == "S" && br == "M")
                        val blTrCrit = (bl == "M" && tr == "S") || (bl == "S" && tr == "M")
                        if (tlBrCrit && blTrCrit) {
                            println("identified X-MAS at ($y,$x)")
                            masCounter++
                        }
                    }
                }
            }
        }
        return masCounter
    }

    fun renderText(input: String, isPart2: Boolean = false): List<String> {
        val lines = input.lines().filter { it.isNotBlank() }.filter { it.isNotEmpty() }
        val leftRight = lines
        val rightLeft = lines.map { it.reversed() }
        val cols = input.cols()
        val topDown = cols
        val bottomDown = cols.map { it.reversed() }
        val diagonalsTLBR = input.diagonalsTLBR()
        val diagonalsBRTL = diagonalsTLBR.map { it.reversed() }
        val diagonalsTRBL = input.diagonalsTRBL()
        val diagonalsBLTR = diagonalsTRBL.map { it.reversed() }

        if (isPart2) {
            return (diagonalsTLBR + diagonalsBRTL + diagonalsTRBL + diagonalsBLTR)
        }

        return (leftRight + rightLeft + topDown + bottomDown + diagonalsTLBR + diagonalsBRTL + diagonalsTRBL + diagonalsBLTR)
    }

    fun String.cols(): List<String> {
        val matrix = lines().map { it.split("").filter { it.isNotBlank() } }.filter { it.isNotEmpty() }
        require(matrix.size == matrix.first().size) //make sure it's a square
        val cols = mutableListOf<String>()
        matrix.indices.map { y ->
            var col = ""
            matrix.indices.map { x ->
                col += matrix[x][y]
            }
            cols.add(col)
        }
        return cols
    }

    fun String.diagonalsTLBR(): List<String> {
        val matrix = lines().map { it.split("").filter { it.isNotBlank() } }.filter { it.isNotEmpty() }
        require(matrix.size == matrix.first().size) //make sure it's a square
        val lines = mutableListOf<String>()
        (matrix.lastIndex * (-1)..matrix.lastIndex).map { x ->
            var line = ""
            matrix.indices.map { y ->
                runCatching { line += matrix[y][x + y] }
            }
            lines.add(line)
        }
        return lines
    }

    fun String.diagonalsTRBL(): List<String> {
        val matrix = lines().map { it.split("").filter { it.isNotBlank() } }.filter { it.isNotEmpty() }
        require(matrix.size == matrix.first().size) //make sure it's a square
        val lines = mutableListOf<String>()
        (0..matrix.lastIndex * 2).map { x ->
            var line = ""
            matrix.indices.map { y ->
                runCatching { line += matrix[y][x - y] }
            }
            lines.add(line)
        }
        return lines
    }
}
