package com.michaelbasov.battleship.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button 
{
	private Texture image;
	private int xLocation, yLocation, xSize, ySize;
	public Button(Texture image,int xLocation,int yLocation, int xSize, int ySize)
	{
		this.image=image;
		this.xLocation=xLocation;
		this.yLocation=yLocation;
		this.xSize=xSize;
		this.ySize=ySize;
	}
	public boolean mouseOverButton(int mouseX, int mouseY)
	{
		mouseY=Gdx.graphics.getHeight()-mouseY;
		if(mouseX>=xLocation&&mouseX<xLocation+xSize&&mouseY>=yLocation&&mouseY<yLocation+ySize)
		{
			return true;
		}
		return false;
	}
	public void setImage(Texture image) {
		this.image = image;
	}
	public void render(SpriteBatch batch)
	{
	batch.draw(image,xLocation,yLocation, xSize, ySize);	
	}

}
