fun main() {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))
    var graph = Graph()
    graph.addVertex(1);
    graph.addVertex(2);
    graph.addVertex(3);
    graph.addVertex(4);
    graph.addVertex(5);
    graph.addEdge(1, 2);
    graph.addEdge(2, 5);
    graph.addEdge(1, 3);
    graph.addEdge(2, 4);
    graph.print()

    println("bfs: ")
    println(graph.bfs(1))


    println("Матрица смежности:")
    val matrix = GetAdjacencyMatrix(mutableListOf())
    matrix.verticies.addAll(graph.verticies)
    matrix.edges.addAll(graph.edges)
    matrix.cnt = graph.cnt

    val matr = matrix.getAdjacencyMatrixFun()
    matr.forEach { m ->
        println(m.joinToString(" "))
    }

    println("матрица вершин:")
    val lists = GetListsVertexs(mutableListOf())
    lists.verticies.addAll(graph.verticies)
    lists.edges.addAll(graph.edges)
    lists.cnt = graph.cnt

    val lst = lists.getListsVertexsFun()
    lst.forEachIndexed { index, l ->
        println("${l.joinToString(", ")}")
    }


    println("список ребер:")
    val edges = GraphListEdge()
    edges.verticies.addAll(graph.verticies)
    edges.edges.addAll(graph.edges)
    edges.getListEdgeFun()
}