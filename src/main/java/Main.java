import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Main {
	
    public static void main(String[] args) throws Exception
    {	
		// Read config file
        String algorithm;
        String heuristic;
		try {
			List<String> configurations = Files.readAllLines(Paths.get("settings.conf"));
			algorithm = configurations.get(0);
			heuristic = configurations.get(1);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Algorithm: " +algorithm +"\nHeuristic: " +heuristic);
        
    	// Read initial board file
        String initBoard;
		try {
			initBoard = String.join("\n", Files.readAllLines(Paths.get("map.conf")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        Board b = new Board(initBoard);
        b.printBoard();
        
        System.out.println("\nPlayer: " +b.getPlayerPosition());
        System.out.println("Boxes: " +b.getBoxPositions());
        System.out.println("Goals: " +b.getGoalPositions());
        System.out.println("Finished: " +b.isCompleted());
        ArtificialIntelligence artificialIntelligence = new ArtificialIntelligence("DFS", 1000);
		artificialIntelligence.solve(b);
    }
}
