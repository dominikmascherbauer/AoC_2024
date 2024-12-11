fun main() {

    fun String.part1(): Long =
        flatMapIndexed { idx, c -> List(c.digitToInt()) { if (idx%2 != 0) "." else "${idx/2}" } }
            .run {
                val reversedNums = this.reversed().filter { it != "." }.map(String::toLong)
                this.take(reversedNums.size).foldIndexed(0L to 0) { idx, acc, s ->
                    if (s == ".") {
                        acc.first + idx * reversedNums[acc.second] to acc.second + 1
                    } else {
                        acc.first + idx * s.toLong() to acc.second
                    }
                }.first
            }

    fun String.part2(): Long =
        mapIndexed { idx, c -> List(c.digitToInt()) { if (idx%2 != 0) -1 else idx/2 } }
            .filter { it.isNotEmpty() }
            .run {
                reversed().filter { it.first() != -1 }
                    .fold(this) { acc, id ->
                        val idx = acc.takeWhile { it.first() != id.first() }.indexOfFirst { it.size >= id.size && it.first() == -1 }
                        if (idx != -1) {
                            acc.take(idx)
                                .plusElement(id)
                                .plusElement(acc[idx].drop(id.size))
                                .plus(acc.drop(idx + 1).map { if (it.first() == id.first()) it.map { -1 } else it })
                                .filter { it.isNotEmpty() }
                        } else {
                            acc
                        }
                    }
            }.flatten()
            .map { if (it == -1) 0 else it }
            .mapIndexed { idx, i -> idx * i.toLong() }
            .sum()


    // Process input string
    val input = readInput("Day09")
        .first()

    // run task implementations
    println(input.part1())
    println(input.part2())
}
