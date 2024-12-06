fun main() {

    fun part1(rules: Map<Int, List<Int>>, updates: List<List<Int>>): Int =
        updates.filter { list ->
            list.filterIndexed { idx, elem ->
                rules[elem]?.any { list.contains(it) && list.indexOf(it) < idx } == true
            }.isEmpty()
        }.sumOf { it[it.size / 2] }

    fun part2(rules: Map<Int, List<Int>>, updates: List<List<Int>>): Int =
        updates.filter { list ->
            list.filterIndexed { idx, elem ->
                rules[elem]?.any { list.contains(it) && list.indexOf(it) < idx } == true
            }.isNotEmpty()
        }.map { list ->
            val mList = list.toMutableList()
            while (mList.filterIndexed { idx, elem -> rules[elem]?.any { mList.contains(it) && mList.indexOf(it) < idx } == true }.isNotEmpty()) {
                val wrongElem = mList.mapIndexed { idx, elem -> elem to rules[elem]?.firstOrNull { mList.contains(it) && mList.indexOf(it) < idx } }.first { it.second != null }
                val idx = mList.indexOf(wrongElem.first)
                val idx2 = mList.indexOf(wrongElem.second)
                mList.add(idx2, mList.removeAt(idx))
            }
            mList
        }.sumOf { it[it.size / 2] }

    // Process input string
    val input = readInput("Day05")

    val rules = input.takeWhile { it.isNotEmpty() }.map { it.split("|").map { s -> s.toInt() } }.map { it[0] to it[1] }.groupBy({ it.first }, { it.second })
    val updates = input.dropWhile { it.isNotEmpty() }.filter { it.isNotEmpty() }.map { it.split(",").map { s -> s.toInt() } }

    // run task implementations
    println(part1(rules, updates))
    println(part2(rules, updates))
}
