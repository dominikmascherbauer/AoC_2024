fun main() {

    fun List<String>.part1(): Any =
        flatMap { line -> line.split("-").chunked(2).flatMap { listOf(it[0] to it[1], it[1] to it[0]) } }
            .groupBy({it.first}) { it.second }
            .run {
                mapValues { e -> e.value.flatMap { v1 ->
                    e.value.filter { v2 -> v1 != v2 }
                        .map { v2 -> v1 to v2 }
                }.filter { value -> value.second in (this[value.first] ?: emptyList()) }
                    .map { value -> listOf(e.key, value.first, value.second) }
                }
            }
            .flatMap { it.value }
            .map { it.sorted() }
            .toSet()
            .filter { it.any { it.first() == 't' } }
            .size

    fun List<String>.part2(): Any =
        flatMap { line -> line.split("-").chunked(2).flatMap { listOf(it[0] to it[1], it[1] to it[0]) } }
            .groupBy({it.first}) { it.second }
            .run {
                flatMap { entry ->
                    generateSequence (setOf(listOf(entry.key))) { groups ->
                        groups.flatMap { group ->
                            entry.value
                                .filter { pc -> pc !in group }
                                .filter { pc -> group.all { groupPc -> pc in (this[groupPc] ?: emptyList()) } }
                                .map { pc -> group.plus(pc)}
                        }.map { it.sorted() }
                            .toSet()
                            .ifEmpty { null }
                    }.last()
                }
            }
            .toSet()
            .run {
                filter { it.size == this.maxOf { it.size } }
            }
            .first()
            .joinToString(",")


    val input = readInput("Day23")
        .filter(String::isNotEmpty)
    println(input.part1())
    println(input.part2())
}