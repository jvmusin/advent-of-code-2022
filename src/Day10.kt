import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): List<Int> {
        val seq = mutableListOf(1)
        for (s in input) {
            if (s.startsWith("addx ")) {
                val value = s.split(" ")[1].toInt()
                seq.add(seq.last())
                seq.add(seq.last() + value)
            } else {
                seq.add(seq.last())
            }
        }
        return seq
    }

    fun part1(input: List<String>): Int {
        val seq = parse(input)
        return listOf(20, 60, 100, 140, 180, 220).sumOf { it * seq[it - 1] }
    }

    fun part2(input: List<String>): Int {
        val seq = parse(input)

        for (i in seq.indices) {
            if (i % 40 == 0) println()
            print(if (abs(seq[i] - i % 40) <= 1) '#' else '.')
        }

        return -1
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 10)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
