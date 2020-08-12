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

	public Board(Board board) {
		this.height = board.getHeight();
		this.width = board.getWidth();
		this.board = new char[height][width];
		for(int i=0; i < height; i++)
			for(int j=0; j < width; j++)
				this.board[i][j] = board.getBoard()[i][j];
		this.playerPosition = new Position(board.getPlayerPosition().getRow(), board.getPlayerPosition().getCol());
		this.boxPositions = new HashSet<>();
		boxPositions.addAll(board.getBoxPositions());
		this.goalPositions = new HashSet<>();
		goalPositions.addAll(board.getGoalPositions());
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
				if(string[index] == GOAL_ICON || string[index] == BOX_ON_GOAL_ICON || string[index] == PLAYER_ON_GOAL_ICON)
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
		boolean boxUpOne = board[playerPosition.getRow() - 1][playerPosition.getCol()] == BOX_ICON || board[playerPosition.getRow() - 1][playerPosition.getCol()] == BOX_ON_GOAL_ICON;
		boolean spaceUpTwo = board[playerPosition.getRow() - 2][playerPosition.getCol()] == EMPTY_ICON || board[playerPosition.getRow() - 2][playerPosition.getCol()] == GOAL_ICON;
		if(playerPosition.getRow() > 1 && boxUpOne && spaceUpTwo)
			return true;
		return false;
	}
	
	public boolean canPlayerMoveRight()
	{
		if(playerPosition.getCol() == width)
			return false;
		if(board[playerPosition.getRow()][playerPosition.getCol() + 1] == EMPTY_ICON)
			return true;
		boolean boxToTheRight = board[playerPosition.getRow()][playerPosition.getCol() + 1] == BOX_ICON || board[playerPosition.getRow()][playerPosition.getCol() + 1] == BOX_ON_GOAL_ICON;
		boolean spaceToBoxRight = board[playerPosition.getRow()][playerPosition.getCol() + 2] == EMPTY_ICON || board[playerPosition.getRow()][playerPosition.getCol() + 2] == GOAL_ICON;
		if(playerPosition.getCol() > 1 && boxToTheRight && spaceToBoxRight)
			return true;
		return false;
	}
	
	public boolean canPlayerMoveLeft()
	{
		if(playerPosition.getCol() == 0)
			return false;
		if(board[playerPosition.getRow()][playerPosition.getCol() - 1] == EMPTY_ICON)
			return true;
		boolean boxToTheLeft = board[playerPosition.getRow()][playerPosition.getCol() - 1] == BOX_ICON || board[playerPosition.getRow()][playerPosition.getCol() - 1] == BOX_ON_GOAL_ICON;
		boolean spaceToBoxLeft = board[playerPosition.getRow()][playerPosition.getCol() - 2] == EMPTY_ICON || board[playerPosition.getRow()][playerPosition.getCol() - 2] == GOAL_ICON;
		if(playerPosition.getCol() > 1 && boxToTheLeft && spaceToBoxLeft)
			return true;
		return false;
	}
	
	public boolean canPlayerMoveDown()
	{
		if(playerPosition.getRow() == height)
			return false;
		if(board[playerPosition.getRow() + 1][playerPosition.getCol()] == EMPTY_ICON)
			return true;
		boolean boxDownOne = board[playerPosition.getRow() + 1][playerPosition.getCol()] == BOX_ICON || board[playerPosition.getRow() + 1][playerPosition.getCol()] == BOX_ON_GOAL_ICON;
		boolean spaceDownTwo = board[playerPosition.getRow() + 2][playerPosition.getCol()] == EMPTY_ICON || board[playerPosition.getRow() + 2][playerPosition.getCol()] == GOAL_ICON;
		if(playerPosition.getRow() > 1 && boxDownOne && spaceDownTwo)
			return true;
		return false;
	}

	public boolean isValidPositionForPlayer(Position position) {
		int row = position.getRow();
		int col = position.getCol();
		return row >= 0 && row < height && col >= 0 && col < width && !isWall(row, col);
	}
	
	public boolean isValidPositionForBox(Position position) {
		int row = position.getRow();
		int col = position.getCol();
		return row >= 0 && row < height && col >= 0 && col < width && isEmpty(position);
	}

	private boolean isEmpty(Position position) {
		return board[position.getRow()][position.getCol()] == EMPTY_ICON || board[position.getRow()][position.getCol()] == GOAL_ICON;
	}

	public boolean isWall(int row, int col) {
		return board[row][col] == WALL_ICON;
	}

	public boolean isGoal(int row, int col) {
		return board[row][col] == GOAL_ICON || board[row][col] ==BOX_ON_GOAL_ICON;
	}

	// Returns the new position assuming that move is valid
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

	public void setPlayerPosition(Position futurePosition) {
		if(this.board[playerPosition.getRow()][playerPosition.getCol()] == PLAYER_ON_GOAL_ICON)
			this.board[playerPosition.getRow()][playerPosition.getCol()] = GOAL_ICON;
		else
			this.board[playerPosition.getRow()][playerPosition.getCol()] = EMPTY_ICON;
		
		if(this.board[futurePosition.getRow()][futurePosition.getCol()] == GOAL_ICON)
			this.board[futurePosition.getRow()][futurePosition.getCol()] = PLAYER_ON_GOAL_ICON;
		else
			this.board[futurePosition.getRow()][futurePosition.getCol()] = PLAYER_ICON;
		this.playerPosition = futurePosition;
	}

	private void setBoxNewPosition(Position currentBoxPosition, Position futureBoxPosition)
	{
		if(this.board[currentBoxPosition.getRow()][currentBoxPosition.getCol()] == BOX_ON_GOAL_ICON)
			this.board[currentBoxPosition.getRow()][currentBoxPosition.getCol()] = GOAL_ICON;
		else
			this.board[currentBoxPosition.getRow()][currentBoxPosition.getCol()] = EMPTY_ICON;
		
		if(this.board[futureBoxPosition.getRow()][futureBoxPosition.getCol()] == GOAL_ICON) {
			this.board[futureBoxPosition.getRow()][futureBoxPosition.getCol()] = BOX_ON_GOAL_ICON;
		} else {
			this.board[futureBoxPosition.getRow()][futureBoxPosition.getCol()] = BOX_ICON;
		}
		boxPositions.remove(currentBoxPosition);
		boxPositions.add(futureBoxPosition);
	}

	public char[][] getBoard() {
		return board;
	}

	private boolean isBox(Position position) {
		return board[position.getRow()][position.getCol()] == BOX_ICON || board[position.getRow()][position.getCol()] == BOX_ON_GOAL_ICON;
	}
	
	public boolean isDeadlock()
	{
		int i, j;
		for(Position p : boxPositions)
		{
			i = p.getRow();
			j = p.getCol();
			
			// NW block
			if(i == 0 && j == 0)
				return true;
			boolean wallsNW = (board[i-1][j] == WALL_ICON || board[i-1][j] == BOX_ICON) && (board[i][j-1] == WALL_ICON || board[i][j-1] == BOX_ICON); // Blocked up and left
			if(i>0 && j>0 && wallsNW && !goalPositions.contains(p))
				return true;
			
			// NE block
			if(i == 0 && j == width-1)
				return true;
			boolean wallsNE = (board[i-1][j] == WALL_ICON || board[i-1][j] == BOX_ICON) && (board[i][j+1] == WALL_ICON || board[i][j+1] == BOX_ICON);
			if(i>0 && j<width-1 && wallsNE && !goalPositions.contains(p))
				return true;
			
			// SW block
			if(i == height-1 && j == 0)
				return true;
			boolean wallsSW = (board[i][j-1] == WALL_ICON || board[i][j-1] == BOX_ICON) && (board[i+1][j] == WALL_ICON || board[i+1][j] == BOX_ICON);
			if(i<height-1 && j>0 && wallsSW && !goalPositions.contains(p))
				return true;
			
			// SE block
			if(i == height-1 && j == width-1)
				return true;
			boolean wallsSE = (board[i+1][j] == WALL_ICON || board[i+1][j] == BOX_ICON) && (board[i][j+1] == WALL_ICON || board[i][j+1] == BOX_ICON);
			if(i<height-1 && j<width-1 && wallsSE && !goalPositions.contains(p))
				return true;
		}
		return false;
	}

	public Board movePlayer(String direction) {
		Position futurePossiblePosition = getCoordinatesWithMoveApplied(getPlayerPosition(), direction);
		if(!isValidPositionForPlayer(futurePossiblePosition))
			return null;
		Board futureBoard = new Board(this);
		if(isEmpty(futurePossiblePosition)) {
			futureBoard.setPlayerPosition(futurePossiblePosition);
		} else if(isBox(futurePossiblePosition)) {
			Position futurePushedBoxPosition = getCoordinatesWithMoveApplied(futurePossiblePosition, direction);
			if(!isValidPositionForBox(futurePushedBoxPosition))
				return null;
			futureBoard.setBoxNewPosition(futurePossiblePosition, futurePushedBoxPosition);
			futureBoard.setPlayerPosition(futurePossiblePosition);
		} else {
			return null;
		}
		//System.out.println("\nGOING " +direction +" IT WOULD LOOK LIKE THIS: " +futureBoard.hashCode());
		//futureBoard.printBoard();
		return futureBoard;
	}

	public List<Board> getPossibleMoves() {
		List<Board> possibleMoves = new ArrayList<>();
		Board currentBoard;
		if(( currentBoard = this.movePlayer("UP") ) != null)
			possibleMoves.add(currentBoard);
		if(( currentBoard = this.movePlayer("DOWN") ) != null)
			possibleMoves.add(currentBoard);
		if(( currentBoard = this.movePlayer("LEFT") ) != null)
			possibleMoves.add(currentBoard);
		if(( currentBoard = this.movePlayer("RIGHT") ) != null)
			possibleMoves.add(currentBoard);
		return possibleMoves;
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
	
	@Override
	public String toString()
	{
		String s = "\n";
		for(int i=0; i < height; i++)
		{
			for(int j=0; j < width; j++)
			{
				s += board[i][j];
			}
			s+="\n";
		}
		return s;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		for(int i=0; i < height; i++)
		{
			for(int j=0; j < width; j++)
			{
				hashCode = 31 * hashCode + board[i][j];
			}
		}
		return hashCode;
	}
}
