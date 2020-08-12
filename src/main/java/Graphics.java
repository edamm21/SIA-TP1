import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Graphics extends Application {

    public static final int TILE_SIZE = 100;
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private Board initialBoard;

    private Parent createContent() {
        Pane root = new Pane();
        int width = initialBoard.getWidth();
        int height = initialBoard.getHeight();
        root.setPrefSize(width * TILE_SIZE, height * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);
        for(int y = 0 ; y < height ; y++) {
            for(int x = 0 ; x < width ; x++){
                Tile tile;
                switch (initialBoard.getBoard()[y][x]) {
                    case '#':
                        tile = new Tile(Color.BROWN, x, y);
                        break;
                    case '@':
                        tile = new Tile(Color.BLUE, x, y);
                        break;
                    case '$':
                        tile = new Tile(Color.RED, x, y);
                        break;
                    case '.':
                        tile = new Tile(Color.YELLOW, x, y);
                        break;
                    default:
                        tile = new Tile(Color.BLACK, x, y);
                        break;
                }
                tileGroup.getChildren().add(tile);
            }
        }
        return root;
    }

    public void drawNewBoard(Board board) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialConfig();
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Sokoban Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initialConfig() throws Exception{
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
        this.initialBoard = new Board(initBoard);
        initialBoard.printBoard();

        System.out.println("\nPlayer: " + initialBoard.getPlayerPosition());
        System.out.println("Goals: " + initialBoard.getGoalPositions());
        System.out.println("Boxes: " + initialBoard.getBoxPositions());
        System.out.println("Finished: " + initialBoard.isCompleted());

        //ArtificialIntelligence artificialIntelligence = new ArtificialIntelligence("DFS", 10000);
        //artificialIntelligence.solve(b);

    }

    private Piece makePlayer(TileTypes tileType, int x, int y) {
        Piece player = new Piece(tileType, x, y);
        return player;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
