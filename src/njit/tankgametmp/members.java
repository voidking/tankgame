package njit.tankgametmp;

import java.util.Vector;

//爆炸类
class Bomb{
	
	//定义爆炸的坐标
	int x,y;
	//爆炸的生命时间
	int life=9;
	
	Boolean live=true;
	
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//减少生命时间
	public void lifedown()
	{
		if(life>0)
		{
			life--;
		}else{
			this.live=false;
		}
		
	}
	
}

//子弹类
class Bullet implements Runnable{
	
	public void run(){
		
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
			switch (direct) {
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;

			default:
				break;
			}
			if(x<0||x>400||y<0||y>300)
			{
				live=false;
				break;
			}
		}
	}
	
	int x;
	int y;
	int direct;
	int speed=3;
	Boolean live=true;
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Bullet(int x,int y,int direct,int speed,Boolean live){
		this.x=x;
		this.y=y;
		this.direct=direct;
		this.speed=speed;
		this.live=live;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public Boolean getLive() {
		return live;
	}

	public void setLive(Boolean live) {
		this.live = live;
	}

	
}

class Tank
{
    //坦克的坐标
    int x=0;
    int y=0;
    //坦克的方向,0表示向上，1表示向右，2表示向下，3表示向左
    int direct=0;
    //坦克的颜色
    int color=0;
    //速度
    int speed=1;
    
    Boolean live=true;
    
	
	public Boolean getLive() {
		return live;
	}

	public void setLive(Boolean live) {
		this.live = live;
	}

	public int getX() {
        return x;
     }
  
     public void setX(int x) {
        this.x = x;
     }
  
     public int getY() {
        return y;
     }
  
     public void setY(int y) {
        this.y = y;
     }
   
    public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}
	
    public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public Tank(int x,int y,int direct,int color,int speed,Boolean live)
    {
       this.x=x;
       this.y=y;
       this.direct=direct;
       this.color=color;
       this.speed=speed;
       this.live=live;
    }
   
}
 
//我的坦克
class Hero extends Tank
{
	Bullet bl=null;
	Vector<Bullet> bls=new Vector<Bullet>();
	public void fire(){
		
		switch (direct) {
		case 0:
			bl=new Bullet(x+10, y, 0,3,true);
			bls.add(bl);
			break;
		
		case 1:
			bl=new Bullet(x+30, y+10, 1,3,true);
			bls.add(bl);
			break;
		case 2:
			bl=new Bullet(x+10, y+30, 2,3,true);
			bls.add(bl);
			break;
		case 3:
			bl=new Bullet(x, y+10, 3,3,true);
			bls.add(bl);
			break;
			
		default:
			break;
		}
		//启动子弹线程
		Thread t=new Thread(bl);
		t.start();
	}
	
    public Hero(int x, int y,int direct,int color,int speed,Boolean live)
    {
       super(x, y,direct,color,speed,live);
    }
   
}

//敌人坦克
class Enemy extends Tank implements Runnable
{
	Vector<Bullet> bls=new Vector<Bullet>();
	Vector<Enemy> en=new Vector<Enemy>();
	
	int times=0;

	public Enemy(int x,int y,int direct,int color,int speed,Boolean live)
	{
		super(x, y, direct, color, speed,live);
		
	}

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
			
			switch (this.direct) {
			//坦克正在向上
			case 0:
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
			//向右
			case 1:
				for(int i=0;i<30;i++)
				{
					if (x<400) {
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
			case 2:
				for(int i=0;i<30;i++)
				{
					if (y<300) {
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
			case 3:
				for(int i=0;i<30;i++)
				{
					if (x>0) {
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
			this.direct=(int)(Math.random()*4);
			this.times++;
			
			if (times%2==0 && live == true && bls.size() < 3) 
			{

				Bullet b = null;
				//添加子弹
				switch (direct) {
				case 0:
					b = new Bullet(x + 10, y, 0, 3, true);
					bls.add(b);
					break;

				case 1:
					b = new Bullet(x + 30, y + 10, 1, 3, true);
					bls.add(b);
					break;
				case 2:
					b = new Bullet(x + 10, y + 30, 2, 3, true);
					bls.add(b);
					break;
				case 3:
					b = new Bullet(x, y + 10, 3, 3, true);
					bls.add(b);
					break;

				default:
					break;
				}
				Thread t = new Thread(b);
				t.start();
								
			}	
		}
	}
}