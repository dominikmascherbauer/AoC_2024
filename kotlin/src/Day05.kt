fun main() {

    fun part1(rules: Map<Int, List<Int>>, updates: List<List<Int>>): Int =
        // iterate over all updates and check if they are in order
        updates.filter { list ->
            list.filterIndexed { idx, elem ->
                // if the right part of a rule contains an element that was already in the list before the page order is violated
                rules[elem]?.any { list.contains(it) && list.indexOf(it) < idx } == true
            }.isEmpty()
        }.sumOf { it[it.size / 2] }

    fun part2(rules: Map<Int, List<Int>>, updates: List<List<Int>>): Int =
        updates.filter { list ->
            // find all incorrectly sorted updates
            list.filterIndexed { idx, elem ->
                rules[elem]?.any { list.contains(it) && list.indexOf(it) < idx } == true
            }.isNotEmpty()
        }.map { update ->
            // sort the update by iterating over all page indices and placing them into the correct position
            // selection sort
            update.foldIndexed(update) { checkIdx, orderedUpdate, page ->
                val incorrectPageIdx = orderedUpdate
                    .take(checkIdx)
                    .indexOfFirst { rules[page]?.contains(it) ?: false }
                if (incorrectPageIdx == -1) {
                    orderedUpdate
                } else {
                    orderedUpdate.flatMapIndexed { idx, oldPage ->
                        when (idx) {
                            checkIdx -> listOf()
                            incorrectPageIdx -> listOf(page, oldPage)
                            else -> listOf(oldPage)
                        }
                    }
                }
            }
        }.sumOf { it[it.size / 2] }

    // Process input string
    val input = readInput("Day05")

    val rules = input.takeWhile { it.isNotEmpty() }
        .map { it.split("|").map { s -> s.toInt() } }
        .map { it[0] to it[1] }
        .groupBy({ it.first }, { it.second })

    val updates = input.dropWhile { it.isNotEmpty() }
        .filter { it.isNotEmpty() }
        .map { it.split(",").map { s -> s.toInt() } }

    // run task implementations
    println(part1(rules, updates))
    println(part2(rules, updates))
}
