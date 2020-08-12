import javafx.collections.transformation.SortedList;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ArtificialIntelligence {

    private String algorithm;
    private int maxAllowedDepth;
    private String heuristicChosen;

    public ArtificialIntelligence(String algorithm, int maxAllowedDepth, String heuristicChosen) {
        this.algorithm = algorithm;
        this.maxAllowedDepth = maxAllowedDepth;
        this.heuristicChosen = heuristicChosen;
    }

    public void solve(Board initialBoard) {
        Node root = new Node(initialBoard, 0, heuristicChosen);
        Node solution = null;
        long startTime = System.currentTimeMillis();
        switch (this.algorithm) {
            case "DFS":
                solution = solveDFS(root);
                break;
            case "BFS":
            	solution = solveBFS(root);
            	break;
            case "IDDFS":
            	solution = solveIDDFSEuge(root);
            	break;
            case "GGS":
                solution = solveGlobalGreedySearch(root);
        }
    	if(solution != null) {
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
        if(depth >= maxAllowedDepth ||
            checkedBoards.contains(currentNode.getBoard().hashCode()) ||
            currentNode.getBoard().isDeadlock())
            return null;
        checkedBoards.add(currentNode.getBoard().hashCode());
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1, heuristicChosen);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveDFSRecursively(possibleChildNode, checkedBoards, depth + 1);
            if(possibleChildNode != null)
            	return possibleChildNode;
        }
        return null;
    }

    private Node solveIDDFSEuge(Node root)
    {
        Set<Integer> checkedBoards = new HashSet<>();
        int maxCopy = maxAllowedDepth;
        maxAllowedDepth = 78;
        System.out.println("\nRunning solver with IDDFS...");
        Node solution = null;
        while(solution == null && maxAllowedDepth <= maxCopy)
        {
            System.out.println("Trying depth " + maxAllowedDepth);
            solution = solveIDDFSRecursivelyEuge(root, checkedBoards, 0);
            maxAllowedDepth += 1;
            checkedBoards.clear();
        }
        return solution;
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

    private Node solveIDDFSRecursivelyEuge(Node currentNode, Set<Integer> checkedBoards, int depth) {
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        if(depth >= maxAllowedDepth ||
                checkedBoards.contains(currentNode.getBoard().hashCode()))
            return null;
        checkedBoards.add(currentNode.getBoard().hashCode());
        if(currentNode.getBoard().isDeadlock())
            return null;
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1, heuristicChosen);
            possibleChildNode.setParentNode(currentNode);
            possibleChildNode = solveIDDFSRecursivelyEuge(possibleChildNode, checkedBoards, depth + 1);
            if(possibleChildNode != null)
                return possibleChildNode;
        }
        checkedBoards.remove(currentNode.getBoard().hashCode());
        return null;
    }

    private Node solveIDDFSRecursively(Node currentNode, Stack<Board> checkedBoards, int depth) {
        if(currentNode.getBoard().isCompleted())
            return currentNode;
        if(depth >= maxAllowedDepth ||
            checkedBoards.contains(currentNode.getBoard()))
            return null;
        checkedBoards.push(currentNode.getBoard());
        if(currentNode.getBoard().isDeadlock())
            return null;
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode = null;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1, heuristicChosen);
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
                    	possibleChildNode = new Node(board, currentNode.getDepth() + 1, heuristicChosen);
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
        List<Node> queue = new ArrayList<>();
        Set<Integer> checkedBoards = new HashSet<>();
        System.out.println("\nRunning solver with Global Greedy Search...");
        Node currentNode;
        queue.add(root);
        while(!queue.isEmpty()) {
            Collections.sort(queue);
            currentNode = queue.remove(queue.size() - 1);
            if(currentNode.getDepth() > maxAllowedDepth)
                return null;
            if(!checkedBoards.contains(currentNode.getBoard().hashCode())) {
                checkedBoards.add(currentNode.getBoard().hashCode());
                if(currentNode.getBoard().isCompleted()) {
                    solution = currentNode;
                    break;
                } else if(!currentNode.getBoard().isDeadlock()) {
                    List<Board> possibleChildren = currentNode.getBoard().getPossibleMoves();
                    Node possibleChildNode;
                    for(Board board : possibleChildren) {
                        possibleChildNode = new Node(board, currentNode.getDepth() + 1, heuristicChosen);
                        possibleChildNode.setParentNode(currentNode);
                        queue.add(possibleChildNode);
                    }
                }
            }
            queue.remove(currentNode);
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
        System.out.println("Time elapsed to process: " + elapsedTime / 1000 + " secs.");
    }

}
