import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {

    private TileTypes tileType;

    public Piece(TileTypes tileType, int x, int y) {
        this.tileType = tileType;
        relocate(x * Graphics.TILE_SIZE, y * Graphics.TILE_SIZE);
        Ellipse player = new Ellipse(Graphics.TILE_SIZE * 0.3125, Graphics.TILE_SIZE * 0.26);
        player.setFill(Color.BLACK);
        player.setTranslateX((Graphics.TILE_SIZE - Graphics.TILE_SIZE * 0.3125 * 2) / 2);
        player.setTranslateX((Graphics.TILE_SIZE - Graphics.TILE_SIZE * 0.26 * 2) / 2);
        getChildren().add(player);
    }

    public TileTypes getTileType() {
        return tileType;
    }
}
