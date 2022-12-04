import kotlin.math.abs

fun main() {
    val cost = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3,

        "A" to 1,
        "B" to 2,
        "C" to 3
    )

    fun outcome(me: Int, he: Int): Int {
        return when {
            me == he -> 3
            abs(me - he) == 1 -> {
                if (me > he) 6 else 0
            }

            else -> {
                if (me == 1) 6 else 0
            }
        }
    }

    val scores = HashMap<Pair<String, String>, Int>().apply {
        for (me in "XYZ".map { it.toString() }) {
            for (he in "ABC".map { it.toString() }) {
                val meCost = cost[me]!!
                val heCost = cost[he]!!
                this[me to he] = meCost + outcome(meCost, heCost)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val ans = input.map { it.split(" ") }.sumOf {
            scores[it[1] to it[0]]!!
        }
        println(ans)
        return ans
    }

    fun part2(input: List<String>): Int {
        val ans = input.map { it.split(" ") }.sumOf {
            val he = cost[it[0]]!!
            val needOutcome = if (it[1] == "X") 0 else if (it[1] == "Y") 3 else 6
            for (me in 1..3) {
                if (outcome(me, he) == needOutcome) {
                    return@sumOf needOutcome + me
                }
            }
            error("no outcome")
        }
        println(ans)
        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
