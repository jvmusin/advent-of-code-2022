import java.util.*

fun main() {
    val dirs = HashMap<Char, Pair<Int, Int>>().apply {
        this['N'] = -1 to 0
        this['S'] = 1 to 0
        this['W'] = 0 to -1
        this['E'] = 0 to 1
    }

    val neighbors = mapOf(
        'N' to "EW",
        'S' to "EW",
        'W' to "NS",
        'E' to "NS"
    )

    fun get(b: Char): List<Pair<Int, Int>> {
        val neighbors = neighbors[b]!!
        val base = dirs[b]!!
        return neighbors.map { base + dirs[it]!! } + base
    }

    val inOrder = "NSWE".map { get(it) }

    fun List<String>.toUsed() = indices.flatMap { i ->
        this[i].indices.map { j ->
            i to j
        }
    }.filter { (i, j) -> this[i][j] == '#' }.toMutableSet()

    fun iter(used: MutableSet<Pair<Int, Int>>, time: Int): Boolean {
        val moves = HashMap<Pair<Int, Int>, Pair<Int, Int>>()
        val countForEach = HashMap<Pair<Int, Int>, Int>()
        for (from in used) {
            var cntAround = 0
            for (i in -1..1) for (j in -1..1) if ((from + (i to j)) in used) cntAround++
            if (cntAround == 1) continue
            var check = 0
            while (check < inOrder.size) {
                val turn = inOrder[(time + check) % inOrder.size]
                if (turn.all { delta -> (from + delta) !in used }) break
                check++
            }
            if (check == inOrder.size) continue
            val dest = inOrder[(time + check) % inOrder.size].last() + from
            moves[from] = dest
            countForEach.merge(dest, 1, Int::plus)
        }
        for ((from, to) in moves) {
            if (countForEach[to]!! == 1) {
                used -= from
                used += to
            }
        }
        return moves.isNotEmpty()
    }

    fun part1(input: List<String>): Any {
        val used = input.toUsed()
        repeat(10) { time ->
            iter(used, time)
        }

        val minX = used.fold(Int.MAX_VALUE) { acc, p -> minOf(acc, p.first) }
        val maxX = used.fold(Int.MIN_VALUE) { acc, p -> maxOf(acc, p.first) }
        val minY = used.fold(Int.MAX_VALUE) { acc, p -> minOf(acc, p.second) }
        val maxY = used.fold(Int.MIN_VALUE) { acc, p -> maxOf(acc, p.second) }

        fun print() {
            for (i in minX..maxX) {
                for (j in minY..maxY) {
                    print(if ((i to j) in used) '#' else '.')
                }
                println()
            }
        }

//        print()
        return (maxX - minX + 1) * (maxY - minY + 1) - used.size
    }

    fun part2(input: List<String>): Any {
        val used = input.toUsed()
        var time = 0
        while (true) {
            val anyMove = iter(used, time++)
            if (!anyMove) break
        }

        return time
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 23)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
