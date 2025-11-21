fun main() {

    data class Gate (val wire1: String, val wire2: String, val op: String) {
        override fun equals(other: Any?): Boolean {
            return other is Gate &&
                    (wire1 == other.wire1 && wire2 == other.wire2 || wire1 == other.wire2 && wire2 == other.wire1) &&
                    op == other.op
        }

        override fun hashCode(): Int {
            var result = wire1.hashCode()
            result = 31 * result + wire2.hashCode()
            result = 31 * result + op.hashCode()
            return result
        }
    }

    fun List<String>.part1(): Any =
        run {
            val wires = takeWhile { it.isNotEmpty() }.associate {
                val wire = it.split(":").first().trim()
                val assignment = it.split(":").last().trim().toInt() == 1
                wire to assignment
            }.toMutableMap()

            val gates = dropWhile { it.isNotEmpty() }.drop(1).associate {
                val wire = it.split("->").last().trim()
                val gate = it.split("->").first().trim()
                val op = listOf("AND", "XOR", "OR").first { it in gate }
                wire to Gate (gate.split(op).first().trim(), gate.split(op).last().trim(), op)
            }

            generateSequence (gates) {
                val next = gates.filter { it.value.wire1 !in wires || it.value.wire2 !in wires }
                gates.filter { it.value.wire1 in wires && it.value.wire2 in wires }
                    .forEach {
                        val wire = it.key
                        val wire1 = wires[it.value.wire1]!!
                        val wire2 = wires[it.value.wire2]!!
                        val assignment = if (it.value.op == "AND")
                            wire1 && wire2
                        else if (it.value.op == "OR")
                            wire1 || wire2
                        else
                            wire1.xor(wire2)
                        wires[wire] = assignment
                    }
                next.ifEmpty { null }
            }.last()
            wires
        }.filterKeys { it.startsWith("z") }
            .map { if (it.value) 1L.shl(it.key.drop(1).toInt()) else 0 }
            .sum()


    fun List<String>.part2(): Any =
        run {
            val gates = dropWhile { it.isNotEmpty() }.drop(1).associate {
                val wire = it.split("->").last().trim()
                val gate = it.split("->").first().trim()
                val op = listOf("AND", "XOR", "OR").first { it in gate }
                wire to Gate (gate.split(op).first().trim(), gate.split(op).last().trim(), op)
            }.toMutableMap()

            // maps from proper name to random name
            val nameMapping = mutableMapOf<String, String>()

            val firstBitHalfAdder = mapOf(
                "z00" to Gate("x00","y00","XOR"),
                "c00" to Gate("x00","y00","AND"),
            )
            nameMapping["z00"] = gates.toList().first { it.second == firstBitHalfAdder["z00"] }.first
            nameMapping["c00"] = gates.toList().first { it.second == firstBitHalfAdder["c00"] }.first
            val correctGates = (1..44).fold(firstBitHalfAdder) { acc, i ->
                val carryIn = nameMapping["c" + (i-1).toString().padStart(2,'0')]!!
                val c = "c" + i.toString().padStart(2,'0')
                val x = "x" + i.toString().padStart(2,'0')
                val y = "y" + i.toString().padStart(2,'0')
                val z = "z" + i.toString().padStart(2,'0')

                val tmpCarryOut = "tmp-$c" to Gate (x, y, "AND")
                nameMapping[tmpCarryOut.first] = gates.toList().firstOrNull { it.second == tmpCarryOut.second }?.first ?: run {
                    gates[tmpCarryOut.first] = tmpCarryOut.second
                    tmpCarryOut.first
                }

                val tmpZ = "tmp-$z" to Gate (x, y, "XOR")
                nameMapping[tmpZ.first] = gates.toList().firstOrNull { it.second == tmpZ.second }?.first ?: run {
                    gates[tmpZ.first] = tmpZ.second
                    tmpZ.first
                }

                val tmpCarryOut2 = "tmp-$c-2" to Gate (nameMapping[tmpZ.first]!!, carryIn, "AND")
                nameMapping[tmpCarryOut2.first] = gates.toList().firstOrNull { it.second == tmpCarryOut2.second }?.first ?: run {
                    gates[tmpCarryOut2.first] = tmpCarryOut2.second
                    tmpCarryOut2.first
                }

                val carryOut = c to Gate(nameMapping[tmpCarryOut.first]!!, nameMapping[tmpCarryOut2.first]!!, "OR")
                nameMapping[carryOut.first] = gates.toList().firstOrNull { it.second == carryOut.second }?.first ?: run {
                    gates[carryOut.first] = carryOut.second
                    carryOut.first
                }

                val zOut = z to Gate(nameMapping[tmpZ.first]!!, carryIn, "XOR")
                nameMapping[zOut.first] = gates.toList().firstOrNull { it.second == zOut.second }?.first ?: run {
                    gates[zOut.first] = zOut.second
                    zOut.first
                }

                acc.plus(listOf(tmpCarryOut, tmpCarryOut2, tmpZ, carryOut, zOut))
            }

            /*
              gvw <-> qjb
              jgc <-> z15
              drg <-> z22
              jbp <-> z35
            */
            nameMapping
        }


    val input = readInput("Day24")
    println(input.part1())
    println(input.part2())
}