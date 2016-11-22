/*
 * ̹�˴�ս03
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
    	//����mypanel������
    	MyPanel mypanel=new MyPanel();
    	mypanel.setBackground(Global.panelBackground);
    	
    	//��mypanel������
    	this.add(mypanel);
        
    	//����mypanel
    	this.addKeyListener(mypanel);
    	
    	//����������̣߳������ػ�
    	Thread t=new Thread (mypanel);
        t.start();
        
    	//�������
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
    //�����ҵ�̹��
    Hero hero=null;
    
    //�������̹��
    Vector<Enemy> enemyVector=new Vector<Enemy>();
    
    public MyPanel()
    {
    	//��ʼ���ҵ�̹��
    	hero=new Hero(Global.heroX,Global.heroY,Global.heroDirect,Global.heroColor,Global.heroSpeed);
    	
    	//��ʼ������̹��
    	for(int i=0;i<Global.enemyNumber;i++)
    	{
    		Enemy enemy=new Enemy((i+1)*Global.enemyDistance, 0,Global.enemyDirect, Global.enemyColor, Global.enemySpeed);
    		enemyVector.add(enemy);
    	}
    }

    public void paint (Graphics g)
    {
    	super.paint(g);
	    
    	//�����ҵ�̹�� 
	    this.drawTank(hero,g);
	    
	    //��������̹�� 
	    for(int i=0;i<enemyVector.size();i++)
	    {
	    	this.drawTank(enemyVector.get(i), g);
	    }
	    
	    //�����ӵ�
	    if(hero.bullet != null && hero.bullet.live == true)
	    {
	    	g.draw3DRect(hero.bullet.x, hero.bullet.y, 1, 1, false);
	    }
    }
   
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
      
	//�������¼��̵Ĳ���
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_W)
		{	
			//������Ϊ��W��ʱ��̹�˷����Ϊ�ϣ�̹��λ�������ƶ�speed����λ����
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
			
			//�����¡�J��ʱ��̹�˷����ӵ�
			this.hero.fire();
		}
		
		//Ϊ�˿����ƶ�Ч������Ҫ�ػ�ͼ�Σ���������������̣߳�����ػ溯������ɾ����
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
	
	//ʵ��Runnable�ӿ�
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
 
 
