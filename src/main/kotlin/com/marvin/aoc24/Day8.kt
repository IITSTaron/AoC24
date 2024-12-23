package com.marvin.aoc24

import org.springframework.stereotype.Service

@Service("Day8")
class Day8 : Day {
    companion object {
        const val EMPTY_CHAR = '.'
    }

    override fun handlePart1(input: String): Int {
        val map = input.split("\n").map { it.trim() }.map { it.split("").filter { it.isNotEmpty() }.map { it.toCharArray().first() } }
                .filter { it.isNotEmpty() }
        val antinodes = calcAntinodesPt1(map)
        println(antinodes)
        return antinodes.size
    }

    override fun handlePart2(input: String): Int {
        val map = input.split("\n").map { it.trim() }.map { it.split("").filter { it.isNotEmpty() }.map { it.toCharArray().first() } }
                .filter { it.isNotEmpty() }
        val antinodes = calcAntinodesPt2(map)
        println(antinodes)
        return antinodes.size
    }

    fun calcAntennas(map: List<List<Char>>): MutableMap<Char, MutableList<Pair<Int, Int>>> {
        val antennaLocations: MutableMap<Char, MutableList<Pair<Int, Int>>> = mutableMapOf()
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, s ->
                if (s != EMPTY_CHAR) {
                    if (antennaLocations[s] == null) {
                        antennaLocations[s] = mutableListOf()
                    }
                    antennaLocations[s]!!.add(Pair(y, x))
                }
            }
        }
        return antennaLocations
    }

    fun calcAntinodesPt1(map: List<List<Char>>): List<Pair<Int, Int>> {
        val antennaLocations: MutableMap<Char, MutableList<Pair<Int, Int>>> = calcAntennas(map)
        var antinodeLocations: MutableList<Pair<Int, Int>> = mutableListOf()

        //Iterate all possible antenna combinations and collect their antinodes
        antennaLocations.forEach { (type, locations) ->
            locations.forEach { a ->
                locations.forEach inner@{ b ->
                    if(a == b) return@inner
                    val diffY = a.first - b.first
                    val diffX = a.second - b.second
                    val antinode = Pair(a.first + diffY, a.second + diffX)
                    if(antinode.first !in map.indices || antinode.second !in map.indices) return@inner
                    antinodeLocations.add(antinode)
                    println("Adding $type antinode at $antinode based on $a and $b")
                }
            }
        }

        //Order for visibility
        antinodeLocations = antinodeLocations.sortedWith (compareBy({it.first}, {it.second})).distinct().toMutableList()
        return antinodeLocations
    }

    fun calcAntinodesPt2(map: List<List<Char>>): List<Pair<Int, Int>> {
        val antennaLocations: MutableMap<Char, MutableList<Pair<Int, Int>>> = calcAntennas(map)
        var antinodeLocations: MutableList<Pair<Int, Int>> = mutableListOf()

        //Iterate all possible antenna combinations and collect their antinodes
        antennaLocations.forEach { (type, locations) ->
            locations.forEach { a ->
                locations.forEach inner@{ b ->
                    if(a == b) return@inner
                    var diffY = a.first - b.first
                    var diffX = a.second - b.second
                    //Break down diff to least possible distance to get smallest hops
                    while(diffX % 2 == 0 && diffY % 2 == 0) {
                        diffX /= 2
                        diffY /= 2
                    }
                    repeat(map.size) { index ->
                        val antinode = Pair(a.first + diffY * index, a.second + diffX * index)
                        if(antinode.first !in map.indices || antinode.second !in map.indices) return@repeat
                        antinodeLocations.add(antinode)
                        println("Adding $type antinode at $antinode based on $a and $b")
                    }
                }
            }
        }

        //Filter inplausible antinodes
        antinodeLocations = antinodeLocations.filter { it.first in map.indices && it.second in map.indices }.distinct().toMutableList()

        //Order for visibility
        antinodeLocations = antinodeLocations.sortedWith (compareBy({it.first}, {it.second})).toMutableList()
        return antinodeLocations
    }
}
