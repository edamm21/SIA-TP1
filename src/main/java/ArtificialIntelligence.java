import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class ArtificialIntelligence {

    private String algorithm;
    public static Board initialBoard;
    public static final int TILE_SIZE = 50;
    public static Group tileGroup = new Group();
    public static Group pieceGroup = new Group();
    public static Node solution = null;
    public static long elapsedTime;
    public static int nodesExpanded;
    public static int frontierNodes;
    public static int finalDepth;
    private int maxAllowedDepth;
    private String heuristic;
    private static int DEFAULT_DEPTH_LIMIT = 3000;
    private static String DEFAULT_HEURISTIC = "MANHATTAN";
    private static final int FOUND = -1;
    private static final int INFINITY = Integer.MAX_VALUE;
    private List<Integer> frontierLimits = new ArrayList<>();


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

    public void solve(Board initialBoard) throws Exception {
        this.initialBoard = initialBoard;
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
            case "IDA*":
                solution = solveIDAStarSearch(root);
                break;
            default:
            	System.out.println("Search algorithm unknown! Quitting");
            	return;
        }
    	if(solution != null)
    	{
    	    setSolution(solution);
    		long endTime = System.currentTimeMillis();
    		elapsedTime = endTime - startTime;
    		printSolution(solution, elapsedTime);
    	}
    	else
    		System.out.println("Couldn't find a solution!");
    }

    private Node solveDFS(Node root) {
        System.out.println("\nRunning solver with DFS...");
        Set<Integer> checkedBoards = new HashSet<>();
        return solveDFSRecursively(root, checkedBoards, 0);
    }

    private void setSolution(Node solution) {
        ArtificialIntelligence.solution = solution;
    }

    private Node solveDFSRecursively(Node currentNode, Set<Integer> checkedBoards, int depth) {
        frontierNodes -= 1;
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
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            possibleChildNode.setParentNode(currentNode);
            nodesExpanded++;
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
		System.out.println("Trying depth " +maxAllowedDepth);
        while(solution == null && !currentRoots.isEmpty() && maxAllowedDepth <= depthLimit)
        {
        	aux = currentRoots.poll();
        	solution = solveIDDFSRecursively(aux, checkedBoards, pendingNodes, aux.getDepth());
        	if(currentRoots.isEmpty())
        	{
        		currentRoots.addAll(pendingNodes);
        		pendingNodes.clear();
        		maxAllowedDepth += 10;
        		System.out.println("Trying depth " +maxAllowedDepth);
        	}
        }
        maxAllowedDepth = depthLimit;
        return solution;
    }
    
    private Node solveIDDFSRecursively(Node currentNode, List<Set<Integer>> checkedBoards, Queue<Node> pendingNodes, int depth)
    {
        frontierNodes -= 1;
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
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            possibleChildNode.setParentNode(currentNode);
            nodesExpanded++;
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
        Node solution = null;
        Queue<Node> queue = new LinkedList<>();
        Set<Integer> checkedBoards = new HashSet<>();

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
            	if(currentNode.getBoard().isCompleted()) {
            	    frontierNodes = queue.size();
            		return currentNode;
            	}
            	else if(!currentNode.getBoard().isDeadlock())
            	{
            		List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
            		Node possibleChildNode;
                    for(Board board : possibleChildren) {
                    	possibleChildNode = new Node(board, currentNode.getDepth() + 1);
                    	possibleChildNode.setParentNode(currentNode);
            		    nodesExpanded++;
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
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
            if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
            {
                checkedBoards.add(currentNode.getBoard().hashCode());
                if(currentNode.getBoard().isCompleted()){
                    frontierNodes = queue.size();
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                    List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
                    Node possibleChildNode;
                    for(Board board : possibleChildren) {
                        possibleChildNode = new Node(board, currentNode.getDepth() + 1);
                        possibleChildNode.setParentNode(currentNode);
                        nodesExpanded++;
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
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
            if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
            {
                checkedBoards.add(currentNode.getBoard().hashCode());
                if(currentNode.getBoard().isCompleted()){
                    frontierNodes = queue.size();
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                    List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
                    Node possibleChildNode;
                    for(Board board : possibleChildren) {
                        possibleChildNode = new Node(board, currentNode.getDepth() + 1);
                        possibleChildNode.setParentNode(currentNode);
                        nodesExpanded++;
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
        Node solution = null;
        int limit = root.getHeuristicAndDepthValue(heuristic);
        while(solution == null) {
            solution = solveIDAStarRecursively(root, checkedBoards, limit);
            Collections.sort(frontierLimits);
            limit = frontierLimits.get(0);
            frontierLimits.remove(0);
            checkedBoards.clear();
        }
        return solution;
    }

    private Node solveIDAStarRecursively(Node currentNode, Stack<Integer> checkedBoards, int limit) {
        frontierNodes -= 1;
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        int f = currentNode.getHeuristicAndDepthValue(heuristic);
        if(checkedBoards.contains(currentNode.getBoard().hashCode()) || currentNode.getBoard().isDeadlock()) {
            if(f > limit) {
                if(!frontierLimits.contains(f))
                    frontierLimits.add(f);
            }
            return null;
        }
        if(f > limit) {
            if(!frontierLimits.contains(f))
                frontierLimits.add(f);
            return null;
        }
        checkedBoards.push(currentNode.getBoard().hashCode());
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        int currentDepth = currentNode.getDepth();
        frontierNodes += boardsToEvaluate.size();
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, currentDepth + 1);
            possibleChildNode.setParentNode(currentNode);
            nodesExpanded++;
            possibleChildNode = solveIDAStarRecursively(possibleChildNode, checkedBoards, limit);
            if(possibleChildNode != null) {
                return possibleChildNode;
            }
        }
        checkedBoards.pop();
        return null;
    }

    private void printSolution(Node solution, long elapsedTime) throws Exception {
        try {
            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(Graphics.class);
                }
            }.start();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
