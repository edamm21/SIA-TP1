import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;

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
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        relocate(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
        File fileImg = new File(imageURL);
        Image img = new Image(fileImg.toURI().toString());
        ImagePattern pattern = new ImagePattern(img);
        setFill(pattern);


    }
}
