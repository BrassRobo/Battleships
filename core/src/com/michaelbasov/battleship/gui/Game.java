package com.michaelbasov.battleship.gui;

import java.io.IOException;
import java.util.ArrayList;

public class Game



{
	public enum GameResponse {
		HIT, MISS, SUNK, WON, INVALID
					}
	private ArrayList<Board> players;
	private EnemyAI enemyAI;
	private int boardSize;

	public Game(int boardSize) throws WrongInputException, IOException 
	{
		this(boardSize, false,false);
	}	
	
	public Game(int boardSize, boolean custom,boolean twoCustom) throws WrongInputException, IOException
	{
		this.boardSize=boardSize;
		BoardFactory boardFactory = new BoardFactory();
		players = new ArrayList<Board>();
		if(custom)
		{
			players.add(boardFactory.getBlankBoard(boardSize));
		}else
		//Board temp =boardFactory.readBoardFromFile("src/com/netbuilder/michael/battleships/files/MyBoard.txt"); //Player 1 Board : 0
//		if(temp.getHorizontal()==boardSize)//The game Uses the imported board if it is the size you requested.
//		{
//		players.add(temp);
//		}else
		{
			players.add(boardFactory.createRandomBoard(boardSize, boardSize));
		}
		if(twoCustom)
		{
			players.add(boardFactory.getBlankBoard(boardSize));
		}else
		{
			players.add(boardFactory.createRandomBoard(boardSize, boardSize)); //Player 2 Board : 1
		}
		players.add(players.get(0).getBlankedOutCopy()); //Player 1 Board(BlankedOut) : 2
		players.add(players.get(1).getBlankedOutCopy()); //Player 2 Board(BlankedOut) : 3
		enemyAI= new EnemyAI(boardSize, boardSize, players.get(2), this, 0);
		//players.add(boardFactory.readBoardFromFile("src/com/netbuilder/michael/battleships/files/MyBoard.txt"));
	}

	public char[][] getPlayerMatrix(int i)
	{
		return players.get(i).getMatrix();
	}
	public GameResponse fire(int x, int y, int board) throws WrongInputException
	{
		GameResponse result= players.get(board).fire(x,y);
		if(result.equals(GameResponse.INVALID))
		{
//			System.out.println("INVALID SHOT "+x+" , "+y+"board "+board);
//			char[][] temp =players.get(board).getMatrix();
//			System.out.println(temp[x][y]);
//			temp =players.get(board+2).getMatrix();
//			System.out.println(temp[x][y]);
			//You don't do anything if the attack is invalid, such as if you already hit that square.
		}else
		if(result.equals(GameResponse.HIT))
		{
			players.get(board).setCell(x, y, 'H');
			players.get((board+2)%4).setCell(x, y, 'H'); //board+2 is the blank version of the board the enemy has.
		}else
		if(result.equals(GameResponse.MISS))
		{
			players.get(board).setCell(x, y, 'M');
			players.get((board+2)%4).setCell(x, y, 'M');
		}else
		if(result.equals(GameResponse.SUNK))
		{
			players.get(board).setCell(x, y, 'S');
			players.get((board+2)%4).setCell(x, y, 'S');
			ArrayList<Ship> temp=players.get(board).getSunk();
			for(Ship ship : temp)
			{
				ArrayList<Cell> tempCells=ship.getSquares();
				for(Cell tempCell : tempCells)
				{
					players.get(board).setCell(tempCell.getX(), tempCell.getY(), 'S');
					players.get((board+2)%4).setCell(tempCell.getX(), tempCell.getY(), 'S');
				}
			}
			if(!players.get(board).shipsLeft())
			{
				return GameResponse.WON;
			}
		}
		return result;
	}
	public GameResponse autoFire(int board) throws WrongInputException 
	{
		return enemyAI.fire();
	}
	public void setBoard(int boardNumber,Board board)
	{
		players.set(boardNumber, board);
	}

	public void resetAI() 
	{
		enemyAI= new EnemyAI(boardSize, boardSize, players.get(2), this, 0);
	}


	
}
