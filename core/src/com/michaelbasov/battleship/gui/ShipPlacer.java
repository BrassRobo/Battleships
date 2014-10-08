package com.michaelbasov.battleship.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.michaelbasov.battleship.gui.BattleshiplibGDX.GameState;
import com.michaelbasov.battleship.gui.BoardFactory.FactoryResponse;

public class ShipPlacer 
{
	ViewPort yourBoard;
	int boardToRead;
	int hoveredOverX=0, hoveredOverY=0, nextShip=6, boardSize;
	BattleshiplibGDX game;
	Texture place;
	BoardFactory boardFactory;
	boolean horizontal=true;
	public ShipPlacer(ViewPort yourBoard, int boardToRead, BattleshiplibGDX game, Texture place,int boardSize)
	{
		this.yourBoard=yourBoard;
		this.boardToRead=boardToRead;
		this.game=game;
		this.place=place;
		this.boardSize=boardSize;
		this.boardFactory=new BoardFactory(boardSize);
	}
	public void hoverPlacingShip() 
	{
		int mouseX=Gdx.input.getX();
		int mouseY=Gdx.input.getY();
		if(yourBoard.mouseInGrid(mouseX, mouseY))
		{
			if(hoveredOverX!=mouseX||hoveredOverY!=mouseY)
			{
			hoveredOverX=mouseX;
			hoveredOverY=mouseY;
			game.readBoard(yourBoard, boardToRead);
			int cellLocation[]=yourBoard.returnMouseLocation(mouseX, mouseY);
			if(horizontal)
			{
				if(cellLocation[0]>=0 && cellLocation[0]+nextShip-1<yourBoard.getxCells()&& cellLocation[1]>=0 && cellLocation[1]<yourBoard.getyCells())
					{
						for(int i=0;i<nextShip;i++)
						{
							yourBoard.setCell(cellLocation[0]+i, cellLocation[1], place);
						}
					}
				}else
				{
				if(cellLocation[0]>=0 && cellLocation[0]<yourBoard.getxCells()&& cellLocation[1]>=0 && cellLocation[1]+nextShip-1<yourBoard.getyCells())
					{
						for(int i=0;i<nextShip;i++)
						{
							yourBoard.setCell(cellLocation[0], cellLocation[1]+i, place);
						}
					}
				}
			}
		}
		
	}
	public void placeShip() 
	{
		int mouseX=Gdx.input.getX();
		int mouseY=Gdx.input.getY();
		int cellLocation[]=yourBoard.returnMouseLocation(mouseX, mouseY);
		FactoryResponse response=boardFactory.addShip(cellLocation[0], cellLocation[1], nextShip, horizontal);
		if(response==FactoryResponse.VALID)
		{
			try {
				Board tempBoard=boardFactory.getCustomBoard();
				game.setBoard(boardToRead,tempBoard);
				game.readBoard(yourBoard, boardToRead);
				if(nextShip>2)
				{
					nextShip--;
				}
				else
				{
					game.setGameState(GameState.PLAY);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void switchOrientation() 
	{
		if(horizontal==true)
		{
			horizontal=false;
		}else
		{
			horizontal=true;
		}
		hoveredOverX=0; 
		hoveredOverY=0;
		
	}
}
