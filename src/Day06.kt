fun main() {
    fun solve(s: String, k: Int): Int {
        return s.indices.first { i -> s.substring(i, i + k).toSet().size == k } + k
    }

    fun part1(input: List<String>): Int {
        return solve(input[0], 4)
    }

    fun part2(input: List<String>): Int {
        return solve(input[0], 14)
    }

    val day = String.format("%02d", 6)
    val testInput = readInput("Day${day}_test")
    println("Part 1 test - " + part1(testInput))
    println("Part 2 test - " + part2(testInput))

    val input = readInput("Day$day")
    println("Part 1 real - " + part1(input))
    println("Part 2 real - " + part2(input))
}
