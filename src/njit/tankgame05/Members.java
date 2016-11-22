/*
 * 定义坦克和子弹类
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

//记录类,同时也可以保存玩家的设置
class Recorder
{
	//记录每关有多少敌人
	private static int enNum=20;
	//设置我有多少可以用的人
	private static int myLife=3;
	//记录总共消灭了多少敌人
	private static int allEnNum=0;
	//从文件中恢复记录点
	static Vector<Node>  nodes=new Vector<Node>();
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	private  Vector<Enemy> ets=new Vector<Enemy>();
	
	
	
	//完成读取认为
	public Vector<Node> getNodesAndEnNums()
	{
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n="";
			//先读取第一行
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
				//后打开则先关闭
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		return nodes;
		
	}
	
	
	//保存击毁敌人的数量和敌人坦克坐标,方向
	
	public  void keepRecAndEnemyTank()
	{
		try {
			
			//创建
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			System.out.println("size="+ets.size());
			//保存当前活的敌人坦克的坐标和方向
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				Enemy et=ets.get(i);
				
				if(et.live)
				{
					//活的就保存
					String recode=et.x+" "+et.y+" "+et.direct;
					
					//写入
					bw.write(recode+"\r\n");
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
		
			//关闭流
			try {
				//后开先关闭
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	
	//从文件中读取，记录
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
				//后打开则先关闭
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	//把玩家击毁敌人坦克数量保存到文件中
	public static void keepRecording()
	{
		try {
			
			//创建
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
		
			//关闭流
			try {
				//后开先关闭
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
	
	//减少敌人数
	public static void reduceEnNum()
	{
		enNum--;
	}
	//消灭敌人
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
    
	//构造函数
	public Tank(int x,int y,Direct d,Color color,int speed)
    {
       this.x=x;
       this.y=y;
       this.direct=d;
       this.color=color;
       this.speed=speed;
    }
	
	//画出坦克函数
    public void drawTank(Tank tank,Graphics g)
    {
    	//设定画笔颜色
    	g.setColor(tank.color);
      
    	//根据方向画出长方形、圆形和线，组成坦克
    	switch(tank.direct)
    	{       
	    	case UP:
	            //1.画出左面的矩形
	            g.fill3DRect(tank.x, tank.y, 5, 30,false);           
	            //2.画出右边的矩形
	            g.fill3DRect(tank.x+15, tank.y, 5, 30,false);            
	            //3.画出坦克的中间矩形
	            g.fill3DRect(tank.x+5, tank.y+5, 10, 20,false);
	            //4.画出中间的圆
	            g.fillOval(tank.x+4, tank.y+10,10,10);
	            //5.画出线
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
    
	//定义发射子弹函数
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
		
		//启动子弹线程
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
				//坦克正在向上
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
				//向右
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
				//向下
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
				//向左
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
				//让坦克随机产生一个方向
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
					//另一个坦克向上或向下
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//左点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
						//右点
						if(this.x + 20 >= anotherEnemy.x && this.x +20 <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//另一个坦克向左或向右
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//左点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
						//右点
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
					//另一个坦克向上或向下
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//左点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 30)
						{
							return true;
						}
						//右点
						if(this.x + 20 >= anotherEnemy.x && this.x +20 <= anotherEnemy.x + 20 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//另一个坦克向左或向右
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//左点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30 && this.y + 30 >= anotherEnemy.y && this.y + 30 <= anotherEnemy.y + 20)
						{
							return true;
						}
						//右点
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
					//另一个坦克向上或向下
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//上点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
						//下点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 20 && this.y +20 >= anotherEnemy.y  && this.y + 20 <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//另一个坦克向左或向右
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//上点
						if(this.x >= anotherEnemy.x && this.x <= anotherEnemy.x + 30  && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
						//下点
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
					//另一个坦克向上或向下
					if(anotherEnemy.direct == Direct.UP || anotherEnemy.direct == Direct.DOWN)
					{
						//上点
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 20 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 30)
						{
							return true;
						}
						//下点
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 20 && this.y +20 >= anotherEnemy.y  && this.y + 20 <= anotherEnemy.y + 30)
						{
							return true;
						}
					}
					//另一个坦克向左或向右
					if(anotherEnemy.direct == Direct.LEFT || anotherEnemy.direct == Direct.RIGHT)
					{
						//上点
						if(this.x + 30 >= anotherEnemy.x && this.x + 30 <= anotherEnemy.x + 30 && this.y >= anotherEnemy.y && this.y <= anotherEnemy.y + 20)
						{
							return true;
						}
						//下点
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
	
	//实现Runnable接口
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
				//当子弹超出框架（面板）时死去，同时循环结束
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
	
	//定义爆炸的坐标
	int x,y;
	
	//爆炸的生命时间
	int lifeTime=9;
	
	Boolean live=true;
	
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//减少生命时间
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

