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

private fun parseInput(testInput: String): Pair<Array<CharArray>, MutableList<Char>> {
    val parts = testInput.split("\n\n")
    val grid = parts[0].lines().map { line -> line.toCharArray() }.toTypedArray()
    val commands = parts[1]
    return grid to commands.toCharArray().toMutableList()
}

private fun part1TestInput() {
//    val testInput = """
//        ########
//        #..O.O.#
//        ##@.O..#
//        #...O..#
//        #.#.O..#
//        #...O..#
//        #......#
//        ########
//
//        <^^>>>vv<v>>v<<
//    """.trimIndent()
    val testInput = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent()

    val (grid, commands) = parseInput(testInput)

    val finalGrid = moveRobot(grid, commands)
    for (row in finalGrid) {
        for (col in row) {
            print(col)
        }
        println()
    }
    val gpsSum = finalGrid.getGPSSum()

    println("Part1 Test: $gpsSum")
}

private fun part1() {
    val input = readInput("Day15")

    val (grid, commands) = parseInput(input)

    val finalGrid = moveRobot(grid, commands)
    for (row in finalGrid) {
        for (col in row) {
            print(col)
        }
        println()
    }
    val gpsSum = finalGrid.getGPSSum()

    println("Part1: $gpsSum")
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
    val input = readInputLines("Day15")

    println("Part2: ")
}

private tailrec fun moveRobot(grid: Array<CharArray>, commands: MutableList<Char>): Array<CharArray> {
    if (commands.isEmpty()) return grid

    val (robotX, robotY) = findRobotPosition(grid)
    val nextMove = commands.removeFirst()

    when (nextMove) {
        '^' -> {
            if (grid.isEmptyAt(robotX, robotY - 1)) {
                grid[robotY][robotX] = '.'
                grid[robotY - 1][robotX] = '@'
            } else if (grid.isWallAt(robotX, robotY - 1)) {
                // do nothing, just for completeness
            } else if (grid.isBoxAt(robotX, robotY - 1)) {
                val nextEmptyPosition = grid.findNextEmptyPositionTop(robotX, robotY)
                if (nextEmptyPosition != null) {
                    var i = robotY
                    while (i >= nextEmptyPosition.y) {
                        grid[i][robotX] = 'O'
                        i--
                    }
                    grid[robotY][robotX] = '.'
                    grid[robotY - 1][robotX] = '@'
                }
            }
        }

        '>' -> {
            if (grid.isEmptyAt(robotX + 1, robotY)) {
                grid[robotY][robotX] = '.'
                grid[robotY][robotX + 1] = '@'
            } else if (grid.isWallAt(robotX + 1, robotY)) {
                // do nothing, just for completeness
            } else if (grid.isBoxAt(robotX + 1, robotY)) {
                val nextEmptyPosition = grid.findNextEmptyPositionRight(robotX, robotY)
                if (nextEmptyPosition != null) {
                    var i = robotX
                    while (i <= nextEmptyPosition.x) {
                        grid[robotY][i] = 'O'
                        i++
                    }
                    grid[robotY][robotX] = '.'
                    grid[robotY][robotX + 1] = '@'
                }
            }
        }

        '<' -> {
            if (grid.isEmptyAt(robotX - 1, robotY)) {
                grid[robotY][robotX] = '.'
                grid[robotY][robotX - 1] = '@'
            } else if (grid.isWallAt(robotX - 1, robotY)) {
                // do nothing, just for completeness
            } else if (grid.isBoxAt(robotX - 1, robotY)) {
                val nextEmptyPosition = grid.findNextEmptyPositionLeft(robotX, robotY)
                if (nextEmptyPosition != null) {
                    var i = robotX
                    while (i >= nextEmptyPosition.x) {
                        grid[robotY][i] = 'O'
                        i--
                    }
                    grid[robotY][robotX] = '.'
                    grid[robotY][robotX - 1] = '@'
                }
            }
        }

        'v' -> {
            if (grid.isEmptyAt(robotX, robotY + 1)) {
                grid[robotY][robotX] = '.'
                grid[robotY + 1][robotX] = '@'
            } else if (grid.isWallAt(robotX, robotY + 1)) {
                // do nothing, just for completeness
            } else if (grid.isBoxAt(robotX, robotY + 1)) {
                val nextEmptyPosition = grid.findNextEmptyPositionBottom(robotX, robotY)
                if (nextEmptyPosition != null) {
                    var i = robotY
                    while (i <= nextEmptyPosition.y) {
                        grid[i][robotX] = 'O'
                        i++
                    }
                    grid[robotY][robotX] = '.'
                    grid[robotY + 1][robotX] = '@'
                }
            }
        }
    }

    return moveRobot(grid, commands.toMutableList())
}

private fun findRobotPosition(grid: Array<CharArray>): Position {
    val y = grid.indexOfFirst { it.contains('@') }
    val x = grid[y].indexOfFirst { it == '@' }
    return Position(x, y)
}

private fun Array<CharArray>.isWallAt(x: Int, y: Int): Boolean = getOrNull(y)?.getOrNull(x) == '#'

private fun Array<CharArray>.isBoxAt(x: Int, y: Int): Boolean = getOrNull(y)?.getOrNull(x) == 'O'

private fun Array<CharArray>.isEmptyAt(x: Int, y: Int): Boolean = getOrNull(y)?.getOrNull(x) == '.'

private fun Array<CharArray>.findNextEmptyPositionTop(x: Int, y: Int): Position? {
    if (y < 0 || isWallAt(x, y)) return null
    if (isEmptyAt(x, y)) return Position(x, y)
    return findNextEmptyPositionTop(x, y - 1)
}

private fun Array<CharArray>.findNextEmptyPositionBottom(x: Int, y: Int): Position? {
    if (y > lastIndex || isWallAt(x, y)) return null
    if (isEmptyAt(x, y)) return Position(x, y)
    return findNextEmptyPositionBottom(x, y + 1)
}

private fun Array<CharArray>.findNextEmptyPositionRight(x: Int, y: Int): Position? {
    if (x > lastIndex || isWallAt(x, y)) return null
    if (isEmptyAt(x, y)) return Position(x, y)
    return findNextEmptyPositionRight(x + 1, y)
}

private fun Array<CharArray>.findNextEmptyPositionLeft(x: Int, y: Int): Position? {
    if (x < 0 || isWallAt(x, y)) return null
    if (isEmptyAt(x, y)) return Position(x, y)
    return findNextEmptyPositionLeft(x - 1, y)
}

private fun Array<CharArray>.getGPSSum(): Long {
    var sum = 0L

    for (y in indices) {
        for (x in get(y).indices) {
            if (isBoxAt(x, y)) {
                sum += 100 * y + x
            }
        }
    }

    return sum
}