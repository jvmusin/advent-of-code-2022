import java.util.*

fun main() {
    val figures = arrayOf(
        """
            ####
        """.trimIndent().lines(),
        """
            .#.
            ###
            .#.
        """.trimIndent().lines(),
        """
            ..#
            ..#
            ###
        """.trimIndent().lines(),
        """
            #
            #
            #
            #
        """.trimIndent().lines(),
        """
            ##
            ##
        """.trimIndent().lines()
    )

    fun IntArray.isZeros() = toSet() == setOf(0)
    fun LongArray.isZeros() = toSet() == setOf(0L)

    fun part1(input: List<String>): Int {
        val s = input.single()
        val levels = mutableListOf<IntArray>()
        val width = 7

        fun getInLevel(r: Int, c: Int) = levels.getOrNull(r)?.getOrNull(c)

        var at = 0
        var figureIndex = 0
        while (figureIndex < 2022) {
            while (levels.firstOrNull()?.isZeros() == true) levels.removeFirst()
            val figure = figures[(figureIndex++) % figures.size]
            repeat(3 + figure.size) { levels.add(0, IntArray(width)) }
            for (row in figure.indices) {
                for (col in figure[row].indices) {
                    if (figure[row][col] == '#') {
                        levels[row][col + 2] = figureIndex
                    }
                }
            }
            var fromTop = 0
            fun checkMove(dr: Int, dc: Int): Boolean {
                for (row in figure.indices) {
                    for (col in 0 until width) {
                        val cur = requireNotNull(getInLevel(row + fromTop, col))
                        if (cur == figureIndex) {
                            val next = getInLevel(row + fromTop + dr, col + dc)
                            if (next == null || (next != 0 && next != figureIndex)) return false
                        }
                    }
                }
                return true
            }

            fun move(dr: Int, dc: Int): Boolean {
                if (!checkMove(dr, dc)) return false
                val freed = mutableListOf<Pair<Int, Int>>()
                for (row in figure.indices) {
                    for (col in 0 until width) {
                        if (levels[row + fromTop][col] == figureIndex) {
                            levels[row + fromTop][col] = 0
                            freed += (row + fromTop) to col
                        }
                    }
                }
                for ((r, c) in freed) {
                    levels[r + dr][c + dc] = figureIndex
                }
                fromTop += dr
                return true
            }
            while (true) {
                move(0, if (s[(at++) % s.length] == '<') -1 else +1)
                if (!move(1, 0)) break
            }
        }

        return levels.dropWhile { it.isZeros() }.size
    }

    fun part2(input: List<String>): Long {
        val remainTop = 100
        val s = input.single()
        val levels = mutableListOf<LongArray>()
        val width = 7

        fun getInLevel(r: Int, c: Int) = levels.getOrNull(r)?.getOrNull(c)

        fun snapshot() = levels.joinToString("") { row ->
            row.joinToString("") {
                if (it == 0L) "." else "#"
            }
        }

        val states = mutableListOf<List<Any>>()
        val droppedTotal = mutableListOf<Long>()

        var at = 0
        var figureIndex = 0L
        var dropped = 0L
        val figuresNeed = 1000000000000
        var optimized = false
        while (figureIndex < figuresNeed) {
            if (!optimized) {
                val state = listOf(snapshot(), at, figureIndex % figures.size)
                if (state in states) {
                    while (states.first() != state) {
                        states.removeFirst()
                        droppedTotal.removeFirst()
                    }
                    val singleCycleFigures = states.size
                    val singleCycleDropped = dropped - droppedTotal.first()
                    val fullLoops = (figuresNeed - figureIndex) / singleCycleFigures - 10

                    figureIndex += fullLoops * singleCycleFigures
                    dropped += fullLoops * singleCycleDropped
                    optimized = true
                } else {
                    states.add(state)
                    droppedTotal.add(dropped)
                }
            }
            while (levels.size > remainTop) {
                dropped++
                levels.removeLast()
            }
            val figure = figures[((figureIndex++) % figures.size).toInt()]
            repeat(3 + figure.size) { levels.add(0, LongArray(width)) }
            for (row in figure.indices) {
                for (col in figure[row].indices) {
                    if (figure[row][col] == '#') {
                        levels[row][col + 2] = figureIndex
                    }
                }
            }
            var fromTop = 0
            fun checkMove(dr: Int, dc: Int): Boolean {
                for (row in figure.indices) {
                    for (col in 0 until width) {
                        val cur = requireNotNull(getInLevel(row + fromTop, col))
                        if (cur == figureIndex) {
                            val next = getInLevel(row + fromTop + dr, col + dc)
                            if (next == null || (next != 0L && next != figureIndex)) return false
                        }
                    }
                }
                return true
            }

            fun move(dr: Int, dc: Int): Boolean {
                if (!checkMove(dr, dc)) return false
                val freed = mutableListOf<Pair<Int, Int>>()
                for (row in figure.indices) {
                    for (col in 0 until width) {
                        if (levels[row + fromTop][col] == figureIndex) {
                            levels[row + fromTop][col] = 0
                            freed += (row + fromTop) to col
                        }
                    }
                }
                for ((r, c) in freed) {
                    levels[r + dr][c + dc] = figureIndex
                }
                fromTop += dr
                return true
            }
            while (true) {
                move(0, if (s[at] == '<') -1 else +1)
                at = (at + 1) % s.length
                if (!move(1, 0)) {
                    if (figureIndex > 5 && levels.last().contains(figureIndex)) {
                        error("Figure fallen down, need to increase stored levels count, figure number $figureIndex")
                    }
                    break
                }
            }
            while (levels.firstOrNull()?.isZeros() == true) levels.removeFirst()
        }

        return levels.dropWhile { it.isZeros() }.size + dropped
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 17)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
