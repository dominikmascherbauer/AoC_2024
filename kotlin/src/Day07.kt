import java.util.function.BiFunction

fun main() {

    fun List<String>.validCombinationSum(vararg ops: BiFunction<Long, Long, Long>): Long =
        map { line -> line.substringBefore(':').toLong() to line.substringAfter(':').split(' ').filter { it.isNotEmpty() }.map { it.toLong() } }
            .filter { line ->
                line.second.reversed().fold(listOf(line.first)) { acc, i ->
                    acc.flatMap { num -> ops.map { it.apply(num, i) } }.filter { it >= 0 }
                }.any { it == 0L }
            }
            .sumOf { it.first }

    fun List<String>.part1(): Long =
        validCombinationSum(
            { a, b -> a - b },
            { a, b -> if (a % b != 0L) -1 else a / b}
        )

    fun List<String>.part2(): Long =
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
