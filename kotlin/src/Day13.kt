import kotlin.math.floor

fun main() {
    data class Point(val x: Long, val y: Long)

    data class Button(val cost: Long, val move: Point)

    data class ClawMachine(
        val btnA: Button,
        val btnB: Button,
        val prize: Point
    )

    fun parseButton(cost: Long, input: String): Button =
        Button(cost, Point(input.substringAfter("X+").takeWhile { it.isDigit() }.toLong(), input.substringAfter("Y+").takeWhile { it.isDigit() }.toLong()))

    fun parsePrize(input: String, actualPos: Boolean): Point =
        Point(input.substringAfter("X=").takeWhile { it.isDigit() }.toLong() + (if (actualPos) 10000000000000 else 0), input.substringAfter("Y=").takeWhile { it.isDigit() }.toLong() + (if (actualPos) 10000000000000 else 0))

    fun ClawMachine.findButtonCombination(): Pair<List<Pair<Long, Button>>, Point> =
        generateSequence(listOf<Pair<Long, Button>>() to prize) { (sequence, prize) ->
            if (prize.x <= 0 || prize.y <= 0) {
                null
            } else if (prize.x % btnB.move.x == 0L && prize.y % btnB.move.y == 0L && prize.x / btnB.move.x == prize.y / btnB.move.y) {
                sequence.plus((prize.x / btnB.move.x) to btnB) to Point(0L, 0L)
            } else {
                val dSteps =
                    ((prize.x.toDouble() / btnB.move.x) - (prize.y.toDouble() / btnB.move.y)) * ((btnB.move.x * btnB.move.y).toDouble() / (btnA.move.x * btnB.move.y - btnA.move.y * btnB.move.x))
                if (dSteps < 0) {
                    null
                } else {
                    val steps = floor(dSteps).toLong().coerceAtLeast(1L)
                    sequence.plusElement(steps to btnA) to Point(prize.x - steps * btnA.move.x, prize.y - steps * btnA.move.y)
                }
            }
        }.last()

    fun ClawMachine.numberOfTokens(): Long =
        findButtonCombination()
            .takeIf { (_, prize) -> prize.x == 0L && prize.y == 0L }
            ?.first
            ?.sumOf { (times, btn) -> times * btn.cost }
            ?: 0L

    fun List<String>.part1(): Long =
        chunked(3)
            .map { ClawMachine(parseButton(3, it.first()), parseButton(1, it.drop(1).first()), parsePrize(it.drop(2).first(), false)) }
            .sumOf(ClawMachine::numberOfTokens)

    fun List<String>.part2(): Long =
        chunked(3)
            .map { ClawMachine(parseButton(3, it.first()), parseButton(1, it.drop(1).first()), parsePrize(it.drop(2).first(), true)) }
            .sumOf(ClawMachine::numberOfTokens)


    // Process input string
    val input = readInput("Day13")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
