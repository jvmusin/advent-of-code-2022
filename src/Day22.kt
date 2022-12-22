import java.util.*

fun main() {
    val dr = intArrayOf(0, 1, 0, -1)
    val dc = intArrayOf(1, 0, -1, 0)
    val R = 0
    val D = 1
    val L = 2
    val U = 3

    fun solve(input: List<String>, wrap: (List<Int>) -> List<Int>): Any {
        val next = HashMap<List<Int>, List<Int>>()
        fun go(r: Int, c: Int, d: Int): List<Int> = next.getOrPut(listOf(r, c, d)) {
            var nr = r + dr[d]
            var nc = c + dc[d]
            var nd = d
            var value = input.getOrNull(nr)?.getOrNull(nc)?.takeIf { it != ' ' }
            if (value == null) {
                val wrapped = wrap(listOf(r, c, d))
                nr = wrapped[0]
                nc = wrapped[1]
                value = input[nr][nc]
                nd = wrapped[2]
            }
            if (value == '#') return@getOrPut listOf(r, c, d)
            if (value == '.') return@getOrPut listOf(nr, nc, nd)
            error("No way")
        }

        val path = input.last()
        var posR = 0
        var posC = input[0].indexOfFirst { it == '.' }
        var dir = 0
        var at = 0
        while (at < path.length) {
            if (path[at].isDigit()) {
                var number = 0
                while (path.getOrNull(at)?.isDigit() == true) {
                    number = number * 10 + path[at++].digitToInt()
                }
                repeat(number) {
                    val (nr, nc, nd) = go(posR, posC, dir)
                    posR = nr
                    posC = nc
                    dir = nd
                }
            } else {
                if (path[at] == 'R') dir++
                else dir--
                dir = dir.positiveModulo(4)
                at++
            }
        }
        return 1000 * (posR + 1) + 4 * (posC + 1) + dir
    }

    fun part1(input: List<String>): Any {
        return solve(input) { (row, col, dir) ->
            var nextRow = row
            var nextCol = col
            while (true) {
                val nextR = nextRow - dr[dir]
                val nextC = nextCol - dc[dir]
                if (input.getOrNull(nextR)?.getOrNull(nextC)?.takeIf { it != ' ' } == null) break
                nextRow = nextR
                nextCol = nextC
            }
            listOf(nextRow, nextCol, dir)
        }
    }

    fun part2(input: List<String>): Any {
        val sideSize = (input.size - 2) / 4
        val sideMap = """
            012
            030
            450
            600
        """.trimIndent().lines()

        fun rotateRight(p: List<Int>): List<Int> {
            val (r, c, d) = p
            val nr = c
            val nc = sideSize - 1 - r
            val nd = (d + 1).positiveModulo(4)
            return listOf(nr, nc, nd)
        }

        fun rotateLeft(p: List<Int>) = rotateRight(rotateRight(rotateRight(p)))
        fun rotateTwice(p: List<Int>) = rotateRight(rotateRight(p))

        data class Action(
            val side: Int,
            val direction: Int,
            val nextSide: Int,
            val transform: (List<Int>) -> List<Int>
        )

        val actions = HashMap<Pair<Int, Int>, Action>()
        fun registerAction(
            side: Int,
            dir: Int,
            nextSide: Int,
            transform: (List<Int>) -> List<Int> = { it }
        ) {
            val action = Action(side, dir, nextSide, transform)
            actions[side to dir] = action
        }

        registerAction(1, R, 2)
        registerAction(1, D, 3)
        registerAction(1, L, 4, ::rotateTwice)
        registerAction(1, U, 6, ::rotateRight)

        registerAction(2, R, 5, ::rotateTwice)
        registerAction(2, D, 3, ::rotateRight)
        registerAction(2, L, 1)
        registerAction(2, U, 6)

        registerAction(3, R, 2, ::rotateLeft)
        registerAction(3, D, 5)
        registerAction(3, L, 4, ::rotateLeft)
        registerAction(3, U, 1)

        registerAction(4, R, 5)
        registerAction(4, D, 6)
        registerAction(4, L, 1, ::rotateTwice)
        registerAction(4, U, 3, ::rotateRight)

        registerAction(5, R, 2, ::rotateTwice)
        registerAction(5, D, 6, ::rotateRight)
        registerAction(5, L, 4)
        registerAction(5, U, 3)

        registerAction(6, R, 5, ::rotateLeft)
        registerAction(6, D, 2)
        registerAction(6, L, 1, ::rotateLeft)
        registerAction(6, U, 4)

        fun getSide(row: Int, col: Int) = sideMap[row / sideSize][col / sideSize].digitToInt()

        return solve(input) { (row, col, dir) ->
            val action = actions[getSide(row, col) to dir]!!

            val normalized = listOf(
                (row + dr[dir]).positiveModulo(sideSize),
                (col + dc[dir]).positiveModulo(sideSize),
                dir
            )
            val transformed = action.transform(normalized)

            val sideStart = sideMap.findPos(action.nextSide.digitToChar()) * sideSize
            val newRow = transformed[0] + sideStart.first
            val newCol = transformed[1] + sideStart.second
            val newDir = transformed[2]

            when (input[newRow][newCol]) {
                '.' -> listOf(newRow, newCol, newDir)
                '#' -> listOf(row, col, dir)
                else -> error("nope")
            }
        }
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 22)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

//        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
