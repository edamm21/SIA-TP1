import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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

    public Tile(String imageURL, int x, int y) {
        setWidth(ArtificialIntelligence.TILE_SIZE);
        setHeight(ArtificialIntelligence.TILE_SIZE);
        relocate(x * ArtificialIntelligence.TILE_SIZE, y * ArtificialIntelligence.TILE_SIZE);
        Image img = new Image(imageURL);
        setFill(new ImagePattern(img));
    }
}
