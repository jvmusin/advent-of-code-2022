fun main() {
    fun readCalories(input: List<String>) = sequence {
        var start = 0
        while (start < input.size) {
            var sum = 0
            while (start < input.size) {
                val line = input[start++]
                if (line.isEmpty()) break
                sum += line.toInt()
            }
            yield(sum)
        }
    }

    fun part1(input: List<String>) = readCalories(input).max()

    fun part2(input: List<String>) = readCalories(input).sortedDescending().take(3).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
