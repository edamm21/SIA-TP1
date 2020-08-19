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
    private Algorithm algorithm;
    private Heuristic heuristic;
    private int maxAllowedDepth;
    private int nodesExpanded = 0;
    private int frontierNodes = 0;
    private int DEFAULT_DEPTH_LIMIT = 8000;
    private Heuristic DEFAULT_HEURISTIC = Heuristic.BOXES_REMAINING;


    public ArtificialIntelligence(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = DEFAULT_DEPTH_LIMIT;
        this.heuristic = DEFAULT_HEURISTIC;
    }
    
    public ArtificialIntelligence(Algorithm algorithm, Heuristic heuristicChosen) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = DEFAULT_DEPTH_LIMIT;
        this.heuristic = heuristicChosen;
    }
    
    public ArtificialIntelligence(Algorithm algorithm, int depth) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = depth;
        this.heuristic = DEFAULT_HEURISTIC;
    }
    
    public ArtificialIntelligence(Algorithm algorithm, int depth, Heuristic heuristicChosen) {
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
            case DFS:
                solution = solveDFS(root);
                break;
            case BFS:
            	solution = solveBFS(root);
            	break;
            case IDDFS:
            	solution = solveIDDFS(root);
            	break;
            case GGS:
                solution = solveGlobalGreedySearch(root);
                break;
            case A_STAR:
                solution = solveAStarSearch(root);
                break;
            case IDA_STAR:
                solution = solveIDAStarSearch(root);
                break;
            default:
            	System.out.println("Search algorithm unknown! Quitting");
            	return null;
        }
        switch (this.heuristic) {
	        case CLOSEST_GOAL:
	            break;
	        case CLOSEST_BOX:
	        	break;
	        case BOXES_REMAINING:
	        	break;
	        default:
	        	System.out.println("Heuristic type unknown! Quitting");
	        	return null;
	    }
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
    	if(solution != null)
    	{
            List<Node> path = new ArrayList<>();
            Node n = solution;
            while(n.getParentNode() != null)
            {
                path.add(0, n);
                n = n.getParentNode();
            }
            path.add(0, n);
            return new Solution(path, elapsedTime, nodesExpanded, frontierNodes, algorithm, heuristic, true);
    	}
    	return new Solution(null, elapsedTime, nodesExpanded, frontierNodes, algorithm, heuristic, false);
    }

    private Node solveDFS(Node root) {
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

    private Node solveIDAStarSearch(Node root)
    {
    	Map<Integer, Set<Integer>> map = new HashMap<Integer, Set<Integer>>();
    	Queue<Node> currentRoots = new LinkedList<>();
    	Queue<Node> pendingNodes = new LinkedList<>();
        Node solution = null;
        Node aux = null;
        int limit = root.getHeuristicAndDepthValue(heuristic);
        currentRoots.add(root);
        while(solution == null && !currentRoots.isEmpty())
        {
        	aux = currentRoots.poll();
        	solution = solveIDAStarRecursively(aux, map, pendingNodes, limit);
        	if(currentRoots.isEmpty()) {
        		currentRoots.addAll(pendingNodes);
        		pendingNodes.clear();
        		if(!currentRoots.isEmpty())
        			limit = currentRoots.peek().getHeuristicAndDepthValue(heuristic);
        		for(Node n : currentRoots)
        		{
        			if(n.getHeuristicAndDepthValue(heuristic) < limit)
        				limit = n.getHeuristicAndDepthValue(heuristic);
        		}
        	}
        }
        return solution;
    }
    
    private Node solveIDAStarRecursively(Node currentNode, Map<Integer, Set<Integer>> map, Queue<Node> pendingNodes, int limit)
    {
        if(frontierNodes > 0)
            frontierNodes--;
    	if(currentNode.getHeuristicAndDepthValue(heuristic) > limit)
        {
        	pendingNodes.add(currentNode);
            return null;
        }
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        if(currentNode.getBoard().isDeadlock())
            return null;
        for(int i=0; i <= currentNode.getHeuristicAndDepthValue(heuristic); i++)
        {
        	if(map.containsKey(i) && map.get(i).contains(currentNode.getBoard().hashCode()))
        		return null;
        }

        if(!map.containsKey(currentNode.getHeuristicAndDepthValue(heuristic)))
        	map.put(currentNode.getHeuristicAndDepthValue(heuristic), new HashSet<>());
        map.get(currentNode.getHeuristicAndDepthValue(heuristic)).add(currentNode.getBoard().hashCode());
        
        
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        frontierNodes += boardsToEvaluate.size();
        nodesExpanded++;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, currentNode.getDepth() + 1);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveIDAStarRecursively(possibleChildNode, map, pendingNodes, limit);
            if(possibleChildNode != null) {
                return possibleChildNode;
            }
        }
        return null;
    }
}
