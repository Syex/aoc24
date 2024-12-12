fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: List<String>) = testInput.map {
    it.trim().toCharArray()
}.toTypedArray()

private fun part1TestInput() {
    val testInput = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines()

//    val testInput = """
//        OOOOO
//        OXOXO
//        OOOOO
//        OXOXO
//        OOOOO
//    """.trimIndent().lines()

//    val testInput = """
//        RRRRIICCFF
//        RRRRIICCCF
//        VVRRRCCFFF
//        VVRCCCJFFF
//        VVVVCJJCFE
//        VVIVCCJJEE
//        VVIIICJJEE
//        MIIIIIJJEE
//        MIIISIJEEE
//        MMMISSJEEE
//    """.trimIndent().lines()

    val gardenPlot = createGardenPlot(parseInput(testInput))
    val groups = findGroups(gardenPlot)
    val totalCost = calculateTotalCost(groups)

    println("Part1 Test: $totalCost")
}

private fun part1() {
    val input = readInputLines("Day12")

    val gardenPlot = createGardenPlot(parseInput(input))
    val groups = findGroups(gardenPlot)
    val totalCost = calculateTotalCost(groups)

    println("Part1: $totalCost")
}


private fun part2TestInput() {
    val testInput = """
        EEEEE
        EXXXX
        EEEEE
        EXXXX
        EEEEE
    """.trimIndent().lines()

    val gardenPlot = createGardenPlot(parseInput(testInput))
    val groups = findGroups(gardenPlot)
    val totalCost = calculateTotalCost2(groups)

    println("Part2 Test: $totalCost")
}

private fun part2() {
    val input = readInputLines("Day12")

    val gardenPlot = createGardenPlot(parseInput(input))
    val groups = findGroups(gardenPlot)
    val totalCost = calculateTotalCost2(groups)

    println("Part2: $totalCost")
}

private fun createGardenPlot(input: Array<CharArray>): Array<Array<PlantSpot>> {
    val gardenPlot = Array<Array<PlantSpot>>(input.size) { _ -> emptyArray() }

    for (y in input.indices) {
        gardenPlot[y] = Array(input[y].size) { x ->
            val plant = input[y][x]
            PlantSpot(
                position = Pair(y, x),
                plant = plant,
                fenceAtTop = input.getOrNull(y - 1)?.getOrNull(x) != plant,
                fenceAtRight = input[y].getOrNull(x + 1) != plant,
                fenceAtBottom = input.getOrNull(y + 1)?.getOrNull(x) != plant,
                fenceAtLeft = input[y].getOrNull(x - 1) != plant,
            )
        }
    }

    return gardenPlot
}

private fun findGroups(gardenPlot: Array<Array<PlantSpot>>): List<List<PlantSpot>> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val groups = mutableListOf<List<PlantSpot>>()

    for (y in gardenPlot.indices) {
        for (x in gardenPlot[y].indices) {
            if (!visited.contains(y to x)) {
                val group = mutableListOf<PlantSpot>()
                dfs(gardenPlot, x, y, group, gardenPlot[y][x])
                visited.addAll(group.map { it.position })
                groups.add(group)
            }
        }
    }

    return groups
}

private fun calculateTotalCost(groups: List<List<PlantSpot>>) = groups.sumOf { plantSpots ->
    val area = plantSpots.size
    val perimeter = plantSpots.sumOf { plantSpot ->
        var fences = 0
        if (plantSpot.fenceAtTop) fences++
        if (plantSpot.fenceAtRight) fences++
        if (plantSpot.fenceAtBottom) fences++
        if (plantSpot.fenceAtLeft) fences++
        fences
    }
    area * perimeter
}

private fun calculateTotalCost2(groups: List<List<PlantSpot>>) = groups.sumOf { plantSpots ->
    var sides = 0

    val topFencePositions = plantSpots.filter { it.fenceAtTop }.map { it.position }
    val topNeighbours = topFencePositions.map { position ->
        (listOf(position) + findHorizontalNeighbours(
            position, topFencePositions.filter { it.first == position.first })).toList().sortedBy { it.second }
    }.toSet()
    sides += topNeighbours.size

    val bottomFencePositions = plantSpots.filter { it.fenceAtBottom }.map { it.position }
    val bottomNeighbours = bottomFencePositions.map { position ->
        (listOf(position) + findHorizontalNeighbours(
            position, bottomFencePositions.filter { it.first == position.first })).toList().sortedBy { it.second }
    }.toSet()
    sides += bottomNeighbours.size

    val rightFencePositions = plantSpots.filter { it.fenceAtRight }.map { it.position }
    val rightNeighbours = rightFencePositions.map { position ->
        (listOf(position) + findVerticalNeighbours(
            position, rightFencePositions.filter { it.second == position.second })).toList().sortedBy { it.first }
    }.toSet()
    sides += rightNeighbours.size

    val leftFencePositions = plantSpots.filter { it.fenceAtLeft }.map { it.position }
    val leftNeighbours = leftFencePositions.map { position ->
        (listOf(position) + findVerticalNeighbours(
            position, leftFencePositions.filter { it.second == position.second })).toList().sortedBy { it.first }
    }.toSet()
    sides += leftNeighbours.size

    val area = plantSpots.size
    area * sides
}

private fun findHorizontalNeighbours(
    startPosition: Pair<Int, Int>,
    positions: List<Pair<Int, Int>>,
    consideredPositions: MutableList<Pair<Int, Int>> = mutableListOf(startPosition),
): Set<Pair<Int, Int>> {
    val neighbours = mutableSetOf<Pair<Int, Int>>()
    for (position in positions) {
        if (position in consideredPositions) continue

        if (position.second == startPosition.second + 1 || position.second == startPosition.second - 1) {
            consideredPositions.add(position)
            neighbours.add(position)
            neighbours.addAll(findHorizontalNeighbours(position, positions, consideredPositions))
        }
    }

    return neighbours
}

private fun findVerticalNeighbours(
    startPosition: Pair<Int, Int>,
    positions: List<Pair<Int, Int>>,
    consideredPositions: MutableList<Pair<Int, Int>> = mutableListOf(startPosition),
): Set<Pair<Int, Int>> {
    val neighbours = mutableSetOf<Pair<Int, Int>>()
    for (position in positions) {
        if (position in consideredPositions) continue

        if (position.first == startPosition.first + 1 || position.first == startPosition.first - 1) {
            consideredPositions.add(position)
            neighbours.add(position)
            neighbours.addAll(findVerticalNeighbours(position, positions, consideredPositions))
        }
    }

    return neighbours
}

private fun dfs(
    gardenPlot: Array<Array<PlantSpot>>,
    x: Int,
    y: Int,
    group: MutableList<PlantSpot>,
    targetPlantSpot: PlantSpot,
    visited: MutableList<Pair<Int, Int>> = mutableListOf(),
) {
    if ((y !in gardenPlot.indices) ||
        (x !in gardenPlot[y].indices) ||
        gardenPlot[y][x].plant != targetPlantSpot.plant ||
        y to x in visited
    ) {
        return
    }

    visited.add(y to x)
    group.add(gardenPlot[y][x])
    dfs(gardenPlot, x + 1, y, group, targetPlantSpot, visited)
    dfs(gardenPlot, x - 1, y, group, targetPlantSpot, visited)
    dfs(gardenPlot, x, y + 1, group, targetPlantSpot, visited)
    dfs(gardenPlot, x, y - 1, group, targetPlantSpot, visited)
}

private data class PlantSpot(
    val position: Pair<Int, Int>,
    val plant: Char,
    val fenceAtTop: Boolean,
    val fenceAtRight: Boolean,
    val fenceAtBottom: Boolean,
    val fenceAtLeft: Boolean,
)