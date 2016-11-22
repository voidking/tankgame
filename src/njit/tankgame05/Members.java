/*
 * ����̹�˺��ӵ���
 */

package njit.tankgame05;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

class Node{
	int x;
	int y;
	int direct;
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}

//��¼��,ͬʱҲ���Ա�����ҵ�����
class Recorder
{
	//��¼ÿ���ж��ٵ���
	private static int enNum=20;
	//�������ж��ٿ����õ���
	private static int myLife=3;
	//��¼�ܹ������˶��ٵ���
	private static int allEnNum=0;
	//���ļ��лָ���¼��
	static Vector<Node>  nodes=new Vector<Node>();
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	private  Vector<Enemy> ets=new Vector<Enemy>();
	
	
	
	//��ɶ�ȡ��Ϊ
	public Vector<Node> getNodesAndEnNums()
	{
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n="";
			//�ȶ�ȡ��һ��
			n=br.readLine();
			allEnNum=Integer.parseInt(n);
			while((n=br.readLine())!=null)
			{
				String []xyz=n.split(" "); 
				
				Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
				nodes.add(node);
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			
			try {
				//������ȹر�
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		return nodes;
		
	}
	
	
	//������ٵ��˵������͵���̹������,����
	
	public  void keepRecAndEnemyTank()
	{
		try {
			
			//����
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			System.out.println("size="+ets.size());
			//���浱ǰ��ĵ���̹�˵�����ͷ���
			for(int i=0;i<ets.size();i++)
			{
				//ȡ����һ��̹��
				Enemy et=ets.get(i);
				
				if(et.live)
				{
					//��ľͱ���
					String recode=et.x+" "+et.y+" "+et.direct;
					
					//д��
					bw.write(recode+"\r\n");
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
		
			//�ر���
			try {
				//���ȹر�
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	
	//���ļ��ж�ȡ����¼
	public static void getRecoring()
	{
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			allEnNum=Integer.parseInt(n);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			
			try {
				//������ȹر�
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	//����һ��ٵ���̹���������浽�ļ���
	public static void keepRecording()
	{
		try {
			
			//����
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
		
			//�ر���
			try {
				//���ȹر�
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	
	//���ٵ�����
	public static void reduceEnNum()
	{
		enNum--;
	}
	//�������
	public static void addEnNumRec()
	{
		allEnNum++;
	}
	public static int getAllEnNum() {
		return allEnNum;
	}
	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}


	public  Vector<Enemy> getEts() {
		return ets;
	}


	public  void setEts(Vector<Enemy> ets1) {
		
		this.ets = ets1;
		System.out.println("ok");
	}
}

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
	public void fire(int bulletSpeed){
		
		switch (direct) {
		case UP:
			bullet=new Bullet(x+9, y, Direct.UP,bulletSpeed);
			bullets.add(bullet);
			break;
		
		case RIGHT:
			bullet=new Bullet(x+30, y+9, Direct.RIGHT,bulletSpeed);
			bullets.add(bullet);
			break;
		case DOWN:
			bullet=new Bullet(x+9, y+30, Direct.DOWN,bulletSpeed);
			bullets.add(bullet);
			break;
		case LEFT:
			bullet=new Bullet(x, y+9, Direct.LEFT,bulletSpeed);
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
	
	int times=0;


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(Global.isStart)
		{
			while (!Global.isStop) 
			{
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				switch (this.direct) {
				//̹����������
				case UP:
					for (int i = 0; i < 30; i++) {
						if (y > 0 && !this.isTouch()) {
							y -= speed;
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
					for (int i = 0; i < 30; i++) {
						if (x < (Global.panelWide - 30) && !this.isTouch()) {
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
					for (int i = 0; i < 30; i++) {
						if (y < (Global.panelHeight - 30) && !this.isTouch()) {
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
					for (int i = 0; i < 30; i++) {
						if (x > 0 && !this.isTouch()) {
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
				this.direct = Direct.valueOf((int) (Math.random() * 4));
				this.times++;
				if (times % 2 == 0 && this.live == true
						&& bullets.size() < Global.enemyBulletNumber) {
					this.fire(Global.enemyBulletSpeed);
				}
			}	
		}
	}
	
	Vector<Enemy> enemies = new Vector<Enemy>();
	public void getPanelTank(Vector<Enemy> enemies)
	{
		this.enemies = enemies;
	}
	
	public boolean isTouch()
	{		
		switch (this.direct) {
		case UP:
			for(int i = 0; i < enemies.size(); i++)
			{
				Enemy anotherEnemy = enemies.get(i);
				if(anotherEnemy != this)
				{
					//��һ��̹�����ϻ�����
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//���
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
						//�ҵ�
						if(this.x + 20 >= anotherEnemy.x && this.x +20 <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//��һ��̹�����������
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//���
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
						//�ҵ�
						if(this.x + 20 >= anotherEnemy.x && this.x + 20 <= anotherEnemy.x + 30 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
					}
					
				}
				
			}
			break;
		case DOWN:
			for(int i = 0; i < enemies.size(); i++)
			{
				Enemy anotherEnemy = enemies.get(i);
				if(anotherEnemy != this)
				{
					//��һ��̹�����ϻ�����
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//���
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 30)
						{
							return true;
						}
						//�ҵ�
						if(this.x + 20 >= anotherEnemy.x && this.x +20 <= anotherEnemy.x + 20 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//��һ��̹�����������
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//���
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 20)
						{
							return true;
						}
						//�ҵ�
						if(this.x + 20 >= anotherEnemy.x && this.x + 20 <= anotherEnemy.x + 30 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 20)
						{
							return true;
						}
					}					
				}
				
			}
			break;
		case LEFT:
			for(int i = 0; i < enemies.size(); i++)
			{
				Enemy anotherEnemy = enemies.get(i);
				if(anotherEnemy != this)
				{
					//��һ��̹�����ϻ�����
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//�ϵ�
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
						//�µ�
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y +20 >= anotherEnemy.y  && this.y + 20 <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//��һ��̹�����������
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//�ϵ�
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30  && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
						//�µ�
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30 && this.y +20 >= anotherEnemy.y && this.y + 20 <= anotherEnemy.y + 20)
						{
							return true;
						}
					}
					
				}
				
			}
			break;
		case RIGHT:
			for(int i = 0; i < enemies.size(); i++)
			{
				Enemy anotherEnemy = enemies.get(i);
				if(anotherEnemy != this)
				{
					//��һ��̹�����ϻ�����
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//�ϵ�
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
						//�µ�
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 20 && this.y +20 >= anotherEnemy.y  && this.y + 20 <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//��һ��̹�����������
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//�ϵ�
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 30 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
						//�µ�
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 30 && this.y +20 >= anotherEnemy.y && this.y + 20 <= anotherEnemy.y + 20)
						{
							return true;
						}
					}
					
				}
				
			}
			break;

		default:
			break;
		}
		
		return false;		
	}

}//End of Enemy

class Bullet implements Runnable{
	
	//ʵ��Runnable�ӿ�
	public void run(){
		
		while(Global.isStart)
		{
			while (!Global.isStop) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				switch (direct) {
				case UP:
					y -= speed;
					break;
				case RIGHT:
					x += speed;
					break;
				case DOWN:
					y += speed;
					break;
				case LEFT:
					x -= speed;
					break;

				default:
					break;
				}
				//���ӵ�������ܣ���壩ʱ��ȥ��ͬʱѭ������
				if (x < 0 || x > Global.panelWide || y < 0 || y > Global.panelHeight) {
					live = false;
					break;
				}
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

