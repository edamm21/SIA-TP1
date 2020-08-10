import java.util.*;

// class responsible for game development
// includes current state, possible moves
public class Board {
	
	private static char PLAYER_ICON = '@';
	private static char PLAYER_ON_GOAL_ICON = '+';
	private static char WALL_ICON = '#';
	private static char BOX_ICON = '$';
	private static char GOAL_ICON = '.';
	private static char BOX_ON_GOAL_ICON = '*';
	private static char EMPTY_ICON = ' ';
	private int height;
	private int width;
	private char board[][];
	
	private Position playerPosition;
	private Set<Position> boxPositions;
	private Set<Position> goalPositions;

	private Board(Board board) {
		this.height = board.getHeight();
		this.width = board.getWidth();
		this.board = board.getBoard();
		this.playerPosition = board.getPlayerPosition();
		this.boxPositions = board.getBoxPositions();
		this.goalPositions = board.getGoalPositions();
	}

	public Board(String s) throws Exception
	{
		int width = 0;
		int height = 0;
		char string[] = s.toCharArray();
		int index = 0;
		
		// Let's build a board the right size
		int line_index = 0;
		while(index < s.length())
		{
			while(index < s.length() && string[index] != '\n')
			{
				if(width <= line_index)
					width++;
				line_index++;
				index++;
			}
			height++;
			line_index = 0;
			index++;
		}
		this.height = height;
		this.width = width;
		this.board = new char[height][width];
		this.playerPosition = null;
		this.boxPositions = new HashSet<Position>();
		this.goalPositions = new HashSet<Position>();
		
		// Let's fill in the board
		int boxes = 0;
		int goals = 0;
		index = 0;
		for(int i=0; i < height; i++)
		{
			for(int j=0; j < width; j++)
			{
				if(index >= s.length() || string[index] == '\n')
					break;
				if(string[index] == PLAYER_ICON || string[index] == PLAYER_ON_GOAL_ICON)
				{
					if(playerPosition == null)
						this.playerPosition = new Position(i, j);
					else
						throw new Exception("There can only be one player per board!");
				}
				if(string[index] == BOX_ICON || string[index] == BOX_ON_GOAL_ICON)
				{
					boxPositions.add(new Position(i, j));
					boxes++;
				}
				if(string[index] == GOAL_ICON || string[index] == BOX_ON_GOAL_ICON)
				{
					goalPositions.add(new Position(i, j));
					goals++;
				}
				this.board[i][j] = string[index];
				index++;
			}
			index++;
		}
		if(goals < boxes)
			throw new Exception("Not enough goals for the boxes on this board!");
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void printBoard()
	{
		for(int i=0; i < height; i++)
		{
			for(int j=0; j < width; j++)
			{
				System.out.print(this.board[i][j]);
			}
			System.out.println();
		}
	}
	
	public Position getPlayerPosition()
	{
		return playerPosition;
	}
	
	public Set<Position> getBoxPositions()
	{
		return boxPositions;
	}
	
	public Set<Position> getGoalPositions()
	{
		return goalPositions;
	}
	
	public boolean isCompleted()
	{
		return goalPositions.equals(boxPositions);
	}
	
	public boolean canPlayerMoveUp()
	{
		if(playerPosition.getRow() == 0)
			return false;
		if(board[playerPosition.getRow() - 1][playerPosition.getCol()] == EMPTY_ICON)
			return true;
		if(board[playerPosition.getRow() - 1][playerPosition.getCol()] == BOX_ICON && board[playerPosition.getRow() - 2][playerPosition.getCol()] == EMPTY_ICON)
			return true;
		return false;
	}
	
	public boolean canPlayerMoveRight()
	{
		if(playerPosition.getCol() == width)
			return false;
		if(board[playerPosition.getRow()][playerPosition.getCol() + 1] == EMPTY_ICON)
			return true;
		if(board[playerPosition.getRow()][playerPosition.getCol() + 1] == BOX_ICON && board[playerPosition.getRow()][playerPosition.getCol() + 2] == EMPTY_ICON)
			return true;
		return false;
	}
	
	public boolean canPlayerMoveLeft()
	{
		if(playerPosition.getCol() == 0)
			return false;
		if(board[playerPosition.getRow()][playerPosition.getCol() - 1] == EMPTY_ICON)
			return true;
		if(board[playerPosition.getRow()][playerPosition.getCol() - 1] == BOX_ICON && board[playerPosition.getRow()][playerPosition.getCol() - 2] == EMPTY_ICON)
			return true;
		return false;
	}
	
	public boolean canPlayerMoveDown()
	{
		if(playerPosition.getRow() == height)
			return false;
		if(board[playerPosition.getRow() + 1][playerPosition.getCol()] == EMPTY_ICON)
			return true;
		if(board[playerPosition.getRow() + 1][playerPosition.getCol()] == BOX_ICON && board[playerPosition.getRow() + 2][playerPosition.getCol()] == EMPTY_ICON)
			return true;
		return false;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Board)
		{
			Board toCompare = (Board) o;
			return this.playerPosition.equals(toCompare.getPlayerPosition()) && this.boxPositions.equals(toCompare.getBoxPositions());
		}
		return false;
	}

	public boolean isValidPosition(Position position) {
		int row = position.getRow();
		int col = position.getCol();
		return row >= 0 && row < width && col >= 0 && col < height && !isWall(row, col);
	}

	private boolean isEmpty(Position position) {
		return board[position.getRow()][position.getCol()] == EMPTY_ICON;
	}

	public boolean isWall(int row, int col) {
		return board[row][col] == WALL_ICON;
	}

	public boolean isGoal(int row, int col) {
		return board[row][col] == GOAL_ICON;
	}

	private Position getCoordinatesWithMoveApplied(Position currentPosition, String direction) {
		Position toReturn = currentPosition;
		switch (direction) {
			case "UP":
				toReturn = new Position(currentPosition.getRow() - 1, currentPosition.getCol());
				break;
			case "RIGHT":
				toReturn = new Position(currentPosition.getRow(), currentPosition.getCol() + 1);
				break;
			case "LEFT":
				toReturn = new Position(currentPosition.getRow(), currentPosition.getCol() - 1);
				break;
			case "DOWN":
				toReturn = new Position(currentPosition.getRow() + 1, currentPosition.getCol());
				break;
		}
		return toReturn;
	}

	public void setPlayerPosition(Position playerPosition) {
		this.playerPosition = playerPosition;
	}

	private void setPlayerNewPosition(Position oldPosition, Position futurePosition) {
		this.board[oldPosition.getRow()][oldPosition.getCol()] = EMPTY_ICON;
		this.board[futurePosition.getRow()][futurePosition.getCol()] = PLAYER_ICON;
		this.setPlayerPosition(futurePosition);
	}

	private void setBoxNewPosition(Position currentPlayerPosition, Position currentBoxPosition, Position futureBoxPosition) {
		this.board[currentPlayerPosition.getRow()][currentPlayerPosition.getCol()] = EMPTY_ICON;
		this.board[currentBoxPosition.getRow()][currentBoxPosition.getCol()] = PLAYER_ICON;
		if(this.board[futureBoxPosition.getRow()][futureBoxPosition.getCol()] == GOAL_ICON) {
			this.board[futureBoxPosition.getRow()][futureBoxPosition.getCol()] = BOX_ON_GOAL_ICON;
		} else {
			this.board[futureBoxPosition.getRow()][futureBoxPosition.getCol()] = BOX_ICON;
		}
	}

	public char[][] getBoard() {
		return board;
	}

	private boolean isBox(Position position) {
		return board[position.getRow()][position.getCol()] == BOX_ICON;
	}

	private Board movePlayer(String direction) {
		Position futurePossiblePosition = getCoordinatesWithMoveApplied(getPlayerPosition(), direction);
		if(!isValidPosition(futurePossiblePosition))
			return null;
		Board futureBoard = new Board(this);
		System.out.println("FUTURE POSITION GOING " + direction + " IS `" + board[futurePossiblePosition.getRow()][futurePossiblePosition.getCol()] + "");
		if(isEmpty(futurePossiblePosition)) {
			futureBoard.setPlayerNewPosition(getPlayerPosition(), futurePossiblePosition);
		} else if(isBox(futurePossiblePosition)) {
			Position futurePushedBoxPosition = getCoordinatesWithMoveApplied(futurePossiblePosition, direction);
			if(!isValidPosition(futurePushedBoxPosition))
				return null;
			futureBoard.setBoxNewPosition(getPlayerPosition(), futurePossiblePosition, futurePushedBoxPosition);
		} else {
			return null;
		}
		return futureBoard;
	}

	public List<Board> getPossibleMoves() {
		List<Board> possibleMoves = new ArrayList<>();
		Board currentBoard;
		List<String> movesDone = new ArrayList<>();
		if(( currentBoard = this.movePlayer("UP") ) != null) {
			movesDone.add("UP");
			possibleMoves.add(currentBoard);
		}
		if(( currentBoard = this.movePlayer("RIGHT") ) != null) {
			movesDone.add("RIGHT");
			possibleMoves.add(currentBoard);
		}
		if(( currentBoard = this.movePlayer("LEFT") ) != null) {
			movesDone.add("LEFT");
			possibleMoves.add(currentBoard);
		}
		if(( currentBoard = this.movePlayer("DOWN") ) != null) {
			movesDone.add("DOWN");
			possibleMoves.add(currentBoard);
		}
		return possibleMoves;
	}
}
