import java.math.BigInteger
import java.util.*

fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = HashMap<String, () -> Long>()
        val cache = HashMap<String, Long>()
        fun get(name: String): Long = cache.getOrPut(name) { return monkeys[name]!!() }
        for (m in input) {
            val parts = m.split(": ")
            val name = parts[0]
            val right = parts[1].split(" ")
            if (right.size == 1) {
                val x = right[0].toLong()
                monkeys[name] = { x }
            } else {
                val x = right[0]
                val y = right[2]
                val op = when (right[1].single()) {
                    '+' -> {
                        { get(x) + get(y) }
                    }

                    '-' -> {
                        { get(x) - get(y) }
                    }

                    '/' -> {
                        { get(x) / get(y) }
                    }

                    '*' -> {
                        { get(x) * get(y) }
                    }

                    else -> error("")
                }
                monkeys[name] = { op() }
            }
        }
        return monkeys["root"]!!()
    }

    val U = BigInteger.valueOf(100).pow(10000).times(BigInteger.valueOf(-1))

    fun part2(input: List<String>): BigInteger {
        val names = input.map { it.split(": ")[0] }
        val depX = IntArray(names.size) { -1 }
        val depY = IntArray(names.size) { -1 }
        val op = CharArray(names.size)
        val value = Array(names.size) { BigInteger.ZERO }
        val cache = Array(names.size) { U }
        for (m in input) {
            val parts = m.split(": ")
            val name = names.indexOf(parts[0])
            val right = parts[1].split(" ")
            if (right.size == 1) {
                val x = right[0].toBigInteger()
                value[name] = x
            } else {
                depX[name] = names.indexOf(right[0])
                depY[name] = names.indexOf(right[2])
                op[name] = right[1].single()
            }
        }

        fun get(idx: Int): BigInteger {
            val ready = cache[idx]
            if (ready != U) return ready
            val res = when (op[idx]) {
                '+' -> get(depX[idx]) + get(depY[idx])
                '-' -> get(depX[idx]) - get(depY[idx])
                '/' -> get(depX[idx]) / get(depY[idx])
                '*' -> get(depX[idx]) * get(depY[idx])
                else -> value[idx]
            }
            cache[idx] = res
            return res
        }

        val rootIndex = names.indexOf("root")
        val rootXIndex = depX[rootIndex]
        val rootYIndex = depY[rootIndex]
        val humanIndex = names.indexOf("humn")

        cache.fill(U)
        val rootYValue = get(rootYIndex)
        println("ROOT Y VALUE = $rootYValue")
        val variable = cache.indices.filter { cache[it] == U }.toIntArray()
        println("Human index $humanIndex")
        println("Variables ${variable.toList()}")
        require(humanIndex in variable)
        require(rootXIndex in variable)

        fun getXWithHuman(human: BigInteger): BigInteger {
            cache.fill(U)
            value[humanIndex] = human
            return get(rootXIndex)
        }

        var step = BigInteger.ONE
        while (getXWithHuman(step) >= rootYValue) {
            require(getXWithHuman(step) >= getXWithHuman(step * BigInteger.TWO)) { "$step" }
            step *= BigInteger.TWO
        }
        var L = step / BigInteger.TWO
        var R = step
        while (R - L > BigInteger.ONE) {
            val M = (L + R) / BigInteger.TWO
            val xWithHuman = getXWithHuman(M)
            if (xWithHuman > rootYValue) L = M
            else R = M
        }
        println("Right is $R value is ${getXWithHuman(R)} yValue is $rootYValue")

        var prev = (-1).toBigInteger()
        var humanValue = L.dec()
        while (true) {
            humanValue = humanValue.inc()
            if (humanValue % BigInteger.valueOf(1_000_000) == BigInteger.ZERO) {
                println("Time $humanValue")
            }
            for (i in variable) cache[i] = U
            value[humanIndex] = humanValue
            val rootXValue = get(rootXIndex)
            if (rootXValue == get(rootYIndex)) {
                return humanValue
            }
            if (rootXValue != prev) {
                prev = rootXValue
                require(rootXValue > prev)
                println("New rootX = $rootXValue, rootY = $rootYValue")
            }
        }
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 21)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

//        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
