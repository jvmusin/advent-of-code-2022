import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.time.toKotlinDuration

private typealias Mask = Long
private typealias Position = Int
private typealias Score = Int

fun main() {
    fun Long.get(i: Int) = (this shr i) and 1 == 1L
    fun Long.set(i: Int) = this or (1L shl i)

    fun part1(input: List<String>): Int {
        val n = input.size
        val p = "Valve (?<name>.*) has flow rate=(?<rate>.*); tunnels? leads? to valves? (?<valves>.*)".toPattern()
        val rates = input.map { s -> p.matcher(s).also { it.find() }.group("rate")!!.toInt() }.toIntArray()
        var start = -1
        val edges = run {
            val names = input.map { s -> p.matcher(s).also { it.find() }.group("name")!! }
            start = names.indexOf("AA")
            input.map { s ->
                p.matcher(s).also { it.find() }
                    .group("valves")!!
                    .split(", ")
                    .map { names.indexOf(it) }
                    .toIntArray()
            }.toTypedArray()
        }

        val dist = Array(n) { IntArray(n) { Int.MAX_VALUE } }.apply {
            for (i in 0 until n) this[i][i] = 0
            for (i in 0 until n) for (to in edges[i]) this[i][to] = 1
            for (i in 0 until n) for (to in edges[i]) this[to][i] = 1
            for (k in 0 until n) {
                for (i in 0 until n) {
                    for (j in 0 until n) {
                        if (this[i][k] != Int.MAX_VALUE && this[k][j] != Int.MAX_VALUE) {
                            this[i][j] = minOf(this[i][j], this[i][k] + this[k][j])
                        }
                    }
                }
            }
        }

        var dp = HashMap<Pair<Int, Long>, Int>() // {pos, used} => score
        dp[start to 0L] = 0
        var maxScore = -1
        val startTime = Instant.now()
        fun spent() = Duration.between(startTime, Instant.now()).toKotlinDuration().toIsoString()
        for (time in 29 downTo 1) {
            println("(${spent()}) Time $time")
            val dp1 = HashMap<Pair<Int, Long>, Int>()
            fun update(pos: Int, used: Long, score: Int) {
                dp1.merge(pos to used, score, ::maxOf)
                if (score > maxScore) {
                    maxScore = score
                    println("(${spent()}) Updated max score: $maxScore")
                }
            }
            for ((k, score) in dp) {
                val (pos, used) = k
                if (((used shr pos) and 1L) == 0L) {
                    update(pos, used or (1L shl pos), score + time * rates[pos])
                }
                for (to in edges[pos]) {
                    update(to, used, score)
                }
            }

            val toRemove = mutableListOf<Pair<Int, Long>>()
            val entries = dp1.entries
                .sortedBy { it.key.second }
                .groupBy { it.key.first }
                .mapValues { (_, v) ->
                    v.associate { entry -> entry.key.second to entry.value }
                }
            for ((pos, v) in entries) {
                val curEntries = v.toList()
                outer@ for (curIndex in curEntries.indices) {
                    val x = curEntries[curIndex]
                    for (j in curIndex + 1 until curEntries.size) {
                        val y = curEntries[j]
                        if (x.second or y.second == y.second) {
                            var addition = 0
                            for (i in 0 until n) {
                                if (!x.first.get(i) && y.first.get(i)) {
                                    val m = maxOf(0, time - dist[pos][i])
                                    addition += m * rates[i]
                                }
                            }
                            if (x.second + addition < y.second) {
                                toRemove += pos to x.first
                                continue@outer
                            }
                        }
                    }
                }
            }
            for (r in toRemove) requireNotNull(dp1.remove(r))
            dp = dp1
        }

        return maxScore
    }

    fun part2(input: List<String>): Int {
        class Part2Solver {
            private val n = input.size
            private val startTime = Instant.now()
            private fun spent() = Duration.between(startTime, Instant.now()).toKotlinDuration().toIsoString()
            private var maxScore = -1

            private val p =
                "Valve (?<name>.*) has flow rate=(?<rate>.*); tunnels? leads? to valves? (?<valves>.*)".toPattern()
            private val rates = input.map { s -> p.matcher(s).also { it.find() }.group("rate")!!.toInt() }.toIntArray()
            private var start = -1
            private val edges = run {
                val names = input.map { s -> p.matcher(s).also { it.find() }.group("name")!! }
                start = names.indexOf("AA")
                input.map { s ->
                    p.matcher(s).also { it.find() }
                        .group("valves")!!
                        .split(", ")
                        .map { names.indexOf(it) }
                        .toIntArray()
                }.toTypedArray()
            }

            private val dist = Array(n) { IntArray(n) { Int.MAX_VALUE } }.apply {
                for (i in 0 until n) this[i][i] = 0
                for (i in 0 until n) for (to in edges[i]) this[i][to] = 1
                for (i in 0 until n) for (to in edges[i]) this[to][i] = 1
                for (k in 0 until n) {
                    for (i in 0 until n) {
                        for (j in 0 until n) {
                            if (this[i][k] != Int.MAX_VALUE && this[k][j] != Int.MAX_VALUE) {
                                this[i][j] = minOf(this[i][j], this[i][k] + this[k][j])
                            }
                        }
                    }
                }
            }

            private val dp = Array(n) { Array(n) { HashMap<Mask, Position>() } }.apply {
                this[start][start][0L] = 0
            }

            private val dp1 = Array(n) { Array(n) { HashMap<Mask, Position>() } }

            private fun update(pos1: Position, pos2: Position, used: Mask, score: Score) {
                val current = dp1[minOf(pos1, pos2)][maxOf(pos1, pos2)]
                val prev = current[used]
                if (prev == null || prev < score) {
                    current[used] = score
                    if (score > maxScore) {
                        maxScore = score
                        println("(${spent()}) Updated max score: $maxScore")
                    }
                }
            }

            fun solve(): Int {
                for (time in 25 downTo 1) {
                    println("(${spent()}) Time $time")
                    for (i in 0 until n) for (j in 0 until n) dp1[i][j] = HashMap()
                    for (pos1 in 0 until n) {
                        val rate1 = rates[pos1]
                        val edges1 = edges[pos1]
                        for (pos2 in pos1 until n) {
                            val rate2 = rates[pos2]
                            val edges2 = edges[pos2]
                            for ((used, score) in dp[pos1][pos2]) {
                                if (!used.get(pos1)) {
                                    val extra = if (used.get(pos2) || pos1 == pos2) 0 else rate2
                                    update(pos1, pos2, used.set(pos1).set(pos2), score + time * (rate1 + extra))
                                    for (to2 in edges2) {
                                        update(pos1, to2, used.set(pos1), score + time * rate1)
                                    }
                                }
                                if (!used.get(pos2)) {
                                    for (to1 in edges1) {
                                        update(to1, pos2, used.set(pos2), score + time * rate2)
                                    }
                                }
                                for (to1 in edges1) {
                                    for (to2 in edges2) {
                                        update(to1, to2, used, score)
                                    }
                                }
                            }
                        }
                    }
                    for (pos1 in 0 until n) {
                        val dist1 = dist[pos1]
                        for (pos2 in pos1 until n) {
                            val current = dp1[pos1][pos2]
                            val entries = current.entries
                                .sortedBy(MutableMap.MutableEntry<Mask, Score>::key)
                            for (big in entries.indices) {
                                val (maskBig, scoreBig) = entries[big]
                                for (small in 0 until big) {
                                    val (maskSmall, scoreSmall) = entries[small]
                                    if ((maskSmall or maskBig) == maskBig && scoreSmall >= scoreBig) {
                                        current.remove(entries[big].key)
                                        break
                                    }
                                }
                            }

                            val dist2 = dist[pos2]
                            for ((mask, score) in current.entries.toList()) {
                                var extra = 0
                                for (i in 0 until n) {
                                    if (!mask.get(i)) {
                                        val minDist = minOf(dist1[i], dist2[i])
                                        extra += maxOf((time - 1 - minDist) * rates[i], 0)
                                    }
                                }
                                if (score + 500 < maxScore || score + extra < maxScore) {
                                    current.remove(mask)
                                }
                            }
                        }
                    }
                    for (i in 0 until n) for (j in 0 until n) dp[i][j] = dp1[i][j]
                }

                return maxScore
            }
        }
        return Part2Solver().solve()
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 16)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
