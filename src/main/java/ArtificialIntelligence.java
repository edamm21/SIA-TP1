import java.util.*;

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
        }
    }

    private void solveDFS(Node root) {
        Stack<Node> solution = new Stack<>(); // Para ir haciendo pop y teniendo la solucion
        List<Board> checkedBoards = new ArrayList<>();
        int depth = 0;
        System.out.println("\nRunning solver with DFS...");
        if(!solveDFSRecursively(root, checkedBoards, solution, depth)) {
            System.out.println("Maze has no solution");
            return;
        }
        solution.add(root);
        printSolution(solution);
    }

    private boolean solveDFSRecursively(Node currentNode, List<Board> checkedBoards, Stack<Node> solution, int depth) {
        if(currentNode.getBoard().isCompleted())
            return true;
        if(depth > maxAllowedDepth)
            return false;
        if(checkedBoards.contains(currentNode.getBoard()))
        {
        	System.out.println("Already saw this one. Ignore!");
        	return false;
        }
        checkedBoards.add(currentNode.getBoard());
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        System.out.println("\nAs you saw, I have " +boardsToEvaluate.size() +" children. I'll go branch by branch now!");
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
