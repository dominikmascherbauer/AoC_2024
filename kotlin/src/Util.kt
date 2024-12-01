import java.io.File
/**
 * Reads lines from the given resource text file.
 */
fun readInput(name: String) = File(ClassLoader.getSystemResource(name).path).readLines()

data class Point<T>(val x: Int, val y: Int, val value: T) {
    // create new point containing the new value
    fun setValue(newValue: T): Point<T> =
        Point(x, y, newValue)
}

/**
 * Greatest common divisor (gcd) for two Long values
 */
fun gcd(a: Long, b: Long): Long =
    generateSequence(a to b) {
        it.second to it.first % it.second
    }
        .first { it.second <= 0 }
        .first

/**
 * lowest common multiple (lcm) for two Long values
 */
fun lcm(a: Long, b: Long): Long =
    a * (b / gcd(a, b))


/**
 * transposes a rectangular grid
 */
fun List<String>.transpose(): List <String> =
    List(get(0).length) { i ->
        map { it[i] }.joinToString("")
    }

/**
 * rotates a rectangular grid to the left
 *  -> last col will be first row
 */
fun List<String>.rotateLeft(): List<String> =
    List(get(0).length) { i ->
        map { it[it.length - i - 1] }.joinToString("")
    }

/**
 * rotates a rectangular grid to the right
 *  -> first col will be first row
 */
fun List<String>.rotateRight(): List<String> =
    List(get(0).length) { i ->
        reversed().map { it[i] }.joinToString("")
    }