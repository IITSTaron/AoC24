package com.marvin.aoc24

import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.util.concurrent.Executors

class Coords(var x: Int, var y: Int, var direction: Direction)
enum class Direction { TOP, BOTTOM, LEFT, RIGHT }
class ElveMap() : ArrayList<ArrayList<String>>() {
    constructor(list: List<List<String>>) : this() {
        addAll(list.map { it.toMutableList() as ArrayList }.toMutableList())
        currentCoords = findStartCoords()
        visitedCoors = mutableListOf()
    }

    lateinit var currentCoords: Coords
    lateinit var visitedCoors: MutableList<Coords>

    fun findStartCoords(): Coords {
        forEachIndexed { y, line ->
            line.forEachIndexed { x, item ->
                if (item == "^") {
                    return Coords(x, y, Direction.TOP)
                }
            }
        }
        error("Coords not found!")
    }

    fun turn() {
        currentCoords.direction = when (currentCoords.direction) {
            Direction.TOP -> Direction.RIGHT
            Direction.RIGHT -> Direction.BOTTOM
            Direction.BOTTOM -> Direction.LEFT
            Direction.LEFT -> Direction.TOP
        }
    }

    fun calcNextCoords(): Coords = when (currentCoords.direction) {
        Direction.TOP -> currentCoords.let { Coords(it.x, it.y - 1, it.direction) }
        Direction.BOTTOM -> currentCoords.let { Coords(it.x, it.y + 1, it.direction) }
        Direction.LEFT -> currentCoords.let { Coords(it.x - 1, it.y, it.direction) }
        Direction.RIGHT -> currentCoords.let { Coords(it.x + 1, it.y, it.direction) }
    }

    fun getNextFieldContent(): String = calcNextCoords().let { this[it.y][it.x] }

    fun isNextBlocked(): Boolean = getNextFieldContent() == "#"
    fun reachedEnd(): Boolean = runCatching { getNextFieldContent() }.isFailure

    fun move() {
        currentCoords = calcNextCoords()
    }

    fun detectLoop(): Boolean {
        while (true) {
            if (reachedEnd()) {
                return false
            }
            while (isNextBlocked()) {
                turn()
            }
            move()
            visitedCoors.add(Coords(currentCoords.x, currentCoords.y, currentCoords.direction))
            if (visitedCoors.count { it.x == currentCoords.x && it.y == currentCoords.y && it.direction == currentCoords.direction } > 1) {
                return true
            }
        }
    }
}


@Service("Day6")
class Day6 : Day {
    override fun handlePart1(input: String): Int {
        val map = input.split("\n").map { it.trim() }.map { it.split("").filter { it.isNotBlank() } }
            .filter { it.isNotEmpty() }
        return simulate1(ElveMap(map))
    }

    override fun handlePart2(input: String): Int {
        val map = input.split("\n").map { it.trim() }.map { it.split("").filter { it.isNotBlank() } }
            .filter { it.isNotEmpty() }
        return simulate2Sequentially(ElveMap(map))
    }

    fun simulate1(map: ElveMap): Int {
        return 0
    }


    fun simulate2(map: ElveMap): Int = runBlocking {
        val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        try {
            runBlocking {
                val chunkSize = 30
                map.chunked(chunkSize).mapIndexed { cindex, chunk ->
                    async(dispatcher) { // Use the custom dispatcher
                        chunk.mapIndexed { y, row ->
                            val absY = y + cindex * chunkSize
                            println("${Thread.currentThread().name} row $absY")
                            row.mapIndexed ob@{ x, c ->
                                val copyMap = ElveMap(map)
                                if (copyMap[absY][x] == ".") {
                                    copyMap[absY][x] = "#"
                                    return@ob if (withTimeout(1000) { copyMap.detectLoop() }) 1 else 0
                                }
                                return@ob 0
                            }.sum()
                        }.sum()
                    }
                }.sumOf { it.await() }
            }
        } finally {
            dispatcher.close()
        }
    }

    fun simulate2Sequentially(map: ElveMap): Int = map.mapIndexed { y, row ->
        println("row $y")
        row.mapIndexed ob@{ x, c ->
            val copyMap = ElveMap(map)
            if (copyMap[y][x] == ".") {
                copyMap[y][x] = "#"
                return@ob if (copyMap.detectLoop()) 1 else 0
            }
            return@ob 0
        }.sum()
    }.sum()
}
