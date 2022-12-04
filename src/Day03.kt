fun main() {
    fun Char.cost(): Int {
        if (isLowerCase()) {
            return code - 'a'.code + 1
        }
        return code - 'A'.code + 27
    }

    fun intersect(a: Iterable<Char>, b: Iterable<Char>): Collection<Char> {
        val res = mutableSetOf<Char>()
        for (c in a) {
            if (c in b) {
                res.add(c)
            }
        }
        return res
    }

    fun part1(input: List<String>): Int {
        val cost = input.map {
            val half = it.length / 2
            val x = it.substring(0, half)
            val y = it.substring(half)
            val z = intersect(x.toMutableList(), y.toMutableList())
            z.sumOf { it.cost() }
        }.sum()
        return cost
    }

    fun part2(input: List<String>): Int {
        var s = 0
        for (i in input.indices step 3) {
            val group = input.subList(i, i + 3)
            val inter = intersect(intersect(group[0].toList(), group[1].toList()), group[2].toList())
            s += inter.sumOf { it.cost() }
        }
        return s
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println(part2(testInput))
//    check(part1(testInput) == 24000)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
