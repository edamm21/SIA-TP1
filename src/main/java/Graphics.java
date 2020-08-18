import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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
            time = (s.getElapsedTime() / 1000.0) / 60 + " minutes";
        else
            time = s.getElapsedTime() / 1000.0 + " seconds";
        final String analyticsText = "MOVE " +move +" / " +(s.getMoves().size()-1) +"\n\n**SOLUTION INFORMATION**\n\nSearch Algorithm Used: " +s.getAlgorithm() +"\nHeuristic Used: " +s.getHeuristic() +"\nElapsed Time: " +time +"\nSolution Depth: " +(s.getMoves().size()-1) +" moves \nSolution Heuristic: " +"???" +"\nRemaining Frontier Set Size: " +s.getFrontierSize() +"\nAmount of Nodes Expanded: " +s.getNodesExpanded();
        
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
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
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
        
        // Solve the challenge
        ArtificialIntelligence artificialIntelligence = new ArtificialIntelligence(algorithm, heuristic);
        Solution s = artificialIntelligence.solve(b);
        
        if(s == null)
        {
        	return;
        }
        
        // Graphics
        Scene scene = createScene(s, primaryStage, 0);
        primaryStage.setTitle("Sokoban Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Piece makePlayer(TileTypes tileType, int x, int y) {
        Piece player = new Piece(tileType, x, y);
        return player;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
