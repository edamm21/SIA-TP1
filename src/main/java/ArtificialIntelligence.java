import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ArtificialIntelligence {
    private String algorithm;
    private String heuristic;
    private int maxAllowedDepth;
    private int nodesExpanded = 0;
    private int frontierNodes = 0;
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
    
    public ArtificialIntelligence(String algorithm, int depth) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = depth;
        this.heuristic = DEFAULT_HEURISTIC;
    }
    
    public ArtificialIntelligence(String algorithm, int depth, String heuristicChosen) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = depth;
        this.heuristic = heuristicChosen;
    }

    public Solution solve(Board initialBoard) throws Exception {
        Node root = new Node(initialBoard, 0);
        Node solution = null;
        long startTime = System.currentTimeMillis();
        nodesExpanded = 0;
        frontierNodes = 0;
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
            case "IDA*":
                solution = solveIDAStarSearch(root);
                break;
            default:
            	System.out.println("Search algorithm unknown! Quitting");
            	return null;
        }
    	if(solution != null)
    	{
    		long endTime = System.currentTimeMillis();
    		long elapsedTime = endTime - startTime;
            List<Node> path = new ArrayList<>();
            Node n = solution;
            while(n.getParentNode() != null)
            {
                path.add(0, n);
                n = n.getParentNode();
            }
            path.add(0, n);
            Solution s = new Solution(path, elapsedTime, nodesExpanded, frontierNodes, algorithm, heuristic);
            printSolution(s);
            return s;
    	}
    	else
    	{
    		System.out.println("Couldn't find a solution!");
    		return null;
    	}
    }

    private Node solveDFS(Node root) {
        System.out.println("\nRunning solver with DFS...");
        Set<Integer> checkedBoards = new HashSet<>();
        return solveDFSRecursively(root, checkedBoards, 0);
    }

    private Node solveDFSRecursively(Node currentNode, Set<Integer> checkedBoards, int depth) {
        if(frontierNodes > 0)
            frontierNodes--;
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
        frontierNodes += boardsToEvaluate.size();
        nodesExpanded++;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveDFSRecursively(possibleChildNode, checkedBoards, depth + 1);
            if(possibleChildNode != null) {
            	return possibleChildNode;
            }
        }
        return null;
    }
 
    private Node solveIDDFS(Node root)
    {
    	List<Set<Integer>> checkedBoards = new ArrayList<Set<Integer>>();
    	for(int i=0; i <= maxAllowedDepth; i++)
    	{
    		checkedBoards.add(new HashSet<Integer>());
    	}
    	Queue<Node> currentRoots = new LinkedList<>();
    	Queue<Node> pendingNodes = new LinkedList<>();
        int depthLimit = maxAllowedDepth;
        System.out.println("\nRunning solver with IDDFS...");
        Node solution = null;
        Node aux = null;
        maxAllowedDepth = 1;
        currentRoots.add(root);
        while(solution == null && !currentRoots.isEmpty() && maxAllowedDepth <= depthLimit)
        {
        	aux = currentRoots.poll();
        	solution = solveIDDFSRecursively(aux, checkedBoards, pendingNodes, aux.getDepth());
        	if(currentRoots.isEmpty()) {
        		currentRoots.addAll(pendingNodes);
        		pendingNodes.clear();
        		maxAllowedDepth += 10;
        	}
        }
        maxAllowedDepth = depthLimit;
        return solution;
    }
    
    private Node solveIDDFSRecursively(Node currentNode, List<Set<Integer>> checkedBoards, Queue<Node> pendingNodes, int depth)
    {
        if(frontierNodes > 0)
            frontierNodes--;
    	if(depth > maxAllowedDepth)
        {
        	pendingNodes.add(currentNode);
            return null;
        }
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        for(int i=0; i <= depth; i++)
        {
        	if(checkedBoards.get(i).contains(currentNode.getBoard().hashCode()))
        	{
        		return null;
        	}
        }
        if(currentNode.getBoard().isDeadlock())
        {
            return null;
        }
        checkedBoards.get(depth).add(currentNode.getBoard().hashCode());
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        frontierNodes += boardsToEvaluate.size();
        nodesExpanded++;
        Map<Integer, Integer> frontierNodesPerDepth = new HashMap<>();
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            possibleChildNode.setParentNode(currentNode);
            frontierNodesPerDepth.put(depth, frontierNodes);
            possibleChildNode = solveIDDFSRecursively(possibleChildNode, checkedBoards, pendingNodes, depth + 1);
            if(possibleChildNode != null) {
                return possibleChildNode;
            }
        }
        return null;
    }
    
    private Node solveBFS(Node root)
    {
        System.out.println("\nRunning solver with BFS...");
        Queue<Node> queue = new LinkedList<>();
        Set<Integer> checkedBoards = new HashSet<>();

        Node currentNode;
        queue.add(root);
        while(!queue.isEmpty())
        {
        	currentNode = queue.poll();
        	frontierNodes = queue.size();
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
        	if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
        	{
        		checkedBoards.add(currentNode.getBoard().hashCode());
            	if(currentNode.getBoard().isCompleted()) {
            		return currentNode;
            	}
            	else if(!currentNode.getBoard().isDeadlock())
            	{
            		nodesExpanded++;
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
        return null;
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
            frontierNodes = queue.size();
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
            if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
            {
                checkedBoards.add(currentNode.getBoard().hashCode());
                if(currentNode.getBoard().isCompleted()){
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                	nodesExpanded++;
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
        System.out.println("\nRunning solver with A* Search...");
        Node solution = null;
        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
															@Override public int compare(Node n1, Node n2) {
																int diff = n1.getHeuristicAndDepthValue(heuristic) - n2.getHeuristicAndDepthValue(heuristic);
																if(diff == 0)
																	return n1.getHeuristicValue(heuristic) - n2.getHeuristicValue(heuristic);
																return diff;
															}});
        Set<Integer> checkedBoards = new HashSet<>();
        Node currentNode;
        queue.add(root);
        while(!queue.isEmpty())
        {            
            currentNode = queue.poll();
            frontierNodes = queue.size();
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
            if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
            {
                checkedBoards.add(currentNode.getBoard().hashCode());
                if(currentNode.getBoard().isCompleted()){
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                	nodesExpanded++;
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

    private Node solveIDAStarSearch(Node root) {
        System.out.println("\nRunning solver with IDA*...");
        Stack<Integer> checkedBoards = new Stack<>();
        PriorityQueue<Integer> frontierLimits = new PriorityQueue<>();
        Node solution = null;
        int limit = 0;
        while(solution == null) {
            solution = solveIDAStarRecursively(root, checkedBoards, frontierLimits, limit);
            if(frontierLimits.isEmpty())
            	return null;
            limit = frontierLimits.poll();
            checkedBoards.clear();
        }
        return solution;
    }

    private Node solveIDAStarRecursively(Node currentNode, Stack<Integer> checkedBoards, PriorityQueue<Integer> frontierLimits, int limit)
    {
        if(frontierNodes > 0)
            frontierNodes--;
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        int f = currentNode.getHeuristicAndDepthValue(heuristic);

        if(f > limit) {
            if(!frontierLimits.contains(f))
                frontierLimits.add(f);
            return null;
        }

        if(checkedBoards.contains(currentNode.getBoard().hashCode()))
            return null;
        
        checkedBoards.push(currentNode.getBoard().hashCode());
        if(currentNode.getBoard().isDeadlock())
        	return null;
        
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        int currentDepth = currentNode.getDepth();
        frontierNodes += boardsToEvaluate.size();
        nodesExpanded++;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, currentDepth + 1);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveIDAStarRecursively(possibleChildNode, checkedBoards, frontierLimits, limit);
            if(possibleChildNode != null) {
                return possibleChildNode;
            }
        }
        checkedBoards.pop();
        return null;
    }

    private void printSolution(Solution sol) throws Exception {
        Iterator<Node> it = sol.getMoves().iterator();
        Node n;
        while(it.hasNext())
        {
        	n = it.next();
            System.out.println("\nMOVE " +n.getDepth() +":");
            n.getBoard().printBoard();
        }
        if(sol.getElapsedTime() > 60*1000)
            System.out.println("Time elapsed to process: " + (sol.getElapsedTime() / 1000.0) / 60 + " minutes");
        else
            System.out.println("Time elapsed to process: " + sol.getElapsedTime() / 1000.0 + " seconds");
    }
}
