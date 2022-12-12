fun main() {
    val dx = intArrayOf(0, 1, 0, -1)
    val dy = intArrayOf(1, 0, -1, 0)
    fun part1(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        fun inside(p: Pair<Int, Int>): Boolean {
            val (x, y) = p
            return x in 0 until n && y in 0 until m
        }

        val dist = Array(n) { IntArray(m) { -1 } }
        fun find(c: Char): Pair<Int, Int> {
            for (i in 0 until n) for (j in 0 until m) if (c == input[i][j]) return i to j
            error("")
        }

        fun getCode(c: Char): Int = when (c) {
            'S' -> getCode('a')
            'E' -> getCode('z')
            else -> c.code
        }
        val (x0, y0) = find('S')
        dist[x0][y0] = 0
        val q = ArrayDeque<Pair<Int, Int>>().apply { add(x0 to y0) }
        while (!q.isEmpty()) {
            val (x, y) = q.removeFirst()
            if (input[x][y] == 'E') return dist[x][y]
            for (d in 0 until 4) {
                val nx = x + dx[d]
                val ny = y + dy[d]
                val inside = inside(nx to ny)
                if (inside && getCode(input[nx][ny]) - getCode(input[x][y]) <= 1 && dist[nx][ny] == -1) {
                    dist[nx][ny] = dist[x][y] + 1
                    q += nx to ny
                }
            }
        }

        return -2
    }

    fun part2(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        fun inside(p: Pair<Int, Int>): Boolean {
            val (x, y) = p
            return x in 0 until n && y in 0 until m
        }

        val dist = Array(n) { IntArray(m) { -1 } }
        fun find(c: Char): Pair<Int, Int> {
            for (i in 0 until n) for (j in 0 until m) if (c == input[i][j]) return i to j
            error("")
        }

        fun getCode(c: Char): Int = when (c) {
            'S' -> getCode('a')
            'E' -> getCode('z')
            else -> c.code
        }
        val (x0, y0) = find('E')
        dist[x0][y0] = 0
        val q = ArrayDeque<Pair<Int, Int>>().apply { add(x0 to y0) }
        while (!q.isEmpty()) {
            val (x, y) = q.removeFirst()
            if (getCode(input[x][y]) == 'a'.code) return dist[x][y]
            for (d in 0 until 4) {
                val nx = x + dx[d]
                val ny = y + dy[d]
                val inside = inside(nx to ny)
                if (inside && getCode(input[x][y]) - getCode(input[nx][ny]) <= 1 && dist[nx][ny] == -1) {
                    dist[nx][ny] = dist[x][y] + 1
                    q += nx to ny
                }
            }
        }

        return -2
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 12)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
