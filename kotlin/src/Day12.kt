import kotlin.math.abs

fun main() {
    fun List<String>.part1(): Any =
        flatMapIndexed { row, s -> s.mapIndexed { col, c -> c to (row to col) } }
            .groupBy({ it.first }, { it.second })
            .flatMap { (c, coords) ->
                generateSequence(coords.drop(1) to listOf(listOf(coords.first()))) { (coords, buckets) ->
                    if (coords.isEmpty()) {
                        null
                    } else {
                        val matchingBuckets = buckets.filter { bucket -> bucket.any { abs(it.first - coords.first().first) + abs(it.second - coords.first().second) == 1 } }
                        if (matchingBuckets.isEmpty()) {
                            coords.drop(1) to buckets.plusElement(listOf(coords.first()))
                        } else if (matchingBuckets.size == 1) {
                            coords.drop(1) to buckets.filter { it != matchingBuckets.first() }.plusElement(matchingBuckets.first().plus(coords.first()))
                        } else {
                            coords.drop(1) to buckets.filter { bucket -> !matchingBuckets.contains(bucket) }.plusElement(matchingBuckets.reduce { a, b -> a.plus(b) }.plus(coords.first()))
                        }
                    }
                }.last().second.map { c to it }
            }.map { it.first to (it.second.size to it.second.sumOf { coord -> 4 - it.second.count { otherCoord -> abs(otherCoord.first - coord.first) + abs(otherCoord.second - coord.second) == 1 } }) }
            .sumOf { (it.second.first * it.second.second).toLong() }

    fun List<String>.part2(): Any =
        flatMapIndexed { row, s -> s.mapIndexed { col, c -> c to (row to col) } }
            .groupBy({ it.first }, { it.second })
            .flatMap { (c, coords) ->
                generateSequence(coords.drop(1) to listOf(listOf(coords.first()))) { (coords, buckets) ->
                    if (coords.isEmpty()) {
                        null
                    } else {
                        val matchingBuckets = buckets.filter { bucket -> bucket.any { abs(it.first - coords.first().first) + abs(it.second - coords.first().second) == 1 } }
                        if (matchingBuckets.isEmpty()) {
                            coords.drop(1) to buckets.plusElement(listOf(coords.first()))
                        } else if (matchingBuckets.size == 1) {
                            coords.drop(1) to buckets.filter { it != matchingBuckets.first() }.plusElement(matchingBuckets.first().plus(coords.first()))
                        } else {
                            coords.drop(1) to buckets.filter { bucket -> !matchingBuckets.contains(bucket) }.plusElement(matchingBuckets.reduce { a, b -> a.plus(b) }.plus(coords.first()))
                        }
                    }
                }.last().second.map { c to it }
            }.map { it.first to (it.second.size to it.second.flatMap { coord ->
                listOf('D' to (coord.first + 1 to coord.second), 'U' to (coord.first - 1 to coord.second), 'L' to (coord.first to coord.second + 1), 'R' to (coord.first to coord.second - 1))
                    .filterNot { edgeCoord -> it.second.contains(edgeCoord.second) }
            }.toSet()
                .sortedBy { it.second.second }
                .sortedBy { it.second.first }
                .groupBy({ it.first }, { it.second })
                .map { entry ->
                    entry.value
                        .groupBy({ if (entry.key == 'U' || entry.key == 'D') it.first else it.second }, { if (entry.key == 'U' || entry.key == 'D') it.second else it.first })
                        .map { it.value.sorted().zipWithNext().filter { it.second - it.first != 1 }.size + 1 }
                        .sum()
                }
                .sum()
            )}
            .sumOf { (it.second.first * it.second.second).toLong() }


    // Process input string
    val input = readInput("Day12")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
