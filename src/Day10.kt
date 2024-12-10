fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: List<String>) = testInput.map {
    it.trim().map { it.digitToInt() }.toIntArray()
}.toTypedArray()

private fun part1TestInput() {
    val testInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()

    val map = parseInput(testInput)
    val trailheads = findAllTrailheads(map)
    val score = trailheads.sumOf { it.score }

    println("Part1 Test: $score")
}

private fun part1() {
    val input = readInputLines("Day10")

    val map = parseInput(input)
    val trailheads = findAllTrailheads(map)
    val score = trailheads.sumOf { it.score }

    println("Part1: $score")
}

private fun part2TestInput() {
    val testInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()

    val map = parseInput(testInput)
    val trailheads = findAllTrailheads(map)
    val rating = trailheads.sumOf { it.rating }

    println("Part2 Test: $rating")
}

private fun part2() {
    val input = readInputLines("Day10")

    val map = parseInput(input)
    val trailheads = findAllTrailheads(map)
    val rating = trailheads.sumOf { it.rating }

    println("Part2: $rating")
}

private fun findAllTrailheads(map: Array<IntArray>): List<Trailhead> {
    val startingPositions = map.mapIndexed { index, ints ->
        ints.mapIndexed { innerIndex, i -> if (i == 0) index to innerIndex else null }
    }.flatten().filterNotNull()

    return startingPositions.map { startPos ->
        val paths = findPathsFrom(startPos, map)
        Trailhead(startPos, paths)
    }
}

private fun findPathsFrom(startPosition: Pair<Int, Int>, map: Array<IntArray>): List<Path> {
    return findNextPathSteps(startPosition, map).map { Path(it) }
}

private fun findNextPathSteps(currentPos: Pair<Int, Int>, map: Array<IntArray>): List<List<Pair<Int, Int>>> {
    val currentValue = map[currentPos.first][currentPos.second]

    if (currentValue == 9) return listOf(listOf(currentPos.first to currentPos.second))

    val topPos = map.getOrNull(currentPos.first - 1)?.get(currentPos.second)
    val bottomPos = map.getOrNull(currentPos.first + 1)?.get(currentPos.second)
    val rightPos = map[currentPos.first].getOrNull(currentPos.second + 1)
    val leftPos = map[currentPos.first].getOrNull(currentPos.second - 1)

    val paths = mutableListOf<List<Pair<Int, Int>>>()
    if (topPos == currentValue + 1) {
        val pathsFromHere = findNextPathSteps(currentPos.first - 1 to currentPos.second, map)
        paths.addAll(pathsFromHere.map { path -> listOf(currentPos) + path })
    }
    if (bottomPos == currentValue + 1) {
        val pathsFromHere = findNextPathSteps(currentPos.first + 1 to currentPos.second, map)
        paths.addAll(pathsFromHere.map { path -> listOf(currentPos) + path })
    }
    if (rightPos == currentValue + 1) {
        val pathsFromHere = findNextPathSteps(currentPos.first to currentPos.second + 1, map)
        paths.addAll(pathsFromHere.map { path -> listOf(currentPos) + path })
    }
    if (leftPos == currentValue + 1) {
        val pathsFromHere = findNextPathSteps(currentPos.first to currentPos.second - 1, map)
        paths.addAll(pathsFromHere.map { path -> listOf(currentPos) + path })
    }

    return paths
}

private data class Trailhead(
    val startPosition: Pair<Int, Int>,
    val hikingTrails: List<Path>,
) {
    val score: Int get() = hikingTrails.groupBy { it.steps.last() }.keys.size
    val rating: Int get() = hikingTrails.size
}

private data class Path(
    val steps: List<Pair<Int, Int>>,
)