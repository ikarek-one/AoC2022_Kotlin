package tasks.day12

object BreadthFirstSearch {

    fun <T> bfs(
        vertexes: List<T>, neighbors: Map<T, List<T>>, source: T
    ): Map<T, T> {
        val color = vertexes.associateWith { BfsGraphColor.WHITE }.toMutableMap()
        val distance = vertexes.associateWith { Int.MAX_VALUE }.toMutableMap()
        val parent = mutableMapOf<T, T>()

        color[source] = BfsGraphColor.GRAY
        distance[source] = 0

        val queue = ArrayDeque<T>()
        queue.addFirst(source)

        while (queue.isNotEmpty()) {
            val u = queue.removeFirst()
            for (v in neighbors[u]!!) {
                if (color[v] == BfsGraphColor.WHITE) {
                    color[v] = BfsGraphColor.GRAY
                    distance[v] = distance[u]!! + 1
                    parent[v] = u
                    queue.add(v)
                }
                color[u] = BfsGraphColor.BLACK
            }
        }

        return parent.toMap()
    }

    fun <T> getPath(source: T, destination: T, parentMap: Map<T, T>): List<T> {
        val nodes = mutableListOf<T>()
        var current = destination
        nodes.add(destination)

        while (current != source) {
            current = parentMap[current] ?: return nodes.reversed()
            nodes.add(current)
        }

        return nodes.reversed()
    }

    private enum class BfsGraphColor {
        WHITE, GRAY, BLACK
    }

}