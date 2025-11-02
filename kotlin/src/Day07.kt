import java.util.function.BiFunction

fun main() {

    // find valid combinations by going from back to the front
    // is faster as we can stop with a path if e.g. the division does not work out or the lef hand side does not end with the right hand side
    // this reduces the size of the search tree massively
    fun List<String>.validCombinationSum(vararg ops: BiFunction<Long, Long, Long>): Long =
        map { line -> line.substringBefore(':').toLong() to line.substringAfter(':').split(' ').filter { it.isNotEmpty() }.map { it.toLong() } }
            .filter { line ->
                line.second.reversed().fold(listOf(line.first)) { acc, i ->
                    acc.flatMap { num -> ops.map { it.apply(num, i) } }.filter { it >= 0 }
                }.any { it == 0L }
            }
            .sumOf { it.first }

    fun List<String>.part1(): Long =
        // there are two operator stolen by the elephants
        // however, we actually do not want to reconstruct the result and rather destruct it to 0 if possible as this is more efficient
        // so, I kindly asked the elephants if the can help me figure out the inverse operations
        validCombinationSum(
            { a, b -> a - b },
            { a, b -> if (a % b != 0L) -1 else a / b}
        )

    fun List<String>.part2(): Long =
        // seems like one operator was missing
        // but the elephants helped me out to find the correct inverse operation again
        validCombinationSum(
            { a, b -> a - b },
            { a, b -> if (a % b != 0L) -1 else a / b},
            { a, b -> if (!"$a".endsWith("$b")) -1 else "0$a".dropLast("$b".length).toLong() }
        )

    // Process input string
    val input = readInput("Day07")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
