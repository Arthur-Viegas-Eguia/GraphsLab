import java.util.List;
import java.util.ArrayList;

/**
 * Implements a graph using the adjacency matrix implementation with its basic
 * methods. 
 * The goal of this lab is to implement an unweighted graph and contrast
 * an adjacency matrix and adjacency list implementation.
 * Some of the implementation is provided for you and some were implemented
 * during the class
 * Developed for Data Structures class taught by Anya Vostinar in Carleton. 
 * It was developed as an in class lab.
 *
 */
public class AdjacencyMatrixGraphImplementation implements UnweightedGraph {
    //Instance variables - Make the comment below more specific about how
    //exactly adjacencyMatrix is organized.
    private List<List<Boolean>> adjacencyMatrix;//Adjacency matrix. Store edges here.
    private boolean directed;
    
    
    /**
     * Constructs an unweighted graph that is directed if
     * directed = true and undirected otherwise. 
     * @param directed
     */
    public AdjacencyMatrixGraphImplementation(boolean directed) {
        adjacencyMatrix = new ArrayList<List<Boolean>>();
        this.directed = directed;
    }

    /** Adds a new vertex.
     * @return the ID of the added vertex.
     */
    @Override
    public int addVertex() {
        List<Boolean> newRow = new ArrayList<Boolean>();
        for(List<Boolean> curList : adjacencyMatrix) {
            //Add one more false to each list for this new vertex
            curList.add(false);
            newRow.add(false);//add a false for the vertex represented by this list.
        }
        //Add one extra false for no self looping edge.
        newRow.add(false);

        //Add the new row to the matrix
        adjacencyMatrix.add(newRow);
        return adjacencyMatrix.size()-1;
    }


    /** Checks whether an edge exists between two vertices.
     * In an undirected graph, this returns the same as hasEdge(end, begin).
     * @return true if there is an edge from begin to end.
     */
    @Override
    public boolean hasEdge(int begin, int end) {
        if(begin+1 > adjacencyMatrix.size() || end+1 > adjacencyMatrix.get(0).size() || begin < 0 || end < 0){
            throw new IndexOutOfBoundsException();
        }
        return adjacencyMatrix.get(begin).get(end);
    }


    /** 
     * Returns the out-degree of the specified vertex. The
     * out-degree is the number of outgoing edges.
     */
    @Override
    public int getDegree(int v) {
        int counter = 0;
        for(int i = 0; i < adjacencyMatrix.size(); i++){
            if(adjacencyMatrix.get(v).get(i)){
                counter++;
            }
        }
        return counter;
    }



    /** 
     *Returns the in-degree of the specified vertex. The
     * in-degree is the number of incoming edges.
     */
    @Override
    public int getInDegree(int v) {
        throw new UnsupportedOperationException("getInDegree not required for lab");
    }


    /** Returns an iterable object that allows iteration over the neighbors of
     * the specified vertex.  In particular, the vertex u is included in the
     * sequence if and only if there is an edge from v to u in the graph.
     */
    @Override
    public Iterable<Integer> getNeighbors(int v) {
        if(v < 0 || v >= adjacencyMatrix.size()) {
            throw new IndexOutOfBoundsException();
        }
        List<Integer> neighbors = new ArrayList<Integer>();
        //Get the row in the matrix that corresponds to the outgoing edges
        List<Boolean> row = adjacencyMatrix.get(v);
        for(int i = 0; i < row.size(); i++) {
            if(row.get(i)) {//column index is a neighboring vertex
                neighbors.add(i);
            }
        }
        return neighbors;
    }



    /** Returns the number of vertices in the graph. */
    @Override
    public int numVerts() {
        return adjacencyMatrix.size();
    }



    /** Returns the number of edges in the graph.
     * The result does *not* double-count edges in undirected graphs.
     */
    @Override
    public int numEdges() {
        throw new UnsupportedOperationException("numEdges not required for lab");
    }



    /** Returns true if the graph is directed. */
    @Override
    public boolean isDirected() {
        return directed;
    }


    /** Returns true if there are no vertices in the graph. */
    @Override
    public boolean isEmpty() {
        return adjacencyMatrix.isEmpty();
    }


    /** Removes all vertices and edges from the graph. */
    @Override
    public void clear() {
        adjacencyMatrix = new ArrayList<List<Boolean>>();
    }


    /** Adds an unweighted edge between two vertices.
     * In an undirected graph, this has the same effect as addEdge(end, begin).
     * @return false if the edge was already in the graph.
     * @throws IndexOutOfBoundsException if either vertex ID is out of bounds.
     */
    @Override
    public boolean addEdge(int begin, int end) {
        if(begin+1 > adjacencyMatrix.size() || end+1 > adjacencyMatrix.get(0).size() || begin < 0 || end < 0){
            throw new IndexOutOfBoundsException();
        }
        if(hasEdge(begin, end)){
            return false;
        }
        adjacencyMatrix.get(begin).set(end, true);
        if(!directed){ 
            adjacencyMatrix.get(end).set(begin, true);
        }
        return true;
    }


    /**
     * Unit tests for the graph implementation.
     * This tests many of the things you might want to test, but not all of them.
     * If you have extra time, brainstorm some more tests and add them.
     * @param args
     */
    public static void main(String[] args) {
        //Tests undirected
        UnweightedGraph g = new AdjacencyMatrixGraphImplementation(false);
        boolean passedAllTests = true;
        //First, some tests to make sure a newly created graph is really empty. You could
        //add more tests here if you wanted.
        if(!g.isEmpty()) {
            System.err.println("Newly created graph is not empty");
            passedAllTests = false;
        }
        if(g.numVerts() != 0) {
            System.err.println("Expected 0 vertices for a new graph, found: " + g.numVerts());
            passedAllTests = false;
        }
        //Now add some vertices and edges and test how things went.
        int numVertices = 18;
        for(int i = 0; i < numVertices; i++) {
            int newVertex = g.addVertex();
            if(g.numVerts() != i+1) {
                System.err.println("Added " + (i+1) + " vertices, but graph says it has " + g.numVerts() + " vertices.");
                passedAllTests = false;
            }
            if(newVertex != i) {
                System.err.println("Added vertex that should have index " + i + " but had index " + newVertex + ".");
                passedAllTests = false;
            }
        }

        //Now let's add edges so that there's an edge from each even numbered vertex to every other
        //vertex, except the final vertex (so that one shouldn't have any edges).
        //We also don't include self loops (you might think through whether your
        //method would be correct with self loops.)
        for(int i = 0; i < numVertices-1; i+=2) {
            for(int j = 0; j < numVertices-1; j++) {
                if(i != j) {
                    g.addEdge(i,j);
                    if(!g.hasEdge(i,j)) {
                        System.err.println("Added edge (" + i + ", " + j + "), but hasEdge returned false.");
                    }
                    if(!g.hasEdge(j,i)) {//Undirected graph, so adding (i,j) should get us (j,i) too
                        System.err.println("Added edge (" + i + ", " + j + "), but hasEdge for opposite order returned false.");
                    }
                }
            }
        }
        //Go through the vertices and make sure edges, in degree, out degree, and neighbors are correct
        for(int i = 0; i < numVertices; i++) {
            int expectedNumNeighbors = 9;
            if(i % 2 == 0) {
                expectedNumNeighbors = 16;
            } else if(i == 17) {
                expectedNumNeighbors = 0;
            }
            //Checking that the neighbor iterator is correct
            int numNeighborsFound = 0;
            Iterable<Integer> neighbors = g.getNeighbors(i);
            for(int v : neighbors) {
                numNeighborsFound++;
                if((i % 2 == 1 && v % 2 == 1) || i == 17 || v == 17) {
                    //Shouldn't have an edge between two odd vertices or any edges involving final vertex!
                    System.err.println("Found an unexpected edge between " + i + " and " + v);
                    passedAllTests = false;
                }   
            }
            if(numNeighborsFound != expectedNumNeighbors) {
                System.err.println("vertex: " + i + "; expected " + expectedNumNeighbors + " neighbors, but found " + numNeighborsFound);
                passedAllTests = false;
            }
            //Now check in and out degree. Since we're not directed, they should both equal expected num neighbors.
            if(g.getDegree(i) != expectedNumNeighbors) {
                System.err.println("Expected degree " + expectedNumNeighbors + " but found degree " + g.getDegree(i));
                passedAllTests = false;
            }
        }

        //Check that calling with negative or too large numbers results in IndexOutOfBoundsException
        try {
            g.getDegree(150);
            System.err.println("Called getDegree with too large of a vertex and no exception was thrown.");
            passedAllTests = false;
        } catch(IndexOutOfBoundsException e) {
            ;
        }

        try {
            g.getDegree(-5);
            System.err.println("Called getDegree with too small of a vertex and no exception was thrown.");
            passedAllTests = false;
        } catch(IndexOutOfBoundsException e) {
            ;
        }
        try {
            g.getNeighbors(150);
            System.err.println("Called getNeighbors with too large of a vertex and no exception was thrown.");
            passedAllTests = false;
        } catch(IndexOutOfBoundsException e) {
            ;
        }
        try {
            g.getNeighbors(-5);
            System.err.println("Called getNeighbors with too small of a vertex and no exception was thrown.");
            passedAllTests = false;
        } catch(IndexOutOfBoundsException e) {
            ;
        }
        System.out.println("All tests passed: " + passedAllTests);


    }


}
