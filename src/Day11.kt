import java.math.BigInteger

fun main() {
    part1TestInput()
    part1()
    part2()
}

private fun parseInput(testInput: String) = testInput.split(" ").map { it.toLong() }

private fun part1TestInput() {
    val testInput = "125 17".trimIndent()
    var stones = parseInput(testInput)

    for (i in 0 until 25) {
        stones = blink(stones)
    }

    println("Part1 Test: ${stones.size}")
}

private fun part1() {
    val input = readInput("Day11")
    var stones = parseInput(input)

    for (i in 0 until 25) {
        stones = blink(stones)
    }

    println("Part1: ${stones.size}")
}

private fun part2() {
    val input = readInput("Day11")
    val stones = parseInput(input)
    val size = blink2(stones, 75)

    println("Part2: $size")
}

private fun blink(stones: List<Long>): List<Long> {
    val newStones = mutableListOf<Long>()

    for (stone in stones) {
        when {
            stone == 0L -> {
                newStones.add(1)
            }

            stone.toString().length % 2 == 0 -> {
                val numberAsString = stone.toString()
                val firstHalf = numberAsString.substring(0, numberAsString.length / 2).toLong()
                val secondHalf = numberAsString.substring(numberAsString.length / 2).toLong()

                newStones.add(firstHalf)
                newStones.add(secondHalf)
            }

            else -> {
                newStones.add(stone * 2024)
            }
        }
    }

    return newStones
}

private val cache = mutableMapOf<BlinkMemo, BigInteger>()

private data class BlinkMemo(val stone: Long, val blinksLeft: Int)

private fun blink2(stones: List<Long>, times: Int): BigInteger {
    if (times == 0) return BigInteger.valueOf(stones.size.toLong())

    return stones
        .map { stone ->
            val blinkMemo = cache[BlinkMemo(stone, times)]
            if (blinkMemo != null) {
                blinkMemo
            } else {
                when {
                    stone == 0L -> {
                        blink2(listOf(1L), times - 1).also {
                            cache[BlinkMemo(stone, times)] = it
                        }
                    }

                    stone.toString().length % 2 == 0 -> {
                        val numberAsString = stone.toString()
                        val firstHalf = numberAsString.substring(0, numberAsString.length / 2).toLong()
                        val secondHalf = numberAsString.substring(numberAsString.length / 2).toLong()

                        blink2(listOf(firstHalf, secondHalf), times - 1).also {
                            cache[BlinkMemo(stone, times)] = it
                        }
                    }

                    else -> {
                        blink2(listOf(stone * 2024), times - 1).also {
                            cache[BlinkMemo(stone, times)] = it
                        }
                    }
                }
            }
        }.sumOf { it }
}
