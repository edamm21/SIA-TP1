import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable{

    private Board board;
    private List<Node> childNodes;
    private Node parentNode;
    private int depth;
    private int heuristicValue;
    private String heuristicChosen;

    public Node(Board board, int depth, String heuristicChosen) {
        this.board = board;
        this.childNodes = new ArrayList<>();
        this.parentNode = null;
        this.depth = depth;
        this.heuristicChosen = heuristicChosen;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void addChild(Node child) {
        this.childNodes.add(child);
        child.setParentNode(this);
    }

    public int getHeuristicValue() {
        switch (heuristicChosen) {
            case "MANHATTAN":
                return board.getManhattanDistance();
            default:
                return -1;
        }
    }

    // For using SortedSet<Node>
    @Override
    public int compareTo(Object other) {
        Node otherNode = (Node)other;
        Integer thisHeuristic = this.getHeuristicValue() + this.getDepth();
        Integer otherHeuristic = otherNode.getHeuristicValue() + otherNode.getDepth();
        return otherHeuristic.compareTo(thisHeuristic);
    }
}

