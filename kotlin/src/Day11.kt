fun main() {

    val lookupMap = mutableMapOf<Pair<Int, String>, List<String>>()

    fun String.lookup(depth: Int): List<String> =
        lookupMap.computeIfAbsent(depth to this) { s ->
            generateSequence(listOf(s.second)) { acc ->
                acc.flatMap { s ->
                    if (s.length == 1 && s.toInt() == 0) {
                        listOf("1")
                    } else if (s.length % 2 == 0) {
                        listOf(s.substring(0, s.length / 2), s.substring(s.length / 2).toLong().toString())
                    } else {
                        listOf("${s.toLong() * 2024}")
                    }
                }
            }.drop(depth)
                .first ()
                .toList()
        }


    fun String.blink(blinkCount: Int, stepSize: Int): Long {
        if (blinkCount <= stepSize) {
            return lookup(blinkCount).size.toLong()
        }
        return lookup(stepSize).groupBy { it }.map { it.key to it.value.size }.sumOf { it.second * it.first.blink(blinkCount - stepSize, stepSize) }
    }

    fun String.part1(): Int =
        generateSequence(split(" ")) { acc ->
            acc.flatMap { s ->
                if (s.length == 1 && s.toInt() == 0) {
                    listOf("1")
                } else if (s.length%2 == 0) {
                    listOf(s.substring(0, s.length/2), s.substring(s.length/2).toLong().toString())
                } else {
                    listOf("${s.toLong() * 2024}")
                }
            }
        }.drop(25)
            .first()
//            .also { println(it.joinToString(" ")) }
            .size

    fun String.part2(): Long =
        split(" ")
            .sumOf { it.blink(75, 20) }


    // Process input string
    val input = readInput("Day11")
        .first()

    // run task implementations
    println(input.part1())
    println(input.part2())
}
