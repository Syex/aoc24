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

private fun parseInput(testInput: List<String>) = testInput.map {
    it.trim().split("   ")
}

private fun part1TestInput() {
    val testInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().lines()

    println("Part1 Test:")
}

private fun part1() {
    val input = readInputLines("Day")

    println("Part1: ")
}


private fun part2TestInput() {
    val testInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().lines()

    println("Part2 Test: ")
}

private fun part2() {
    val input = readInputLines("Day")

    println("Part2: ")
}

