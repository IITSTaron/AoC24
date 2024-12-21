package com.marvin.aoc24

import java.io.File

object FileUtil {
    fun readFileContent(path: String): String = File(path).readText()
}
