package njit.tmp;

/*********************************
 *�����ļ����ƣ�Members.java
 *���ܣ�����̹���ࡢ���̹���ࡢ����̹���ࡢ
 *�ӵ��ࡢ��ը��Ч�ࡢʥ������
 *********************************/

import java.util.*;

 //̹����
class Tank
{
	//̹�˵ĺ�����
	int x=0;
	//̹�˵�������
	int y=0;
	//̹�˵ķ���directΪ0��1��2��3ʱ�ֱ��Ӧ̹��ͷ���ϣ� ̹��ͷ���ҡ�̹��ͷ���¡�̹��ͷ����
	int direct=0;
	//̹�˵��ٶ�
	int speed=1;
	//̹�˵���ɫ
	int color;
	//̹�˻��Ż�����
	boolean isLive=true;
	//̹�˵Ĺ�����
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//̹�˸������Ե�getter������setter����
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

//�ӵ��ࣨRunnable��Thread�����ڶ��̣߳�
class Bullet implements Runnable
{
	//�ӵ��ĺ�����
	int x;
	//�ӵ���������
	int y;
	//�ӵ��ķ���
	int direct;
	//�ӵ����ٶ�
	int speed=2;
	//�ӵ��Ƿ����
	boolean isLive=true;
	//�ӵ��Ĺ�����
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
				//Ϊ�˼����ڴ渺��
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
			//System.out.println("�ӵ����꣺x="+x+"y"+y);
			//�жϸ��ӵ��Ƿ�����ս����Ե��������Եʱ�ӵ�����
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;
				break;
			}
		}//while	
	}//run()
}//Bullet


//��ҵ�̹��
class Hero extends Tank
{
	int speed=3;
	//�ӵ�����̹�ˣ�Ϊ���������ü��ϣ� Vector�ȶ��Խϸ�
	Vector<Bullet> bs=new Vector<Bullet>();
	//�����ӵ�
	Bullet b=null;
	//���̹�˵Ĺ�����
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//̹�����������ƶ��ķ���,if�ж�ʹ���̹�˲������
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
	//̹�˿���ķ���
	public void shotEnemy()
	{
		switch(this.direct)
		{	
			case 0: //�����ӵ�
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
		//�����ӵ��߳�
		Thread t=new Thread(b);
		t.start();
	}//shotEnemy		
}//Hero
	
	
//���˵�̹��Ҳ�����߳���
class EnemiesTank extends Tank implements Runnable
{
	//�������������Ϊ�˿����ӵ��������
	int times=0;
	//����һ�����������Է��ʵ�MyPanel�����е��˵�̹��
	Vector<EnemiesTank> ets=new Vector<EnemiesTank>();
	//����һ����������ŵ��˵��ӵ�
	Vector<Bullet> bs=new Vector<Bullet>();
	//��������ӵ�Ӧ���ڸոմ�������̹�˺͵��˵�̹���ӵ�������
	public EnemiesTank(int x,int y)
	{
		super(x, y);
	}
	
	//�ж��Ƿ������˱�ĵ���̹��
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;		
		switch(this.direct)
		{
			//�ҵ�̹�����ϻ�����
			case 0:				
			case 2:			
					//ȡ�����еĵ���̹��
					for(int i=0;i<ets.size();i++)
					{
						//ȡ����һ��̹��
						EnemiesTank et=ets.get(i);
						//��������Լ�
						if(et!=this)
						{
							//����������˵ķ��������»�������
							if(et.direct==0||et.direct==2)
							{
								if(this.x-20<et.x&&et.x<this.x+20&&this.y-30<et.y&&et.y<this.y+30)
									return true;
							}
							//����������˵�̹�������һ�����
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
			//̹�����һ�����
			//ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ����һ��̹��
				EnemiesTank et=ets.get(i);
				//��������Լ�
				if(et!=this)
				{
					//������˵ķ��������»�������
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
				//̹�������ƶ�
				case 0:
					//���ѭ������ʹ����̹���ڱ���ǰ����һ��
					for(int i=0;i<30;i++)
					{
						//��֤̹�˲����߽�
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
				//̹�������ƶ�	
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
				//̹�������ƶ�
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
					//̹�������ƶ�
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
				//��Ϊ30*50=1500ms����times%2==0ʱ������3��
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
							//�����ӵ��߳�
							Thread t=new Thread(b);
							t.start();
						}//if(bs.size()<5)
					}//if(isLive)				
				}//if(times%2==0)
				
				//��̹���������һ���µķ���
				this.direct=(int)(Math.random()*4);
				//�жϵ���̹���Ƿ�����
				if(this.isLive==false)
				{//��̹���������˳��߳�
					break;
				}
			
			}//while
		}//run
	}//class









//����ը��Ч��ը����(������겻��Ĳ��������߳�)
class Bomb
{
	int x,y;
	//ը����������ը���̵�ʣ��ʱ�䣩
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}	
	//��������ֵ�����ڲ�ͬ����ֵ��ʱ����ʾ��ͬͼƬ
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
//����һ��������
class Tree
{
	int x,y;
	public Tree(int x,int y)
	{
		this.x=x;
		this.y=y;
	}	
}
