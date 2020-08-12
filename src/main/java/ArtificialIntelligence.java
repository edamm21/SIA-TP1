import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class ArtificialIntelligence {

    private String algorithm;
    private int maxAllowedDepth;

    public ArtificialIntelligence(String algorithm, int maxAllowedDepth) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = maxAllowedDepth;
    }

    public void solve(Board initialBoard) {
        Node root = new Node(initialBoard, 0);
        Node solution = null;
        switch (this.algorithm) {
            case "DFS":
                solution = solveDFS(root);
                break;
            case "BFS":
            	solution = solveBFS(root);
            	break;
            case "IDDFS":
            	solution = solveIDDFS(root);
            	break;
        }
    	if(solution != null)
    		printSolution(solution);
    	else
    		System.out.println("Couldn't find a solution!");
    }

    private Node solveDFS(Node root) {
    	Set<Integer> checkedBoards = new HashSet<>();
        System.out.println("\nRunning solver with DFS...");
        return solveDFSRecursively(root, checkedBoards, 0);
    }

    private Node solveDFSRecursively(Node currentNode, Set<Integer> checkedBoards, int depth) {
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        if(depth >= maxAllowedDepth)
            return null;
        if(checkedBoards.contains(currentNode.getBoard().hashCode()))
        	return null;
        checkedBoards.add(currentNode.getBoard().hashCode());
        
        if(currentNode.getBoard().isDeadlock())
            return null;
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveDFSRecursively(possibleChildNode, checkedBoards, depth + 1);
            if(possibleChildNode != null)
            	return possibleChildNode;
        }
        return null;
    }

    private Node solveIDDFS(Node root)
    {
    	Stack<Board> checkedBoards = new Stack<>();
        int maxDepth = maxAllowedDepth;
        System.out.println("\nRunning solver with IDDFS...");
        Node solution = null;
        maxAllowedDepth = 1;
        while(solution == null && maxAllowedDepth <= maxDepth)
        {
        	System.out.println("Trying depth " +maxAllowedDepth);
        	solution = solveIDDFSRecursively(root, checkedBoards, 0);
        	maxAllowedDepth += 10;
        	checkedBoards.clear();
        }
        maxAllowedDepth = maxDepth;
        return solution;
    }
    
    private Node solveIDDFSRecursively(Node currentNode, Stack<Board> checkedBoards, int depth) {
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        if(depth >= maxAllowedDepth)
        {
            return null;
        }
        if(checkedBoards.contains(currentNode.getBoard()))
        {
        	return null;
        }
        checkedBoards.push(currentNode.getBoard());
        
        if(currentNode.getBoard().isDeadlock())
        {
            return null;
        }
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveIDDFSRecursively(possibleChildNode, checkedBoards, depth + 1);
            if(possibleChildNode != null)
            	return possibleChildNode;
        }
        checkedBoards.pop();
        return null;
    }
    
    private Node solveBFS(Node root)
    {
        Node solution = null;
        Queue<Node> queue = new LinkedBlockingQueue<>();
        Set<Integer> checkedBoards = new HashSet<>();
        System.out.println("\nRunning solver with BFS...");
        
        Node currentNode;
        queue.add(root);
        while(!queue.isEmpty())
        {
        	currentNode = queue.poll();
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
        	if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
        	{
        		checkedBoards.add(currentNode.getBoard().hashCode());
            	if(currentNode.getBoard().isCompleted())
            	{
            		solution = currentNode;
            		break;
            	}
            	else if(!currentNode.getBoard().isDeadlock())
            	{
            		List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
            		Node possibleChildNode;
                    for(Board board : possibleChildren)
                    {
                    	possibleChildNode = new Node(board, currentNode.getDepth() + 1);
                    	possibleChildNode.setParentNode(currentNode);
                    	queue.add(possibleChildNode);
                    }
            	}
        	}
        }
        return solution;
    }
    
    private void printSolution(Node solution)
    {
		Stack<Node> path = new Stack<>();
		Node n = solution;
		while(n.getParentNode() != null)
		{
			path.push(n);
			n = n.getParentNode();
		}
		path.push(n);
		
		while(!path.isEmpty())
		{
			n = path.pop();
			System.out.println("\nMOVE " +n.getDepth() +":");
			n.getBoard().printBoard();
		}
    }

}
