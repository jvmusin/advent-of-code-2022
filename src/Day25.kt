import java.util.*
import kotlin.math.abs

fun main() {
    val cost = mapOf(
        '2' to 2,
        '1' to 1,
        '0' to 0,
        '-' to -1,
        '=' to -2
    )

    fun String.snafuToLong(): Long {
        var n = 0L
        var k = 1L
        for (c in reversed()) {
            n += k * cost[c]!!
            k *= 5L
        }
        return n
    }

    fun max(len: Int): Long {
        if (len == 0) return 0
        var k = 1L
        var sum = 2L
        repeat(len - 1) {
            k *= 5
            sum += k * 2
        }
        return sum
    }

    fun part1(input: List<String>): Any {
        val sum = input.sumOf { it.snafuToLong() }

        var len = 1
        var k = 1L
        while (max(len) < sum) {
            len++
            k *= 5
        }

        var rem = sum
        var res = ""
        repeat(len) { done ->
            var fixed = false
            for (d in 2 downTo -2) {
                val willRemain = rem - d * k
                val ok = abs(willRemain) <= max(len - done - 1)
                if (!ok) continue
                rem -= d * k
                res += cost.entries.first { it.value == d }.key
                fixed = true
                break
            }
            require(fixed)
            k /= 5
        }

        return res
    }

    fun part2(input: List<String>): Any {
        return Unit
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 25)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
