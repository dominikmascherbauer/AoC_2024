import kotlin.math.abs

fun main() {

    fun List<String>.asColumnListPair(): Pair<List<Int>, List<Int>> =
        fold(mutableListOf<Int>() to mutableListOf<Int>()) { acc, s ->
            acc.first.add(s.split(' ').first().toInt())
            acc.second.add(s.split(' ').last().toInt())
            acc
        }

    fun Pair<List<Int>, List<Int>>.part1(): Int =
        first.sorted()
            .zip(second.sorted()) { a, b -> abs(a - b) }
            .sum()

    fun Pair<List<Int>, List<Int>>.part2(): Int =
        with(second.groupingBy { it }.eachCount()) {
            first.sumOf { it * this.getOrElse(it) { 0 } }
        }

    val input = readInput("Day01").filterNot { it.isEmpty() }.asColumnListPair()
    println(input.part1())
    println(input.part2())
}
