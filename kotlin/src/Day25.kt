fun main() {

    fun List<String>.part1(): Any =
        chunked(7).map {
            if (it.first().first() == '#')
                "lock" to it.transpose().map { col -> col.drop(1).count { c -> c == '#' } }
            else
                "key" to it.transpose().map { col -> 5 - col.drop(1).count { c -> c == '.' } }
        }.groupBy({ it.first }) { it.second }
            .run {
                val locks = this["lock"]!!
                val keys = this["key"]!!

                locks.sumOf { lock ->
                    keys.count { key -> key.zip(lock).all { (k, l) -> k + l < 6 } }
                }
            }

    fun List<String>.part2(): Any =
        0


    val input = readInput("Day25")
        .filter(String::isNotEmpty)
    println(input.part1())
    println(input.part2())
}