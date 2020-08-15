import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ArtificialIntelligence {

    private String algorithm;
    private int maxAllowedDepth;
    private String heuristic;
    private static int DEFAULT_DEPTH_LIMIT = 3000;
    private static String DEFAULT_HEURISTIC = "MANHATTAN";

    public ArtificialIntelligence(String algorithm) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = DEFAULT_DEPTH_LIMIT;
        this.heuristic = DEFAULT_HEURISTIC;
    }
    
    public ArtificialIntelligence(String algorithm, String heuristicChosen) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = DEFAULT_DEPTH_LIMIT;
        this.heuristic = heuristicChosen;
    }

    public void solve(Board initialBoard) {
        Node root = new Node(initialBoard, 0);
        Node solution = null;
        long startTime = System.currentTimeMillis();
        // TODO: Check the heuristic exists
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
            case "GGS":
                solution = solveGlobalGreedySearch(root);
                break;
            case "A*":
                solution = solveAStarSearch(root);
                break;
            default:
            	System.out.println("Search algorithm unknown! Quitting");
            	return;
        }
    	if(solution != null)
    	{
    		long endTime = System.currentTimeMillis();
    		printSolution(solution, endTime - startTime);
    	}
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
    	Stack<Integer> checkedBoards = new Stack<>();
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
    
    private Node solveIDDFSRecursively(Node currentNode, Stack<Integer> checkedBoards, int depth) {
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        if(depth >= maxAllowedDepth || checkedBoards.contains(currentNode.getBoard().hashCode()) || currentNode.getBoard().isDeadlock())
        {
            return null;
        }
        checkedBoards.push(currentNode.getBoard().hashCode());
        
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
        Queue<Node> queue = new LinkedList<>();
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
    
    private Node solveGlobalGreedySearch(Node root) {
        Node solution = null;
        PriorityQueue<Node> queue = new PriorityQueue<>((Node n1, Node n2) -> n1.getHeuristicValue(heuristic) - n2.getHeuristicValue(heuristic));
        Set<Integer> checkedBoards = new HashSet<>();
        System.out.println("\nRunning solver with Global Greedy Search...");
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
                if(currentNode.getBoard().isCompleted()){
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                    List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
                    Node possibleChildNode;
                    for(Board board : possibleChildren) {
                        possibleChildNode = new Node(board, currentNode.getDepth() + 1);
                        possibleChildNode.setParentNode(currentNode);
                        queue.add(possibleChildNode);
                    }
                }
            }
        }
        return solution;
    }
    
    private Node solveAStarSearch(Node root) {
        Node solution = null;
        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
															@Override public int compare(Node n1, Node n2) {
																int diff = n1.getHeuristicAndDepthValue(heuristic) - n2.getHeuristicAndDepthValue(heuristic);
																if(diff == 0)
																	return n1.getHeuristicValue(heuristic) - n2.getHeuristicValue(heuristic);
																return diff;
															}});
        Set<Integer> checkedBoards = new HashSet<>();
        System.out.println("\nRunning solver with A* Search...");
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
                if(currentNode.getBoard().isCompleted()){
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                    List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
                    Node possibleChildNode;
                    for(Board board : possibleChildren) {
                        possibleChildNode = new Node(board, currentNode.getDepth() + 1);
                        possibleChildNode.setParentNode(currentNode);
                        queue.add(possibleChildNode);
                    }
                }
            }
        }
        return solution;
    }
    
    private void printSolution(Node solution, long elapsedTime)
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
		if(elapsedTime > 60*1000)
			System.out.println("Time elapsed to process: " + (elapsedTime / 1000.0) / 60 + " minutes");
		else
			System.out.println("Time elapsed to process: " + elapsedTime / 1000.0 + " seconds");
    }

}
