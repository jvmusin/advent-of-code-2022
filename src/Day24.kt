import java.util.*

fun main() {
    val dx = intArrayOf(0, 1, 0, -1)
    val dy = intArrayOf(1, 0, -1, 0)
    val dirs = ">v<^"

    data class Wind(val p: Pair<Int, Int>, val d: Int)

    fun solve(input: List<String>, needCycle: Int): Any {
        val n = input.size
        val m = input[0].length
        fun inside(p: Pair<Int, Int>): Boolean {
            val (x, y) = p
            return x in 0 until n && y in 0 until m && input[x][y] != '#'
        }

        fun move(w: Wind): Wind {
            val (x, y) = w.p
            var nx = x + dx[w.d]
            var ny = y + dy[w.d]
            if (!inside(nx to ny)) {
                if (w.d % 2 == 0) {
                    ny = if (y == 1) m - 2 else 1
                } else {
                    nx = if (x == 1) n - 2 else 1
                }
            }
            return Wind(nx to ny, w.d)
        }

        data class State(val p: Pair<Int, Int>, val cycle: Int)

        val startPos = 0 to 1
        val endPos = n - 1 to m - 2
        val canBe = buildSet {
            add(State(startPos, 0))
        }.toHashSet()
        var activeWinds = buildSet {
            for (i in input.indices) {
                for (j in input[i].indices) {
                    val dir = dirs.indexOf(input[i][j])
                    if (dir == -1) continue
                    add(Wind(i to j, dir))
                }
            }
        }
        var time = 0
        val endState = State(endPos, needCycle)
        while (true) {
            if (endState in canBe) return time
            val nextWinds = activeWinds.map { move(it) }
            val nextWindsPositions = nextWinds.map { it.p }.toSet()
            val nextCanBe = canBe.flatMap { cur ->
                val (x, y) = cur.p
                (0 until 4).map { d ->
                    val nx = x + dx[d]
                    val ny = y + dy[d]
                    State(nx to ny, cur.cycle)
                }.filter { created ->
                    inside(created.p)
                }.flatMap { created ->
                    val newCycle = when {
                        created.cycle == 0 && created.p == endPos -> 1
                        created.cycle == 1 && created.p == startPos -> 2
                        else -> created.cycle
                    }
                    setOf(created, created.copy(cycle = newCycle))
                }
            }
            canBe += nextCanBe
            canBe.removeIf { it.p in nextWindsPositions }
            activeWinds = nextWinds.toSet()
            time++
        }
    }

    fun part1(input: List<String>): Any {
        return solve(input, 0)
    }

    fun part2(input: List<String>): Any {
        return solve(input, 2)
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 24)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
