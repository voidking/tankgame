/*
 * ����̹�˺��ӵ���
 */

package njit.tankgame03;

import java.awt.Color;

class Tank
{
    int x=0;
    int y=0;
    Direct direct;
    Color color;
    int speed;
    Bullet bullet=null;
    
	//���캯��
	public Tank(int x,int y,Direct d,Color color,int speed)
    {
       this.x=x;
       this.y=y;
       this.direct=d;
       this.color=color;
       this.speed=speed;
    }
	
	//���巢���ӵ�����
	public void fire(){
		
		switch (direct) {
		case UP:
			bullet=new Bullet(x+9, y, Direct.UP,2);
			break;
		
		case RIGHT:
			bullet=new Bullet(x+30, y+9, Direct.RIGHT,2);
			break;
		case DOWN:
			bullet=new Bullet(x+9, y+30, Direct.DOWN,2);
			break;
		case LEFT:
			bullet=new Bullet(x, y+9, Direct.LEFT,2);
			break;
			
		default:
			break;
		}
		
		//�����ӵ��߳�
		Thread t=new Thread(bullet);
		t.start();
	}//End of fire()
   
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

class Bullet implements Runnable{
	
	//ʵ��Runnable�ӿ�
	public void run(){
		
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			switch (direct) {
				case UP:
					y-=speed;
					break;
				case RIGHT:
					x+=speed;
					break;
				case DOWN:
					y+=speed;
					break;
				case LEFT:
					x-=speed;
					break;
	
				default:
					break;
			}
			
			//���ӵ�������ܣ���壩ʱ��ȥ��ͬʱѭ������
			if(x<0||x>Global.jframeX||y<0||y>Global.jframeY)
			{
				live = false;
				break;
			}
		}
	}
	
	int x;
	int y;
	Direct direct;
	int speed = 1;
	boolean live = true;

	public Bullet(int x,int y,Direct direct,int speed){
		this.x = x;
		this.y = y;
		this.direct = direct;
		this.speed = speed;
	}
	
	
}//End of Bullet

