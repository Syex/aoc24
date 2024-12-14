import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        part1TestInput()
    }.also { println("Part1 Test finished in $it ms") }
    measureTimeMillis {
        part1()
    }.also { println("Part1 finished in $it ms") }
    measureTimeMillis {
        part2()
    }.also { println("Part2 finished in $it ms") }
}

private fun parseInput(testInput: List<String>) = testInput.map {
    val parts = it.trim().split(" ")
    val initialPosition = parts[0].substringAfter("p=").split(",").let {
        Position(it[0].toInt(), it[1].toInt())
    }

    val velocityX = parts[1].substringAfter("v=").split(",")[0].toInt()
    val velocityY = parts[1].substringAfter("v=").split(",")[1].toInt()

    Robot(initialPosition, velocityX, velocityY)
}

private fun part1TestInput() {
    val testInput = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent().lines()

    val initialRobots = parseInput(testInput)
    val grid = simulate(initialRobots, 100, 7, 11)
    val safetyFactor = getSafetyFactor(grid)

    println("Part1 Test: $safetyFactor")
}

private fun part1() {
    val input = readInputLines("Day14")

    val initialRobots = parseInput(input)
    val grid = simulate(initialRobots, 100, 103, 101)

    val safetyFactor = getSafetyFactor(grid)

    println("Part1: $safetyFactor")
}

private fun part2() {
    val input = readInputLines("Day14")
    val initialRobots = parseInput(input)
    val grid = simulate(initialRobots, 100000, 103, 101)
}

private fun simulate(robots: List<Robot>, seconds: Int, tilesY: Int, tilesX: Int): Array<IntArray> {
    val grid = Array(tilesY) { IntArray(tilesX) { 0 } }

    for (robot in robots) {
        grid[robot.position.y][robot.position.x] = grid[robot.position.y][robot.position.x] + 1
    }

    for (second in 1..seconds) {
        for (robot in robots) {
            grid[robot.position.y][robot.position.x] = grid[robot.position.y][robot.position.x] - 1
            var newPositionY = (robot.position.y + robot.velocityY) % tilesY
            if (newPositionY < 0) {
                newPositionY += tilesY
            }
            var newPositionX = (robot.position.x + robot.velocityX) % tilesX
            if (newPositionX < 0) {
                newPositionX += tilesX
            }
            robot.position.x = newPositionX
            robot.position.y = newPositionY
            grid[robot.position.y][robot.position.x] = grid[robot.position.y][robot.position.x] + 1
        }

        if (grid.any { it.hasBorder() }) {
            println("After $second")
            for (row in grid) {
                for (col in row) {
                    print(if (col > 0) "#" else ".")
                }
                println()
            }
        }
    }

    return grid
}

private fun IntArray.hasBorder(): Boolean {
    var robotsInLine = 0
    for (i in this) {
        if (i > 0) {
            robotsInLine++
        } else {
            robotsInLine = 0
        }

        if (robotsInLine > 20) return true
    }

    return false
}

private fun getSafetyFactor(grid: Array<IntArray>): Int {
    val middleY = grid.size / 2
    val middleX = grid[0].size / 2

    var robotsInFirstQuadrant = 0
    var robotsInSecondQuadrant = 0
    var robotsInThirdQuadrant = 0
    var robotsInFourthQuadrant = 0

    for (y in grid.indices) {
        for (x in grid[y].indices) {
            val robotsHere = grid[y][x]
            when {
                y < middleY && x < middleX -> robotsInFirstQuadrant += robotsHere
                y > middleY && x < middleX -> robotsInThirdQuadrant += robotsHere
                y < middleY && x > middleX -> robotsInSecondQuadrant += robotsHere
                y > middleY && x > middleX -> robotsInFourthQuadrant += robotsHere
            }
        }
    }

    return robotsInFirstQuadrant * robotsInSecondQuadrant * robotsInThirdQuadrant * robotsInFourthQuadrant
}

private data class Robot(val position: Position, val velocityX: Int, val velocityY: Int)