package com.marvin.aoc24

import org.springframework.stereotype.Service
import java.util.ArrayList

@Service("Day9")
class Day9 : Day {

    override fun handlePart1(input: String): Long {
        val memory = renderStrToMemList(input.trim()).toMutableList().also { it.reassignRtL() }
        return memory.mapIndexed { index, i ->
            if (i == -1) {
                0
            } else {
                index * (i.toLong())
            }
        }.sum()
    }

    override fun handlePart2(input: String): Long {
        val memory = renderStrToMemList(input.trim()).toMutableList().also { it.reassignRtLChunked() }
        println(memory)
        return memory.mapIndexed { index, i ->
            if (i == -1) {
                0
            } else {
                index * (i.toLong())
            }
        }.sum()
    }

    fun renderStrToMemList(input: String): List<Int> = input.flatMapIndexed { index, c ->
        val char = if (index % 2 == 0) {
            index / 2
        } else -1
        List(c.toString().toInt()) { char }
    }

    fun MutableList<Int>.reassignRtL() {
        var freeIndex = indexOfFirst { it == -1 }
        for (i in this.indices.reversed()) {
            if (i <= freeIndex) {
                return
            }
            if (get(i) != -1) {
                set(freeIndex, get(i))
                set(i, -1)
                freeIndex = indexOfFirst { it == -1 }
            }
        }
    }

    fun MutableList<Int>.reassignRtLChunked() {
        distinct().filter { it != -1 }.sortedDescending().forEach { idToMove ->
            val firstIndexToMove = indexOfFirst { it == idToMove }
            val lastIndexToMove = indexOfLast { it == idToMove }
            val widthToMove = lastIndexToMove - firstIndexToMove + 1

            var firstFreeIndex = 0
            var lastFreeIndex = 0
            fun calculateNextFreeIndices() {
                firstFreeIndex = lastFreeIndex + 1 + subList(lastFreeIndex + 1, lastIndex).indexOfFirst { it == -1 }
                lastFreeIndex = firstFreeIndex + subList(firstFreeIndex, lastIndex).indexOfFirst { it != -1 } - 1
            }
            calculateNextFreeIndices()
            while(firstIndexToMove >= lastFreeIndex) {
                val freeWidth = lastFreeIndex - firstFreeIndex + 1
                if(freeWidth >= widthToMove) {
                    repeat(widthToMove) { i ->
                        set(firstFreeIndex + i, get(firstIndexToMove + i))
                        set(firstIndexToMove + i, -1)
                    }
                    return@forEach
                }
                calculateNextFreeIndices()
            }
        }
    }
}
