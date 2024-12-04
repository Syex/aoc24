fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun part1TestInput() {
    val testInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()

    val xmasFound = findXMAS(testInput)

    println("Part1 Test: $xmasFound")
    check(xmasFound == 18)
}

private fun part1() {
    val input = readInputLines("Day04")

    val xmasFound = findXMAS(input)

    println("Part1: $xmasFound")
}

private fun findXMAS(input: List<String>): Int {
    var xmasFound = 0

    for (i in input.indices) {
        val currentLine = input[i].toCharArray()

        for (j in currentLine.indices) {
            if (currentLine[j] == 'X') {
                // left
                if (currentLine.getOrNull(j - 1) == 'M' &&
                    currentLine.getOrNull(j - 2) == 'A' &&
                    currentLine.getOrNull(j - 3) == 'S'
                ) {
                    xmasFound++
                }

                // right
                if (currentLine.getOrNull(j + 1) == 'M' &&
                    currentLine.getOrNull(j + 2) == 'A' &&
                    currentLine.getOrNull(j + 3) == 'S'
                ) {
                    xmasFound++
                }

                // top
                if (input.getOrNull(i - 1)?.getOrNull(j) == 'M' &&
                    input.getOrNull(i - 2)?.getOrNull(j) == 'A' &&
                    input.getOrNull(i - 3)?.getOrNull(j) == 'S'
                ) {
                    xmasFound++
                }

                // down
                if (input.getOrNull(i + 1)?.getOrNull(j) == 'M' &&
                    input.getOrNull(i + 2)?.getOrNull(j) == 'A' &&
                    input.getOrNull(i + 3)?.getOrNull(j) == 'S'
                ) {
                    xmasFound++
                }

                // diagonal left top
                if (input.getOrNull(i - 1)?.getOrNull(j - 1) == 'M' &&
                    input.getOrNull(i - 2)?.getOrNull(j - 2) == 'A' &&
                    input.getOrNull(i - 3)?.getOrNull(j - 3) == 'S'
                ) {
                    xmasFound++
                }

                // diagonal right top
                if (input.getOrNull(i - 1)?.getOrNull(j + 1) == 'M' &&
                    input.getOrNull(i - 2)?.getOrNull(j + 2) == 'A' &&
                    input.getOrNull(i - 3)?.getOrNull(j + 3) == 'S'
                ) {
                    xmasFound++
                }

                // diagonal left bottom
                if (input.getOrNull(i + 1)?.getOrNull(j - 1) == 'M' &&
                    input.getOrNull(i + 2)?.getOrNull(j - 2) == 'A' &&
                    input.getOrNull(i + 3)?.getOrNull(j - 3) == 'S'
                ) {
                    xmasFound++
                }

                // diagonal right bottom
                if (input.getOrNull(i + 1)?.getOrNull(j + 1) == 'M' &&
                    input.getOrNull(i + 2)?.getOrNull(j + 2) == 'A' &&
                    input.getOrNull(i + 3)?.getOrNull(j + 3) == 'S'
                ) {
                    xmasFound++
                }
            }
        }
    }

    return xmasFound
}

private fun part2TestInput() {
    val testInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()

    val xmasFound = findXMAS2(testInput)

    println("Part2 Test: $xmasFound")
    check(xmasFound == 9)
}

private fun part2() {
    val input = readInputLines("Day04")

    val xmasFound = findXMAS2(input)

    println("Part2: $xmasFound")
}

private fun findXMAS2(input: List<String>): Int {
    var xmasFound = 0

    for (i in input.indices) {
        val currentLine = input[i].toCharArray()

        for (j in currentLine.indices) {
            if (currentLine[j] == 'A') {
                // MAS both
                if (input.getOrNull(i - 1)?.getOrNull(j - 1) == 'M' &&
                    input.getOrNull(i - 1)?.getOrNull(j + 1) == 'M' &&
                    input.getOrNull(i + 1)?.getOrNull(j - 1) == 'S' &&
                    input.getOrNull(i + 1)?.getOrNull(j + 1) == 'S'
                ) {
                    xmasFound++
                }

                // SAM both
                if (input.getOrNull(i - 1)?.getOrNull(j - 1) == 'S' &&
                    input.getOrNull(i - 1)?.getOrNull(j + 1) == 'S' &&
                    input.getOrNull(i + 1)?.getOrNull(j - 1) == 'M' &&
                    input.getOrNull(i + 1)?.getOrNull(j + 1) == 'M'
                ) {
                    xmasFound++
                }

                // MAS SAM
                if (input.getOrNull(i - 1)?.getOrNull(j - 1) == 'M' &&
                    input.getOrNull(i - 1)?.getOrNull(j + 1) == 'S' &&
                    input.getOrNull(i + 1)?.getOrNull(j - 1) == 'M' &&
                    input.getOrNull(i + 1)?.getOrNull(j + 1) == 'S'
                ) {
                    xmasFound++
                }

                // SAM MAS
                if (input.getOrNull(i - 1)?.getOrNull(j - 1) == 'S' &&
                    input.getOrNull(i - 1)?.getOrNull(j + 1) == 'M' &&
                    input.getOrNull(i + 1)?.getOrNull(j - 1) == 'S' &&
                    input.getOrNull(i + 1)?.getOrNull(j + 1) == 'M'
                ) {
                    xmasFound++
                }
            }
        }
    }

    return xmasFound
}

