fun main() {

    fun nextSecret(number: Long): Long {
        val n1 = number.xor(number.shl(6)) % 16777216
        val n2 = n1.xor(n1.shr(5)) % 16777216
        val n3 = n2.xor(n2.shl(11)) % 16777216
        return if (n3 < 0)
            n3 + 16777216
        else
            n3
    }

    fun List<String>.part1(): Long =
        map(String::toLong)
            .sumOf { number ->
                generateSequence(number) {nextSecret(it) }.take(2001).last()
            }

    fun List<String>.part2(): Long =
        map(String::toLong)
            .run {
                val combinations = (-9L..9).flatMap { a ->
                    (-9L..9).flatMap { b ->
                        (-9L..9).flatMap { c ->
                            (-9L..9).map { d ->
                                listOf(a,b,c,d)
                            }
                        }
                    }
                }

                val secretNumbers = this.map { number ->
                    generateSequence(listOf<Long>() to number) {
                        val n = it.second
                        val n1 = nextSecret(n)
                        val n2 = nextSecret(n1)
                        val n3 = nextSecret(n2)
                        val n4 = nextSecret(n3)
                        listOf(n1%10 - n%10, n2%10 - n1%10, n3%10 - n2%10, n4%10 - n3%10) to n1
                    }.take(1998).toList()
                        .groupBy { it.first }
                        .filter { it.key.size == 4 }
                        .mapValues { it.value.first().second % 10 + it.key[1] + it.key[2] + it.key[3] }
                        .toMap()
                }

                combinations.maxOf { combination ->
                    secretNumbers.sumOf { secretNumber ->
                        secretNumber[combination] ?: 0
                    }
                }
            }


    val input = readInput("Day22")
        .filter(String::isNotEmpty)
    println(input.part1())
    println(input.part2())
}