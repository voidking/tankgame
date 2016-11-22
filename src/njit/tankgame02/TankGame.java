/*
 * 坦克大战02
 */
package njit.tankgame02;

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
    	//定义面板大小
    	final int wide = 500;
    	final int hight = 400;
    	
    	MyPanel mypanel=new MyPanel();
    	mypanel.setBackground(Color.black);
      
    	this.add(mypanel);
       
    	this.addKeyListener(mypanel);
       
    	this.setIconImage((new ImageIcon("images/tank.gif")).getImage());
    	this.setTitle("坦克大战（待完成）");
    	this.setSize(wide,hight);
    	this.setLocation(400,400);
    	this.setVisible(true);
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }
 
}
 
class MyPanel extends JPanel implements KeyListener
{	
    //定义我的坦克
    Hero hero=null;
    final int heroSpeed = 3;
    //定义敌人坦克
    Vector<Enemy> enemyVector=new Vector<Enemy>();
    final int enemySpeed = 1;
    
    int enemyNumber=3;
    
    public MyPanel()
    {
    	//初始位置，方向，颜色，速度
    	hero=new Hero(100,100,Direct.UP,Color.cyan,heroSpeed);
    	for(int i=0;i<enemyNumber;i++)
    	{
    		Enemy enemy=new Enemy((i+1)*50, 0,Direct.DOWN, Color.yellow, enemySpeed);
    		enemyVector.add(enemy);
    	}
    }

    public void paint (Graphics g)
    {
    	super.paint(g);
	     
	    this.drawTank(hero,g);
	     
	    for(int i=0;i<enemyVector.size();i++)
	    {
	    	this.drawTank(enemyVector.get(i), g);
	    }
    }
   
    public void drawTank(Tank tank,Graphics g)
    {
    	g.setColor(tank.color);
      
    	switch(tank.direct)
    	{
       
    	case UP:
            //1.画出左面的矩形
            g.fill3DRect(tank.x, tank.y, 5, 30,false);           
            //2.画出右边的矩形
            g.fill3DRect(tank.x+15, tank.y, 5, 30,false);            
            //3.画出坦克的中间矩形
            g.fill3DRect(tank.x+5, tank.y+5, 10, 20,false);
            //画出中间的圆
            g.fillOval(tank.x+4, tank.y+10,10,10);
            //画出线
            g.drawLine(tank.x+9, tank.y+15, tank.x+9, tank.y);
            break;

    	case RIGHT:
    		g.fill3DRect(tank.x, tank.y, 30, 5, false);
    		g.fill3DRect(tank.x, tank.y+15, 30, 5, false);
    		g.fill3DRect(tank.x+5, tank.y+5, 20, 10, false);
    		g.fillOval(tank.x+10, tank.y+4, 10, 10);
    		g.drawLine(tank.x+15, tank.y+9, tank.x+30, tank.y+9);
    		break;
    	   
    	case DOWN:
    		g.fill3DRect(tank.x, tank.y, 5, 30,false);
    		g.fill3DRect(tank.x+15, tank.y, 5, 30,false);
    		g.fill3DRect(tank.x+5, tank.y+5, 10, 20,false);
    		g.fillOval(tank.x+4, tank.y+10,10,10);
    		g.drawLine(tank.x+9, tank.y+15, tank.x+9, tank.y+30);
    		break;
        
    	case LEFT:
    		g.fill3DRect(tank.x, tank.y, 30, 5, false);
    		g.fill3DRect(tank.x, tank.y+15, 30, 5, false);
    		g.fill3DRect(tank.x+5, tank.y+5, 20, 10, false);
    		g.fillOval(tank.x+10, tank.y+4, 10, 10);
    		g.drawLine(tank.x+15, tank.y+9, tank.x, tank.y+9);
    		break;
    	   
    	}
            
    }
      
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_W)
		{
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
		}
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
      
}
 
 
