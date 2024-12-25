package com.marvin.aoc24

import com.marvin.aoc24.ListUtil.second
import com.marvin.aoc24.ListUtil.toMatrix
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service("Day11")
class Day11 : Day {

    override fun handlePart1(input: String): Int {
        val list = input.split(" ").map { it.toLong() }.toMutableList()
        var map = list.groupBy { it }.mapValues { it.value.size.toLong() }.toMutableMap()
        repeat(25) {
            println(map)
            map = map.blink()
        }
        return map.map { it.value }.sum().toInt()
    }

    override fun handlePart2(input: String): Long {
        val list = input.split(" ").map { it.toLong() }.toMutableList()
        var map = list.groupBy { it }.mapValues { it.value.size.toLong() }.toMutableMap()
        repeat(75) {
            println(map)
            map = map.blink()
        }
        return map.map { it.value }.sum().toLong()
    }

    fun MutableList<Long>.blink() {
        var index = 0
        while (index in indices) {
            val l = get(index)
            val lStr = l.toString()
            when {
                l == 0L -> set(index, 1)
                lStr.length % 2 == 0 -> {
                    lStr.chunked(lStr.length / 2).map { it.toLong() }.let {
                        set(index, it.first())
                        add(index + 1, it.second())
                        index++
                    }
                }

                else -> set(index, l * 2024)
            }
            index++
        }
    }

    fun MutableMap<Long, Long>.blink(): MutableMap<Long, Long> {
        fun MutableMap<Long, Long>.increment(key: Long, amount: Long) = set(key, (get(key) ?: 0) + amount)
        val newMap = mutableMapOf<Long, Long>()
        forEach { (l, nos) ->
            val lStr = l.toString()
            when {
                l == 0L -> newMap.increment(1L, nos)
                lStr.length % 2 == 0 -> {
                    lStr.chunked(lStr.length / 2).map { it.toLong() }.let {
                        newMap.increment(it.first(), nos)
                        newMap.increment(it.second(), nos)
                    }
                }

                else -> newMap.increment(l * 2024, nos)
            }
        }
        return newMap
    }
}
