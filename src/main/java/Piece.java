import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {

    private TileTypes tileType;

    public Piece(TileTypes tileType, int x, int y) {
        this.tileType = tileType;
        relocate(x * ArtificialIntelligence.TILE_SIZE, y * ArtificialIntelligence.TILE_SIZE);
        Ellipse player = new Ellipse(ArtificialIntelligence.TILE_SIZE * 0.3125, ArtificialIntelligence.TILE_SIZE * 0.26);
        player.setFill(Color.BLACK);
        player.setTranslateX((ArtificialIntelligence.TILE_SIZE - ArtificialIntelligence.TILE_SIZE * 0.3125 * 2) / 2);
        player.setTranslateX((ArtificialIntelligence.TILE_SIZE - ArtificialIntelligence.TILE_SIZE * 0.26 * 2) / 2);
        getChildren().add(player);
    }

    public TileTypes getTileType() {
        return tileType;
    }
}
