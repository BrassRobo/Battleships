package com.michaelbasov.battleship.gui;

public class Cell 
{
	private int x;
	private int y;
	private boolean hit;
	
	public Cell(int x, int y, boolean hit)
	{
		this.x=x;
		this.y=y;
		this.hit=hit;
	}
	public int getX()
	{
		return x;
	}
	@Override
	public boolean equals(Object obj) 
	{
		if(obj.getClass()!=this.getClass())
		{
			return false;
		}else
		{
			if(this.x==((Cell)obj).getX()&&this.y==((Cell)obj).getY())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	public int getY()
	{
		return y;
	}
	@Override
	public String toString() {
		return "Cell [x" + x + ", y" + y + ", ]";
	}
	public boolean isHit()
	{
		return hit;
	}
	public void hit()
	{
		hit=true;
	}
}
