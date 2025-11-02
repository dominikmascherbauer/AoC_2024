fun main() {

    fun String.readReport(): List<Int> =
        // parse int levels from string report
        split(' ')
            .filter { it.isNotEmpty() }
            .map { it.toInt() }

    fun List<Int>.isSafeReport(): Boolean =
        // check with following level
        zipWithNext { a, b -> a - b }
            // either all level higher or all levels lower than the level before by a maximum difference of 3
            .run { all { it in -3..-1 } || all { it in 1..3 } }


    fun List<String>.part1(): Int =
        map(String::readReport)
            .count(List<Int>::isSafeReport)

    fun List<String>.part2(): Int =
        map(String::readReport)
            // check all combinations with exactly one dampened level
            // a report that is safe will stay safe e.g. by only dampening the first level
            .count { levels ->
                levels.indices
                    .map { skip -> levels.filterIndexed { idx, _ -> idx != skip } }
                    .any(List<Int>::isSafeReport)
            }

    // Process input string
    val input = readInput("Day02")
        .filter { it.isNotBlank() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
