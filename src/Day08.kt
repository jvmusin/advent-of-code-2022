fun main() {
    val dx = intArrayOf(-1, 0, 1, 0)
    val dy = intArrayOf(0, 1, 0, -1)
    fun part1(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        val a = input.map { s -> s.map { it.digitToInt() } }

        fun check(x: Int, y: Int): Boolean {
            fun check(d: Int): Boolean {
                var x1 = x
                var y1 = y
                while (x1 in 0 until n && y1 in 0 until m) {
                    val nx = x1 + dx[d]
                    val ny = y1 + dy[d]
                    if (nx in 0 until n && ny in 0 until m) {
                        val cur = a[x][y]
                        val next = a[nx][ny]
                        if (next >= cur) return false
                    }
                    x1 = nx
                    y1 = ny
                }
                return true
            }
            for (d in 0 until 4) if (check(d)) return true
            return false
        }

        var cnt = 0
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (check(i, j)) {
                    cnt++
                }
            }
        }

        return cnt
    }

    fun part2(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        val a = input.map { s -> s.map { it.digitToInt() } }

        fun check(x: Int, y: Int): Int {
            fun check(d: Int): Int {
                var x1 = x
                var y1 = y
                var steps = 0
                while (true) {
                    val nx = x1 + dx[d]
                    val ny = y1 + dy[d]
                    if (nx in 0 until n && ny in 0 until m) {
                        steps++
                        val cur = a[x][y]
                        val next = a[nx][ny]
                        if (next >= cur) break
                    } else break
                    x1 = nx
                    y1 = ny
                }
                return steps
            }

            var r = 1
            for (d in 0 until 4) r *= check(d)
            return r
        }

        var mx = 0
        for (i in 0 until n) {
            for (j in 0 until m) {
                mx = maxOf(mx, check(i, j))
            }
        }

        return mx
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 8)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
