data class Position(val row: Int, val col: Int) {
    // create a new position offset by the given diff
    fun update(diff: Position): Position =
        Position(this.row + diff.row, this.col + diff.col)

    operator fun unaryMinus(): Position =
        Position(-this.row, -this.col)
}

class Guard(
    private val path: List<Pair<Position, Position>>,
    private val pos: Position,
    private val dir: Position,
) {
    companion object {
        // creates a new guard at the given start position moving into the start direction
        fun create(pos: Position, dir: Position): Guard =
            Guard(listOf(), pos, dir)
    }

    // advances time by one unit (the guard does one action, i.e. move or turn)
    private fun advanceTime(newPos: Position, newDir: Position): Guard =
        Guard(path.plus(pos to dir), newPos, newDir)

    // the guard moves by one spot in the direction she is looking
    fun move(): Guard =
        advanceTime(looksAt(), dir)

    // performance improvement for searching looping paths after placing an additional obstacle
    fun moveUntilObstacle(map: List<String>): Guard =
        advanceTime(
            generateSequence(pos) {
                if (it.row in map.indices && it.col in map.indices && map[it.row][it.col] != '#') it.update(dir) else null
            }.last().update(-dir),
            dir
        )

    // the guard turns 90° to the right
    fun turn(): Guard =
        advanceTime(pos, Position(dir.col, -dir.row))

    // the guard looks what is in front of it
    // if she is at the edge of the map, she just sees nothing -> ' '
    fun look(map: List<String>): Char =
        if (looksAt().row in map.indices && looksAt().col in map[0].indices) map[looksAt().row][looksAt().col] else ' '

    // the row/col position on the map the guard looks at
    fun looksAt(): Position =
        pos.update(dir)

    // the set of positions the guard visited on her patrol
    fun visited(): Set<Position> =
        path.map { it.first }.plus(pos).toSet()

    // to check whether the guard visited one specific tile
    fun visited(pos: Position): Boolean =
        path.map { it.first }.contains(pos)

    // check if the guard already moved over the current spot in the same direction
    fun wasHere(): Boolean =
        path.any { it.first == pos && it.second == dir }

    // the patrol map the guard has drawn so far
    fun pathMap(map: List<String>, directions: Boolean): List<String> =
        map.mapIndexed { row, str ->
            str.mapIndexed { col, c ->
                path.plus(pos to dir).filter { it.first.row == row && it.first.col == col }
                    .map { if (!directions) 'X' else if (it.second.row != 0) '|' else '-' }
                    .reduceOrNull { a, b -> if (a == b) a else '+' } ?: c
            }.joinToString("")
        }
}

// add an obstacle to the map manually
fun List<String>.addObstacle(pos: Position, obstacle: Char): List<String> =
    mapIndexed { row, str -> if (row != pos.row) str else str.mapIndexed { col, c -> if (col != pos.col) c else obstacle }.joinToString("") }

fun main() {

    // find the number of visited fields
    fun List<String>.part1(): Int =
        // the guard keeps walking until it reaches the end of the map
        generateSequence(Guard.create(Position(indexOfFirst { it.contains('^') }, first { it.contains('^') }.indexOf('^')), Position(-1, 0))) { guard ->
            // the guard decides what to do based on what she looks at
            when (guard.look(this)) {
                // if the guard is at the end of the map, the patrol is finished
                ' ' -> null
                // if the guard sees an obstacle she turns right
                '#' -> guard.turn()
                // otherwise the guard follows its direction
                else -> guard.move()
            }
        }.last()
//            .also { it.pathMap(this, directions = false).forEach(::println) }
            .visited()
            .size


    fun List<String>.part2(): Int =
        // first let the guard walk its path, same as part1
        generateSequence(Guard.create(Position(indexOfFirst { it.contains('^') }, first { it.contains('^') }.indexOf('^')), Position(-1, 0))) { guard ->
            when (guard.look(this)) {
                ' ' -> null
                '#' -> guard.turn()
                else -> guard.move()
            }
        }
            // we are only interested in positions where we can place a new obstacle
            .filter { guard -> guard.look(this) != '#' && guard.look(this) != ' ' }
            .filter { guard -> !guard.visited(guard.looksAt()) }
            .filter { guard ->
                // place obstacle and predict the guards path
                with(addObstacle(guard.looksAt(), '#')) {
                    // the guards movement is predicted based on the changed map, rules are the same as before
                    generateSequence(guard) { futureGuard ->
                        when (futureGuard.look(this)) {
                            ' ' -> null
                            '#' -> futureGuard.turn()
                            else -> futureGuard.moveUntilObstacle(this)
                        }
                    }.any { futureGuard ->
                        // if the guard would revisit a position in the same direction, then the guard is in a loop
                        futureGuard.wasHere()
                    }
                }
            }
//            .onEach { it.pathMap(this, directions = true).addObstacle(it.looksAt(), 'O').forEach(::println) }
            .groupBy { guard -> guard.looksAt() }
            .size

    // Process input string
    val input = readInput("Day06")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
