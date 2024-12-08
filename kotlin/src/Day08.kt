fun main() {

    fun List<String>.part1(): Int =
        flatMapIndexed { row, s ->
            s.mapIndexed { col, c ->
                c to (row to col)
            }
        }
            .groupBy { it.first }
            .filter { it.key != '.' && it.value.size > 1 }
            .map { frequency -> frequency.value.map { it.second } }
            .flatMap { antennas ->
                antennas.flatMap { antenna ->
                    antennas.filter { it != antenna }
                        .map { it.first - antenna.first to it.second - antenna.second }
                        .flatMap { listOf(antenna.first - it.first to antenna.second - it.second, antenna.first + 2*it.first to antenna.second + 2*it.second) }
                }
            }
            .toSet()
            .filter { it.first in indices && it.second in this[0].indices }
            .size

    fun List<String>.part2(): Any =
        flatMapIndexed { row, s ->
            s.mapIndexed { col, c ->
                c to (row to col)
            }
        }
            .groupBy { it.first }
            .filter { it.key != '.' && it.value.size > 1 }
            .map { frequency -> frequency.value.map { it.second } }
            .flatMap { antennas ->
                antennas.flatMap { antenna ->
                    antennas.filter { it != antenna }
                        .map { it.first - antenna.first to it.second - antenna.second }
                        .flatMap {
                            generateSequence(antenna) { antinode ->
                                if (antinode.first - it.first in indices && antinode.second - it.second in this[0].indices) {
                                    antinode.first - it.first to antinode.second - it.second
                                } else {
                                    null
                                }
                            }.toList().plus(
                                generateSequence(antenna) { antinode ->
                                    if (antinode.first + it.first in indices && antinode.second + it.second in this[0].indices) {
                                        antinode.first + it.first to antinode.second + it.second
                                    } else {
                                        null
                                    }
                                }.toList()
                            )
                        }
                }
            }
            .toSet()
            .filter { it.first in indices && it.second in this[0].indices }
            .size

    // Process input string
    val input = readInput("Day08")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
