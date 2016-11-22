package njit.tmp;

/*********************************
 *程序文件名称：Members.java
 *功能：放置坦克类、玩家坦克类、敌人坦克类、
 *子弹类、爆炸特效类、圣诞树类
 *********************************/

import java.util.*;

 //坦克类
class Tank
{
	//坦克的横坐标
	int x=0;
	//坦克的纵坐标
	int y=0;
	//坦克的方向direct为0、1、2、3时分别对应坦克头向上， 坦克头向右、坦克头向下、坦克头向左。
	int direct=0;
	//坦克的速度
	int speed=1;
	//坦克的颜色
	int color;
	//坦克活着或死亡
	boolean isLive=true;
	//坦克的构造器
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//坦克各种属性的getter方法与setter方法
	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public int getDirect()
	{
		return direct;
	}

	public void setDirect(int direct)
	{
		this.direct = direct;
	}
	public int getX() 
	{
		return x;
	}
	public void setX(int x) 
	{
		this.x = x;
	}
	public int getY() 
	{
		return y;
	}
	public void setY(int y) 
	{
		this.y = y;
	}

	public int getColor() 
	{
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}	
}

//子弹类（Runnable和Thread都用于多线程）
class Bullet implements Runnable
{
	//子弹的横坐标
	int x;
	//子弹的纵坐标
	int y;
	//子弹的方向
	int direct;
	//子弹的速度
	int speed=2;
	//子弹是否活着
	boolean isLive=true;
	//子弹的构造器
	public Bullet(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	public void run()
	{
		while(true)
		{
			try
			{			
				//为了减少内存负荷
				Thread.sleep(50);			
			}
			catch(Exception e)
			{			
			}
			switch(direct)
			{
				case 0:y=y-speed;break;
				case 1:x=x+speed;break;
				case 2:y=y+speed;break;
				case 3:x=x-speed;break;
			}
			//System.out.println("子弹坐标：x="+x+"y"+y);
			//判断该子弹是否碰到战场边缘，碰到边缘时子弹死亡
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;
				break;
			}
		}//while	
	}//run()
}//Bullet


//玩家的坦克
class Hero extends Tank
{
	int speed=3;
	//子弹属于坦克，为了连发可用集合， Vector稳定性较高
	Vector<Bullet> bs=new Vector<Bullet>();
	//单颗子弹
	Bullet b=null;
	//玩家坦克的构造器
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//坦克上下左右移动的方法,if判断使玩家坦克不会出界
	public void moveUp()
	{
		if(y-4>0)
		this.y=this.y-speed;
	}
	public void moveRight()
	{
		if(x+29<400)
		this.x=this.x+speed;
	}
	public void moveDown()
	{
		if(y+34<300)
		this.y=this.y+speed;
	}
	public void moveLeft()
	{
		if(x-9>0)
		this.x=this.x-speed;
	} 
	//坦克开火的方法
	public void shotEnemy()
	{
		switch(this.direct)
		{	
			case 0: //生成子弹
					b=new Bullet(x+10,y-4,0);
					bs.add(b);
					break;
			case 1:
					b=new Bullet(x+29,y+15,1);
					bs.add(b);
					break;
			case 2:
					b=new Bullet(x+10,y+34,2);
					bs.add(b);
					break;
			case 3:
					b=new Bullet(x-9,y+15,3);
					bs.add(b);
					break;
		}
		//启动子弹线程
		Thread t=new Thread(b);
		t.start();
	}//shotEnemy		
}//Hero
	
	
//敌人的坦克也做成线程类
class EnemiesTank extends Tank implements Runnable
{
	//定义这个变量是为了控制子弹发弹间隔
	int times=0;
	//定义一个向量，可以访问到MyPanel上所有敌人的坦克
	Vector<EnemiesTank> ets=new Vector<EnemiesTank>();
	//定义一个向量来存放敌人的子弹
	Vector<Bullet> bs=new Vector<Bullet>();
	//敌人添加子弹应该在刚刚创建敌人坦克和敌人的坦克子弹死亡后
	public EnemiesTank(int x,int y)
	{
		super(x, y);
	}
	
	//判断是否碰到了别的敌人坦克
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;		
		switch(this.direct)
		{
			//我的坦克向上或向下
			case 0:				
			case 2:			
					//取出所有的敌人坦克
					for(int i=0;i<ets.size();i++)
					{
						//取出第一个坦克
						EnemiesTank et=ets.get(i);
						//如果不是自己
						if(et!=this)
						{
							//如果其他敌人的方向是向下或者向上
							if(et.direct==0||et.direct==2)
							{
								if(this.x-20<et.x&&et.x<this.x+20&&this.y-30<et.y&&et.y<this.y+30)
									return true;
							}
							//如果其他敌人的坦克是向右或向左
							if(et.direct==3||et.direct==1)
							{
								if(this.x-30<et.x-5&&et.x-5<this.x+20&&this.y-20<et.y+5&&et.y+5<this.y+30)
									return true;
							}
						}
					}
				break;
			case 1:
			case 3:
			//坦克向右或向左
			//取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemiesTank et=ets.get(i);
				//如果不是自己
				if(et!=this)
				{
					//如果敌人的方向是向下或者向上
					if(et.direct==0||et.direct==2)
					{
						if(this.x-20-5<et.x&&et.x<this.x+30-5&&this.y-30+5<et.y&&et.y<this.y+20+5)
							return true;
					}
					if(et.direct==3||et.direct==1)
					{
						if(this.x-30<et.x&&et.x<this.x+30&&this.y-20<et.y&&et.y<this.y+20)
							return true;
					}
				}
			}
			break;
			
		}		
		return b;
	}
	
	public void run() 
	{
		
		while(true)
		{
			switch(this.direct)
			{
				//坦克向上移动
				case 0:
					//这个循环用于使敌人坦克在变向前行走一段
					for(int i=0;i<30;i++)
					{
						//保证坦克不出边界
						if(y-4>0&&!this.isTouchOtherEnemy())
						{
															
							y=y-speed;
						}
						try
						{
							Thread.sleep(50);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					break;
				//坦克向右移动	
				case 1:
					for(int i=0;i<30;i++)
					{
						if(x+29<400&&!this.isTouchOtherEnemy())
						{
							x=x+speed;
						}
						try
						{
							Thread.sleep(50);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					break;
				//坦克向下移动
				case 2:
					for(int i=0;i<30;i++)
					{
						if(y+34<300&&!this.isTouchOtherEnemy())
						{
							y=y+speed;
						}
						try
						{
							Thread.sleep(50);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					break;
					//坦克向左移动
				case 3:
					for(int i=0;i<30;i++)
					{
						if(x-9>0&&!this.isTouchOtherEnemy())
						{
							x=x-speed;
						}
						try
						{
							Thread.sleep(50);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					break;
			}//switch
				this.times++;
				//因为30*50=1500ms，当times%2==0时经过了3秒
				if(times%2==0)
				{
					if(isLive)
					{
						if(bs.size()<5)
						{
							Bullet b=null;
							switch(direct)
							{
							case 0:
								b=new Bullet(x+10,y-4,0);
								bs.add(b);
								break;
							case 1:
								b=new Bullet(x+29,y+15,1);
								bs.add(b);
								break;
							case 2:
								b=new Bullet(x+10,y+34,2);
								bs.add(b);
								break;
							case 3:
								b=new Bullet(x-9,y+15,3);
								bs.add(b);
								break;						
							}
							//启动子弹线程
							Thread t=new Thread(b);
							t.start();
						}//if(bs.size()<5)
					}//if(isLive)				
				}//if(times%2==0)
				
				//让坦克随机产生一个新的方向
				this.direct=(int)(Math.random()*4);
				//判断敌人坦克是否死亡
				if(this.isLive==false)
				{//让坦克死亡后退出线程
					break;
				}
			
			}//while
		}//run
	}//class









//做爆炸特效的炸弹类(如果坐标不变的不该做成线程)
class Bomb
{
	int x,y;
	//炸弹生命（爆炸过程的剩余时间）
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}	
	//减少生命值，处于不同生命值的时将显示不同图片
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}
		else
		{
			this.isLive=false;	
		}
	}
}
//画下一棵树的类
class Tree
{
	int x,y;
	public Tree(int x,int y)
	{
		this.x=x;
		this.y=y;
	}	
}
