import kotlin.math.absoluteValue

fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: List<String>) = testInput.map {
    val numbers = it.trim().split("   ")
    numbers[0].toInt() to numbers[1].toInt()
}.unzip()

private fun part1TestInput() {
    val testInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent().lines()

    val (firstList, secondList) = parseInput(testInput)

    val distance = getTotalDistance(firstList, secondList)
    println("Distance of test input: $distance")

    check(distance == 11)
}

private fun part1() {
    val input = readInput("Day01")

    val (firstList, secondList) = parseInput(input)

    val distance = getTotalDistance(firstList, secondList)
    println("Distance of part1 input: $distance")
}

private fun getTotalDistance(firstList: List<Int>, secondList: List<Int>): Int {
    val sortedFirstList = firstList.sorted()
    val sortedSecondList = secondList.sorted()

    return sortedFirstList.mapIndexed { index, i ->
        (i - sortedSecondList[index]).absoluteValue
    }.sum()
}

private fun part2TestInput() {
    val testInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent().lines()

    val (firstList, secondList) = parseInput(testInput)

    val similarityScore = getSimilarityScore(firstList, secondList)
    println("Similarity score of test input: $similarityScore")

    check(similarityScore == 31)
}

private fun part2() {
    val input = readInput("Day01")

    val (firstList, secondList) = parseInput(input)

    val similarityScore = getSimilarityScore(firstList, secondList)
    println("Similarity score of part2 input: $similarityScore")
}

private fun getSimilarityScore(firstList: List<Int>, secondList: List<Int>): Int {
    return firstList.fold(0) { acc, i -> acc + (i * secondList.count { it == i }) }
}
