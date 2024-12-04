fun main() {

    fun String.part1(): Int =
        """mul\((?<X>\d+),(?<Y>\d+)\)""".toRegex()
            .findAll(this)
            .sumOf { (it.groups[1]?.value?.toInt() ?: 0) * (it.groups[2]?.value?.toInt() ?: 0) }

    fun String.part2(): Int {
        val dos = """do\(\)""".toRegex().findAll(this).map { it.range.first }
        val donts = """don't\(\)""".toRegex().findAll(this).map { it.range.first }

        return """mul\((?<X>\d+),(?<Y>\d+)\)""".toRegex()
            .findAll(this)
            .filter { match ->
                val lastDo = dos.filter { it < match.range.first }.lastOrNull()
                val lastDont = donts.filter { it < match.range.first }.lastOrNull()
                lastDont == null || (lastDo != null && lastDo > lastDont)
            }
            .sumOf { (it.groups[1]?.value?.toInt() ?: 0) * (it.groups[2]?.value?.toInt() ?: 0) }
    }

    // Process input string
    val input = readInput("Day03")
        .filter { it.isNotEmpty() }
        .joinToString()

    // run task implementations
    println(input.part1())
    println(input.part2())
}
