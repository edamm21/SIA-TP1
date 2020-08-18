public class Node {

    private Board board;
    private Node parentNode;
    private int depth;

    public Node(Board board, int depth) {
        this.board = board;
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

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
    
	public boolean isLoop()
	{
		Node n = this;
		while(n.getParentNode() != null)
		{
			n = n.getParentNode();
			if(n.getBoard().hashCode() == this.board.hashCode())
				return true;
		}
		return false;
	}
    
    public int getHeuristicValue(Heuristic heuristic)
    {
        switch (heuristic)
        {
            case CLOSEST_GOAL:
                return board.getManhattanDistances();
            case CLOSEST_BOX:
            	return board.getPlayerClosestBoxDistance();
            case BOXES_REMAINING:
            	return board.getRemainingBoxes();
            default:
                return -1;
        }
    }
    
    public int getHeuristicAndDepthValue(Heuristic heuristic)
    {
    	int h = getHeuristicValue(heuristic);
    	if(h == -1)
    		return -1;
        return depth + getHeuristicValue(heuristic);
    }
}