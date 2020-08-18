import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Graphics extends Application {
    private List<Scene> scenes = new ArrayList<>();
    private static Group tileGroup = new Group();
    private static Group pieceGroup = new Group();
    public static final int TILE_SIZE = 50;

    private Scene createScene(Solution s, Stage stage, int move) {
        if(s.getMoves().get(move) == null)
            return new Scene(new Pane());
        int width = s.getMoves().get(move).getBoard().getWidth();
        int height = s.getMoves().get(move).getBoard().getHeight();
        GridPane gridpane = new GridPane();
        
        // Information
    	String time;
        if(s.getElapsedTime() > 60*1000)
        	time = new DecimalFormat("#.#####").format((s.getElapsedTime() / 1000.0) / 60) + " minutes";
        else
            time = new DecimalFormat("#.#####").format(s.getElapsedTime() / 1000.0) + " seconds";
        String heuristicText = "N/A";
        if(s.getHeuristic() != null)
        	heuristicText = s.getHeuristic().toString();
        final String analyticsText = "MOVE " +move +" / " +(s.getMoves().size()-1) +"\n\n**SOLUTION INFORMATION**\n\nSearch Algorithm Used: " +s.getAlgorithm().getCodename() +"\nHeuristic Used: " +heuristicText +"\nElapsed Time: " +time +"\nSolution Depth: " +(s.getMoves().size()-1) +" moves \nRemaining Frontier Set Size: " +s.getFrontierSize() +"\nAmount of Nodes Expanded: " +s.getNodesExpanded();
        
        Button prevButton = new Button("< Prev");
        Button nextButton = new Button("Next >");
        prevButton.setOnAction(e -> stage.setScene(createScene(s, stage, move - 1)));
        nextButton.setOnAction(e -> stage.setScene(createScene(s, stage, move + 1)));
        
        TextArea textArea = new TextArea();
        textArea.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
        textArea.setText(analyticsText);
        textArea.setEditable(false);
        textArea.setPrefWidth(TILE_SIZE*5);
        textArea.setPrefHeight(TILE_SIZE*10);

        gridpane.setRowIndex(tileGroup, 0);
        gridpane.setColumnIndex(tileGroup, 0);
        gridpane.setRowSpan(tileGroup, height);
        gridpane.setColumnSpan(tileGroup, width);
        
        gridpane.setRowIndex(pieceGroup, 0);
        gridpane.setColumnIndex(pieceGroup, 0);
        gridpane.setRowIndex(pieceGroup, height);
        gridpane.setColumnSpan(pieceGroup, width);

        gridpane.setRowSpan(textArea, height-1);
        gridpane.setColumnSpan(textArea, 2);
        gridpane.setColumnIndex(textArea, width);

        gridpane.setConstraints(prevButton, width, height-1);
        gridpane.setConstraints(nextButton, width+1, height-1);
        gridpane.setPrefSize(width*TILE_SIZE + textArea.getPrefWidth(), 0);
        
        gridpane.getChildren().addAll(tileGroup, pieceGroup, prevButton, nextButton, textArea);
        if(move <= 0)
        	prevButton.setDisable(true);
        if(move >= s.getMoves().size()-1)
        	nextButton.setDisable(true);
        
        // Draw
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile;
                switch (s.getMoves().get(move).getBoard().getBoard()[y][x]) {
                    case '#':
                        tile = new Tile("/assets/wall.png", x, y);
                        break;
                    case '@':
                        tile = new Tile("/assets/player.png", x, y);
                        break;
                    case '$':
                        tile = new Tile("/assets/box.png", x, y);
                        break;
                    case '+':
                        tile = new Tile("/assets/player-on-goal.png", x, y);
                        break;
                    case '*':
                        tile = new Tile("/assets/box-on-goal.png", x, y);
                        break;
                    case ' ':
                        tile = new Tile("/assets/floor.png", x, y);
                        break;
                    case '.':
                        tile = new Tile("/assets/goal.png", x, y);
                        break;
                    default:
                        tile = new Tile("/assets/floor.png", x, y);
                        break;
                }
                tileGroup.getChildren().add(tile);
            }
        }
        Scene scene = new Scene(gridpane);
        return scene;
    }
    
    private Scene createErrorScreen(String message)
    {
        GridPane pane = new GridPane();
        //pane.setPrefSize(400, 200);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(50));
        
        
        Text text = new Text();
        text.setText(message);
        
        pane.getChildren().addAll(text);
        Scene scene = new Scene(pane);
        return scene;
    }
    
    private void launchErrorScreen(String message, Stage stage)
    {
        Scene scene = createErrorScreen(message);
        stage.setTitle("Sokoban Solver");
        stage.setScene(scene);
        stage.show();
    }
    
    private void printSolution(Solution s) throws Exception {
        Iterator<Node> it = s.getMoves().iterator();
        Node n;
        while(it.hasNext())
        {
        	n = it.next();
            System.out.println("\nMOVE " +n.getDepth() +":");
            n.getBoard().printBoard();
        }
        System.out.println("\n**SOLUTION INFORMATION**\n\nSearch Algorithm Used: " +s.getAlgorithm().getCodename());
        if(s.getHeuristic() != null)
        	System.out.println("Heuristic Used: " +s.getHeuristic());
        else
        	System.out.println("Heuristic Used: N/A");
        if(s.getElapsedTime() > 60*1000)
            System.out.println("Elapsed Time: " + (s.getElapsedTime() / 1000.0) / 60 + " minutes");
        else
            System.out.println("Elapsed Time: " + s.getElapsedTime() / 1000.0 + " seconds");
       System.out.println("Solution Depth: " +(s.getMoves().size()-1) +" moves \nRemaining Frontier Set Size: " +s.getFrontierSize() +"\nAmount of Nodes Expanded: " +s.getNodesExpanded());
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        String readAlgorithm;
        String readHeuristic;
		try {
			List<String> configurations = Files.readAllLines(Paths.get("settings.conf"));
			readAlgorithm = configurations.get(0);
			readHeuristic = configurations.get(1);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Algorithm algorithm = null;
		Heuristic heuristic = null;
		for(Algorithm a : Algorithm.values())
		{
			if(a.getCodename().equals(readAlgorithm))
				algorithm = a;
		}
		for(Heuristic h : Heuristic.values())
		{
			if(h.getCodename().equals(readHeuristic))
				heuristic = h;
		}
		if(algorithm == null)
		{
			System.out.println("Algorithm " +readAlgorithm +" unknown!");
			launchErrorScreen("ERROR\nAlgorithm " +readAlgorithm +" unknown!", primaryStage);
			return;
		}
		if(heuristic == null)
		{
			System.out.println("Heuristic " +readHeuristic +" unknown!");
			launchErrorScreen("ERROR\nHeuristic " +readHeuristic +" unknown!", primaryStage);
			return;
		}
        
    	// Read initial board file
        String initBoard;
		try {
			initBoard = String.join("\n", Files.readAllLines(Paths.get("map.conf")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        Board b = new Board(initBoard);
        
        // Solve the challenge
        ArtificialIntelligence artificialIntelligence = new ArtificialIntelligence(algorithm, heuristic);
        System.out.println("Solving map. Please wait...");
        Solution s = artificialIntelligence.solve(b);
        
        if(s == null)
        {
        	System.out.println("Couldn't find a solution for the requested map!");
        	launchErrorScreen("Couldn't find a solution for the requested map!", primaryStage);
        	return;
        }
        
        // Graphics
        Scene scene = createScene(s, primaryStage, 0);
        primaryStage.setTitle("Sokoban Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
        printSolution(s);
    }

    private Piece makePlayer(TileTypes tileType, int x, int y) {
        Piece player = new Piece(tileType, x, y);
        return player;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
