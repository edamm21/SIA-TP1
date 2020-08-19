import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URISyntaxException;
import java.text.CollationElementIterator;

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
        setWidth(Graphics.TILE_SIZE);
        setHeight(Graphics.TILE_SIZE);
        relocate(x * Graphics.TILE_SIZE, y * Graphics.TILE_SIZE);
        File fileImg = new File(imageURL);
        Image img = new Image(fileImg.toURI().toString());
        ImagePattern pattern = new ImagePattern(img);
        setFill(pattern);


    }
}
