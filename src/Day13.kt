fun main() {
    data class Node(
        var single: Int? = null,
        var list: List<Node>? = null,
    ) : Comparable<Node> {
        fun compare(a: List<Node>, b: List<Node>): Int {
            for (i in 0 until minOf(a.size, b.size)) {
                val c = a[i].compareTo(b[i])
                if (c != 0) return c
            }
            return a.size.compareTo(b.size)
        }

        override fun compareTo(other: Node): Int {
            if (single != null && other.single != null) return single!!.compareTo(other.single!!)
            val a = list ?: listOf(Node(single = single))
            val b = other.list ?: listOf(Node(single = other.single))
            return compare(a, b)
        }

        override fun toString(): String {
            return if (single != null) single.toString() else list!!.joinToString(",", "[", "]")
        }
    }

    fun parse(a: String): Node {
        var at = 0
        fun parse(): Node {
            val result = if (a[at] == '[') {
                at++
                val list = mutableListOf<Node>()
                while (a[at] != ']') {
                    list += parse()
                    if (a[at] == ',') at++
                }
                Node(list = list).also { at++ }
            } else {
                var x = 0
                while (a[at].isDigit()) {
                    x = x * 10 + a[at++].digitToInt()
                }
                Node(single = x)
            }
            return result
        }
        return parse().also { require(at == a.length) }
    }

    fun compare(a: String, b: String): Int {
        val s = parse(a)
        val t = parse(b)
        return s.compareTo(t)
    }

    fun part1(input: List<String>): Int {
        var cnt = 0
        for (i in input.indices step 3) {
            if (compare(input[i], input[i + 1]) <= 0) {
                cnt += i / 3 + 1
            }
        }

        return cnt
    }

    fun part2(input: List<String>): Int {
        val a = mutableListOf<Node>()
        for (i in input.indices step 3) {
            a += parse(input[i])
            a += parse(input[i + 1])
        }
        val n2 = parse("[[2]]")
        val n6 = parse("[[6]]")
        a += n2
        a += n6
        a.sort()
        return (a.indexOf(n2) + 1) * (a.indexOf(n6) + 1)
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 13)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
