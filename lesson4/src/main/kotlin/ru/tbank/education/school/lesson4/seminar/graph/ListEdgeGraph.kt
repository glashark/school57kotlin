import java.util.ArrayList
import java.util.HashSet


open class Graph(): InterfaceGraph {
    public var edges: MutableList<Pair<Int, Int>> = ArrayList<Pair<Int,Int>>()
    public var verticies: MutableSet<Int> = HashSet<Int>()

    public var cnt : Int = 0
    override fun addVertex(vertex: Int) {
        verticies.add(vertex)
        cnt++
    }

    override fun addEdge(start: Int, finish: Int) {
        if (!verticies.contains(start) || !verticies.contains(finish)) {
            throw IllegalArgumentException("Cannot add this edge because some of verticies does not exist!")
        }
        edges.add(Pair<Int, Int>(start, finish))
    }

    override fun bfs(vertex: Int): List<Pair<Int, Int>> {
        val qu = ArrayDeque<Int>()
        val visited: MutableMap<Int, Int> = HashMap<Int, Int>( )
        for (vertex in verticies) {
            visited[vertex] = 0
        }
        qu.addLast(vertex)
        visited[vertex] = 1
        val result: MutableMap<Int, Int> = HashMap<Int, Int>()

        result.put(vertex, 0)
        while (qu.isNotEmpty()) {
            val vertex = qu.removeFirst()
            for(edge in edges) {
                if (edge.first == vertex || edge.second == vertex) {
                    if (visited[edge.first] == 0) {
                        qu.add(edge.first)
                        visited[edge.first] = 1
                        result.put(edge.first, result[vertex]?.plus(1) ?: -1)
                    }
                    if (visited[edge.second] == 0) {
                        qu.add(edge.second)
                        visited[edge.second] = 1
                        result.put(edge.second, result[vertex]?.plus(1) ?: -1)
                    }
                }
            }
        }

        return result.toList()
    }

    fun print() {
        println("список вершин:")
        println(verticies.joinToString(" "))
    }
}

open class GetAdjacencyMatrix(val getadjmatrix : MutableList<MutableList<Int>>): Graph() {
    // матрица смежности
    fun getAdjacencyMatrixFun(): MutableList<MutableList<Int>> {
        val getadjmatrix = MutableList(cnt) { MutableList(cnt) { 0 } }
        for (v : Pair<Int, Int> in edges) {
            getadjmatrix[v.first - 1][v.second - 1] = 1
            getadjmatrix[v.second - 1][v.first - 1] = 1
        }
        return getadjmatrix
    }
}

open class GetListsVertexs(val getlistvert : MutableList<MutableList<Int>>): Graph() {
    // матрица вершин
    fun getListsVertexsFun(): MutableList<MutableList<Int>> {
        val getlistvert = MutableList(cnt) { mutableListOf<Int>()}
        for (v : Pair<Int, Int> in edges) {
            getlistvert[v.first - 1].add(v.second)
            getlistvert[v.second - 1].add(v.first)
        }
        return getlistvert
    }
}

open class GraphListEdge(): Graph() {
    // список ребер
    fun getListEdgeFun(): String {
        for (edge in edges) {
            println("(${edge.first}, ${edge.second})")
        }
        return ""
    }
}