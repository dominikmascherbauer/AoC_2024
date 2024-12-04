fun main() {

    fun List<String>.getDiagonals(): List <String> =
        List(size + this[0].length - 1) { i ->
            var row = ""
            if (i < size) {
                for (j in 0..i.coerceAtMost(this[0].length - 1)) {
                    row += this[size-1-i+j][j]
                }
            } else {
                val k = i - size + 1
                for (j in 0..(this[0].length - 1 - k).coerceAtMost(this.size - 1)) {
                    row += this[j][j+k]
                }
            }
            row
        }

    fun List<String>.part1(): Int =
        run {
            sumOf { it.split("XMAS").count() + it.split("SAMX").count() - 2 } +
                    rotateLeft().sumOf { it.split("XMAS").count() + it.split("SAMX").count() - 2 } +
                    getDiagonals().sumOf { it.split("XMAS").count() + it.split("SAMX").count() - 2 } +
                    rotateLeft().getDiagonals().sumOf { it.split("XMAS").count() + it.split("SAMX").count() - 2 }
        }

    fun List<String>.part2(): Int =
        drop(1).dropLast(1).mapIndexed { i, line ->
            line.drop(1).dropLast(1).mapIndexed { j, c ->
                // checking char at i+1,j+1
                val leftToRight = "(MAS|SAM)".toRegex().matches("${this[i][j]}$c${this[i+2][j+2]}")
                val rightToLeft = "(MAS|SAM)".toRegex().matches("${this[i][j+2]}$c${this[i+2][j]}")
                if (c == 'A' && leftToRight && rightToLeft) 1 else 0
            }.sum()
        }.sum()

    // Process input string
    val input = readInput("Day04")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
