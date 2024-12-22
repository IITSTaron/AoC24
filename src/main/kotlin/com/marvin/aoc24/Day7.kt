package com.marvin.aoc24

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow

@Service("Day7")
class Day7 : Day {
    class Equation(val result: Long, private val components: List<Long>) {
        fun solveable(includeJoiningOp: Boolean = false): Boolean {
            val start = Instant.now()
            val noOperators = if (includeJoiningOp) 3 else 2
            val noFactors = components.size - 1
            val maxCombinations = noOperators.toDouble().pow(noFactors.toDouble()).toInt() - 1
            var combinationsNAry = (0..maxCombinations).map {
                when (noOperators) {
                    2 -> Integer.toBinaryString(it)
                    3 -> toTernaryString(it)
                    else -> error("$noOperators not implemented yet")
                }
            }
            //Filter out Step1 only conditions (already calculated) if Step2
            if(includeJoiningOp) {
                combinationsNAry = combinationsNAry.filter { it.contains("2") }
            }
            combinationsNAry = combinationsNAry.map { it.padStart(combinationsNAry.maxOf { it.length }, '0') }
            combinationsNAry.forEach {
                var result = components.first()
                it.mapIndexed { index, operand ->
                    val factor = components[index + 1]
                    when (operand) {
                        '0' -> result += factor
                        '1' -> result *= factor
                        '2' -> result = "$result$factor".toLong()
                    }
                    if (result > this.result) return@forEach
                }
                if (result == this.result) {
                    println("Equation ${this.result} | $components solved in ${Instant.now().toEpochMilli() - start.toEpochMilli()}ms")
                    return true
                }
            }
            println("Equation ${this.result} | $components exhausted in ${Instant.now().toEpochMilli() - start.toEpochMilli()}ms")
            return false
        }

        private fun toTernaryString(number: Int): String {
            if (number == 0) return "0"
            var n = number

            val ternaryBuilder = StringBuilder()
            while (n > 0) {
                ternaryBuilder.append(n % 3)
                n /= 3
            }

            return ternaryBuilder.reverse().toString()
        }
    }

    override fun handlePart1(input: String): Long = inputToEquations(input).sumOf {
        if (it.solveable()) it.result else 0
    }

    override fun handlePart2(input: String): Long = runBlocking(Dispatchers.Default) {
        val readyCounter = AtomicInteger(0)
        val equations = inputToEquations(input)
        val solvedByPart1 = equations.filter { it.solveable(false) }
        val unsolvedEquations = equations.filter { it !in solvedByPart1 }
        val deferredResults = unsolvedEquations.map { equation ->
            async(Dispatchers.Default) {
                val result = if (equation.solveable(true)) equation.result else 0
                val readyCount = readyCounter.incrementAndGet()
                println("Results ready: $readyCount/${unsolvedEquations.size}")
                result
            }
        }
        deferredResults.sumOf { it.await() } + solvedByPart1.sumOf { it.result }
    }

    fun inputToEquations(input: String): List<Equation> = input.lines().filter { it.isNotBlank() }.map { line ->
        line.split(":").let {
            Equation(it[0].toLong(), it[1].trim().split(" ").map { it.toLong() })
        }
    }
}
