import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Graphics extends Application {

    private Stack<Node> path = new Stack<>();
    private List<Scene> scenes = new ArrayList<>();
    private int finalDepth;

    private Scene createNextScene(Node node, Stage stage, int move) {
        if(node == null)
            return new Scene(new Pane());
        int width = ArtificialIntelligence.initialBoard.getWidth();
        int height = ArtificialIntelligence.initialBoard.getHeight();
        Pane pane = new Pane();
        if(!path.isEmpty()) {
            final String analyticsText = "Current Move Analytics:\nCurrent depth:" + move ;
            Button nextButton = new Button("Move " + (move + 1));
            nextButton.setOnAction(e -> stage.setScene(createNextScene(path.pop(), stage, move + 1)));
            Button prevButton = null;
            TextArea textArea = new TextArea();
            textArea.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
            textArea.setPrefSize(4 * ArtificialIntelligence.TILE_SIZE, height * ArtificialIntelligence.TILE_SIZE);
            textArea.setPrefColumnCount(1);
            textArea.setText(analyticsText);
            textArea.setLayoutX(width * ArtificialIntelligence.TILE_SIZE);
            pane.setPrefSize(width * ArtificialIntelligence.TILE_SIZE + 3 * ArtificialIntelligence.TILE_SIZE, height * ArtificialIntelligence.TILE_SIZE);
            pane.getChildren().addAll(ArtificialIntelligence.tileGroup, ArtificialIntelligence.pieceGroup, nextButton, textArea);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Tile tile;
                    switch (node.getBoard().getBoard()[y][x]) {
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
                            tile = new Tile("/assets/wall.png", x, y);
                            break;
                    }
                    ArtificialIntelligence.tileGroup.getChildren().add(tile);
                }
            }
            Scene scene = new Scene(pane);
            scenes.add(scene);
            return scene;
        } else {
            Pane paneFinal = new Pane();
            paneFinal.setPrefSize(width * ArtificialIntelligence.TILE_SIZE + 3 * ArtificialIntelligence.TILE_SIZE, height * ArtificialIntelligence.TILE_SIZE);
            Text text = new Text();
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            long elapsedTime = ArtificialIntelligence.elapsedTime;
            text.setText("\nRESULTS:\n" +
                    "Solution found: true\n" +
                    "Time elapsed: " + (elapsedTime > 60000 ? (((elapsedTime / 1000.0) * 60) + " minutes.\n") : ((elapsedTime / 1000.0) + " seconds.\n")) +
                    "Depth reached: " + finalDepth + "\n" +
                    "Total nodes expanded: " + ArtificialIntelligence.nodesExpanded + "\n" +
                    "Frontier nodes: " + ArtificialIntelligence.frontierNodes
            );
            paneFinal.getChildren().addAll(text);
            return new Scene(paneFinal);
        }
    }

    private Scene createAllScenes(Stage stage) {
        Node n = ArtificialIntelligence.solution;
        while(n.getParentNode() != null) {
            path.push(n);
            n = n.getParentNode();
        }
        path.push(n);
        finalDepth = path.size();
        return createNextScene(path.pop(), stage, 0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = createAllScenes(primaryStage);;
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
