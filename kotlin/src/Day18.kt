import kotlin.math.abs

fun main() {

    fun List<List<Boolean>>.bfs(start: Pair<Int,Int>, goal: Pair<Int,Int>): Int =
        generateSequence (listOf(0 to start) to setOf(start)) { l ->
            val open = l.first
            if (open.isEmpty()) {
                null
            } else {
                val visited = l.second
                val n = open.first().second
                val depth = open.first().first

                if (n == goal) {
                    null
                } else {
                    open.drop(1)
                        .plus((-1..1).filter { dx -> n.first + dx in this.indices }
                            .flatMap { dx ->
                                (-1..1).filter { dy -> n.second + dy in this[0].indices }
                                    .filter { dy -> abs(dy) + abs(dx) == 1 }
                                    .map { dy -> n.first + dx to n.second + dy }
                                    .filter { n != it && !open.map { it.second }.contains(it) }
                                    .filter { !this[it.first][it.second] && !visited.contains(it.first to it.second) }
                            }.map { depth + 1 to it }) to visited.plus(n)
                }
            }
        }.toList()
            .last()
            .first
            .ifEmpty { listOf(-1 to -1) }
            .first()
            .first

    fun List<String>.part1(): Any =
        map { line -> line.split(",", limit = 2).map { it.toInt() }.zipWithNext().first() }
            .take(1024)
            .run {
                val start = 0 to 0
                val goal = 70 to 70
                (start.first..goal.first).map { x ->
                    (start.second..goal.second).map { y ->
                        y to x in this
                    }
                }.bfs (start, goal)
            }

    fun List<String>.part2(): Any =
        map { line -> line.split(",", limit = 2).map { it.toInt() }.zipWithNext().first() }
            .run {
                val idx = generateSequence(0 to this.size) { bounds ->
                    if (bounds.first == bounds.second)
                        null
                    else {
                        val fallenBytes = (bounds.first + bounds.second) / 2
                        val result = this.take(fallenBytes)
                            .run {
                                val start = 0 to 0
                                val goal = 70 to 70
                                (start.first..goal.first).map { x ->
                                    (start.second..goal.second).map { y ->
                                        y to x in this
                                    }
                                }.bfs(start, goal)
                            }
                        if (result == -1) {
                            bounds.first to fallenBytes
                        } else {
                            fallenBytes + 1 to bounds.second
                        }
                    }
                }.toList().last().first - 1
                this[idx]
            }.run {
                "${first},${second}"
            }


    val input = readInput("Day18")
        .filter(String::isNotBlank)
    println(input.part1())
    println(input.part2())
}