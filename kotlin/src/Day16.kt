import kotlin.math.abs

private enum class Direction(val move: Pair<Int, Int>) {
    EAST(0 to 1), WEST(0 to -1), NORTH(-1 to 0), SOUTH(1 to 0);

    fun x(): Int = this.move.second
    fun y(): Int = this.move.first

    fun left(): Direction =
        when (this) {
            EAST -> NORTH
            WEST -> SOUTH
            NORTH -> WEST
            SOUTH -> EAST
        }

    fun right(): Direction =
        when (this) {
            EAST -> SOUTH
            WEST -> NORTH
            NORTH -> EAST
            SOUTH -> WEST
        }
}

fun main() {

    data class Position(val x: Int, val y: Int)

    data class Node(val pos: Position, val from: Node?, val dir: Direction, val dist: Long)

    fun List<String>.getNodes(start: Position, startDir: Direction): List<Node> =
        generateSequence(listOf<Node>() to listOf(Node(start, null, startDir, 0L))) { (visited, acc) ->
            visited.plus(acc).groupBy { it.pos }.map { it.value.minBy { it.dist } } to acc.flatMap { node ->
                sequenceOf(node.dir, node.dir.left(), node.dir.right()).filter { dir ->
                    this[node.pos.y + dir.y()][node.pos.x + dir.x()] != '#'
                }.map { dir ->
                    dir to generateSequence(Position(node.pos.x + dir.x(), node.pos.y + dir.y())) { pos ->
                        if (this[pos.y + dir.y()][pos.x + dir.x()] == '#' || this[pos.y + dir.left().y()][pos.x + dir.left().x()] != '#' || this[pos.y + dir.right().y()][pos.x + dir.right()
                                .x()] != '#'
                        ) {
                            null
                        } else {
                            Position(pos.x + dir.x(), pos.y + dir.y())
                        }
                    }.toList()
                }.map { (dir, path) ->
                    Node(path.last(), node, dir, node.dist + (if (dir == node.dir) path.size.toLong() else path.size + 1000L))
                }.filter { n ->
                    // every pos is in the visited set only once
                    // check with -1000 because if this is a node where the shortest path was seen before a turn on to the following shortest path
                    // we might not need to turn if we come from another path and if it is exactly 1000 longer it still is the shortest path as it does not but continues straight
                    n.dist-1000 <= (visited.firstOrNull { it.pos == n.pos }?.dist ?: Long.MAX_VALUE)
                }
            }
        }.takeWhile { it.second.isNotEmpty() }.toList().flatMap { it.second }

    fun List<String>.part1(): Any =
        getNodes(Position(1, this.size - 2), Direction.EAST)
            .filter { it.pos == Position(this[0].length - 2, 1) }
            .minOf { it.dist }

    fun List<String>.part2(): Any =
        getNodes(Position(1, this.size - 2), Direction.EAST)
            .filter { it.pos == Position(this[0].length - 2, 1) }
            .groupBy { it.dist }
            .minBy { it.key }
            .value
            .flatMap { endNode ->
                generateSequence(endNode) { node ->
                    node.from
                }.zipWithNext { a, b ->
                    List(abs(a.pos.x - b.pos.x + a.pos.y - b.pos.y) + 1) { i ->
                        Position(a.pos.x - i * a.dir.x(), a.pos.y - i * a.dir.y())
                    }
                }.toList().flatten()
            }.toSet().size


    // Process input string
    val input = readInput("Day16")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
