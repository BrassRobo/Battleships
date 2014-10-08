package com.michaelbasov.battleship.gui;

import java.util.ArrayList;

public class Ship 
{
	private ArrayList<Cell> squares;
	private boolean sunk=false;
	
	public Ship()
	{
		squares = new ArrayList<Cell>();
	}
	public void addSquare(int x, int y)
	{
		squares.add(new Cell(x,y,false));
	}
	public ArrayList<Cell> getSquares()
	{
		return squares;
	}
	@Override
	public String toString() {
		return "Ship [squares=" + squares + "]";
	}
	public int size()
	{
		return squares.size();
	}
	/** Called to see if an attack at X Y will hit, miss or sink this ship. A combination of hit detection and death animation. **/
	public String hit(int x, int y) 
	{
		for(Cell cell : squares)
		{
			if(cell.getX()==x&&cell.getY()==y)
			{
				cell.hit();
				for(Cell square : squares)
				{
					if(square.isHit()==false)
					{
						return "HIT";
					}
				}
				sunk=true;
				return "SUNK";
			}
		}
		return "MISS";
		
	}

}
