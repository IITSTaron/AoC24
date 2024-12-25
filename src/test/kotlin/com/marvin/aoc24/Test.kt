package com.marvin.aoc24

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Test {
    companion object {
        private const val DAY = 12
        private const val PATH = "C:\\Users\\Marvin\\IdeaProjects\\AoC24\\src\\main\\resources\\"
    }

    @Autowired
    @Qualifier("Day${DAY}")
    private lateinit var day: Day

    @Test
    fun producePart1Result() {
        val input = FileUtil.readFileContent("${PATH}day${DAY}.txt")
        println(day.handlePart1(input))
    }

    @Test
    fun producePart2Result() {
        val input = FileUtil.readFileContent("${PATH}day${DAY}.txt")
        println(day.handlePart2(input))
    }

    @Test
    fun test1() {
        val input = FileUtil.readFileContent("${PATH}day${DAY}Test.txt")
        println(day.handlePart1(input))
    }

    @Test
    fun test2() {
        val input = FileUtil.readFileContent("${PATH}day${DAY}Test.txt")
        println(day.handlePart2(input))
    }
}
