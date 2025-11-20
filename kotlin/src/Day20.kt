fun main() {

    fun List<String>.part1(): Any =
        run {
            val x = indexOfFirst { 'S' in it }
            val y = this[x].indexOfFirst { it == 'S' }

            val visited = mutableSetOf<Pair<Int,Int>>()
            val path = generateSequence((x to y) to 0) { node ->
                visited.add(node.first)
                val time = node.second
                val x = node.first.first
                val y = node.first.second

                if (this[x][y] == 'E')
                    null
                else {
                    val dir = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1).first { dir ->
                        val newX = x + dir.first
                        val newY = y + dir.second

                        newX to newY !in visited && this[newX][newY] in listOf('.', 'E')
                    }

                    val newX = x + dir.first
                    val newY = y + dir.second
                    (newX to newY) to time + 1
                }
            }.toMap()

            visited.map {
                it to generateSequence(0 to listOf(it)) { nodes ->
                    if (nodes.first == 2)
                        null
                    else {
                        nodes.first + 1 to nodes.second.flatMap { node ->
                            listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1).map { dir ->
                                node.first + dir.first to node.second + dir.second
                            }
                        }
                    }
                }.last()
                    .second
                    .filter { it.first in this.indices && it.second in this[0].indices  }
                    .filter { this[it.first][it.second] != '#' }
                    .toSet()
            }.flatMap { it.second.map { node ->  path.getOrDefault(node, 0) - path.getOrDefault(it.first, 0) } }
                .map { it - 2 }  // 2 seconds of cheat used
                .filter { it >= 100}
                .size
        }

    fun List<String>.part2(): Any =
        run {
            val x = indexOfFirst { 'S' in it }
            val y = this[x].indexOfFirst { it == 'S' }

            val visited = mutableSetOf<Pair<Int,Int>>()
            val path = generateSequence((x to y) to 0) { node ->
                visited.add(node.first)
                val time = node.second
                val x = node.first.first
                val y = node.first.second

                if (this[x][y] == 'E')
                    null
                else {
                    val dir = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1).first { dir ->
                        val newX = x + dir.first
                        val newY = y + dir.second

                        newX to newY !in visited && this[newX][newY] in listOf('.', 'E')
                    }

                    val newX = x + dir.first
                    val newY = y + dir.second
                    (newX to newY) to time + 1
                }
            }.toMap()

            visited.map {
                it to generateSequence(0 to mapOf(it to 0)) { nodes ->
                    if (nodes.first == 20)
                        null
                    else {
                        nodes.first + 1 to nodes.second.plus(nodes.second.flatMap { node ->
                            val time = node.value
                            val node = node.key
                            listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1).map { dir ->
                                (node.first + dir.first to node.second + dir.second) to time + 1
                            }.filter { node -> node.first.first in this.indices && node.first.second in this[0].indices }
                                .filter { node -> node.first !in nodes.second.keys }
                        }.toSet())
                    }
                }.last()
                    .second
                    .filterKeys { this[it.first][it.second] != '#' }
            }.flatMap { it.second.map { node ->  path.getOrDefault(node.key, 0) - path.getOrDefault(it.first, 0) - node.value } }
                .filter { it >= 100 }
                .size
        }

    val input = readInput("Day20")
        .filter(String::isNotBlank)
    println(input.part1())
    println(input.part2())
}