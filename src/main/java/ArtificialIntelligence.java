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
        switch (this.algorithm) {
            case "DFS":
                solveDFS(root);
                break;
            case "BFS":
            	Node solution = solveBFS(root);
            	if(solution == null)
            		System.out.println("Couldn't find a solution!");
            	else
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
            	break;
        }
    }

    private void solveDFS(Node root) {
        Stack<Node> solution = new Stack<>(); // Para ir haciendo pop y teniendo la solucion
        Set<Board> checkedBoards = new HashSet<>();
        int depth = 0;
        System.out.println("\nRunning solver with DFS...");
        if(!solveDFSRecursively(root, checkedBoards, solution, depth)) {
            System.out.println("Maze has no solution");
            return;
        }
        solution.add(root);
        printSolution(solution);
    }

    private boolean solveDFSRecursively(Node currentNode, Set<Board> checkedBoards, Stack<Node> solution, int depth) {
        if(currentNode.getBoard().isCompleted())
            return true;
        if(depth > maxAllowedDepth)
            return false;
        if(checkedBoards.contains(currentNode.getBoard()))
        {
        	//System.out.println("Already saw this one. Ignore!");
        	return false;
        }
        checkedBoards.add(currentNode.getBoard());
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        //System.out.println("\nAs you saw, I have " +boardsToEvaluate.size() +" children. I'll go branch by branch now!");
        Node possibleChildNode;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            currentNode.addChild(possibleChildNode);
            if(solveDFSRecursively(possibleChildNode, checkedBoards, solution, depth + 1)) {
                solution.push(possibleChildNode);
                return true;
            }
        }
        return false;
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
        	if(!checkedBoards.contains(currentNode.getBoard().hashCode()))
        	{
        		checkedBoards.add(currentNode.getBoard().hashCode());
            	if(currentNode.getBoard().isCompleted())
            	{
            		//System.out.println("Found one!");
            		solution = currentNode;
            		break;
            	}
            	else
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
    
    private void printSolution(Stack<Node> solution) {
        int depth = solution.size();
        System.out.println("\n\n\nFinished! Solution is as follows:");
        for(int i = 0 ; i < depth ; i++) {
            System.out.println("\nMOVE: " + i);
            solution.pop().getBoard().printBoard();
        }
        System.out.println("\nFINAL DEPTH: " + depth);
    }

}
