/*
 * 坦克大战03
 */

package njit.tankgame03;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
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
    	this.setSize(Global.wide,Global.hight);
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
    
    public MyPanel()
    {
    	//初始化我的坦克
    	hero=new Hero(Global.heroX,Global.heroY,Global.heroDirect,Global.heroColor,Global.heroSpeed);
    	
    	//初始化敌人坦克
    	for(int i=0;i<Global.enemyNumber;i++)
    	{
    		Enemy enemy=new Enemy((i+1)*Global.enemyDistance, 0,Global.enemyDirect, Global.enemyColor, Global.enemySpeed);
    		enemyVector.add(enemy);
    	}
    }

    public void paint (Graphics g)
    {
    	super.paint(g);
	    
    	//画出我的坦克 
	    this.drawTank(hero,g);
	    
	    //画出敌人坦克 
	    for(int i=0;i<enemyVector.size();i++)
	    {
	    	this.drawTank(enemyVector.get(i), g);
	    }
	    
	    //画出子弹
	    if(hero.bullet != null && hero.bullet.live == true)
	    {
	    	g.draw3DRect(hero.bullet.x, hero.bullet.y, 1, 1, false);
	    }
    }
   
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
      
	//监听按下键盘的操作
	public void keyPressed(KeyEvent e) {
		
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
			this.hero.fire();
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
			repaint();			
		}
	}
      
}//End of MyPanel
 
 
