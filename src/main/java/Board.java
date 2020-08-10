import java.util.HashSet;
import java.util.Set;

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
}
