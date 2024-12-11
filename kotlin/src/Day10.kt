import kotlin.math.abs

fun main() {

    fun List<String>.part1(): Int =
        flatMapIndexed { row, s ->
            s.mapIndexed { col, c ->
                c.digitToInt() to (row to col)
            }
        }.groupBy { it.first }
            .run {
                generateSequence(0 to get(0)!!.map { setOf(it.second) }) { acc ->
                    if (acc.first >= 9) {
                        null
                    } else {
                        acc.first + 1 to acc.second.map { subList ->
                            subList.flatMap { pos ->
                                this[acc.first + 1]?.map { it.second }?.filter { abs(it.first - pos.first) + abs(it.second - pos.second) == 1 } ?: listOf()
                            }.toSet()
                        }
                    }
                }.last().second.sumOf { it.size }
            }

    fun List<String>.part2(): Int =
        flatMapIndexed { row, s ->
            s.mapIndexed { col, c ->
                c.digitToInt() to (row to col)
            }
        }.groupBy { it.first }
            .run {
                generateSequence(0 to get(0)!!.map { it.second }) { acc ->
                    if (acc.first >= 9) {
                        null
                    } else {
                        acc.first + 1 to acc.second.flatMap { pos ->
                            this[acc.first + 1]?.map { it.second }?.filter { abs(it.first - pos.first) + abs(it.second - pos.second) == 1 } ?: listOf()
                        }
                    }
                }.last().second.size
            }


    // Process input string
    val input = readInput("Day10")
        .filter(String::isNotEmpty)

    // run task implementations
    println(input.part1())
    println(input.part2())
}
