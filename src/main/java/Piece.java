import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {

    private TileTypes tileType;

    public Piece(TileTypes tileType, int x, int y) {
        this.tileType = tileType;
        relocate(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
        Ellipse player = new Ellipse(Main.TILE_SIZE * 0.3125, Main.TILE_SIZE * 0.26);
        player.setFill(Color.BLACK);
        player.setTranslateX((Main.TILE_SIZE - Main.TILE_SIZE * 0.3125 * 2) / 2);
        player.setTranslateX((Main.TILE_SIZE - Main.TILE_SIZE * 0.26 * 2) / 2);
        getChildren().add(player);
    }

    public TileTypes getTileType() {
        return tileType;
    }
}
