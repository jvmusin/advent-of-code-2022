import java.util.*
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    fun part1(input: List<String>): Int {
        val list = input.mapIndexed { index, s -> s.toInt() to index }.toMutableList()
        for (i in list.indices) {
            var pos = list.indexOfFirst { it.second == i }
            val x = list[pos]
            val delta = x.first.sign
            repeat(abs(x.first)) {
                Collections.swap(list, pos, (pos + delta + list.size) % list.size)
                pos += delta
                pos += list.size
                pos %= list.size
            }
        }
        val pos0 = list.indexOfFirst { it.first == 0 }
        val res = listOf(1000, 2000, 3000).map { list[(pos0 + it) % list.size] }
        return res.sumOf { it.first }
    }

    fun MutableList<Pair<Long, Int>>.normalize(): MutableList<Pair<Long, Int>> {
        while (first().second != 0) add(removeFirst())
        return this
    }

    fun MutableList<Pair<Long, Int>>.putFirstIndex(i: Int) {
        while (first().second != i) add(removeFirst())
    }

    fun MutableList<Pair<Long, Int>>.applyShift(i: Int) {
        putFirstIndex(i)
        val x = first().first
        val singleShifts = abs(x) / (size - 1)
        repeat((singleShifts % size).toInt()) {
            if (x > 0) add(removeFirst())
            else add(0, removeLast())
        }
        val rem = abs(x) % (size - 1)

        putFirstIndex(i)
        var pos = 0
        val direction = x.sign
        repeat(rem.toInt()) {
            val next = (pos + direction + size) % size
            Collections.swap(this, pos, next)
            pos = next
        }
    }

    fun test(a: List<Pair<Long, Int>>) {
        fun naive(): MutableList<Pair<Long, Int>> {
            val list = a.toMutableList()
            for (i in list.indices) {
                var pos = list.indexOfFirst { it.second == i }
                val x = list[pos].first
                val direction = x.sign
                repeat(abs(x).toInt()) {
                    val next = (pos + direction + list.size) % list.size
                    Collections.swap(list, pos, next)
                    pos = next
                }
            }
            return list
        }

        fun faster(): MutableList<Pair<Long, Int>> {
            val list = a.toMutableList()
            for (i in list.indices) {
                list.applyShift(i)
            }
            return list
        }

        val naive = naive().normalize()
        val faster = faster().normalize()
        println(a)
        println(naive)
        println(faster)
        println(naive == faster)
        require(naive == faster)
    }

    /*
    12345
    21345
    23145
    23415
    23451
    13452


    12345
    52341
    52314
    52134
    51234
    15234
     */

    fun part2(input: List<String>): Long {
        val rnd = Random(239)
        while (false) {
            test(List(10) { rnd.nextInt(-5000000..5000000).toLong() to it })
        }
        val list = input.mapIndexedTo(mutableListOf()) { index, s -> s.toLong() * 811589153L to index }
        val size = list.size
        repeat(10) {
            for (i in list.indices) {
                list.applyShift(i)
            }
            println(list.map { it.first })
        }
        val pos0 = list.indexOfFirst { it.first == 0L }
        val res = arrayOf(1000, 2000, 3000).map { list[(pos0 + it) % size] }
        return res.sumOf { it.first }
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 20)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
