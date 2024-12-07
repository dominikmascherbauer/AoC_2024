fun main() {

    fun List<String>.part1(): Long =
        map { line -> line.substringBefore(':').toLong() to line.substringAfter(':').split(' ').filter { it.isNotEmpty() }.map { it.toLong() } }
            .filter { line ->
                line.second.drop(1).fold(listOf(line.second.first())) { acc, i ->
                    acc.flatMap { listOf(it + i, it * i) }.filter { it <= line.first }
                }.count { it == line.first } > 0
            }
            .sumOf { it.first }

    fun List<String>.part2(): Long =
        map { line -> line.substringBefore(':').toLong() to line.substringAfter(':').split(' ').filter { it.isNotEmpty() }.map { it.toLong() } }
            .filter { line ->
                line.second.drop(1).fold(listOf(line.second.first())) { acc, i ->
                    acc.flatMap { listOf(it + i, it * i, "$it$i".toLong()) }.filter { it <= line.first }
                }.count { it == line.first } > 0
            }
            .sumOf { it.first }

    // Process input string
    val input = readInput("Day07")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
