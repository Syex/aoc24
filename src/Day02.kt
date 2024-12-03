import kotlin.math.absoluteValue

fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: List<String>) = testInput.map { line ->
    Report(line.trim().split(" ").map { it.toInt() })
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

    val reports = parseInput(testInput)
    val safeReports = reports.count { isReportSafe(it) }
    println("Part1 Test: $safeReports")

    check(safeReports == 2)
}

private fun part1() {
    val input = readInputLines("Day02")
    val reports = parseInput(input)
    val safeReports = reports.count { isReportSafe(it) }
    println("Part1: $safeReports")
}

private fun isReportSafe(report: Report): Boolean {
    val levels = report.levels
    if (levels.isEmpty()) return true

    var startedAsIncreasing: Boolean? = null

    levels.zipWithNext().forEach { (first, second) ->
        val difference = (first - second).absoluteValue
        if (difference !in 1..3) return false

        when (startedAsIncreasing) {
            null -> {
                startedAsIncreasing = second > first
            }

            true -> {
                if (first > second) return false
            }

            false -> {
                if (second > first) return false
            }
        }
    }

    return true
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

    val reports = parseInput(testInput)
    val safeReports = reports.count { isReportSafePart2(it) }
    println("Part2 Test: $safeReports")

    check(safeReports == 4)
}

private fun part2() {
    val input = readInputLines("Day02")
    val reports = parseInput(input)
    val safeReports = reports.count { isReportSafePart2(it) }
    println("Part2: $safeReports")
}

private fun isReportSafePart2(report: Report, badLevelToleratedOnce: Boolean = false): Boolean {
    val levels = report.levels
    if (levels.isEmpty()) return true

    var startedAsIncreasing: Boolean? = null

    fun runDampener() = if (badLevelToleratedOnce) {
        false
    } else {
        levels.indices.any { j ->
            isReportSafePart2(
                Report(levels.toMutableList().apply { removeAt(j) }),
                badLevelToleratedOnce = true,
            )
        }
    }


    levels.zipWithNext { first, second ->
        val difference = (first - second).absoluteValue
        if (difference !in 1..3) {
            return runDampener()
        }

        when (startedAsIncreasing) {
            null -> {
                startedAsIncreasing = second > first
            }

            true -> {
                if (first > second) {
                    return runDampener()
                }
            }

            false -> {
                if (second > first) {
                    return runDampener()
                }
            }
        }
    }

    return true
}

private data class Report(
    val levels: List<Int>,
)
