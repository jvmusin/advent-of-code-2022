import java.time.Duration
import java.time.Instant
import java.util.*

fun main() {
    data class Blueprint(val robotToCosts: Array<IntArray>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Blueprint) return false

            if (!robotToCosts.contentDeepEquals(other.robotToCosts)) return false

            return true
        }

        override fun hashCode(): Int {
            return robotToCosts.contentDeepHashCode()
        }
    }

    val allMaterials = arrayOf("ore", "clay", "obsidian", "geode")

    fun readBlueprint(s: String): Blueprint {
        val parts = s.dropLast(1).split(".")
        require(allMaterials.size == parts.size)
        val costs = Array(allMaterials.size) { intArrayOf() }
        for (i in allMaterials.indices) {
            val line = parts[i].split("costs ")[1]
            val res = IntArray(allMaterials.size)
            for (part in line.split(" and ")) {
                val amount = part.split(' ')[0].toInt()
                val type = allMaterials.indexOf(part.split(' ')[1])
                require(type != -1)
                res[type] = amount
            }
            costs[i] = res
        }
        return Blueprint(costs)
    }

    fun part1(input: List<String>): Int {
        val blueprints = input.map(::readBlueprint)

        fun solve(bIndex: Int, b: Blueprint): Int {
            data class State(val robots: IntArray, val materials: IntArray, val robotBuilt: Boolean) {
                override fun equals(other: Any?): Boolean {
                    if (this === other) return true
                    if (other !is State) return false

                    if (!robots.contentEquals(other.robots)) return false
                    if (!materials.contentEquals(other.materials)) return false
                    if (robotBuilt != other.robotBuilt) return false

                    return true
                }

                override fun hashCode(): Int {
                    var result = robots.contentHashCode()
                    result = 31 * result + materials.contentHashCode()
                    result = 31 * result + robotBuilt.hashCode()
                    return result
                }
            }

            fun State.couldCreateRobot() = BooleanArray(allMaterials.size) { robot ->
                val costs = b.robotToCosts[robot]
                for (material in allMaterials.indices) {
                    if (costs[material] > materials[material] - robots[material]) {
                        return@BooleanArray false
                    }
                }
                true
            }

            val dp = HashSet<State>()
            dp.add(
                State(
                    robots = IntArray(allMaterials.size).also { it[0] = 1 },
                    materials = IntArray(allMaterials.size),
                    robotBuilt = false
                )
            )
            repeat(24) {
                println("BIndex $bIndex Time $it dpSize ${dp.size}")
                val nextDP = HashSet<State>()
                for (state in dp) {
                    val couldCreateRobot = state.couldCreateRobot()
                    val newMaterials = IntArray(allMaterials.size) { state.materials[it] + state.robots[it] }
                    for (robotName in allMaterials.indices) {
                        if (couldCreateRobot[3] && robotName != 3) continue
                        if (state.robotBuilt || !couldCreateRobot[robotName]) {
                            var enough = true
                            val curRobotCosts = b.robotToCosts[robotName]
                            for (material in allMaterials.indices) {
                                if (state.materials[material] < curRobotCosts[material]) {
                                    enough = false
                                    break
                                }
                            }
                            if (enough) {
                                nextDP.add(
                                    State(
                                        robots = IntArray(allMaterials.size) { state.robots[it] }.also { it[robotName]++ },
                                        materials = IntArray(allMaterials.size) { newMaterials[it] - curRobotCosts[it] },
                                        robotBuilt = true
                                    )
                                )
                            }
                        }
                    }
                    if (!couldCreateRobot[3]) nextDP.add(State(state.robots, newMaterials, false))
                }
                dp.clear()
                dp += nextDP
            }

            val result = dp.maxOf { it.materials.last() }
            return result
        }

        var sum = 0
        for ((i, b) in blueprints.withIndex()) {
            sum += (i + 1) * solve(i, b)
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        val startTime = Instant.now()
        fun String.prependTime() = "(${Duration.between(startTime, Instant.now())}) $this"
        val blueprints = input.map(::readBlueprint)

        fun solve(bIndex: Int, b: Blueprint): Int {
            data class State(val robots: IntArray, val materials: IntArray, val robotBuilt: Boolean) {
                override fun equals(other: Any?): Boolean {
                    if (this === other) return true
                    if (other !is State) return false

                    if (!robots.contentEquals(other.robots)) return false
                    if (!materials.contentEquals(other.materials)) return false
                    if (robotBuilt != other.robotBuilt) return false

                    return true
                }

                override fun hashCode(): Int {
                    var result = robots.contentHashCode()
                    result = 31 * result + materials.contentHashCode()
                    result = 31 * result + robotBuilt.hashCode()
                    return result
                }
            }

            fun State.couldCreateRobot() = BooleanArray(allMaterials.size) { robot ->
                val costs = b.robotToCosts[robot]
                for (material in allMaterials.indices) {
                    if (costs[material] > materials[material] - robots[material]) {
                        return@BooleanArray false
                    }
                }
                true
            }

            val dp = HashSet<State>()
            dp.add(
                State(
                    robots = IntArray(allMaterials.size).also { it[0] = 1 },
                    materials = IntArray(allMaterials.size),
                    robotBuilt = false
                )
            )
            repeat(32) {
                println("BIndex $bIndex Time $it dpSize ${dp.size}".prependTime())
                val nextDP = HashSet<State>()
                for (state in dp) {
                    val couldCreateRobot = state.couldCreateRobot()
                    val newMaterials = IntArray(allMaterials.size) { state.materials[it] + state.robots[it] }
                    for (robotName in allMaterials.indices) {
                        if (couldCreateRobot[3] && robotName != 3) continue
                        if (state.robotBuilt || !couldCreateRobot[robotName]) {
                            var enough = true
                            val curRobotCosts = b.robotToCosts[robotName]
                            for (material in allMaterials.indices) {
                                if (state.materials[material] < curRobotCosts[material]) {
                                    enough = false
                                    break
                                }
                            }
                            if (enough) {
                                nextDP.add(
                                    State(
                                        robots = IntArray(allMaterials.size) { state.robots[it] }.also { it[robotName]++ },
                                        materials = IntArray(allMaterials.size) { newMaterials[it] - curRobotCosts[it] },
                                        robotBuilt = true
                                    )
                                )
                            }
                        }
                    }
                    if (!couldCreateRobot[3]) nextDP.add(State(state.robots, newMaterials, false))
                }
                dp.clear()
                dp += nextDP
                    .sortedByDescending { it.robots[3] }
                    .take(1_000_000)
            }

            val result = dp.maxOf { it.materials.last() }
            println("BIndex $bIndex result $result".prependTime())
            return result
        }

        var sum = 1L
        for ((i, b) in blueprints.take(3).withIndex()) {
            sum *= solve(i, b)
        }

        return sum
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 19)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
