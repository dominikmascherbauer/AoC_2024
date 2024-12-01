import kotlin.math.abs

fun main() {

    fun Pair<List<Int>, List<Int>>.part1(): Int =
        first.sorted().zip(second.sorted())
            .sumOf { abs(it.first - it.second) }

    fun Pair<List<Int>, List<Int>>.part2(): Int =
        with(second.groupingBy { it }.eachCount()) {
            first.sumOf { it * this.getOrElse(it) { 0 } }
        }

    // Process input string
    val input = readInput("Day01")
        .filter { it.isNotEmpty() }
        .map { line -> line.substringBefore(' ').toInt() to line.substringAfterLast(' ').toInt() }
        .unzip()

    // run task implementations
    println(input.part1())
    println(input.part2())
}
