private fun create(a: String, b: String): Node {
    return if (a == "dir") Node(name = b, inside = hashMapOf())
    else Node(name = b, size = a.toInt())
}

private class Node(
    val name: String,
    val size: Int? = null,
    val inside: MutableMap<String, Node>? = null,
) {
    fun advance(name: String): Node {
        return addIfNeeded(create("dir", name))
    }

    fun addIfNeeded(node: Node): Node {
        if (node.name !in inside!!) inside[node.name] = node
        return inside[node.name]!!
    }

    fun isDir() = inside != null
}

private fun build(input: List<String>): Node {
    val root = create("dir", "")
    val curPath = mutableListOf(root)

    var at = 0
    while (at < input.size) {
        val split = input[at].split(" ")
        when (split[1]) {
            "cd" -> {
                if (split[2] == "/") while (curPath.size > 1) curPath.removeLast()
                else {
                    for (change in split[2].split("/")) {
                        if (change == ".") continue
                        else if (change == "..") curPath.removeLast()
                        else curPath.add(curPath.last().advance(change))
                    }
                }
            }

            "ls" -> {
                while (at + 1 < input.size && !input[at + 1].startsWith("$ ")) {
                    at++
                    val p = input[at].split(" ")
                    curPath.last().addIfNeeded(create(p[0], p[1]))
                }
            }
        }
        at++
    }

    return root
}

private fun allDirs(root: Node): List<Pair<Node, Int>> {
    val found = mutableListOf<Pair<Node, Int>>()
    fun dfs(cur: Node): Int {
        if (!cur.isDir()) return cur.size!!
        var sz = 0
        for (i in cur.inside!!) sz += dfs(i.value)
        found.add(cur to sz)
        return sz
    }
    dfs(root)
    return found
}

fun main() {
    fun part1(input: List<String>): Int {
        val root = build(input)
        return allDirs(root).map { it.second }.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val root = build(input)
        val all = allDirs(root)
        val used = all.maxOf { it.second }
        val free = 70000000 - used
        val needFree = 30000000 - free
        return all.map { it.second }.filter { it >= needFree }.min()
    }

    @Suppress("DuplicatedCode")
    run {
        val day = String.format("%02d", 7)
        val testInput = readInput("Day${day}_test")
        val input = readInput("Day$day")
        println("Part 1 test - " + part1(testInput))
        println("Part 1 real - " + part1(input))

        println("---")

        println("Part 2 test - " + part2(testInput))
        println("Part 2 real - " + part2(input))
    }
}
