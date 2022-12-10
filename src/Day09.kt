import kotlin.math.abs

fun main() {
    val dx = mapOf(
        'U' to 0,
        'D' to 0,
        'L' to -1,
        'R' to 1
    )
    val dy = mapOf(
        'U' to 1,
        'D' to -1,
        'L' to 0,
        'R' to 0
    )

    fun part1(input: List<String>): Int {
        var curHead = 0 to 0
        var curTail = 0 to 0
        val visited = LinkedHashSet<Pair<Int, Int>>().apply { add(0 to 0) }
        for ((dir, times) in input.map { it.split(" ") }.map { it[0][0] to it[1].toInt() }) {
            repeat(times) {
                curHead = (curHead.first + dx[dir]!!) to (curHead.second + dy[dir]!!)
                if (maxOf(abs(curHead.first - curTail.first), abs(curHead.second - curTail.second)) > 1) {
                    val moveX = curHead.first.compareTo(curTail.first)
                    val moveY = curHead.second.compareTo(curTail.second)
                    curTail = (curTail.first + moveX) to (curTail.second + moveY)
                    visited += curTail
                }
            }
        }

        return visited.size
    }

    fun part2(input: List<String>): Int {
        val positions = mutableListOf<Pair<Int, Int>>().apply {
            repeat(10) {
                add(0 to 0)
            }
        }
        val visited = LinkedHashSet<Pair<Int, Int>>().apply { add(0 to 0) }
        for ((dir, times) in input.map { it.split(" ") }.map { it[0][0] to it[1].toInt() }) {
            repeat(times) {
                positions[0] = (positions[0].first + dx[dir]!!) to (positions[0].second + dy[dir]!!)
                for (i in 1 until positions.size) {
                    val curHead = positions[i - 1]
                    val curTail = positions[i]
                    if (maxOf(abs(curHead.first - curTail.first), abs(curHead.second - curTail.second)) > 1) {
                        val moveX = curHead.first.compareTo(curTail.first)
                        val moveY = curHead.second.compareTo(curTail.second)
                        positions[i] = (curTail.first + moveX) to (curTail.second + moveY)
                    } else break
                }
                visited.add(positions.last())
            }
        }

        return visited.size
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 9)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
