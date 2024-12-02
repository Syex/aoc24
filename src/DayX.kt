fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
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
    val input = readInput("Day")

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
    val input = readInput("Day0")

    println("Part2: ")
}

