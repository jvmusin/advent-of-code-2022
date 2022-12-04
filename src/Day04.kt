fun main() {
    fun String.toRange() = split("-").map { it.toInt() }.let { it[0]..it[1] }
    fun part1(input: List<String>): Int {
        var cnt = 0
        for (s in input) {
            val p = s.split(",")
            val r1 = p[0].toRange().toList()
            val r2 = p[1].toRange().toList()
            if (r1.containsAll(r2) || r2.containsAll(r1)) cnt++
        }
        return cnt
    }

    fun part2(input: List<String>): Int {
        var cnt = 0
        for (s in input) {
            val p = s.split(",")
            val r1 = p[0].toRange().toList()
            val r2 = p[1].toRange().toList()
            if (r1.any { it in r2 } || r2.any { it in r1 }) cnt++
        }
        return cnt
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
