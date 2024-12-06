fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun part1TestInput() {
    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()

    val distinctPositions = checkDistinctPositions(
        testInput,
        visitedPosition = testInput.map { it.indices.map { '.' }.toCharArray() }
    )
    val visualized = distinctPositions.map { it.concatToString() }
    val visited = visualized.sumOf { it.count { it == 'X' } }

    println("Part1 Test: $visited")
}

private fun part1() {
    val input = readInputLines("Day06")
    val distinctPositions = checkDistinctPositions(
        input,
        visitedPosition = input.map { it.indices.map { '.' }.toCharArray() })
    val visualized = distinctPositions.map { it.concatToString() }
    val visited = visualized.sumOf { it.count { it == 'X' } }

    println("Part1: $visited")
}

private val guardSigns = listOf('^', 'v', '<', '>')

private fun checkDistinctPositions(
    input: List<String>,
    visitedPosition: List<CharArray>,
): List<CharArray> {
    val currentGuardPosition = input.mapIndexedNotNull { index, s ->
        s.forEachIndexed { indexC, c -> if (c in guardSigns) return@mapIndexedNotNull index to indexC }

        return@mapIndexedNotNull null
    }.firstOrNull()

    if (currentGuardPosition == null) return visitedPosition

    var currentTakenSteps = 0
    val guardDirectionSign = input[currentGuardPosition.first][currentGuardPosition.second]
    val allVisitedPositions = visitedPosition.toMutableList()
    allVisitedPositions[currentGuardPosition.first][currentGuardPosition.second] = 'X'

    when (guardDirectionSign) {
        '^' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first - 1)?.getOrNull(currentGuardPosition.second)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkDistinctPositions(
                        input = input.toMutableList().apply {
                            val initialLine = input[currentGuardPosition.first].toCharArray().apply {
                                set(currentGuardPosition.second, '.')
                            }
                            set(currentGuardPosition.first, String(initialLine))
                            val currentLine = input[currentGuardPosition.first - currentTakenSteps].toCharArray()
                                .apply { set(currentGuardPosition.second, '>') }
                            set(currentGuardPosition.first - currentTakenSteps, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions
                    )
                }

                currentTakenSteps++
                allVisitedPositions[currentGuardPosition.first - currentTakenSteps][currentGuardPosition.second] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first - currentTakenSteps - 1)
                    ?.getOrNull(currentGuardPosition.second)
            }
        }

        'v' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first + 1)?.getOrNull(currentGuardPosition.second)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkDistinctPositions(
                        input = input.toMutableList().apply {
                            val initialLine = input[currentGuardPosition.first].toCharArray().apply {
                                set(currentGuardPosition.second, '.')
                            }
                            set(currentGuardPosition.first, String(initialLine))
                            val currentLine = input[currentGuardPosition.first + currentTakenSteps].toCharArray()
                                .apply { set(currentGuardPosition.second, '<') }
                            set(currentGuardPosition.first + currentTakenSteps, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions
                    )
                }

                currentTakenSteps++
                allVisitedPositions[currentGuardPosition.first + currentTakenSteps][currentGuardPosition.second] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first + currentTakenSteps + 1)
                    ?.getOrNull(currentGuardPosition.second)
            }
        }

        '<' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first)?.getOrNull(currentGuardPosition.second - 1)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkDistinctPositions(
                        input = input.toMutableList().apply {
                            val currentLine = input[currentGuardPosition.first].toCharArray()
                                .apply {
                                    set(currentGuardPosition.second, '.')
                                    set(currentGuardPosition.second - currentTakenSteps, '^')
                                }
                            set(currentGuardPosition.first, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions
                    )
                }

                currentTakenSteps++
                allVisitedPositions[currentGuardPosition.first][currentGuardPosition.second - currentTakenSteps] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first)
                    ?.getOrNull(currentGuardPosition.second - currentTakenSteps - 1)
            }
        }

        '>' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first)?.getOrNull(currentGuardPosition.second + 1)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkDistinctPositions(
                        input = input.toMutableList().apply {
                            val currentLine = input[currentGuardPosition.first].toCharArray()
                                .apply {
                                    set(currentGuardPosition.second, '.')
                                    set(currentGuardPosition.second + currentTakenSteps, 'v')
                                }
                            set(currentGuardPosition.first, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions
                    )
                }

                currentTakenSteps++
                allVisitedPositions[currentGuardPosition.first][currentGuardPosition.second + currentTakenSteps] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first)
                    ?.getOrNull(currentGuardPosition.second + currentTakenSteps + 1)
            }
        }
    }

    return allVisitedPositions
}

private fun part2TestInput() {
    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()

    val obstaclePositions =
        checkObstaclePositions(
            testInput,
            visitedPosition = testInput.map { it.indices.map { '.' }.toCharArray() },
            possibleObstaclePositions = mutableSetOf()
        )

    println("Part2 Test: ${obstaclePositions.size} = $obstaclePositions")
}

private fun part2() {
    val input = readInputLines("Day0")

    println("Part2: ")
}

private fun checkObstaclePositions(
    input: List<String>,
    visitedPosition: List<CharArray>,
    possibleObstaclePositions: MutableSet<Pair<Int, Int>>,
): Set<Pair<Int, Int>> {
    val currentGuardPosition = input.mapIndexedNotNull { index, s ->
        s.forEachIndexed { indexC, c -> if (c in guardSigns) return@mapIndexedNotNull index to indexC }

        return@mapIndexedNotNull null
    }.firstOrNull()

    if (currentGuardPosition == null) return possibleObstaclePositions

    var currentTakenSteps = 0
    val guardDirectionSign = input[currentGuardPosition.first][currentGuardPosition.second]
    val allVisitedPositions = visitedPosition.toMutableList()
    allVisitedPositions[currentGuardPosition.first][currentGuardPosition.second] = 'X'

    when (guardDirectionSign) {
        '^' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first - 1)?.getOrNull(currentGuardPosition.second)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkObstaclePositions(
                        input = input.toMutableList().apply {
                            val initialLine = input[currentGuardPosition.first].toCharArray().apply {
                                set(currentGuardPosition.second, '.')
                            }
                            set(currentGuardPosition.first, String(initialLine))
                            val currentLine = input[currentGuardPosition.first - currentTakenSteps].toCharArray()
                                .apply { set(currentGuardPosition.second, '>') }
                            set(currentGuardPosition.first - currentTakenSteps, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions,
                        possibleObstaclePositions,
                    )
                }

                currentTakenSteps++

                if (allVisitedPositions.getOrNull(currentGuardPosition.first - currentTakenSteps)
                        ?.getOrNull(currentGuardPosition.second + 1) == 'X'
                ) {
                    possibleObstaclePositions.add(currentGuardPosition.first - currentTakenSteps - 1 to currentGuardPosition.second)
                }

                allVisitedPositions[currentGuardPosition.first - currentTakenSteps][currentGuardPosition.second] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first - currentTakenSteps - 1)
                    ?.getOrNull(currentGuardPosition.second)
            }
        }

        'v' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first + 1)?.getOrNull(currentGuardPosition.second)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkObstaclePositions(
                        input = input.toMutableList().apply {
                            val initialLine = input[currentGuardPosition.first].toCharArray().apply {
                                set(currentGuardPosition.second, '.')
                            }
                            set(currentGuardPosition.first, String(initialLine))
                            val currentLine = input[currentGuardPosition.first + currentTakenSteps].toCharArray()
                                .apply { set(currentGuardPosition.second, '<') }
                            set(currentGuardPosition.first + currentTakenSteps, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions,
                        possibleObstaclePositions,
                    )
                }

                currentTakenSteps++

                if (allVisitedPositions.getOrNull(currentGuardPosition.first + currentTakenSteps)
                        ?.getOrNull(currentGuardPosition.second - 1) == 'X'
                ) {
                    possibleObstaclePositions.add(currentGuardPosition.first + currentTakenSteps + 1 to currentGuardPosition.second)
                }

                allVisitedPositions[currentGuardPosition.first + currentTakenSteps][currentGuardPosition.second] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first + currentTakenSteps + 1)
                    ?.getOrNull(currentGuardPosition.second)
            }
        }

        '<' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first)?.getOrNull(currentGuardPosition.second - 1)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkObstaclePositions(
                        input = input.toMutableList().apply {
                            val currentLine = input[currentGuardPosition.first].toCharArray()
                                .apply {
                                    set(currentGuardPosition.second, '.')
                                    set(currentGuardPosition.second - currentTakenSteps, '^')
                                }
                            set(currentGuardPosition.first, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions,
                        possibleObstaclePositions,
                    )
                }

                currentTakenSteps++

                if (allVisitedPositions.getOrNull(currentGuardPosition.first - 1)
                        ?.getOrNull(currentGuardPosition.second - currentTakenSteps) == 'X'
                ) {
                    possibleObstaclePositions.add(currentGuardPosition.first to currentGuardPosition.second - currentTakenSteps - 1)
                }

                allVisitedPositions[currentGuardPosition.first][currentGuardPosition.second - currentTakenSteps] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first)
                    ?.getOrNull(currentGuardPosition.second - currentTakenSteps - 1)
            }
        }

        '>' -> {
            var nextObject = input.getOrNull(currentGuardPosition.first)?.getOrNull(currentGuardPosition.second + 1)
            while (nextObject != null) {
                if (nextObject == '#') {
                    return checkObstaclePositions(
                        input = input.toMutableList().apply {
                            val currentLine = input[currentGuardPosition.first].toCharArray()
                                .apply {
                                    set(currentGuardPosition.second, '.')
                                    set(currentGuardPosition.second + currentTakenSteps, 'v')
                                }
                            set(currentGuardPosition.first, String(currentLine))
                        },
                        visitedPosition = allVisitedPositions,
                        possibleObstaclePositions,
                    )
                }

                currentTakenSteps++

                if (allVisitedPositions.getOrNull(currentGuardPosition.first + 1)
                        ?.getOrNull(currentGuardPosition.second + currentTakenSteps) == 'X'
                ) {
                    possibleObstaclePositions.add(currentGuardPosition.first to currentGuardPosition.second + currentTakenSteps + 1)
                }

                allVisitedPositions[currentGuardPosition.first][currentGuardPosition.second + currentTakenSteps] = 'X'
                nextObject = input.getOrNull(currentGuardPosition.first)
                    ?.getOrNull(currentGuardPosition.second + currentTakenSteps + 1)
            }
        }
    }

    return possibleObstaclePositions
}
