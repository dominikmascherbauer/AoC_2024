fun main() {

    val cache1 = mutableMapOf<String, Boolean>()
    val cache2 = mutableMapOf<String, Long>()

    fun String.findPattern(combinations: List<String>): Boolean =
        cache1.getOrPut(this) {
            isEmpty() || combinations.filter { this.startsWith(it) }
                .any { this.drop(it.length).findPattern(combinations) }
        }

    fun String.findAllPatterns(combinations: List<String>): Long =
        cache2.getOrPut(this) {
            (if (isEmpty()) 1L else 0L) + combinations.filter { this.startsWith(it) }
                .sumOf { this.drop(it.length).findAllPatterns(combinations) }
        }

    fun List<String>.part1(): Any =
        (takeWhile(String::isNotBlank).flatMap { it.split(",") }.map { it.trim() } to dropWhile(String::isNotBlank).drop(1))
            .run { second.filter { it.findPattern(first) } }
            .size

    fun List<String>.part2(): Any =
        (takeWhile(String::isNotBlank).flatMap { it.split(",") }.map { it.trim() } to dropWhile(String::isNotBlank).drop(1))
            .run { second.sumOf { it.findAllPatterns(first) } }


    val input = readInput("Day19")
    println(input.part1())
    println(input.part2())
}