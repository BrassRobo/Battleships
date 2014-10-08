package com.michaelbasov.battleship.gui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.michaelbasov.battleship.gui.Game.GameResponse;

public class BattleshiplibGDX extends ApplicationAdapter  implements ApplicationListener, InputProcessor  {
	
	public enum GameState {PLAY,WON,LOST,PLACE,CREDITS};
	
	SpriteBatch batch;
	BitmapFont font;
	Game game;
	ViewPort yourBoard, enemyBoard;
	int boardSize=10;
	ShipPlacer shipPlacer;
	Texture water,ship ,blank, hit, sunk, miss,back, place,logo, musicNote,musicNoteCancelled,rotate,credits,popup,gameWon,gameLost,playAgain;
	Music music;
	Sound explosion;
	Button musicButton, rotateButton,creditsButton, playButton;
	boolean playSound=true;
	GameState gameState, tempGameState;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
        font.setColor(Color.BLACK);
		try {
			gameState=GameState.PLACE;
			game = new Game(boardSize, true, false);
			yourBoard= new ViewPort(400,400,boardSize,boardSize,10,30);
			enemyBoard= new ViewPort(400,400,boardSize,boardSize,430,30);
			
			water = new Texture("waterTile1.png");
			blank = new Texture("waterTileBlank1.png");
			hit = new Texture("hitTile1.png");
			sunk = new Texture("sunkTile1.png");
			miss = new Texture("missTile1.png");
			ship = new Texture("shipTile1.png");
			back= new Texture("backTile2.jpg");
			place= new Texture("placeTile1.png");
			logo = new Texture("logo1.png");
			rotate = new Texture("rotate.png");
			musicNote=new Texture("music.png");
			credits=new Texture("credits2.png");
			popup= new Texture("popup.png");
			gameWon= new Texture("gameWon1.png");
			gameLost= new Texture("gameLost1.png");
			playAgain = new Texture("playAgain.png");
			musicNoteCancelled=new Texture("musicCancel.png");
			
			rotateButton=new Button(rotate,32,440,32,32);
			musicButton = new Button(musicNote, 780, 450, 32, 32);
			creditsButton = new Button(credits, 388, 0, 64, 32);
			playButton= new Button(playAgain,388,40,96,32);
			
			music=Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
			music.play();
			music.setLooping(true);
			explosion=Gdx.audio.newSound(Gdx.files.internal("sound.mp3"));
			shipPlacer=new ShipPlacer(yourBoard, 0, this, place,boardSize);
			readBoard(yourBoard,0);
			readBoard(enemyBoard,3);
			Gdx.input.setInputProcessor(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void readBoard(ViewPort board, int gameBoard)
	{
		char[][] matrix = game.getPlayerMatrix(gameBoard);
		for(int i=0; i<boardSize;i++)
		{
			for(int j=0; j<boardSize;j++)
			{
				if(matrix[i][j]=='~')
				{
					board.setCell(i, j, water);
				}
				else if(matrix[i][j]=='.')
				{
					board.setCell(i, j, blank);
				}
				else if(matrix[i][j]=='H')
				{
					board.setCell(i, j, hit);
				}else if(matrix[i][j]=='S')
				{
					board.setCell(i, j, sunk);
				}else if(matrix[i][j]=='M')
				{
					board.setCell(i, j, miss);
				}
				else
				{
					board.setCell(i, j, ship);
				}
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		this.renderBackground();
		yourBoard.render(batch);
		enemyBoard.render(batch);
		batch.draw(logo,220,350);
		musicButton.render(batch);		
		if(gameState==GameState.PLACE)
		{
			shipPlacer.hoverPlacingShip();
			rotateButton.render(batch);
		}
		
		creditsButton.render(batch);
		//System.out.println(Gdx.input.getX()+" "+Gdx.input.getY());
		
		if(gameState==GameState.CREDITS)
		{
			this.renderCredits();
		}
		if(gameState==GameState.WON)
		{
			this.renderWon();
			playButton.render(batch);
		}
		if(gameState==GameState.LOST)
		{
			this.renderLost();
			playButton.render(batch);
		}
		
		batch.end();
	}


	private void renderCredits() 
	{
		batch.draw(popup,168,38);
		font.draw(batch, "Coded by : Michael Basov", 200, 390);
		font.draw(batch, "Tiles : http://crawl.develz.org/", 200, 360);
		font.draw(batch, "Steel Background : http://www.123rf.com/", 200, 330);
		font.draw(batch, "Music : eccogulliver http://charas-project.net/", 200, 300);
	}
	private void renderWon() 
	{
		batch.draw(popup,168,38);
		batch.draw(gameWon,250,350);
	}
	private void renderLost() 
	{
		batch.draw(popup,168,38);
		batch.draw(gameLost,250,350);
	}
	private void renderBackground()
	{
		int width=Gdx.graphics.getWidth(), height=Gdx.graphics.getHeight();
		int backWidth=back.getWidth(), backHeight=back.getHeight();
		for(int i=0;i<width;i=i+backWidth)
		{
			for(int j=0;j<height;j=j+backHeight)
			{
			batch.draw(back,i,j);
			}
		}
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character==' ')
		{
		if(gameState==GameState.PLACE)
		 {
			shipPlacer.switchOrientation();
		 }
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		 if(button == Buttons.LEFT)
		 {
			 if(yourBoard.mouseInGrid(screenX, screenY))
			 {
			 int[] location =yourBoard.returnMouseLocation(screenX, screenY);
			 if(gameState==GameState.PLACE)
			 {
				 shipPlacer.placeShip();
			 }
			 }else if (enemyBoard.mouseInGrid(screenX, screenY))
			 {
				 if(gameState==GameState.PLAY)
				 	{
					 int[] location =enemyBoard.returnMouseLocation(screenX, screenY);
					 try {
						GameResponse response= game.fire(location[0], location[1], 3);
						if(response==GameResponse.HIT && playSound)
						{
							explosion.play();
						}
						if(response==GameResponse.INVALID)
						{
							//Do nothing if trying to shoot a space that has been hit before
						}else if(response==GameResponse.WON)
						{
							this.win();
						}else
						{
						response=game.autoFire(0);
						if(response==GameResponse.HIT && playSound)
						{
							explosion.play();
						}
						if(response==GameResponse.WON)
							{
							this.lose();
							}
						}
						readBoard(yourBoard,0);
						readBoard(enemyBoard,3);
					} catch (WrongInputException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 } else if(musicButton.mouseOverButton(screenX, screenY))
			 {
				 if(playSound)
				 {
					 music.pause();
					 playSound=false;
					 musicButton.setImage(musicNoteCancelled);
				 }else
				 {
					 music.play();
					 playSound=true;
					 musicButton.setImage(musicNote);
				 }
			 }
			 if(gameState==GameState.PLACE)
			 {
				 if(rotateButton.mouseOverButton(screenX, screenY))
				 {
					 shipPlacer.switchOrientation();
				 }
			 }
			 if(gameState==GameState.PLACE || gameState==GameState.PLAY)
			 {
				 
				 if(creditsButton.mouseOverButton(screenX, screenY))
				 {
					 tempGameState=gameState;
					 gameState=GameState.CREDITS;
				 }
			 }else
			 if(gameState==GameState.CREDITS)
			 {
				 gameState=tempGameState;
			 }else
				 if(gameState==GameState.WON || gameState==GameState.LOST)
				 {
					 if(playButton.mouseOverButton(screenX, screenY))
					 {
						    music.dispose();
							this.create();
					}
				 }
	     }else if(button==Buttons.RIGHT)
	     {
	    	 if(gameState==GameState.PLACE)
			 {
				 shipPlacer.switchOrientation();
			 }
	     }
		return false;
	}

	private void lose() {
		gameState=GameState.LOST;
		
	}
	private void win() 
	{
		gameState=GameState.WON;
		
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	public void setBoard(int boardNum,Board board) 
	{
		game.setBoard(boardNum, board);
		
	}
	public void setGameState(GameState gameState) {
		this.gameState=gameState;
		
	}
}
