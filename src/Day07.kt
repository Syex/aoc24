fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: List<String>) = testInput.map {
    it.trim().split(":").let {
        val result = it[0].toLong()
        val numbers = it[1].trim().split(" ").map { it.toLong() }
        Equation(result, numbers)
    }
}

private fun part1TestInput() {
    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()

    val equations = parseInput(testInput)
    val sum = equations.sumOf { equation ->
        val possibleWaysToSolve = findPossibleWayToSolve(equation, false)
        if (possibleWaysToSolve.any { check(equation.result, it) }) {
            equation.result
        } else {
            0
        }
    }

    println("Part1 Test: $sum")
}

private fun part1() {
    val input = readInputLines("Day07")

    val equations = parseInput(input)
    val sum = equations.sumOf { equation ->
        val possibleWaysToSolve = findPossibleWayToSolve(equation, false)
        if (possibleWaysToSolve.any { check(equation.result, it) }) {
            equation.result
        } else {
            0
        }
    }

    println("Part1: $sum")
}

private fun check(result: Long, possibleWayToSolve: PossibleWayToSolve): Boolean {
    var sum = possibleWayToSolve.initial

    for (step in possibleWayToSolve.equationSteps) {
        when (step.operation) {
            Operation.Add -> sum += step.number
            Operation.Multiply -> sum *= step.number
            Operation.Concat -> sum = (sum.toString() + step.number.toString()).toLong()
        }
    }

    return sum == result
}

private fun findPossibleWayToSolve(equation: Equation, withConcat: Boolean): List<PossibleWayToSolve> {
    val first = equation.numbers.first()
    val equationSteps = expand(equation.numbers.drop(1), withConcat)

    return equationSteps.map { PossibleWayToSolve(initial = first, equationSteps = it) }
}

private fun expand(numbers: List<Long>, withConcat: Boolean): List<List<EquationStep>> {
    if (numbers.size == 1) {
        return mutableListOf(
            listOf(EquationStep(number = numbers.first(), operation = Operation.Add)),
            listOf(EquationStep(number = numbers.first(), operation = Operation.Multiply)),
        ).apply {
            if (withConcat) {
                add(listOf(EquationStep(number = numbers.first(), operation = Operation.Concat)))
            }
        }
    }

    val current = numbers.first()
    val rest = numbers.drop(1)

    val restExpanded = expand(rest, withConcat)
    val all = mutableListOf<List<EquationStep>>()
    restExpanded.forEach { equationSteps ->
        all.add(
            listOf(EquationStep(number = current, operation = Operation.Add)) + equationSteps,
        )

        all.add(
            listOf(EquationStep(number = current, operation = Operation.Multiply)) + equationSteps,
        )

        if (withConcat) {
            all.add(
                listOf(EquationStep(number = current, operation = Operation.Concat)) + equationSteps,
            )
        }
    }

    return all
}

private fun part2TestInput() {
    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()

    val equations = parseInput(testInput)
    val sum = equations.sumOf { equation ->
        val possibleWaysToSolve = findPossibleWayToSolve(equation, true)
        if (possibleWaysToSolve.any { check(equation.result, it) }) {
            equation.result
        } else {
            0
        }
    }

    println("Part2 Test: $sum")
}

private fun part2() {
    val input = readInputLines("Day07")

    val equations = parseInput(input)
    val sum = equations.sumOf { equation ->
        val possibleWaysToSolve = findPossibleWayToSolve(equation, true)
        if (possibleWaysToSolve.any { check(equation.result, it) }) {
            equation.result
        } else {
            0
        }
    }

    println("Part2: $sum")
}

private data class Equation(
    val result: Long,
    val numbers: List<Long>,
)

private data class PossibleWayToSolve(
    val initial: Long,
    val equationSteps: List<EquationStep>,
)

private data class EquationStep(
    val number: Long,
    val operation: Operation,
)

private enum class Operation {
    Add,
    Multiply,
    Concat
}