// official tile ASCII representation from www.game-sokoban.com
public enum TileTypes {
    WALL('#'),
    FREE_SPACE(' '),
    BOX('$'),
    GOAL_EMPTY('.'),
    GOAL_WITH_BOX('*'),
    SOKOBAN('@'),
    SOKOBAN_ON_GOAL('+');

    private char symbol;

    TileTypes(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
