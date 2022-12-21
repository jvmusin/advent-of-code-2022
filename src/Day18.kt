import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs

fun main() {
    data class Cube(val x: Int, val y: Int, val z: Int)

    fun part1(input: List<String>): Int {
        val cubes = HashSet<Cube>()
        for (s in input) {
            val (x, y, z) = s.split(',').map { it.toInt() }
            cubes += Cube(x, y, z)
        }
        var cnt = 0
        for (c in cubes) {
            val (x, y, z) = c
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (abs(dx) + abs(dy) + abs(dz) == 1) {
                            val c1 = Cube(x + dx, y + dy, z + dz)
                            if (c1 !in cubes) cnt++
                        }
                    }
                }
            }
        }
        return cnt
    }

    fun <T, R : Comparable<R>> List<T>.minMax(getValue: (T) -> R): Pair<R, R> {
        var min = getValue(this[0])
        var max = min
        for (i in 1 until size) {
            min = minOf(min, getValue(this[i]))
            max = maxOf(max, getValue(this[i]))
        }
        return min to max
    }

    fun part2(input: List<String>): Int {
        val cubes = mutableListOf<Cube>()
        for (s in input) {
            val (x, y, z) = s.split(',').map { it.toInt() }
            cubes += Cube(x, y, z)
        }
        fun Pair<Int, Int>.toRange() = first - 1..second + 1
        val dxs = cubes.minMax { it.x }.toRange()
        val dys = cubes.minMax { it.y }.toRange()
        val dzs = cubes.minMax { it.z }.toRange()
        fun IntRange.minMax() = listOf(first, last)

        val q = ArrayDeque<Cube>()
        val used = HashSet<Cube>()
        for (x in dxs.minMax()) {
            for (y in dys.minMax()) {
                for (z in dzs.minMax()) {
                    q += Cube(x, y, z)
                    used += Cube(x, y, z)
                }
            }
        }
        while (!q.isEmpty()) {
            val (x, y, z) = q.removeFirst()
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (abs(dx) + abs(dy) + abs(dz) == 1) {
                            val c1 = Cube(x + dx, y + dy, z + dz)
                            if (c1 in cubes) continue
                            if (c1.x in dxs && c1.y in dys && c1.z in dzs) {
                                if (used.add(c1)) {
                                    q.add(c1)
                                }
                            }
                        }
                    }
                }
            }
        }

        var cnt = 0
        for (c in cubes) {
            val (x, y, z) = c
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (abs(dx) + abs(dy) + abs(dz) == 1) {
                            val c1 = Cube(x + dx, y + dy, z + dz)
                            if (c1 in used) cnt++
                        }
                    }
                }
            }
        }
        return cnt
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 18)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
