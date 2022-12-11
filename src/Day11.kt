fun main() {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: Long,
        val ifTrue: Int,
        val ifFalse: Int,
    )

    fun parseOperation(op: String): (Long) -> Long {
        val parts = op.split(' ')

        fun get(v: String): (Long) -> Long = if (v == "old") { x -> x } else { _ -> v.toLong() }
        val v1 = get(parts[0])
        val v2 = get(parts[2])

        val operation: (Long, Long) -> Long = when (parts[1]) {
            "+" -> Long::plus
            "*" -> Long::times
            else -> error("Unknown op: ${parts[1]}")
        }

        return { x -> operation(v1(x), v2(x)) }
    }

    fun parse(input: List<String>): MutableList<Monkey> {
        var i = 0
        val monkeys = mutableListOf<Monkey>()
        while (i < input.size) {
            val startingItems = input[++i].split(": ")[1].split(", ").map { it.toLong() }
            val operation = input[++i].split("new = ")[1].let(::parseOperation)
            val test = input[++i].split(' ').last().toLong()
            val ifTrue = input[++i].split(' ').last().toInt()
            val ifFalse = input[++i].split(' ').last().toInt()
            monkeys += Monkey(startingItems.toMutableList(), operation, test, ifTrue, ifFalse)
            i++
            i++
        }
        return monkeys
    }

    fun part1(input: List<String>): Int {
        val monkeys = parse(input)

        val inspects = IntArray(monkeys.size)
        repeat(20) {
            for (monkeyIndex in monkeys.indices) {
                val monkey = monkeys[monkeyIndex]
                inspects[monkeyIndex] += monkey.items.size
                repeat(monkey.items.size) {
                    var item = monkey.items.removeFirst()
                    item = monkey.operation(item)
                    item /= 3
                    monkeys[if (item % monkey.test == 0L) monkey.ifTrue else monkey.ifFalse].items.add(item)
                }
            }
        }

        println(inspects.toList())

        return inspects.sortedDescending().take(2).let { (a, b) -> a * b }
    }

    fun part2(input: List<String>): Long {
        val monkeys = parse(input)

        var lcm = 1L
        for (m in monkeys) {
            lcm = lcm(lcm, m.test)
            println(lcm)
        }

        val inspects = IntArray(monkeys.size)
        repeat(10_000) {
            for (monkeyIndex in monkeys.indices) {
                val monkey = monkeys[monkeyIndex]
                inspects[monkeyIndex] += monkey.items.size
                repeat(monkey.items.size) {
                    var item = monkey.items.removeFirst()
                    item = monkey.operation(item) % lcm
                    monkeys[if (item % monkey.test == 0L) monkey.ifTrue else monkey.ifFalse].items.add(item)
                }
            }
        }

        println(inspects.toList())

        return inspects.sortedDescending().take(2).let { (a, b) -> a.toLong() * b }
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 11)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
