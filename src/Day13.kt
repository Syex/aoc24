import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        part1TestInput()
    }.also { println("Part1 Test finished in $it ms") }
    measureTimeMillis {
        part1()
    }.also { println("Part1 finished in $it ms") }
    measureTimeMillis {
        part2TestInput()
    }.also { println("Part2 Test finished in $it ms") }
    measureTimeMillis {
        part2()
    }.also { println("Part2 finished in $it ms") }
}

private fun parseInput(testInput: String) = testInput.split("\n\n").map {
    val lines = it.lines()
    val buttonAParts = lines[0].split(": ", ", ", ", ")
    val buttonA = Button(
        x = buttonAParts[1].substringAfter("X").toInt(),
        y = buttonAParts[2].substringAfter("Y").toInt()
    )
    val buttonBParts = lines[1].split(": ", ", ", ", ")
    val buttonB = Button(
        x = buttonBParts[1].substringAfter("X").toInt(),
        y = buttonBParts[2].substringAfter("Y").toInt()
    )
    val priceParts = lines[2].split(": ", ", ", ", ")
    val prize = Prize(
        x = priceParts[1].substringAfter("X=").toInt(),
        y = priceParts[2].substringAfter("Y=").toInt()
    )

    MachineSimulation(buttonA, buttonB, prize)
}

private fun part1TestInput() {
    val testInput = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400
        
        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176
        
        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450
        
        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent()

    val simulations = parseInput(testInput)
    val tokensRequired = simulations.mapNotNull { it.findBestCombination() }
        .sumOf { it.first * 3 + it.second }

    println("Part1 Test: $tokensRequired")
}

private fun part1() {
    val input = readInput("Day13")

    val simulations = parseInput(input)
    val tokensRequired = simulations.mapNotNull { it.findBestCombination() }
        .sumOf { it.first * 3 + it.second }

    println("Part1: $tokensRequired")
}

private fun part2TestInput() {
    val testInput = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400
        
        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176
        
        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450
        
        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent()

    val simulations = parseInput(testInput)
    val tokensRequired = simulations
        .mapNotNull { it.findBestCombination2() }
        .sumOf { it.first * 3 + it.second }

    println("Part2 Test: $tokensRequired")
}

private fun part2() {
    val input = readInput("Day13")

    val simulations = parseInput(input)
    val tokensRequired = simulations.mapNotNull { it.findBestCombination2() }
        .sumOf { it.first * 3 + it.second }

    println("Part2: $tokensRequired")
}

private fun MachineSimulation.findBestCombination(): Pair<Int, Int>? {
    val maxPresses = 100

    var lowestCost = Int.MAX_VALUE
    var btnAPresses: Int? = null
    var btnBPresses: Int? = null

    for (i in 0..maxPresses) {
        for (j in 0..maxPresses) {
            val xSum = i * buttonA.x + j * buttonB.x
            val ySum = i * buttonA.y + j * buttonB.y
            if (xSum == prize.x && ySum == prize.y) {
                val cost = i * 3 + j
                if (cost < lowestCost) {
                    lowestCost = cost
                    btnAPresses = i
                    btnBPresses = j
                }
            }
        }
    }

    return if (btnAPresses != null && btnBPresses != null) btnAPresses to btnBPresses else null
}

private fun MachineSimulation.findBestCombination2(): Pair<Long, Long>? {
    val prizeX = prize.x + 10000000000000
    val prizeY = prize.y + 10000000000000
    val btnAPresses = (prizeY * buttonB.x - prizeX * buttonB.y) / (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
    val btnBPresses = (prizeX - btnAPresses * buttonA.x) / buttonB.x

    return if (btnAPresses * buttonA.x + btnBPresses * buttonB.x == prizeX &&
        btnAPresses * buttonA.y + btnBPresses * buttonB.y == prizeY
    ) {
        btnAPresses to btnBPresses
    } else {
        null
    }
}

private data class MachineSimulation(
    val buttonA: Button,
    val buttonB: Button,
    val prize: Prize,
)

private data class Button(val x: Int, val y: Int)

private data class Prize(val x: Int, val y: Int)