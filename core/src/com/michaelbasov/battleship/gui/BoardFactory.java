package com.michaelbasov.battleship.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class BoardFactory
{
	public enum FactoryResponse 
	{
	VALID, INVALID
				}
	private char[][] map;
	private ArrayList<Ship> customShips;
	private Integer width;
	private Integer height;
	private int numberOfShips;
	private Board board;
	/** Creates a game board from an existing file. 
	 * @throws IOException **/
	public BoardFactory()//Used for random/read in boards
	{
		
	}
	public BoardFactory(int size)//Used to create custom board.
	{
		customShips= new ArrayList<Ship>();
		width=size;
		height=size;
		map = new char[width][height];//Create a new blank map
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				map[i][j]='~';
			}
		}
		numberOfShips=((width*height)/100)*5;
		
	}
	
	public Board getBlankBoard(int size) throws WrongInputException
	{
		width=size;
		height=size;
		map = new char[height][width]; //Fills the new board with blank characters.
		for(int i = 0; i <height;i++)
		{
			for(int j=0; j<width;j++)
			{
				map[j][i]=((char)126);
			}
		}
		ArrayList<Ship> tempShips=new ArrayList<Ship>();
		return new Board(map,tempShips); //Returns a completed board.
	}
	
//	public Board readBoardFromFile(String fileLocation) throws WrongInputException, IOException
//{
//	ArrayList<Ship> ships = new ArrayList<Ship>();
//	
//	InputStream inputStream = new FileInputStream(fileLocation);
//	Scanner scanner = new Scanner(inputStream);
//	String line = scanner.nextLine();
//	
//	width=Integer.parseInt(line.substring(0, line.toUpperCase().indexOf('X')));
//	height=Integer.parseInt(line.substring(line.toUpperCase().indexOf('X')+1,line.length()));
//	
//	//Board board= new Board(width,height);
//	map = new char[width][height];
//	char[][] map2 = new char[width][height];
//	
//	char c;
//	for(int i = 0; i <height;i++)
//	{
//	line = scanner.nextLine().toUpperCase();
//	for(int j=0; j<width;j++)
//	{
//		c=line.charAt(j);
//		//board.setCell(j, i,c);
//		map[j][i]=c;
//		map2[j][i]=c;
//	}
//	
//	
//	
//	}
//	for(int i = 0; i <width;i++)
//	{
//	for(int j=0; j<height;j++)
//	{
//		c=map[i][j];
//		switch (c) {
//        case 'P':  
//        		ships.add(getShip(i, j, 2,'P'));
//                 break;
//        case 'U':  
//    		ships.add(getShip(i, j, 3,'U'));
//             break;
//        case 'D':  
//    		ships.add(getShip(i, j, 4,'D'));
//             break;
//        case 'B':  
//    		ships.add(getShip(i, j, 5,'B'));
//             break;
//        case 'A':  
//    		ships.add(getShip(i, j, 6,'A'));
//             break;
//		}
//	}
//	board= new Board(map2,ships);
//	}
//	
//	inputStream.close();
//	return board;
//}
/** a Helper method that creates the ship objects. **/
private Ship getShip(int x, int y,int size, char letter) throws WrongInputException
{
	Ship ship = new Ship();
	if(x+size<width&& map[x+1][y]==letter) //If ship is horizontal.
	{
		for(int i=0; i<size;i++)
		{
			if(map[x+i][y]==letter)
			{
				ship.addSquare(x+i, y);
				map[x+i][y]='.';
			}
			else
			{
				throw new WrongInputException("Map File is improperly formatted."); //Ship too short.
			}
		}
		if(map[x+size][y]==letter) //Ship too long
		{
			throw new WrongInputException("Map File is improperly formatted; battleship "+ letter+ " is too long.");
		}
	}else if(y+size<height&& map[x][y+1]==letter) //Ship is verticle.
	{
		for(int i=0; i<size;i++)
		{
			if(map[x][y+i]==letter) 
			{
				ship.addSquare(x, y+i);
				map[x][y+i]='.';
			}
			else //Ship too short.
			{
				throw new WrongInputException("Map File is improperly formatted.");
			}
		}
		if(map[x][y+size]==letter) //Ship too long.
		{
			throw new WrongInputException("Map File is improperly formatted; battleship "+ letter+ " is too long.");
		}
	}else //Something else is wrong
	{
		throw new WrongInputException("Map File is improperly formatted.");
	}
	return ship;
}

/**Creates a board of the given width and height. **/
public Board createRandomBoard(int width, int height) throws WrongInputException
{
	map = new char[height][width]; //Fills the new board with blank characters.
	for(int i = 0; i <height;i++)
	{
	for(int j=0; j<width;j++)
	{
		map[j][i]=((char)126);
	}
}
	ArrayList<Ship> tempShips=createRandomShips(width,height); //Creates an ArrayList of Ships.
	for(Ship ship : tempShips) //Modifies map to hold ship cell info.
	{
		ArrayList<Cell> tempCells= ship.getSquares();
		int tempShipSize=ship.size();
		for(Cell cell : tempCells)
		{
			switch (tempShipSize)
			{
			case 2 :
				map[cell.getX()][cell.getY()]='P';
				break;
			case 3 :
				map[cell.getX()][cell.getY()]='U';
				break;
			case 4 :
				map[cell.getX()][cell.getY()]='D';
				break;
			case 5 :
				map[cell.getX()][cell.getY()]='B';
				break;
			case 6 :
				map[cell.getX()][cell.getY()]='A';
				break;
			}
			
		}
	}
	return new Board(map,tempShips ); //Returns a completed board.
}

/** A helper method that generates 5 random ships. They can be horizontal or verticle and will be on the grid **/
private ArrayList<Ship> createRandomShips(int horizontal, int verticle)
{
	ArrayList<Ship> ships = new ArrayList<Ship>();
	ArrayList<Cell> occupiedCells = new ArrayList<Cell>();
	
	int i=6;//Largest ship
	int shipSet=((horizontal*verticle)/100);
	boolean valid=true;
	for(int j=0;j<shipSet;j++)//Ships come in a set of 5 per 100 squares.
	{	
		while(i>1)
		{
			Ship temp =createRandomShip(horizontal,verticle,i);
			
			for(Cell cell : temp.getSquares())
			{
				for(Cell existingCell : occupiedCells)
				{
					if(cell.equals(existingCell))
					{
					valid=false; //Invalid because it intersects an existing ship.
					}
				}
				if(cell.getX()>=horizontal||cell.getY()>=verticle)
				{
					valid=false; // Invalid because it goes off the grid.
				}
			}
			if(valid)
			{
			ships.add(temp);
			occupiedCells.addAll(temp.getSquares());
			i--; //Go to next ship.
			}
			else
			{
				valid=true; // Try again.
			}
		}
		i=6;
	}
	return ships;
}
/** Creates a horizontal or verticle ship. The ships starting point is on the grid, but it might go off it.  **/
private Ship createRandomShip(int horizontal, int verticle, int size)
{
	Random random = new Random();
	int x = random.nextInt(horizontal);
	int y = random.nextInt(verticle);
	boolean orientation = random.nextBoolean();
	
	Ship ship = new Ship();
	ship.addSquare(x, y);
	if(orientation)
	{
		for(int i=1;i<size;i++)
		{
			ship.addSquare(x+i, y);
		}
	}else
	{
		for(int i=1;i<size;i++)
		{
			ship.addSquare(x, y+i);
		}
	}
	return ship;
}
public FactoryResponse addShip(int x,int y, int shipSize, boolean horizontal)
{
	Ship ship = new Ship();
	if(horizontal)
	{
		if(x+shipSize>width)//Keeps ships from being placed off the map
		{
			return FactoryResponse.INVALID;
		}
		for(int i=0; i<shipSize;i++)
		{
			ship.addSquare(x+i, y);
		}
	}else
	{
		if(y+shipSize>height)//Keeps ships from being placed off the map
		{
			return FactoryResponse.INVALID;
		}
		for(int i=0; i<shipSize;i++)
		{
			ship.addSquare(x, y+i);
		}
	}
	for(Ship tempShip : customShips)
	{
		for(Cell occupiedCell : tempShip.getSquares())
		{
			for(Cell tempCell : ship.getSquares())
			{
				if(occupiedCell.equals(tempCell))
				{
					return FactoryResponse.INVALID;
				}
			}
		}
	}
	customShips.add(ship);
	return FactoryResponse.VALID;
}
public Board getCustomBoard() throws WrongInputException
{
	for(Ship ship : customShips)
	{
		for(Cell cell : ship.getSquares())
		{
			try
			{
			map[cell.getX()][cell.getY()]='A';
			}
			catch(Exception e)
			{
				
			}
		}
	}
	return new Board(map,customShips);
}
}
