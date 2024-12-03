fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: String): List<MulInstruction> {
    val regex = "mul\\(\\d+,\\d+\\)".toRegex()

    return regex.findAll(testInput).toList().map { matchResult ->
        matchResult.value.removePrefix("mul(").removeSuffix(")").split(",").map { it.toInt() }
            .let { MulInstruction(it[0], it[1]) }
    }
}

private fun part1TestInput() {
    val testInput = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

    val instructions = parseInput(testInput)
    val result = instructions.sumOf { mulInstruction ->
        mulInstruction.firstFactor * mulInstruction.secondFactor
    }

    println("Part1 Test: $result")
    check(result == 161)
}

private fun part1() {
    val input = readInput("Day03")

    val instructions = parseInput(input)
    val result = instructions.sumOf { mulInstruction ->
        mulInstruction.firstFactor * mulInstruction.secondFactor
    }

    println("Part1: $result")
}

private fun parseInput2(testInput: String): List<MulInstruction> {
    val regex = "mul\\(\\d+,\\d+\\)".toRegex()

    val enablingRegex = "(do|don\'t)\\(\\)".toRegex()
    val enableInstructions = enablingRegex.findAll(testInput).toMutableList()
    var enableMul = true

    return regex.findAll(testInput).toList().mapNotNull { matchResult ->
        if (enableInstructions.isNotEmpty() && matchResult.range.first > enableInstructions.first().range.last) {
            val nextCommand = enableInstructions.removeFirst()
            enableMul = nextCommand.value == "do()"
        }
        matchResult.value.removePrefix("mul(").removeSuffix(")").split(",").map { it.toInt() }
            .let {
                if (enableMul) MulInstruction(it[0], it[1]) else null
            }
    }
}

private fun part2TestInput() {
    val testInput = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    val instructions = parseInput2(testInput)
    val result = instructions.sumOf { mulInstruction ->
        mulInstruction.firstFactor * mulInstruction.secondFactor
    }

    println("Part2 Test: $result")
    check(result == 48)
}

private fun part2() {
    val input = readInput("Day03")

    val instructions = parseInput2(input)
    val result = instructions.sumOf { mulInstruction ->
        mulInstruction.firstFactor * mulInstruction.secondFactor
    }

    println("Part2: $result")
}

private data class MulInstruction(
    val firstFactor: Int,
    val secondFactor: Int,
)
