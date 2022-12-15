import kotlin.math.abs

fun main() {
    data class Position(val x: Int, val y: Int) {
        fun distance(p: Position) = abs(x - p.x) + abs(y - p.y)
    }

    data class SensorBeacon(val sensor: Position, val beacon: Position) {
        fun distance() = sensor.distance(beacon)
    }

    fun parse(input: List<String>): List<SensorBeacon> {
        val r = Regex("Sensor at x=(?<x1>.*), y=(?<y1>.*): closest beacon is at x=(?<x2>.*), y=(?<y2>.*)").toPattern()
        val pairs = input.map { r.matcher(it).also { m -> m.find() } }.map {
            val x1 = it.group("x1")
            val y1 = it.group("y1")
            val x2 = it.group("x2")
            val y2 = it.group("y2")
            SensorBeacon(
                Position(x1.toInt(), y1.toInt()),
                Position(x2.toInt(), y2.toInt())
            )
        }
        return pairs
    }

    fun part1(input: List<String>): Int {
        val pairs = parse(input)

        val all = pairs.map { it.beacon } + pairs.map { it.sensor }.toSet()
        val minX = all.minOf { it.x }
        val maxX = all.maxOf { it.x }
        val maxDistance = pairs.maxOf { it.distance() }

        val Y = 2000000
//        val Y = 10
        var cnt = 0
        for (x in minX - maxDistance - 1..maxX + maxDistance + 1) {
            val p = Position(x, Y)
            if (p in all) continue
            val anyFail = pairs.any { (sensor, beacon) -> sensor.distance(p) <= sensor.distance(beacon) }
            if (anyFail) {
                cnt++
            }
        }

        return cnt
    }

    fun sum(to: Int) = to * (to + 1L) / 2
    fun sum(from: Int, to: Int) = if (from > to) 0 else sum(to) - sum(from - 1)

    fun part2(input: List<String>): Long {
        val pairs = parse(input)

        val max = 4000000
        val multiplier = 4000000
        var tf = 0L
        for (y in 0..max) {
            val segments = mutableListOf(max + 1..max + 1)
            for ((sensor, beacon) in pairs) {
                val dist = sensor.distance(beacon)
                val r = dist - abs(y - sensor.y)
                if (r >= 0) {
                    val left = maxOf(0, sensor.x - r)
                    val right = minOf(max, sensor.x + r)
                    segments += left..right
                }
            }

            segments.sortBy { it.first }
            var start = 0
            for (s in segments) {
                val cnt = s.first - start
                if (cnt > 0) {
                    val sumX = sum(start, s.first - 1)
                    val sumY = cnt * y.toLong()
                    tf += sumX * multiplier + sumY
                }
                start = maxOf(start, s.last + 1)
            }
        }

        return tf
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 15)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
