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
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()

    val map = parseInput(testInput)
    val uniqueLocations = findUniqueLocations(map)

    println("Part1 Test: $uniqueLocations")
}

private fun part1() {
    val input = readInputLines("Day08")

    val map = parseInput(input)
    val uniqueLocations = findUniqueLocations(map)

    println("Part1: $uniqueLocations")
}

private fun findUniqueLocations(map: Array<CharArray>, expandOnce: Boolean = true): Int {
    val antennaToPositionMap = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    map.forEachIndexed { index, chars ->
        chars.forEachIndexed { charIndex, c ->
            if (c != '.') {
                antennaToPositionMap.getOrPut(c, { mutableListOf() }).add(index to charIndex)
            }
        }
    }

    for ((char, positions) in antennaToPositionMap.entries) {
        for (i in positions) {
            for (j in positions) {
                if (i == j) continue

                val distance = i.first - j.first to i.second - j.second

                if (expandOnce) {
                    // top
                    var current = map.getOrNull(i.first + distance.first)?.getOrNull(i.second + distance.second)
                    if (current != null) {
                        map[i.first + distance.first][i.second + distance.second] = '#'
                    }

                    // bottom
                    current = map.getOrNull(j.first - distance.first)?.getOrNull(j.second - distance.second)
                    if (current != null) {
                        map[j.first - distance.first][j.second - distance.second] = '#'
                    }
                } else {
                    // top
                    var distanceMultiplier = 1
                    var current = map.getOrNull(i.first + distance.first)?.getOrNull(i.second + distance.second)
                    while (current != null) {
                        map[i.first + distance.first * distanceMultiplier][i.second + distance.second * distanceMultiplier] =
                            '#'
                        distanceMultiplier++
                        current = map.getOrNull(i.first + distance.first * distanceMultiplier)
                            ?.getOrNull(i.second + distance.second * distanceMultiplier)
                    }

                    // bottom
                    distanceMultiplier = 1
                    current = map.getOrNull(i.first - distance.first)?.getOrNull(i.second - distance.second)
                    while (current != null) {
                        map[i.first - distance.first * distanceMultiplier][i.second - distance.second * distanceMultiplier] =
                            '#'
                        distanceMultiplier++
                        current = map.getOrNull(i.first - distance.first * distanceMultiplier)
                            ?.getOrNull(i.second - distance.second * distanceMultiplier)
                    }
                }
            }
        }
    }

    return map.sumOf { it.count { it == '#' } }
}

private fun part2TestInput() {
    val testInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()

    val map = parseInput(testInput)
    val uniqueLocations = findUniqueLocations(map, expandOnce = false)

    println("Part2 Test: $uniqueLocations")
}

private fun part2() {
    val input = readInputLines("Day08")

    val map = parseInput(input)
    val uniqueLocations = findUniqueLocations(map, expandOnce = false)

    println("Part2: $uniqueLocations")
}

