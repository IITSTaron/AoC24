package com.marvin.aoc24

object ListUtil {
    fun<T> List<T>.second() = this[1]
    fun<T> List<T>.third() = this[2]
    fun<T> List<T>.fourth() = this[3]
    fun<T> List<T>.fifth() = this[4]

    fun String.toMatrix(): List<List<String>> = lines().map { it.split("").filter { it.isNotBlank() } }.filter { it.isNotEmpty() }
}
