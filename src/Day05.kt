fun main() {
    data class Command(val amount: Int, val from: Int, val to: Int)
    data class Input(val stacks: List<MutableList<String>>, val commands: List<Command>)

    fun input(input: List<String>): Input {
        val stacks = mutableListOf<MutableList<String>>()
        var at = 0
        while ('[' in input[at]) {
            for (i in 0 until Int.MAX_VALUE) {
                val p = 1 + i * 4
                if (p !in input[at].indices) break
                if (stacks.size == i) stacks.add(mutableListOf())
                if (input[at][p] != ' ') {
                    stacks[i].add(input[at][p].toString())
                }
            }
            at++
        }
        at += 2
        val commands = mutableListOf<Command>()
        while (at < input.size) {
            val ways = input[at].split(" ").map { it.toIntOrNull() }.filterNotNull()
            commands.add(Command(ways[0], ways[1] - 1, ways[2] - 1))
            at++
        }
        return Input(stacks, commands)
    }

    fun part1(input: List<String>): String {
        val (stacks, commands) = input(input)

        for (c in commands) {
            repeat(c.amount) {
                val x = stacks[c.from].removeFirst()
                stacks[c.to].add(0, x)
            }
        }

        return stacks.joinToString("") { it.first() }
    }

    fun part2(input: List<String>): String {
        val (stacks, commands) = input(input)

        for (c in commands) {
            val p = c.amount - 1
            repeat(c.amount) {
                val x = stacks[c.from].removeAt(p - it)
                stacks[c.to].add(0, x)
            }
        }

        mutableListOf<Int>().sortedWith { a, b -> a.compareTo(b) }

        return stacks.joinToString("") { it.first() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    println("P1 test " + part1(testInput))
    println("P1 test " + part2(testInput))

    val input = readInput("Day05")
    println("P1 " + part1(input))
    println("P2 " + part2(input))
}
