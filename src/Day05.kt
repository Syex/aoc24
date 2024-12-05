fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun parseInput(testInput: List<String>): Prints {
    val rules = mutableListOf<Pair<Int, Int>>()
    val pages = mutableListOf<List<Int>>()

    testInput.forEach { line ->
        if (line.isNotBlank()) {
            if (line.split("|").size == 2) {
                line.split("|").let {
                    rules.add(it[0].toInt() to it[1].toInt())
                }
            } else if (line.split(",").isNotEmpty()) {
                pages.add(line.split(",").map { it.toInt() })
            }
        }
    }

    return Prints(rules, pages)
}

private fun part1TestInput() {
    val testInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
        
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()

    val prints = parseInput(testInput)
    val validUpdates = getValidUpdates(prints)
    val sumOfMiddlePages = validUpdates.sumOf { it[it.size / 2] }

    println("Part1 Test: $sumOfMiddlePages")
}

private fun part1() {
    val input = readInputLines("Day05")

    val prints = parseInput(input)
    val validUpdates = getValidUpdates(prints)
    val sumOfMiddlePages = validUpdates.sumOf { it[it.size / 2] }

    println("Part1: $sumOfMiddlePages")
}

private fun getValidUpdates(prints: Prints): List<List<Int>> {
    return prints.pagesToProduce.filter { updates ->
        updates.zipWithNext().all { pair ->
            pair in prints.orderRules
        }
    }
}


private fun part2TestInput() {
    val testInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
        
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()

    val prints = parseInput(testInput)
    val invalidUpdates = prints.pagesToProduce - getValidUpdates(prints)
    val orderedInvalidUpdates = invalidUpdates.map { bubbleSort(it.toIntArray(), prints.orderRules) }

    println("Part2 Test: $orderedInvalidUpdates")
}

private fun part2() {
    val input = readInputLines("Day05")

    val prints = parseInput(input)
    val invalidUpdates = prints.pagesToProduce - getValidUpdates(prints)
    val orderedInvalidUpdates = invalidUpdates.map { bubbleSort(it.toIntArray(), prints.orderRules) }
    val sumOfMiddlePages = orderedInvalidUpdates.sumOf { it[it.size / 2] }

    println("Part2: $sumOfMiddlePages")
}

private fun bubbleSort(arr: IntArray, rules: List<Pair<Int, Int>>): List<Int> {
    val n = arr.size

    for (i in 0 until n - 1) {
        for (j in 0 until n - i - 1) {
            if (arr[j + 1] to arr[j] in rules) {
                // Swap the elements
                val temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
            }
        }
    }

    return arr.toList()
}

private data class Prints(
    val orderRules: List<Pair<Int, Int>>,
    val pagesToProduce: List<List<Int>>,
)
