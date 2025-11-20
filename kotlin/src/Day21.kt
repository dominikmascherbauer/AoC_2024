fun main() {

    // 0,0 is always the non-existent slot
    val numpad = mapOf(
        '7' to (3 to 0),
        '8' to (3 to 1),
        '9' to (3 to 2),
        '4' to (2 to 0),
        '5' to (2 to 1),
        '6' to (2 to 2),
        '1' to (1 to 0),
        '2' to (1 to 1),
        '3' to (1 to 2),
        '0' to (0 to 1),
        'A' to (0 to 2)
    )

    val dirpad = mapOf(
        '^' to (0 to 1),
        'A' to (0 to 2),
        '<' to (1 to 0),
        'v' to (1 to 1),
        '>' to (1 to 2)
    )

    val numpadCache = mutableMapOf<Pair<Char, Char>, String>()
    val dirpadCache = mutableMapOf<Pair<Char, Char>, String>()

    fun getNumSequence(from: Char, to: Char): String =
        numpadCache.getOrPut(from to to) {
            val row = numpad[to]!!.first - numpad[from]!!.first
            val col = numpad[to]!!.second - numpad[from]!!.second
            if (row == 0) {
                if (col == 0)
                    "A"
                else if (col > 0)
                    ">".repeat(col) + "A"
                else // col < 0
                    "<".repeat(-col) + "A"
            } else if (col == 0) {
                if (row > 0)
                    "^".repeat(row) + "A"
                else // row < 0
                    "v".repeat(-row) + "A"
            } else if (row > 0 && col > 0) {
                "^".repeat(row) + ">".repeat(col) + "A"
            } else if (row > 0) {  // col < 0
                if (numpad[from]!!.second + col == 0 && numpad[from]!!.first == 0) {
                    "^".repeat(row) + "<".repeat(-col) + "A"
                } else {
                    "<".repeat(-col) + "^".repeat(row) + "A"
                }
            } else if (col > 0) {  // row < 0
                if (numpad[from]!!.first + row == 0 && numpad[from]!!.second == 0) {
                    ">".repeat(col) + "v".repeat(-row) + "A"
                } else {
                    "v".repeat(-row) + ">".repeat(col) + "A"
                }
            } else {
                "<".repeat(-col) + "v".repeat(-row) + "A"
            }
        }

    fun getDirSequence(from: Char, to: Char): String =
        dirpadCache.getOrPut(from to to) {
            val row = dirpad[to]!!.first - dirpad[from]!!.first
            val col = dirpad[to]!!.second - dirpad[from]!!.second
            if (row == 0) {
                if (col == 0)
                    "A"
                else if (col > 0)
                    ">".repeat(col) + "A"
                else // col < 0
                    "<".repeat(-col) + "A"
            } else if (col == 0) {
                if (row > 0)
                    "v".repeat(row) + "A"
                else // row < 0
                    "^".repeat(-row) + "A"
            } else if (row > 0 && col > 0) {
                "v".repeat(row) + ">".repeat(col) + "A"
            } else if (row > 0) {  // col < 0
                if (dirpad[from]!!.second + col == 0 && dirpad[from]!!.first == 0) {
                    "v".repeat(row) + "<".repeat(-col) + "A"
                } else {
                    "<".repeat(-col) + "v".repeat(row) + "A"
                }
            } else if (col > 0) {  // row < 0
                if (dirpad[from]!!.first + row == 0 && dirpad[from]!!.second == 0) {
                    ">".repeat(col) + "^".repeat(-row) + "A"
                } else {
                    "^".repeat(-row) + ">".repeat(col) + "A"
                }
            } else {
                "<".repeat(-col) + "^".repeat(-row) + "A"
            }
        }

    fun List<String>.part1(): Any =
        map { line ->
            line.dropLast(1).toLong() to line.fold( 'A' to listOf("")) { acc, ch ->
                ch to acc.second.map { it + getNumSequence(acc.first, ch) }
            }.second
        }.map { line ->
            line.first to (0..1).fold(line.second) { acc, i ->
                acc.flatMap { l ->
                    l.fold('A' to listOf("")) { acc, ch ->
                        ch to acc.second.map { it + getDirSequence(acc.first, ch) }
                    }.second
                }
            }
        }.sumOf { it.first * it.second.minOf { it.length } }

    fun List<String>.part2(): Any =
        map { line ->
            line.dropLast(1).toLong() to "A$line".zipWithNext().map { getNumSequence(it.first, it.second) }
        }.map { line ->
            line.first to (0..24).fold(line.second.groupBy { it }.mapValues { it.value.size.toLong() }) { acc, i ->
                acc.flatMap { (k, v) ->
                    "A$k".zipWithNext().map { v to getDirSequence(it.first, it.second) }
                }.groupBy { it.second }.mapValues { it.value.sumOf { it.first } }
            }.map { it.key.length * it.value }.sum()
        }.sumOf { it.first * it.second }


    val input = readInput("Day21")
        .filter(String::isNotEmpty)
    println(input.part1())
    println(input.part2())
}