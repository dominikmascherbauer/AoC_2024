fun main() {
    data class Point(val x: Int, val y: Int)

    fun List<String>.findRobot(): Point =
        indexOfFirst { it.contains('@') }
            .let { idx -> Point(this[idx].indexOf('@'), idx) }

    fun List<String>.widen(): List<String> =
        map { line ->
            line.map { c ->
                when (c) {
                    '#' -> "##"
                    'O' -> "[]"
                    '@' -> "@."
                    else -> ".."
                }
            }.joinToString("")
        }

    fun Char.rotateForShift(robot: Point, grid: List<String>): Pair<Point, List<String>> =
        when (this) {
            'v' -> Point(grid.size - robot.y - 1, robot.x) to grid.rotateRight()
            '>' -> Point(grid[0].length - robot.x - 1, robot.y) to grid.map { it.reversed() }
            '^' -> Point(robot.y, grid[0].length - robot.x - 1) to grid.rotateLeft()
            else -> robot to grid
        }

    fun Char.rotateBack(robot: Point, grid: List<String>): Pair<Point, List<String>> =
        when (this) {
            'v' -> Point(robot.y, grid[0].length - robot.x - 1) to grid.rotateLeft()
            '>' -> Point(grid[0].length - robot.x - 1, robot.y) to grid.map { it.reversed() }
            '^' -> Point(grid.size - robot.y - 1, robot.x) to grid.rotateRight()
            else -> robot to grid
        }

    fun Char.getCheckLines(robot: Point, grid: List<String>): List<Pair<Int, String>> =
        when (this) {
            '^' ->
                generateSequence(listOf(robot.x to grid.map { it[robot.x] }.take(robot.y))) { lines ->
                    lines.flatMap { (x, line) ->
                        line.mapIndexed { y, c ->
                            if (y < line.indexOfLast { it == '.' }) {
                                null
                            } else if (c == '[') {
                                x + 1 to grid.map { it[x + 1] }.take(y)
                            } else if (c == ']') {
                                x - 1 to grid.map { it[x - 1] }.take(y)
                            } else {
                                null
                            }
                        }.filterNotNull()
                    }
                }.takeWhile { it.isNotEmpty() }.toList().flatten().map { it.first to it.second.joinToString("") }
            'v' ->
                generateSequence(listOf(robot.x to grid.map { it[robot.x] }.drop(robot.y + 1))) { lines ->
                    lines.flatMap { (x, line) ->
                        line.mapIndexed { y, c ->
                            if (line.contains('.') && y > line.indexOfFirst { it == '.' }) {
                                null
                            } else if (c == '[') {
                                x + 1 to grid.map { it[x + 1] }.drop(grid.size - line.size + y + 1)
                            } else if (c == ']') {
                                x - 1 to grid.map { it[x - 1] }.drop(grid.size - line.size + y + 1)
                            } else {
                                null
                            }
                        }.filterNotNull()
                    }
                }.takeWhile { it.isNotEmpty() }.toList().flatten().map { it.first to it.second.reversed().joinToString("") }
            '>' -> listOf(robot.y to grid[robot.y].drop(robot.x + 1).reversed())
            else -> listOf(robot.y to grid[robot.y].take(robot.x))
        }

    /*
    fun Char.getUpdateGrid(robot: Point, grid: List<String>, checkLines: List<Pair<Int, String>>): List<String> =
        when (this) {
            '^' ->
                checkLines.fold(grid) { updatedGrid, checkLine ->

                }
                grid.mapIndexed { idx, line ->
                    if (checkLines.any { it.first == idx }) {
                        val checkLineIndices = checkLines.filter { it.first == idx }.map { it.second.length }.toSet()
                        checkLineIndices.sorted().fold(line) { newLine, checkLineIndex ->
                            newLine.take(checkLineIndex).substringBeforeLast('.') + newLine.take(checkLineIndex + 1).substringAfterLast('.') + '.' + newLine.substring(checkLineIndex + 1)
                        }
                    } else {
                        line
                    }
                }
        }

     */


    fun String.part1(grid: List<String>): Any =
        fold(grid.findRobot() to grid) { (robot, updatedGrid), move ->
            var (newRobot, newGrid) = move.rotateForShift(robot, updatedGrid)
            if (newGrid[newRobot.y].take(newRobot.x).substringAfterLast('#').any { it == '.' }) {
                newGrid = newGrid.mapIndexed { idx, line ->
                    if (idx == newRobot.y) {
                        line.take(newRobot.x).substringBeforeLast('.') + line.take(newRobot.x).substringAfterLast('.') + "@." + line.substring(newRobot.x + 1)
                    } else {
                        line
                    }
                }
                newRobot = Point(newRobot.x - 1, newRobot.y)
                move.rotateBack(newRobot, newGrid)
            } else {
                robot to updatedGrid
            }
        }.second
            .flatMapIndexed { y, line -> line.mapIndexed { x, c -> if (c == 'O') Point(x, y) else null } }
            .filterNotNull()
            .sumOf { 100 * it.y + it.x }

    fun String.part2(grid: List<String>): Any =
        fold(grid.widen().findRobot() to grid.widen()) { (robot, updatedGrid), move ->
            var (newRobot, newGrid) = move.rotateForShift(robot, updatedGrid)
            val checkLines = if (move == 'v' || move == '^') {
                generateSequence(listOf(newRobot.y to newGrid[newRobot.y].take(newRobot.x))) { lines ->
                    lines.flatMap { (y, line) ->
                        line.mapIndexed { x, c ->
                            if (x < line.indexOfLast { it == '.' }) {
                                null
                            } else if ((c == '[' && move == 'v') || (c == ']' && move == '^')) {
                                y + 1 to newGrid[y + 1].take(x)
                            } else if ((c == ']' && move == 'v') || (c == '[' && move == '^')) {
                                y - 1 to newGrid[y - 1].take(x)
                            } else {
                                null
                            }
                        }.filterNotNull()
                    }
                }.takeWhile { it.isNotEmpty() }.toList().flatten()
            } else {
                listOf(newRobot.y to (newGrid[newRobot.y].take(newRobot.x)))
            }

            move.getCheckLines(robot, updatedGrid)

            if (checkLines.all { checkLine -> checkLine.second.substringAfterLast('#').any { it == '.' } }) {
                newGrid = newGrid.mapIndexed { idx, line ->
                    if (checkLines.any { it.first == idx }) {
                        val checkLineIndices = checkLines.filter { it.first == idx }.map { it.second.length }.toSet()
                        checkLineIndices.sorted().fold(line) { newLine, checkLineIndex ->
                            newLine.take(checkLineIndex).substringBeforeLast('.') + newLine.take(checkLineIndex + 1).substringAfterLast('.') + '.' + newLine.substring(checkLineIndex + 1)
                        }
                    } else {
                        line
                    }
                }
                newRobot = Point(newRobot.x - 1, newRobot.y)
                move.rotateBack(newRobot, newGrid)
            } else {
                robot to updatedGrid
            }
        }.second
            .flatMapIndexed { y, line -> line.mapIndexed { x, c -> if (c == '[') Point(x, y) else null } }
            .filterNotNull()
            .sumOf { 100 * it.y + it.x }


    // Process input string
    val input = readInput("Day15")

    val grid = input.takeWhile { it.isNotEmpty() }
    val moves = input.dropWhile { it.isNotEmpty() }
        .drop(1)
        .joinToString("")

    // run task implementations
    println(moves.part1(grid))
    println(moves.part2(grid))
}
