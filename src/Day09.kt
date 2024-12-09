fun main() {
    part1TestInput()
    part1()
    part2TestInput()
    part2()
}

private fun part1TestInput() {
    val testInput = "2333133121414131402"

    val expanded = expand(testInput)
    val optimized = optimize(expanded)
    val checksum = checksum(optimized)

    println("Part1 Test: $checksum")
}

private fun part1() {
    val input = readInput("Day09")

    val expanded = expand(input)
    val optimized = optimize(expanded)
    val checksum = checksum(optimized)

    println("Part1: $checksum")
}

private fun part2TestInput() {
    val testInput = "2333133121414131402"

    val expanded = expand(testInput)
    val optimized = optimizePart2(expanded)
    val checksum = checksum(optimized)

    println("Part2 Test: $checksum")
}

private fun part2() {
    val input = readInput("Day09")

    val expanded = expand(input)
    val optimized = optimizePart2(expanded)
    val checksum = checksum(optimized)

    println("Part2: $checksum")
}

private fun expand(diskMap: String): List<DiskBlock> {
    var id = 0
    val diskBlocks = mutableListOf<DiskBlock>()

    diskMap.forEachIndexed { index, c ->
        val amount = c.digitToInt()
        if (index % 2 == 0) {
            (0 until amount).forEach {
                diskBlocks.add(DiskBlock.File(id))
            }

            id++
        } else {
            (0 until amount).forEach {
                diskBlocks.add(DiskBlock.FreeSpace)
            }
        }
    }

    return diskBlocks
}

private fun optimize(diskBlocks: List<DiskBlock>): List<DiskBlock> {
    val newDiskBlocks = diskBlocks.toTypedArray()
    val amountOfFreeSpace = diskBlocks.count { it == DiskBlock.FreeSpace }

    val filesToOptimizes = diskBlocks.filterIsInstance<DiskBlock.File>().reversed().take(amountOfFreeSpace)

    for (file in filesToOptimizes) {
        val firstFreeSpace = newDiskBlocks.indexOfFirst { it == DiskBlock.FreeSpace }
        newDiskBlocks[firstFreeSpace] = file

        val lastIndexThatIsAFile = newDiskBlocks.lastIndexOf(file)
        newDiskBlocks[lastIndexThatIsAFile] = DiskBlock.FreeSpace
    }

    return newDiskBlocks.toList()
}

private fun optimizePart2(diskBlocks: List<DiskBlock>): List<DiskBlock> {
    val newDiskBlocks = diskBlocks.toTypedArray()

    val fileGroupsToOptimizes = diskBlocks.filterIsInstance<DiskBlock.File>().groupBy { it.id }

    for (fileGroup in fileGroupsToOptimizes.entries.sortedBy { it.key }.reversed()) {
        val amount = fileGroup.value.size

        var firstFreeIndex = -1
        for (index in diskBlocks.indices) {
            var fits = true
            for (i in 0 until amount) {
                if (newDiskBlocks.getOrNull(i + index) !is DiskBlock.FreeSpace) {
                    fits = false
                }
            }
            if (fits) {
                firstFreeIndex = index
                break
            }
        }

        if (firstFreeIndex != -1) {
            for (i in 0 until amount) {
                newDiskBlocks[i + firstFreeIndex] = fileGroup.value[i]

                val lastIndexThatIsAFile = newDiskBlocks.lastIndexOf(fileGroup.value[i])
                newDiskBlocks[lastIndexThatIsAFile] = DiskBlock.FreeSpace
            }
        }
    }

    return newDiskBlocks.toList()
}

private fun checksum(diskBlocks: List<DiskBlock>): Long {
    return diskBlocks.foldIndexed(initial = 0L) { index, acc, diskBlock ->
        acc + if (diskBlock is DiskBlock.File) index * diskBlock.id else 0
    }
}

private sealed interface DiskBlock {

    val representation: String

    data class File(val id: Int) : DiskBlock {
        override val representation: String = id.toString()
    }

    data object FreeSpace : DiskBlock {
        override val representation: String = "."
    }
}