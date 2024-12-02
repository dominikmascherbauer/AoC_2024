fun main() {

    fun List<String>.part1(): Int =
        map { line ->
            line.split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }
            .map { it.zipWithNext { a, b -> a - b } }
            .count { levels -> levels.all { it in -3..-1 } || levels.all { it in 1..3 } }

    fun List<String>.part2(): Int =
        map { line ->
            line.split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }
            .count { levels ->
                val toleratedLevelsA = levels.toMutableList()
                val toleratedLevelsB = levels.toMutableList()
                val toleratedLevelsC = levels.toMutableList()
                val toleratedLevelsD = levels.toMutableList()
                if (!levels.zipWithNext { a, b -> a - b }.all { it in -3..-1 }) {
                    val wrongLevel = levels.zipWithNext { a, b -> a to b }.indexOfFirst { (it.first - it.second) !in -3..-1 }
                    toleratedLevelsA.removeAt(wrongLevel)
                    toleratedLevelsB.removeAt(wrongLevel + 1)
                }
                if (!levels.zipWithNext { a, b -> a - b }.all { it in 1..3 }) {
                    val wrongLevel = levels.zipWithNext { a, b -> a to b }.indexOfFirst { (it.first - it.second) !in 1..3 }
                    toleratedLevelsC.removeAt(wrongLevel)
                    toleratedLevelsD.removeAt(wrongLevel + 1)
                }
                toleratedLevelsA.zipWithNext { a, b -> a - b }.all { it in -3..-1 } || toleratedLevelsB.zipWithNext { a, b -> a - b }.all { it in -3..-1 } ||
                        toleratedLevelsC.zipWithNext { a, b -> a - b }.all { it in 1..3 } || toleratedLevelsD.zipWithNext { a, b -> a - b }.all { it in 1..3 }
            }

    // Process input string
    val input = readInput("Day02").filter { it.isNotBlank() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
