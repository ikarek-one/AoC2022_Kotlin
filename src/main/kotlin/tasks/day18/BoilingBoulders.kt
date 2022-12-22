package tasks.day18

import base.Task


class BoilingBoulders : Task {
    override val id: Int
        get() = 18

    override fun part1(lines: Sequence<String>): Long {
        val cubes = lines
            .filter { it.isNotBlank() }
            .map { parsePoint(it) }
            .toList()

        return cubes
            .sumOf { it.outsideWalls(cubes) }.toLong()
    }

    override fun part2(lines: Sequence<String>): Long {
        val cubes = lines
            .filter { it.isNotBlank() }
            .map { parsePoint(it) }
            .toSet()

        val xRange = cubes.minOf { it.x }..cubes.maxOf { it.x }
        val yRange = cubes.minOf { it.y }..cubes.maxOf { it.y }
        val zRange = cubes.minOf { it.z }..cubes.maxOf { it.z }

        val continuum = mutableSetOf<Point3D>()

        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    continuum.add(Point3D(x, y, z))
                }
            }
        }

        val inClosedSpace = continuum
            .filter { isInClosedSpace(it, cubes, xRange, yRange, zRange) }


        val filtered = inClosedSpace.filter { point3D ->
            val neighbors = point3D.neighbours()
            neighbors.all { cubes.contains(it) || inClosedSpace.contains(it) }
        }

        val insideSpace = filtered.plus(cubes).toSet()

        return cubes
            .sumOf { it.outsideWalls(insideSpace) }.toLong()
    }


    private fun isInClosedSpace(
        p: Point3D,
        cubes: Collection<Point3D>,
        xRange: IntRange,
        yRange: IntRange,
        zRange: IntRange
    ): Boolean {
        if (cubes.contains(p)) {
            return false
        }

        var notFound = true

        for (x in p.x downTo xRange.first) {
            if (cubes.contains(Point3D(x, p.y, p.z))) {
                notFound = false
                break
            }
        }
        if (notFound) return false
        notFound = true
        for (x in p.x..xRange.last) {
            if (cubes.contains(Point3D(x, p.y, p.z))) {
                notFound = false
                break
            }
        }
        if (notFound) return false
        notFound = true

        for (y in p.y downTo yRange.first) {
            if (cubes.contains(Point3D(p.x, y, p.z))) {
                notFound = false
                break
            }
        }
        if (notFound) return false
        notFound = true
        for (y in p.y..yRange.last) {
            if (cubes.contains(Point3D(p.x, y, p.z))) {
                notFound = false
                break
            }
        }
        if (notFound) return false
        notFound = true


        for (z in p.z downTo zRange.first) {
            if (cubes.contains(Point3D(p.x, p.y, z))) {
                notFound = false
                break
            }
        }
        if (notFound) return false
        notFound = true
        for (z in p.z..zRange.last) {
            if (cubes.contains(Point3D(p.x, p.y, z))) {
                notFound = false
                break
            }
        }
        return !notFound
    }

    private fun parsePoint(line: String): Point3D {
        val (x, y, z) = line.split(",").map { it.toInt() }
        return Point3D(x, y, z)
    }


    private data class Point3D(val x: Int, val y: Int, val z: Int) {
        fun outsideWalls(pointCollection: Collection<Point3D>) =
            numberOfWalls - countNeighbours(pointCollection)

        fun countNeighbours(pointCollection: Collection<Point3D>): Int {
            return neighbours()
                .count { pointCollection.contains(it) }
        }

        fun neighbours() = neighbourFactors
            .map { Point3D(it.x + this.x, it.y + this.y, it.z + this.z) }

        companion object {
            const val numberOfWalls = 6

            val neighbourFactors = listOf(
                Point3D(1, 0, 0),
                Point3D(-1, 0, 0),
                Point3D(0, 1, 0),
                Point3D(0, -1, 0),
                Point3D(0, 0, 1),
                Point3D(0, 0, -1),
            )
        }
    }

}