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
        System.out.println("Running solver with DFS...");
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
        checkedBoards.add(currentNode.getBoard());
        List<Board> boardsToEvaluate = currentNode.getBoard().getPossibleMoves();
        Node possibleChildNode;
        for(Board board : boardsToEvaluate) {
            possibleChildNode = new Node(board, depth + 1);
            currentNode.addChild(possibleChildNode);
            board.printBoard();
            if(solveDFSRecursively(possibleChildNode, checkedBoards, solution, depth + 1)) {
                solution.push(possibleChildNode);
                return true;
            }
        }
        return false;
    }

    private void printSolution(Stack<Node> solution) {
        int depth = solution.size();
        System.out.println("Finished!\nSolution is as follows:");
        for(int i = 0 ; i < depth ; i++) {
            System.out.println("MOVE: " + i);
            solution.pop().getBoard().printBoard();
        }
        System.out.println("FINAL DEPTH: " + depth);
    }

}
