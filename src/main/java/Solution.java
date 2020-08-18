import java.util.ArrayList;
import java.util.List;

public class Solution {
	private List<Node> moves;
	private long elapsedTime;
    private int nodesExpanded;
    private int frontierSize;
    private Algorithm algorithm;
    private Heuristic heuristic;
    private boolean solved;
    
    public Solution(List<Node> moves, long time, int nodesExpanded, int frontierSize, Algorithm algorithm, Heuristic heuristic, boolean solved)
    {
    	if(moves == null)
    		this.moves = new ArrayList<>();
    	else
    		this.moves = moves;
    	this.elapsedTime = time;
    	this.nodesExpanded = nodesExpanded;
    	this.frontierSize = frontierSize;
    	this.solved = solved;
    	this.algorithm = algorithm;
    	switch(algorithm)
    	{
	        case DFS:
	        	this.heuristic = null;
	            break;
	        case BFS:
	        	this.heuristic = null;
	        	break;
	        case IDDFS:
	        	this.heuristic = null;
	        	break;
	        default:
	        	this.heuristic = heuristic;
	        	break;
    	}
    }

	public List<Node> getMoves() {
		return moves;
	}

	public void setMoves(List<Node> moves) {
		this.moves = moves;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public int getNodesExpanded() {
		return nodesExpanded;
	}

	public void setNodesExpanded(int nodesExpanded) {
		this.nodesExpanded = nodesExpanded;
	}

	public int getFrontierSize() {
		return frontierSize;
	}

	public void setFrontierSize(int frontierSize) {
		this.frontierSize = frontierSize;
	}

	public Heuristic getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public boolean isSolved() {
		return solved;
	}
}
