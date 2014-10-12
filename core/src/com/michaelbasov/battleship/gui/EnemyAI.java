package com.michaelbasov.battleship.gui;

import java.util.Random;

import com.michaelbasov.battleship.gui.Game.GameResponse;

public class EnemyAI 
{
	private Board enemyBoard;
	private int enemyBoardNumber;
	private int horizontal;
	private int verticle;
	private Game game;
	private Random random;
	private boolean acquired=false;//Hit a target, not yet sunk
	private int acquiredDirection=-1;//Hit a target twice
	private boolean reversedOnce=false;//Sets to true when going in opposite direction of ship.
	private boolean checkForOrphans=false; //Used to check for orphaned hits after every sunk ship.
	private int[] enemyShips= {0,0,1,1,1,1,1};
	private int numberOfTimesFFAHasRun=0;
	
	private int acquiredX;
	private int acquiredY;
	
	private int lastX;
	private int lastY;
	private GameResponse gameResponse;
	private int direction; //Up=0,Right=1,Down=2,Left=3, -1=None
 public EnemyAI(int horizontal, int verticle, Board enemyBoard, Game game, int enemyBoardNumber)
 {
	 this.enemyBoard=enemyBoard;
	 this.horizontal=horizontal;
	 this.verticle=verticle;
	 this.game=game;
	 this.enemyBoardNumber=enemyBoardNumber;
	 random = new Random();
 }
 public GameResponse fire() throws WrongInputException
 {
	 if(acquired==false) //If You Don't have a target
	 {
		 if(enemyBoard.hasChar('H')) //You sunk a ship but managed to hit another ship.
		 {
			 System.out.println("FOUND AN H");
			int enemyH=enemyBoard.getHorizontal();
			int enemyW=enemyBoard.getVerticle();
			for(int i=0;i<enemyW;i++)
			{
				for(int j=0;j<enemyH;j++)
				{
					if(enemyBoard.readCell(i, j)=='H')
					{
					System.out.println("FOUND AT "+i+" "+j);
					 acquired=true; 
					 acquiredX=i;
					 acquiredY=j;
					 return this.fireFourAround();
					}
				}
			}
		 }
//		 if(checkForOrphans==true)
//		 {
//			 if(checkForOrphans)
//			 {
//				 return fireFourAround();//If you found an orphan, fire.
//			 }
//		 }
		 gameResponse = randomFire(); //Fire at random
		 if(gameResponse.equals(GameResponse.HIT)) //If you hit
		 {
			 acquired=true; //You now have a target.
			 acquiredX=lastX;//Keep track of original hit location;
			 acquiredY=lastY;
		 }
		 return  gameResponse;
	 }
	 else //Target Acquired
	 {
		 if(acquiredDirection==-1)
		 {
			 try
			 {
			 return fireFourAround();
			 }
			 catch (Error e)
			 {
				 return this.randomFire();
			 }
		 }
		 else
		 {
		 return fireInAcquiredDirection();
		 }
	 }
 }
 
private GameResponse randomFire() throws WrongInputException 
{
	int x = random.nextInt(horizontal);
	int y = random.nextInt(verticle);
	
	if(validAttack(x,y)) //If cell is on grid and you haven't hit this cell before.
	{
	lastX=x;
	lastY=y;
	return game.fire(x, y, enemyBoardNumber); // Hit it
	}
	else
	{
		return randomFire();// Find another cell
	}
}

private GameResponse fireFourAround() throws WrongInputException 
{
	int x=0;
	int y=0;
	int i=1;
	if(numberOfTimesFFAHasRun>4)
	{
		i=2;
	}
	direction=random.nextInt(4);
	switch (direction)
	{
	case 0 : //Up
		x=acquiredX;
		y=acquiredY-i;
		break;
	case 1 : //Right
		x=acquiredX+i;
		y=acquiredY;
		break;
	case 2 : //Down
		x=acquiredX;
		y=acquiredY+i;
		break;
		
	case 3 : //Left
		x=acquiredX-i;
		y=acquiredY;
		break;
		default:
		
	}
		if(validAttack(x,y)) //If valid Attack
		{
			numberOfTimesFFAHasRun=0;
			gameResponse = game.fire(x, y, enemyBoardNumber); //Fire
			if(gameResponse==GameResponse.HIT) //If you get a second hit
			{
				lastX=x;//Record last hit location.
				lastY=y;
				acquiredDirection=direction; //Record where you were travelling.
			}else if(gameResponse==GameResponse.SUNK)
			{
				acquired= false;
				lastX=x;
				lastY=y;
				acquiredDirection=-1;;
				checkForOrphans=true;
			}
			return gameResponse;
		}
		else
		{
			numberOfTimesFFAHasRun++;//Increment counter to prevent Stack Overflow.
			if(numberOfTimesFFAHasRun>8)
			{
				numberOfTimesFFAHasRun=0;
				return this.randomFire();
			}
			return fireFourAround();
		}
		
	
}
private boolean validAttack(int x, int y) throws WrongInputException
{
	if(enemyBoard.onGrid(x, y)) //If hit location is on grid
	{
		if(enemyBoard.readCell(x, y)=='.') //If You haven't hit this cell before.
		{
			return true;
		}
		return false;
	}
	return false;
}
private boolean checkForOrphans() throws WrongInputException
{
	if(enemyBoard.hasChar('H'))//If there is a hit on the board after a sink it is an orphan.
	{
	for(int i=0;i<enemyBoard.getHorizontal();i++)
	{
		for(int j=0;i<enemyBoard.getVerticle();j++)
		{
			if(enemyBoard.readCell(i, j)=='H')
			{
				 acquired=true; //The Orphan becomes your target.
				 acquiredX=lastX;
				 acquiredY=lastY;
				 checkForOrphans=false;//You only run this check once
				 return true;
			}
		}
	}
	}
	checkForOrphans=false;
	return false;
	
}
private GameResponse fireInAcquiredDirection() throws WrongInputException
{
	int x=0;
	int y=0;
	switch (acquiredDirection)
	{
	case 0 : //Up
		x=lastX;
		y=lastY-1;
		break;
	case 1 : //Right
		x=lastX+1;
		y=acquiredY;
		break;
	case 2 : //Down
		x=lastX;
		y=lastY+1;
		break;
		
	case 3 : //Left
		x=lastX-1;
		y=lastY;
		break;
	}
	if(validAttack(x,y))
	{
		gameResponse = game.fire(x, y, enemyBoardNumber); 
		if(gameResponse==GameResponse.HIT)//If You hit, keep going.
		{
			lastX=x;
			lastY=y;
		}else if(gameResponse==GameResponse.SUNK)//If you sunk go back to random.
		{
			acquired= false;
			lastX=x;
			lastY=y;
			acquiredDirection=-1;
		}else if(gameResponse==GameResponse.MISS)//If you miss then go back to square 1, opposite direction.
		{
			lastX=acquiredX;
			lastY=acquiredY;
			acquiredDirection=((acquiredDirection+2)%4);
		}
		return gameResponse; 
	}else //If you can't make the attack, go in the opposite direction.
	{
		lastX=acquiredX;
		lastY=acquiredY;
		if(reversedOnce==false)
		{
		acquiredDirection=((acquiredDirection+2)%4);
		reversedOnce=true;
		return gameResponse; 
		}else
		{
			reversedOnce=false;
			return fireFourAround();
		}
	}
}
 
}
