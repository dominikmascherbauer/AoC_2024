fun main() {
    // magic numbers
    val xTiles = 101
    val xAnamoly = 97
    val yTiles = 103
    val yAnamoly = 50
    val seconds = 100
    val verbose = false


    data class Point(val x: Int, val y: Int) {
        operator fun plus(move: Point): Point =
            Point((x + move.x + xTiles) % xTiles, (y + move.y + yTiles) % yTiles)

        fun quadrant(): Int =
            if (x < xTiles/2 && y < yTiles/2) {
                1
            } else if (x > xTiles/2 && y < yTiles/2) {
                2
            } else if (x < xTiles/2 && y > yTiles/2) {
                3
            } else if (x > xTiles/2 && y > yTiles/2) {
                4
            } else {
                0
            }
    }

    data class Robot(val pos: Point, val move: Point)

    fun String.parseRobot(): Robot? =
        """p=(?<x>\d+),(?<y>\d+)\s+v=(?<dx>-?\d+),(?<dy>-?\d+)""".toRegex()
            .find(this)
            ?.groupValues
            ?.drop(1)
            ?.map { it.toInt() }
            ?.chunked(2)
            ?.map { pos -> Point(pos[0], pos[1])  }
            ?.zipWithNext { pos, move -> Robot(pos, move) }
            ?.firstOrNull()

    fun Robot.findPath(): List<Point> =
        generateSequence(0 to this.pos) { (idx, pos) -> idx+1 to pos + this.move }
            .takeWhile { (idx, pos) -> idx == 0 || pos != this.pos }
            .toList()
            .map { it.second }

    fun List<String>.part1(): Long =
        mapNotNull(String::parseRobot)
            .map(Robot::findPath)
            .map { it[seconds % it.size] }
            .groupBy { it.quadrant() }
            .filterKeys { it != 0  }
            .values
            .fold(1L) { acc, list -> acc * list.size }

    fun List<String>.part2(): Any =
        mapNotNull(String::parseRobot)
            .map(Robot::findPath)
            .let { positions ->
                // 50 and 97 are special -> 50 in y direction, 97 in x direction
                // 153/256/... and 198/299/..  -> multiples of width and height added to special configurations
                // 7672 % 103 = 50, 7672 % 101 = 97
                // could be solved with the chinese remainder theorem, but as it is only 2 numbers, this should be enough
                generateSequence(yAnamoly to xAnamoly) { (y, x) -> if (y < x) y + yTiles to x else y to x + xTiles }
                    .first { it.first == it.second }
                    .first
                    .also { seconds ->
                        if (verbose) {
                            // print the positions of the robots
                            println("After $seconds seconds:")
                            (0..<xTiles).forEach { x ->
                                (0..<yTiles).forEach { y ->
                                    print(positions.count { it[seconds % it.size] == Point(x, y) }.toString().replace('0', '.'))
                                }
                                println()
                            }
                        }
                    }
            }


    // Process input string
    val input = readInput("Day14")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
