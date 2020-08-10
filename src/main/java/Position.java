public class Position {

    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
    @Override
    public String toString()
    {
    	return "("+row +", " +col +")";
    }
    
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Position)
		{
			Position toCompare = (Position) o;
			return this.row == toCompare.getRow() && this.col == toCompare.getCol();
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = (int) (31 * hashCode + row);
		hashCode = 31 * hashCode + col;
		return hashCode;
	}
}
