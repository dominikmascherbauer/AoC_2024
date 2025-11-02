import kotlin.math.abs

fun main() {

    fun Pair<List<Int>, List<Int>>.part1(): Int =
        // compare the sorted location ids
        first.sorted().zip(second.sorted())
            .sumOf { abs(it.first - it.second) }

    fun Pair<List<Int>, List<Int>>.part2(): Int =
        // use second list to create a location id lookup map
        with(second.groupingBy { it }.eachCount()) {
            // sum up the similarity score for each location id in the first list
            first.sumOf { it * getOrDefault(it, 0) }
        }

    // Process input string
    val input = readInput("Day01")
        .filter { it.isNotEmpty() }
        // creates pairs of location ids
        .map { line -> line.substringBefore(' ').toInt() to line.substringAfterLast(' ').toInt() }
        // unwraps the list of location id pairs to a pair of location id lists
        .unzip()

    // run task implementations
    println(input.part1())
    println(input.part2())
}
