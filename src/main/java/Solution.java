import java.util.List;

public class Solution {
	private List<Node> moves;
	private long elapsedTime;
    private int nodesExpanded;
    private int frontierSize;
    private String algorithm;
    private String heuristic;
    
    public Solution(List<Node> moves, long time, int nodesExpanded, int frontierSize, String algorithm, String heuristic)
    {
    	this.moves = moves;
    	this.elapsedTime = time;
    	this.nodesExpanded = nodesExpanded;
    	this.frontierSize = frontierSize;
    	this.algorithm = algorithm;
    	this.heuristic = heuristic;
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

	public String getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(String heuristic) {
		this.heuristic = heuristic;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
}
