fun main() {

    fun String.part1(): Int =
        // filter out all correct mul operations and sum up the multiplied values
        """mul\((?<X>\d+),(?<Y>\d+)\)""".toRegex()
            .findAll(this)
            .sumOf { (it.groups[1]?.value?.toInt() ?: 0) * (it.groups[2]?.value?.toInt() ?: 0) }

    fun String.part2(): Int =
        // filter out all correct mul operations that are enabled (regions before dos after donts are cut out)
        """mul\((?<X>\d+),(?<Y>\d+)\)""".toRegex()
            .findAll(
                split("do()")
                    .joinToString("") { it.split("don't()").first() }
            ).sumOf { (it.groups[1]?.value?.toInt() ?: 0) * (it.groups[2]?.value?.toInt() ?: 0) }

    // Process input string
    val input = readInput("Day03")
        .joinToString()

    // run task implementations
    println(input.part1())
    println(input.part2())
}
