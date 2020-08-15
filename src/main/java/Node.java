import java.util.ArrayList;
import java.util.List;

public class Node {

    private Board board;
    private List<Node> childNodes;
    private Node parentNode;
    private int depth;

    public Node(Board board, int depth) {
        this.board = board;
        this.childNodes = new ArrayList<>();
        this.parentNode = null;
        this.depth = depth;
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
    
    public int getHeuristicValue(String heuristic)
    {
        switch (heuristic)
        {
            case "MANHATTAN":
                return board.getManhattanDistances();
            default:
                return -1;
        }
    }
    
    public int getHeuristicAndDepthValue(String heuristic)
    {
    	int h = getHeuristicValue(heuristic);
    	if(h == -1)
    		return -1;
        return depth + getHeuristicValue(heuristic);
    }
}