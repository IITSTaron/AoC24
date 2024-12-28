package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.second
import com.marvin.aoc24.ListUtil.third
import com.marvin.aoc24.ListUtil.toMatrix
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service("Day13")
class Day13 : Day {
    override fun handlePart1(input: String): Int {
        val games = renderGames(input)
        return games.mapNotNull { it.findCheapestOption() }.sum()
    }

    override fun handlePart2(input: String): Long {
        val increasement = 10000000000000
        val games = renderGames(input).onEach { it.priceCoords = it.priceCoords.first + increasement to it.priceCoords.second + increasement}
        return games.mapNotNull { it.findCheapestOptionMathematically() }.sum()
    }

    fun renderGames(input: String): List<Game> = input.lines().chunked(4).map {
        val aVectors = it.first().replace("Button A: ", "").split(",").map { it.trim().split("+").second().toInt() }.let { it.first() to it.second() }
        val bVectors = it.second().replace("Button B: ", "").split(",").map { it.trim().split("+").second().toInt() }.let { it.first() to it.second() }
        val priceCoords = it.third().replace("Price: ", "").split(",").map { it.trim().split("=").second().toInt() }.let { it.first().toLong() to it.second().toLong() }
        Game(aVectors, bVectors, priceCoords)
    }

    class Game(
            private val aVectors: Pair<Int, Int>,
            private val bVectors: Pair<Int, Int>,
            var priceCoords: Pair<Long, Long>,
    ) {
        fun findCheapestOption(): Int? {
            val winningChips = mutableListOf<Int>()
            repeat(MAX_PRESSES) { aIndex ->
                repeat(MAX_PRESSES) { bIndex ->
                    val aPresses = aIndex + 1
                    val bPresses = bIndex + 1
                    if ((aPresses * aVectors.first + bPresses * bVectors.first).toLong() == priceCoords.first &&
                            (aPresses * aVectors.second + bPresses * bVectors.second).toLong() == priceCoords.second) {
                        winningChips.add(aPresses * A_PRICE + bPresses * B_PRICE)
                    }
                }
            }
            return winningChips.minOrNull()
        }

        fun findCheapestOptionMathematically(): Long? {
            // 94a + 22b = 8400; 34a + 67b = 5400
            // a = 8400/94 - 22b/94; [...]
            val resLeft = priceCoords.first.toDouble() / aVectors.first.toDouble()
            val bLeft = bVectors.first.toDouble() / aVectors.first.toDouble()
            // [...]; 34a + 67b = 5400
            val resRight = priceCoords.second.toDouble() - aVectors.second.toDouble() * resLeft
            val bRight = aVectors.second.toDouble() * -1 * bLeft + bVectors.second.toDouble()
            // [...]; 59,18b = 2361,76
            var bFinal = resRight / bRight
            var aFinal = resLeft - bLeft * bFinal
            bFinal = bFinal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
            aFinal = aFinal.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
            if (aFinal < 0 || bFinal < 0 || aFinal % 1 != 0.0 || bFinal % 1 != 0.0) {
                return null
            }
            return aFinal.toLong() * A_PRICE + bFinal.toLong() * B_PRICE
        }

        companion object {
            const val A_PRICE = 3
            const val B_PRICE = 1
            const val MAX_PRESSES = 100
        }
    }

}
