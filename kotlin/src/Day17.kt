fun main() {

    data class Opcode(val opcode: Int, val name: String, val op: Function2<Int, Int, Int>)

    data class Register(val name: Char, var value: Long)

    data class Instruction(val opcode: Opcode, val operand: Int)

    val registers: List<Register> = listOf(
        Register(name = 'A', value = 0),
        Register(name = 'B', value = 0),
        Register(name = 'C', value = 0),
    )

    fun parseOperand(operand: Int, isCombo: Boolean): Long =
        if (isCombo) {
            when (operand) {
                in 0..3 -> operand.toLong()
                4 -> registers[0].value
                5 -> registers[1].value
                6 -> registers[2].value
                else -> throw IllegalArgumentException("Combo operand invalid.")
            }
        } else {
            operand.toLong()
        }

    var output = ""

    val opcodes: List<Opcode> = listOf(
        Opcode(opcode = 0, name = "adv") { pc, operand ->
            registers[0].value = registers[0].value.shr(parseOperand(operand, true).toInt())
            pc + 2
        },
        Opcode(opcode = 1, name = "bxl") { pc, operand ->
            registers[1].value = registers[1].value.xor(parseOperand(operand, false))
            pc + 2
        },
        Opcode(opcode = 2, name = "bst") { pc, operand ->
            registers[1].value = parseOperand(operand, true).and(0x7)
            pc + 2
        },
        Opcode(opcode = 3, name = "jnz") { pc, operand ->
            if (registers[0].value != 0L) {
                parseOperand(operand, false).toInt()
            } else {
                pc + 2
            }
        },
        Opcode(opcode = 4, name = "bxc") { pc, _ ->
            registers[1].value = registers[1].value.xor(registers[2].value)
            pc + 2
        },
        Opcode(opcode = 5, name = "out") { pc, operand ->
            output += "${parseOperand(operand, true).and(0x7)},"
            pc + 2
        },
        Opcode(opcode = 6, name = "bdv") { pc, operand ->
            registers[1].value = registers[0].value.shr(parseOperand(operand, true).toInt())
            pc + 2
        },
        Opcode(opcode = 7, name = "cdv") { pc, operand ->
            registers[2].value = registers[0].value.shr(parseOperand(operand, true).toInt())
            pc + 2
        },
    )


    fun List<String>.part1(): Any =
        run {
            registers[0].value = this.first().substringAfter(':').trim().toLong()
            registers[1].value = this.drop(1).first().substringAfter(':').trim().toLong()
            registers[2].value = this.drop(2).first().substringAfter(':').trim().toLong()

            val instructions: List<Instruction> = this.drop(3).first().substringAfter(':').trim().split(',').map { it.toInt() }.chunked(2).map { Instruction(opcodes[it[0]], it[1]) }

            generateSequence(0) { pc ->
                if (pc/2 in instructions.indices) {
                    instructions[pc/2].opcode.op(pc, instructions[pc/2].operand)
                } else {
                    null
                }
            }.toList()
            output.dropLast(1)
        }

    fun List<String>.part2(): Any =
        run {
            val programString = this.drop(3).first().substringAfter(':').trim()
            val programCode = programString.split(',').map { it.toInt() }
            val instructions: List<Instruction> = programCode.chunked(2).map { Instruction(opcodes[it[0]], it[1]) }

            val aVals = generateSequence(listOf(0L to "")) { l ->
                val nextL = l.flatMap { aVal ->
                    (0..7).filter {
                        registers[0].value = aVal.first.shl(3) + it
                        registers[1].value = 0
                        registers[2].value = 0
                        output = ""
                        generateSequence(0) { pc ->
                            if (pc / 2 in instructions.indices) {
                                instructions[pc / 2].opcode.op(pc, instructions[pc / 2].operand)
                            } else {
                                null
                            }
                        }.toList()
                        output = output.dropLast(1)
                        programString.reversed().startsWith(output.reversed())
                    }.map { aVal.first.shl(3) + it to output }
                }
                nextL.ifEmpty { null }
            }.toList()
            return aVals.last().first { it.second == programString }.first
        }


    // Process input string
    val input = readInput("Day17")
        .filter { it.isNotEmpty() }

    // run task implementations
    println(input.part1())
    println(input.part2())
}
