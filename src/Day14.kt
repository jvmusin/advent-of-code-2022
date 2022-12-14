fun main() {
    fun input(input: List<String>): MutableSet<Pair<Int, Int>> {
        val used = HashSet<Pair<Int, Int>>()
        for (s in input) {
            val parts = s.split(" -> ")
            for (i in 0 until parts.size - 1) {
                val (x0, y0) = parts[i].split(',').map { it.toInt() }
                val (x1, y1) = parts[i + 1].split(',').map { it.toInt() }
                for (x in minOf(x0, x1)..maxOf(x0, x1)) {
                    for (y in minOf(y0, y1)..maxOf(y0, y1)) {
                        used += x to y
                    }
                }
            }
        }
        return used
    }

    val start = 500 to 0

    fun fallNext(used: MutableSet<Pair<Int, Int>>, lowest: Int): Pair<Int, Int> {
        var (x, y) = start
        while (true) {
            val ny = y + 1
            if (y > lowest) break
            for (dx in intArrayOf(0, -1, 1)) {
                val nx = x + dx
                if ((nx to ny) !in used) {
                    x = nx
                    y = ny
                    break
                }
            }
            if (y != ny) break
        }
        return (x to y).also { used += it }
    }

    fun part1(input: List<String>): Int {
        val used = input(input)
        val lowest = used.maxOf { (_, y) -> y }

        var cnt = 0
        while (true) {
            val (x, y) = fallNext(used, lowest)
            if (y > lowest) break
            used += x to y
            cnt++
        }

        return cnt
    }

    fun part2(input: List<String>): Int {
        val used = input(input)
        val lowest = used.maxOf { (_, y) -> y }

        var cnt = 0
        while (start !in used) {
            used += fallNext(used, lowest)
            cnt++
        }

        return cnt
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 14)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
