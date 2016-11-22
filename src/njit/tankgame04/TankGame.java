/*
 * 坦克大战04
 */

package njit.tankgame04;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class TankGame  extends JFrame
{
    public static void main(String[] args)
    {
    	TankGame tankgame=new TankGame();      
    }
	
    public TankGame()
    {    	
    	//生成mypanel面板组件
    	MyPanel mypanel=new MyPanel();
    	mypanel.setBackground(Global.panelBackground);
    	
    	//把mypanel加入框架
    	this.add(mypanel);
        
    	//监听mypanel
    	this.addKeyListener(mypanel);
    	
    	//把面板做成线程，不断重绘
    	Thread t=new Thread (mypanel);
        t.start();
        
    	//设置外观
    	this.setIconImage((new ImageIcon("images/tank.gif")).getImage());
    	this.setTitle(Global.title);
    	this.setSize(Global.wide,Global.height);
    	this.setLocation(Global.jframeX,Global.jframeY);
    	this.setVisible(true);
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }
 
}//End of TankGame
 
class MyPanel extends JPanel implements KeyListener,Runnable
{	
    //定义我的坦克
    Hero hero=null;
    
    //定义敌人坦克
    Vector<Enemy> enemyVector=new Vector<Enemy>();
    
    //定义爆炸集合
    Vector<Bomb> bombVector=new Vector<Bomb>();
    
	//定义三张图片,三张图片切换构成一个爆炸效果
    Image image1=null;
    Image image2=null;
    Image image3=null;
    
    public MyPanel()
    {
    	//初始化我的坦克
    	hero=new Hero(Global.heroX,Global.heroY,Global.heroDirect,Global.heroColor,Global.heroSpeed);
    	
    	//初始化敌人坦克
    	for(int i=0;i<Global.enemyNumber;i++)
    	{
    		Enemy enemy=new Enemy((i+1)*Global.enemyDistance, 0,Global.enemyDirect, Global.enemyColor, Global.enemySpeed);
    		
    		Thread t=new Thread(enemy);
    	 	t.start();
    	 	//给敌人坦克添加一颗子弹
    	 	Bullet b=new Bullet(enemy.x+9, enemy.y+30,Direct.DOWN, 3);
    	 	enemy.bullets.add(b);
    	 	Thread tb=new Thread(b);
    	 	tb.start();
    	 	enemyVector.add(enemy);
    	} 
    	
    	//初始化图片
    	try {
    		image1=ImageIO.read(new File("bomb_1.gif"));
    		image2=ImageIO.read(new File("bomb_2.gif"));
    		image3=ImageIO.read(new File("bomb_3.gif"));
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    }

    public void paint (Graphics g)
    {
    	super.paint(g);
    	
    	//画出我的坦克 
    	if (this.hero.live == true) {			
			this.hero.drawTank(hero, g);
		}
    	
		//画出我的子弹
	    for(int i = 0;i < this.hero.bullets.size();i++)
	    {
		    Bullet heroBulletInUse=hero.bullets.get(i);
	    	if(heroBulletInUse != null && heroBulletInUse.live == true)
		    {
		    	g.draw3DRect(heroBulletInUse.x, heroBulletInUse.y, 1, 1, false);
		    }
	    	 
	    	//如果子弹死亡，则从向量中去掉
	    	if(heroBulletInUse.live == false)
	    	{
	    		hero.bullets.remove(heroBulletInUse); 
	    	}
	    }
	    
	    //画出敌人坦克	    
	    for(int i=0;i<enemyVector.size();i++)
	    {
	    	Enemy enemy=enemyVector.get(i);
	    	if(enemy.live == true)
	    	{
	    		this.enemyVector.get(i).drawTank(enemyVector.get(i), g);
	    		//画出敌人子弹
	    		for(int j=0;j<enemy.bullets.size();j++)
	    		{
	    			//取出子弹
	    			Bullet eb=enemy.bullets.get(j);
	    			if(eb.live==true)
	    			{
	    				g.draw3DRect(eb.x, eb.y, 1, 1, false);
	    			} 
	    			if(eb.live==false){
						enemy.bullets.remove(eb);
					}	    			
	    		}
	    	}
	    }
	    //画出爆炸效果
	    for(int i=0;i<bombVector.size();i++)
	    {
	    	 //取出炸弹
	    	 Bomb bomb=bombVector.get(i);
	    	 if(bomb.lifeTime>6)
	    	 {
	    		 g.drawImage(image1, bomb.x, bomb.y, 30, 30, this);
	    	 }else if(bomb.lifeTime>3){
	    		 g.drawImage(image2, bomb.x, bomb.y, 30, 30, this);
	    	 }else{
	    		 g.drawImage(image3, bomb.x, bomb.y, 30, 30, this);
	    	 }
	    	 //让b的生命值减少
	    	 bomb.lifeDown();
	    	 
	    	 if(bomb.lifeTime==0)
	    	 {
	    		 bombVector.remove(bomb);
	    	 }
	    	 
	     }
    }
    
    //判断坦克是否被子弹击中
    public void hit(Bullet bullet,Tank tank)
    {
    	switch (tank.direct) 
    	{
	    	//敌人坦克方向向上或向下
			case UP:
			case DOWN:
				if(bullet.x > tank.x && bullet.x<tank.x+20 && bullet.y>tank.y && bullet.y<tank.y+30)
				{
					bullet.live=false;
					tank.live=false;
					//创建一次爆炸，放入Vector
					Bomb bomb=new Bomb(tank.x,tank.y);
					bombVector.add(bomb);
				}
				break;
			//敌人坦克方向向左或向右
			case RIGHT:
			case LEFT:
				if(bullet.x>tank.x&&bullet.x<tank.x+30&&bullet.y>tank.y&&bullet.y<tank.y+20)
				{
					bullet.live=false;
					tank.live=false;
					//创建一次爆炸，放入Vector
					Bomb bomb=new Bomb(tank.x,tank.y);
					bombVector.add(bomb);
				}	
				break;
			default:
				break;
		} 	 	
    }
    
    //判断是否击中敌人坦克
    public void hitEnemy()
    {
		for(int i=0;i<hero.bullets.size();i++)
		{
			Bullet b=hero.bullets.get(i);
			if(b.live==true)
			{
				for(int j=0;j<enemyVector.size();j++)
				{
					Enemy e=enemyVector.get(j);
					if(e.live==true)						
					{
						this.hit(b, e);
					}
				}
			}
		}
    }
    
    //判断是否被击中
    public void hitme()
    {
    	if (this.hero.live == true) 
    	{
			//取出每一个敌人坦克
			for (int i = 0; i < this.enemyVector.size(); i++) 
			{
				//取出坦克
				Enemy e = enemyVector.get(i);
				//取出每一颗子弹
				for (int j = 0; j < e.bullets.size(); j++) 
				{
					//取出子弹
					Bullet b = e.bullets.get(j);
					this.hit(b, hero);
				}
			}
		}
    }
         
	//监听按下键盘的操作
	public void keyPressed(KeyEvent e) 
	{		
		if(e.getKeyCode()==KeyEvent.VK_W)
		{	
			//当按键为“W”时，坦克方向变为上，坦克位置向上移动speed个单位像素
			hero.direct=Direct.UP;
			hero.y-=hero.speed;
		}else if (e.getKeyCode()==KeyEvent.VK_D) {
			hero.direct=Direct.RIGHT;
			hero.x+=hero.speed;
		}else if (e.getKeyCode()==KeyEvent.VK_S) {
			hero.direct=Direct.DOWN;
			hero.y+=hero.speed;
		}else if (e.getKeyCode()==KeyEvent.VK_A) {
			hero.direct=Direct.LEFT;
			hero.x-=hero.speed;
		}else if(e.getKeyCode()==KeyEvent.VK_J) {
			
			//当按下“J”时，坦克发射子弹
			if(this.hero.bullets.size()<Global.heroBulletNumber)
			{
				this.hero.fire();
			}
		}
		
		//为了看到移动效果，需要重绘图形（后来面板做成了线程，这个重绘函数可以删除）
		repaint();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//实现Runnable接口
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{			
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			//判断是否击中敌人的坦克
			this.hitEnemy();
			
			//判断是否被击中
			this.hitme();
			
			repaint();			
		}
	}
      
}//End of MyPanel
 
 
