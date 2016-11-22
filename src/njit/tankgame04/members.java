/*
 * ����̹�˺��ӵ���
 */

package njit.tankgame04;

import java.awt.*;
import java.util.*;

class Tank
{
    boolean live = true;
	int x=0;
    int y=0;
    Direct direct;
    Color color;
    int speed;
    Vector<Bullet> bullets=new Vector<Bullet>();
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
	
	//����̹�˺���
    public void drawTank(Tank tank,Graphics g)
    {
    	//�趨������ɫ
    	g.setColor(tank.color);
      
    	//���ݷ��򻭳������Ρ�Բ�κ��ߣ����̹��
    	switch(tank.direct)
    	{       
	    	case UP:
	            //1.��������ľ���
	            g.fill3DRect(tank.x, tank.y, 5, 30,false);           
	            //2.�����ұߵľ���
	            g.fill3DRect(tank.x+15, tank.y, 5, 30,false);            
	            //3.����̹�˵��м����
	            g.fill3DRect(tank.x+5, tank.y+5, 10, 20,false);
	            //4.�����м��Բ
	            g.fillOval(tank.x+4, tank.y+10,10,10);
	            //5.������
	            g.drawLine(tank.x+9, tank.y+15, tank.x+9, tank.y);
	            g.drawLine(tank.x+10, tank.y+15, tank.x+10, tank.y);
	            break;
	
	    	case RIGHT:
	    		g.fill3DRect(tank.x, tank.y, 30, 5, false);
	    		g.fill3DRect(tank.x, tank.y+15, 30, 5, false);
	    		g.fill3DRect(tank.x+5, tank.y+5, 20, 10, false);
	    		g.fillOval(tank.x+10, tank.y+4, 10, 10);
	    		g.drawLine(tank.x+15, tank.y+9, tank.x+30, tank.y+9);
	    		g.drawLine(tank.x+15, tank.y+10, tank.x+30, tank.y+10);
	    		break;
	    	   
	    	case DOWN:
	    		g.fill3DRect(tank.x, tank.y, 5, 30,false);
	    		g.fill3DRect(tank.x+15, tank.y, 5, 30,false);
	    		g.fill3DRect(tank.x+5, tank.y+5, 10, 20,false);
	    		g.fillOval(tank.x+4, tank.y+10,10,10);
	    		g.drawLine(tank.x+9, tank.y+15, tank.x+9, tank.y+30);
	    		g.drawLine(tank.x+10, tank.y+15, tank.x+10, tank.y+30);
	    		break;
	        
	    	case LEFT:
	    		g.fill3DRect(tank.x, tank.y, 30, 5, false);
	    		g.fill3DRect(tank.x, tank.y+15, 30, 5, false);
	    		g.fill3DRect(tank.x+5, tank.y+5, 20, 10, false);
	    		g.fillOval(tank.x+10, tank.y+4, 10, 10);
	    		g.drawLine(tank.x+15, tank.y+9, tank.x, tank.y+9);
	    		g.drawLine(tank.x+15, tank.y+10, tank.x, tank.y+10);
	    		break;	    	   
    	}
            
    }
    
	//���巢���ӵ�����
	public void fire(){
		
		switch (direct) {
		case UP:
			bullet=new Bullet(x+9, y, Direct.UP,2);
			bullets.add(bullet);
			break;
		
		case RIGHT:
			bullet=new Bullet(x+30, y+9, Direct.RIGHT,2);
			bullets.add(bullet);
			break;
		case DOWN:
			bullet=new Bullet(x+9, y+30, Direct.DOWN,2);
			bullets.add(bullet);
			break;
		case LEFT:
			bullet=new Bullet(x, y+9, Direct.LEFT,2);
			bullets.add(bullet);
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

class Enemy extends Tank implements Runnable
{	
	public Enemy(int x,int y,Direct direct,Color color,int speed)
	{
		super(x, y, direct, color, speed);
		
	}
	
	Vector<Enemy> enemies=new Vector<Enemy>();
	int times=0;


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{

			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			switch (this.direct) 
			{
				//̹����������
				case UP:
					for(int i=0;i<30;i++)
					{
						if(y>0)
						{
							y-=speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	
					}
					break;
				//����
				case RIGHT:
					for(int i=0;i<30;i++)
					{
						if (x < (Global.wide-30)) {
							x += speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	
					}
					break;
				//����
				case DOWN:
					for(int i=0;i<30;i++)
					{
						if (y < (Global.height-30)) {
							y += speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	
					}
					break;
				//����
				case LEFT:
					for(int i=0;i<30;i++)
					{
						if (x > 0) {
							x -= speed;
						}
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	
					}
					break;
					
				default:
					break;
			}
			//��̹���������һ������
			this.direct = Direct.valueOf((int)(Math.random()*4));
			this.times++;
			
			if (times%2==0 && this.live == true && bullets.size() < Global.enemyBulletNumber) 
			{
				this.fire();
			}	
		}
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
			if(x<0||x>Global.wide || y<0||y>Global.height)
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

class Bomb{
	
	//���屬ը������
	int x,y;
	
	//��ը������ʱ��
	int lifeTime=9;
	
	Boolean live=true;
	
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//��������ʱ��
	public void lifeDown()
	{
		if(lifeTime>0)
		{
			lifeTime--;
		}else{
			this.live=false;
		}		
	}
	
}//End of Bomb

