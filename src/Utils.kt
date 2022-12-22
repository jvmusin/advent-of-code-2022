import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')



fun List<String>.findPos(c: Char): Pair<Int, Int> {
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j] == c) return i to j
        }
    }
    error("No such symbol")
}

fun Int.positiveModulo(mod: Int) = (this % mod + mod) % mod
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)
operator fun Pair<Int, Int>.times(k: Int) = (first * k) to (second * k)