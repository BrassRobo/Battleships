package com.michaelbasov.battleship.gui;

import java.util.ArrayList;

import com.michaelbasov.battleship.gui.Game.GameResponse;

public class Board extends TwoDMatrix 
{
	private ArrayList<Ship> ships;
	private ArrayList<Ship> sunkShips;
	private int horizontal;
	private int verticle;
	/** Returns a board object with the same ship references, but a new map that is blank. **/
	public Board getBlankedOutCopy() throws WrongInputException
	{
		char[][] newMap= new char[verticle][horizontal];
		for(int i =0; i<verticle; i++)
		{
			for(int j= 0; j<horizontal;j++)
			{
				newMap[i][j]='.';
			}
		}
		return new Board(newMap, ships);
	}
	public Board(int x, int y,ArrayList<Ship> ships) throws WrongInputException 
	{
		super(x, y);
		this.ships=ships;
		sunkShips= new ArrayList<Ship>();
		horizontal=x;
		verticle=y;
	}
	public Board(char[][] map,ArrayList<Ship> ships) throws WrongInputException 
	{
		super(map[0].length, map.length);
		this.ships=ships;
		this.matrix=map;
		sunkShips= new ArrayList<Ship>();
		horizontal=map[0].length;
		verticle=map.length;
	}
	public int getHorizontal() {
		return horizontal;
	}
	public int getVerticle() {
		return verticle;
	}
	/** Returns wether the shot Hit, Missed, or Sunk a ship. Puts sunk ships into sunkShips arrayList **/
	public GameResponse fire(int x, int y) 
	{
		if(matrix[x][y]=='H'||matrix[x][y]=='M'||matrix[x][y]=='S')
		{
			return GameResponse.INVALID;
		}
		for(Ship ship : ships)
		{
			if(ship.hit(x,y).equals("HIT"))
			{
				return GameResponse.HIT;
			}else
				if(ship.hit(x,y).equals("SUNK"))
			{
				sunkShips.add(ship);
				ships.remove(ship);
				return GameResponse.SUNK;
			}
		}
		return GameResponse.MISS;
		
	}
	public ArrayList<Ship> getSunk() 
	{
			return sunkShips;
	}
	/** Returns true if there are any unsunk ships. Else : false **/
	public boolean shipsLeft()
	{
		if(ships.size()==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/** Tells you if a matrix contains a certain character. **/
	public boolean hasChar(char c)
	{
		for(int i=0; i< xSize; i++)
		{
			for(int j=0; j< ySize; j++)
			{
				if(matrix[i][j]==c)
				{
					return true;
				}
				
			}
		}
		return false;
	}
	/** Tells you if a set of coardinates are on your grid. **/
	public boolean onGrid(int x, int y)
	{
		if(x>=0 && x< xSize)
		{
			if(y>=0 && y< ySize)
			{
				return true;
			}
			return false;
		}
	return false;	
	}
	@Override
	public String toString() {
		return "Board [ships=" + ships + "]";
	}
	

}
