package njit.tankgame02;

import java.awt.Color;

class Tank
{
    int x=0;
    int y=0;
    Direct direct;
    Color color;
    int speed;
	
	public Tank(int x,int y,Direct d,Color color,int speed)
    {
       this.x=x;
       this.y=y;
       this.direct=d;
       this.color=color;
       this.speed=speed;
    }
   
}//End of Tank

class Hero extends Tank
{
 
    public Hero(int x, int y,Direct direct,Color color,int speed)
    {
       super(x, y,direct,color,speed);
    }
   
}//End of Hero

class Enemy extends Tank
{
	public Enemy(int x,int y,Direct direct,Color color,int speed)
	{
		super(x, y, direct, color, speed);
		
	}

}//End of Enemy



