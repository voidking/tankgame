/*
 * ��һ��������̹��
 * �ڶ�����ʵ��̹���˶�
 * �������������ӵ�,ʵ������
 * ���Ĳ�������̹�˺���ʧ(��ըЧ��)
 */
package njit.tankgametmp;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.security.auth.x500.X500Principal;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
 
public class TankGame  extends JFrame
{
    MyPanel mp=null;
    public static void main(String[] args)
    {
       TankGame tankgame1=new TankGame();
      
    }
   
    public TankGame()
    {
       mp=new MyPanel();
      
       this.add(mp);
       
       this.addKeyListener(mp);
       
       Thread t=new Thread (mp);
       t.start();
       
       this.setTitle("����̹�˴�ս");
       this.setSize(600,500);
       this.setLocation(400,300);
       this.setVisible(true);
       this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }
 
}
 
class MyPanel extends JPanel implements KeyListener,Runnable
{	

    //�����ҵ�̹�ˣ���Ա����
    Hero hero=null;
    //�������̹��
    Vector<Enemy> en=new Vector<Enemy>();
    
    //���屬ը����
    Vector<Bomb> bm=new Vector<Bomb>();
    
    int ensize=3;
    
    //��������ͼƬ,����ͼƬ�л�����һ����ըЧ��
    Image image1=null;
    Image image2=null;
    Image image3=null;
    
    public MyPanel()
    {	//��ʼλ�ã�������ɫ���ٶ�
       hero=new Hero(100,100,0,0,3,true);
       for(int i=0;i<ensize;i++)
       {
    	   Enemy eni=new Enemy((i+1)*50, 0, 2, 1, 1,true);
    	   
    	   Thread t=new Thread(eni);
    	   t.start();
    	   //������̹�����һ���ӵ�
    	   Bullet b=new Bullet(eni.x+10, eni.y+30,2, 3, true);
    	   eni.bls.add(b);
    	   Thread tb=new Thread(b);
    	   tb.start();
    	   en.add(eni);
       }
       //��ʼ��ͼƬ����1,��ȡ����src�����ͼƬ���������ֳ�ʼ��������һ�α�ըû��Ч������Ҫ��paint������������仰
//       image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//       image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//       image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
       //��ʼ��ͼƬ����2,��ȡ����Test10 �����ͼƬ��ע�⣺û��б��
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
	     //��ʼ��ͼƬ����1,û����������Ļ�����һ�α�ը�ͻ�û��Ч����Ϊʲô��
//	     g.drawImage(image1, 100, 100, 30, 30, this);
//	     g.drawImage(image2, 100, 100, 30, 30, this);
//	     g.drawImage(image3, 100, 100, 30, 30, this);
	     
	     g.fillRect(0,0,400,300);
	     //�����ҵ�̹��
	     if(this.hero.live==true)
	     {
	    	 this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), hero.getColor());
	     }
	     //��������̹��
	     for(int i=0;i<en.size();i++){
	    	 Enemy e=en.get(i);
	    	 if(e.live==true)
	    	 {
	    		 this.drawTank(en.get(i).getX(), en.get(i).getY(), g, en.get(i).getDirect(), en.get(i).getColor());
	    		 //���������ӵ�
	    		 for(int j=0;j<e.bls.size();j++){
	    			 //ȡ���ӵ�
	    			 Bullet eb=e.bls.get(j);
	    			 if(eb.live==true)
	    			 {
	    				 g.draw3DRect(eb.x, eb.y, 1, 1, false);
	    			 } 
	    			 if(eb.live==false){
						e.bls.remove(eb);
					 }
	    			 	    			 
	    		 }
	    	 }
	     }
	     //������ըЧ��
	     for(int i=0;i<bm.size();i++)
	     {
	    	 //ȡ��ը��
	    	 Bomb b=bm.get(i);
	    	 if(b.life>6)
	    	 {
	    		 g.drawImage(image1, b.x, b.y, 30, 30, this);
	    	 }else if(b.life>3){
	    		 g.drawImage(image2, b.x, b.y, 30, 30, this);
	    	 }else{
	    		 g.drawImage(image3, b.x, b.y, 30, 30, this);
	    	 }
	    	 //��b������ֵ����
	    	 b.lifedown();
	    	 
	    	 if(b.life==0)
	    	 {
	    		 bm.remove(b);
	    	 }
	    	 
	     }

	     //�����ӵ�
	     for(int i=0;i<this.hero.bls.size();i++){
		     Bullet bln=hero.bls.get(i);
	    	 if(bln!=null&&bln.live==true)
		     {
		    	 g.draw3DRect(bln.x, bln.y, 1, 1, false);
		     }
	    	 
	    	 //����ӵ����������������ȥ��
	    	 if(bln.live==false)
	    	 {
	    		hero.bls.remove(bln); 
	    	 }
	     }
	     
    }
    
    //�ж�̹���Ƿ��ӵ�����
    public void hit(Bullet b,Tank t)
    {
    	switch (t.direct) {
    	//����̹�˷������ϻ�����
		case 0:
		case 2:
			if(b.x>t.x&&b.x<t.x+20&&b.y>t.y&&b.y<t.y+30)
			{
				b.live=false;
				t.live=false;
				//����һ�α�ը������Vector
				Bomb bo=new Bomb(t.x,t.y);
				bm.add(bo);
				
			}
			break;
		//����̹�˷������������
		case 1:
		case 3:
			if(b.x>t.x&&b.x<t.x+30&&b.y>t.y&&b.y<t.y+20)
			{
				b.live=false;
				t.live=false;
				//����һ�α�ը������Vector
				Bomb bo=new Bomb(t.x,t.y);
				bm.add(bo);
			}	
			break;
		default:
			break;
		} 	 	
    }
    //�ж��Ƿ���е���̹��
    public void hitenemy()
    {
		for(int i=0;i<hero.bls.size();i++)
		{
			Bullet b=hero.bls.get(i);
			if(b.live==true)
			{
				for(int j=0;j<en.size();j++)
				{
					Enemy e=en.get(j);
					if(e.live==true)						
					{
						this.hit(b, e);
					}
				}
			}
		}
    }
    //�ж��Ƿ񱻻���
    public void hitme()
    {
    	//ȡ��ÿһ������̹��
    	for(int i=0;i<this.en.size();i++)
    	{
    		//ȡ��̹��
    		Enemy e=en.get(i);
    		//ȡ��ÿһ���ӵ�
    		for(int j=0;j<e.bls.size();j++)
    		{
    			//ȡ���ӵ�
    			Bullet b=e.bls.get(j);
    			this.hit(b, hero);
    		}
    	}
    }
    public void drawTank(int x,int y,Graphics g,int direct,int type)
    {
       //�ж�����
       switch (type)
       {
       case 0:
           g.setColor(Color.cyan);break;
       case 1:
           g.setColor(Color.yellow);break;   
       }
       //�жϷ���
      
       switch(direct)
       {
       //����
       case 0:
            //�����ҵ�̹�ˣ���ʱ���ٷ�װ��һ��������
            //1.��������ľ���
            //g.drawRect(hero.getX(), hero.getY(), 5, 30);
            g.fill3DRect(x, y, 5, 30,false);
            
            //2.�����ұߵľ���
            g.fill3DRect(x+15, y, 5, 30,false);
            
            //3.����̹�˵��м����
            g.fill3DRect(x+5, y+5, 10, 20,false);
            //�����м��Բ
            g.fillOval(x+4, y+10,10,10);
            //������
            g.drawLine(x+9, y+15, x+9, y);
            break;
       //����
       case 1:
    	   g.fill3DRect(x, y, 30, 5, false);
    	   g.fill3DRect(x, y+15, 30, 5, false);
    	   g.fill3DRect(x+5, y+5, 20, 10, false);
    	   g.fillOval(x+10, y+4, 10, 10);
    	   g.drawLine(x+15, y+9, x+30, y+9);
    	   break;
       //����
       case 2:
    	   g.fill3DRect(x, y, 5, 30,false);
           g.fill3DRect(x+15, y, 5, 30,false);
           g.fill3DRect(x+5, y+5, 10, 20,false);
           g.fillOval(x+4, y+10,10,10);
           g.drawLine(x+9, y+15, x+9, y+30);
           break;
       //����    
       case 3:
    	   g.fill3DRect(x, y, 30, 5, false);
    	   g.fill3DRect(x, y+15, 30, 5, false);
    	   g.fill3DRect(x+5, y+5, 20, 10, false);
    	   g.fillOval(x+10, y+4, 10, 10);
    	   g.drawLine(x+15, y+9, x, y+9);
    	   break;
    	   
       }
      
      
      
    }
      
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_W)
		{
			//System.out.println("press");
			hero.direct=0;
			hero.y-=hero.speed;
		}else if (e.getKeyCode()==KeyEvent.VK_D) {
			hero.direct=1;
			hero.x+=hero.speed;
		}else if (e.getKeyCode()==KeyEvent.VK_S) {
			hero.direct=2;
			hero.y+=hero.speed;
		}else if (e.getKeyCode()==KeyEvent.VK_A) {
			hero.direct=3;
			hero.x-=hero.speed;
		}
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			if(this.hero.bls.size()<5)
			{
				this.hero.fire();
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			//�ж��Ƿ���е��˵�̹��
			this.hitenemy();
			//�ж��Ƿ񱻻���
			this.hitme();
			repaint();
			
		}
	}
      
}
 
 
