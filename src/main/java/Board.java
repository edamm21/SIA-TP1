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
	private static Integer INFINITY = Integer.MAX_VALUE;
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
			throw new WrongBoardException("Not enough goals for the boxes on this board!");
		if(boxes < goals)
			throw new WrongBoardException("Not enough boxes for the goals on this board!");
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
	
	private boolean isCornered(Position p)
	{
		int i = p.getRow();
		int j = p.getCol();
		
		// NW block
		if(i == 0 && j == 0)
			return true;
		boolean wallsNW = (board[i-1][j] == WALL_ICON) && (board[i][j-1] == WALL_ICON);
		if(i>0 && j>0 && wallsNW && !goalPositions.contains(p))
			return true;
		
		// NE block
		if(i == 0 && j == width-1)
			return true;
		boolean wallsNE = (board[i-1][j] == WALL_ICON) && (board[i][j+1] == WALL_ICON);
		if(i>0 && j<width-1 && wallsNE && !goalPositions.contains(p))
			return true;
		
		// SW block
		if(i == height-1 && j == 0)
			return true;
		boolean wallsSW = (board[i][j-1] == WALL_ICON) && (board[i+1][j] == WALL_ICON);
		if(i<height-1 && j>0 && wallsSW && !goalPositions.contains(p))
			return true;
		
		// SE block
		if(i == height-1 && j == width-1)
			return true;
		boolean wallsSE = (board[i+1][j] == WALL_ICON) && (board[i][j+1] == WALL_ICON);
		if(i<height-1 && j<width-1 && wallsSE && !goalPositions.contains(p))
			return true;
		return false;
	}
	
	public boolean isDeadlock()
	{
		for(Position p : boxPositions)
		{
			if(isCornered(p))
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
	
	public int getMinTotalFreeBoxToGoal()
	{
		if(isCompleted())
			return 0;
		if(isDeadlock())
			return INFINITY;
		int totalMinDistances = 0;
		int manhattanDistance = INFINITY;
		int aux;
		
		for(Position boxCurrentPosition : boxPositions)
		{
			// If this box is still free, measure distance to each empty goal and keep smallest value
			if(!goalPositions.contains(boxCurrentPosition))
			{
				manhattanDistance = INFINITY;
				for(Position goalPosition : goalPositions)
				{
					if(!boxPositions.contains(goalPosition))
					{
						aux = Math.abs(boxCurrentPosition.getRow() - goalPosition.getRow()) + Math.abs(boxCurrentPosition.getCol() - goalPosition.getCol());
						if(aux < manhattanDistance)
							manhattanDistance = aux;
					}
				}
				totalMinDistances += manhattanDistance;
			}
		}
		return totalMinDistances;
	}
	
	public int getRemainingBoxes()
	{
		if(isCompleted())
			return 0;
		if(isDeadlock())
			return INFINITY;
		int boxesRemaining = 0;
		for(Position p : boxPositions)
		{
			if(!goalPositions.contains(p))
				boxesRemaining++;
		}
		return boxesRemaining;
	}
	
	public int getPlayerClosestBoxDistance()
	{
		if(isCompleted())
			return 0;
		if(isDeadlock())
			return INFINITY;
		int closestDistance = INFINITY;
		int currentDistance = 0;
		
		for(Position boxCurrentPosition : boxPositions)
		{
			// If this box is still free, measure distance to player and update min distance
			if(!goalPositions.contains(boxCurrentPosition))
			{
				currentDistance = Math.abs(boxCurrentPosition.getRow() - playerPosition.getRow()) + Math.abs(boxCurrentPosition.getCol() - playerPosition.getCol());
				if(currentDistance < closestDistance)
					closestDistance = currentDistance;
			}
		}
		return closestDistance;
	}

	public int getMix1_2() {
		double a = 0.5, b = 0.5;
		return (int)(a * getMinTotalFreeBoxToGoal() + b * getPlayerClosestBoxDistance());
	}

	public int getMix1_3() {
		double a = 0.5, b = 0.5;
		return (int)(a * getMinTotalFreeBoxToGoal() + b * getRemainingBoxes());
	}

	public int getMix2_3() {
		double a = 0.5, b = 0.5;
		return (int)(a * getPlayerClosestBoxDistance() + b * getRemainingBoxes());
	}

	public int getAllMixed() {
		double a = 0.4, b = 0.2, c = 0.4;
		return (int)(a * getMinTotalFreeBoxToGoal() + b * getPlayerClosestBoxDistance() + c * getRemainingBoxes());
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
