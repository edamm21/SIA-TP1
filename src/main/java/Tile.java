import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Piece piece;

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Tile(Color color, int x, int y) {
        setWidth(Graphics.TILE_SIZE);
        setHeight(Graphics.TILE_SIZE);
        relocate(x * Graphics.TILE_SIZE, y * Graphics.TILE_SIZE);
        setFill(color);
    }
}
