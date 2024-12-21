package com.marvin.aoc24

import org.springframework.stereotype.Service
import kotlin.math.pow

@Service("Day7")
class Day7 : Day {
    class Equation(val result: Int, val components: List<Int>) {
        fun solveable(): Boolean {
            val noOperators = 2
            val noFactors = components.size - 1
            val maxCombinations = noOperators.toDouble().pow(noFactors.toDouble()).toInt()
            println((1..maxCombinations).map { Integer.toBinaryString(it) })
            (1..maxCombinations).map { Integer.toBinaryString(it) }.forEach {
                var result = components.first()
                it.mapIndexed { index, operand ->
                    if (operand == '0') {
                        result += components[index + 1]
                    } else {
                        result *= components[index + 1]
                    }
                    if (result == this.result) return true
                }
            }
            return false
        }
    }

    override fun handlePart1(input: String): Int = inputToEquations(input).sumOf {
        if(it.solveable()) it.result else 0
    }

    override fun handlePart2(input: String): Int {
        return 0

    }

    fun inputToEquations(input: String): List<Equation> = input.lines().filter { it.isNotBlank() }.map { line ->
        line.split(":").let {
            Equation(it[0].toInt(), it[1].trim().split(" ").map { it.toInt() })
        }
    }

}
