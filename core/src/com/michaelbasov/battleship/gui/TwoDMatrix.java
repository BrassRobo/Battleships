package com.michaelbasov.battleship.gui;
public class TwoDMatrix 
{
protected char[][] matrix;
protected int xSize;
protected int ySize;

 public TwoDMatrix(int x, int y)throws WrongInputException
 {
 if(x<=0 || y<=0)
 {
	throw new WrongInputException("An Array cannot be made of size "+x+" X "+y);
 }
  xSize=x;
  ySize=y;
  matrix = new char[x][y];
 }
 public void setCell(int x, int y, char c) throws WrongInputException
 {
	if(checkWithinMatrix(x,y))
	{
	 matrix[x][y]=c;
	}
	else
	{
	 throw new WrongInputException("Cell "+x+" X "+y+" is not within the Grid.");
	}
 }
 private boolean checkWithinMatrix(int x, int y)
 {
 	 if(x< xSize && x>=0)
	 {
		if(y<ySize && y>=0)
		{
			return true;
		}
		else
		{
		return false;
		}
	 }
	 else
	 {
	  return false;
	 }
 }
 
 public char[][] getMatrix()
 {
	return matrix;
}
public char readCell(int x, int y) throws WrongInputException
 {
if(checkWithinMatrix(x,y))
	{
	 return matrix[x][y];
	}
	else
	{
	 throw new WrongInputException("Cell "+x+" X "+y+" is not within the Grid.");
	}
 }

}


