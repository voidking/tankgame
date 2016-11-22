/*
 * 第一步，画出坦克
 * 第二步，实现坦克运动
 * 第三步，发射子弹,实现连发
 * 第四步，击中坦克后消失(爆炸效果)
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
       
       this.setTitle("盗版坦克大战");
       this.setSize(600,500);
       this.setLocation(400,300);
       this.setVisible(true);
       this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }
 
}
 
class MyPanel extends JPanel implements KeyListener,Runnable
{	

    //定义我的坦克，成员变量
    Hero hero=null;
    //定义敌人坦克
    Vector<Enemy> en=new Vector<Enemy>();
    
    //定义爆炸集合
    Vector<Bomb> bm=new Vector<Bomb>();
    
    int ensize=3;
    
    //定义三张图片,三张图片切换构成一个爆炸效果
    Image image1=null;
    Image image2=null;
    Image image3=null;
    
    public MyPanel()
    {	//初始位置，方向，颜色，速度
       hero=new Hero(100,100,0,0,3,true);
       for(int i=0;i<ensize;i++)
       {
    	   Enemy eni=new Enemy((i+1)*50, 0, 2, 1, 1,true);
    	   
    	   Thread t=new Thread(eni);
    	   t.start();
    	   //给敌人坦克添加一颗子弹
    	   Bullet b=new Bullet(eni.x+10, eni.y+30,2, 3, true);
    	   eni.bls.add(b);
    	   Thread tb=new Thread(b);
    	   tb.start();
    	   en.add(eni);
       }
       //初始化图片方法1,读取的是src下面的图片。但是这种初始化方法第一次爆炸没有效果，需要在paint函数中添加三句话
//       image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//       image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//       image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
       //初始化图片方法2,读取的是Test10 下面的图片，注意：没有斜杠
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
	     //初始化图片方法1,没有下面三句的话，第一次爆炸就会没有效果，为什么？
//	     g.drawImage(image1, 100, 100, 30, 30, this);
//	     g.drawImage(image2, 100, 100, 30, 30, this);
//	     g.drawImage(image3, 100, 100, 30, 30, this);
	     
	     g.fillRect(0,0,400,300);
	     //画出我的坦克
	     if(this.hero.live==true)
	     {
	    	 this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), hero.getColor());
	     }
	     //画出敌人坦克
	     for(int i=0;i<en.size();i++){
	    	 Enemy e=en.get(i);
	    	 if(e.live==true)
	    	 {
	    		 this.drawTank(en.get(i).getX(), en.get(i).getY(), g, en.get(i).getDirect(), en.get(i).getColor());
	    		 //画出敌人子弹
	    		 for(int j=0;j<e.bls.size();j++){
	    			 //取出子弹
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
	     //画出爆炸效果
	     for(int i=0;i<bm.size();i++)
	     {
	    	 //取出炸弹
	    	 Bomb b=bm.get(i);
	    	 if(b.life>6)
	    	 {
	    		 g.drawImage(image1, b.x, b.y, 30, 30, this);
	    	 }else if(b.life>3){
	    		 g.drawImage(image2, b.x, b.y, 30, 30, this);
	    	 }else{
	    		 g.drawImage(image3, b.x, b.y, 30, 30, this);
	    	 }
	    	 //让b的生命值减少
	    	 b.lifedown();
	    	 
	    	 if(b.life==0)
	    	 {
	    		 bm.remove(b);
	    	 }
	    	 
	     }

	     //画出子弹
	     for(int i=0;i<this.hero.bls.size();i++){
		     Bullet bln=hero.bls.get(i);
	    	 if(bln!=null&&bln.live==true)
		     {
		    	 g.draw3DRect(bln.x, bln.y, 1, 1, false);
		     }
	    	 
	    	 //如果子弹死亡，则从向量中去掉
	    	 if(bln.live==false)
	    	 {
	    		hero.bls.remove(bln); 
	    	 }
	     }
	     
    }
    
    //判断坦克是否被子弹击中
    public void hit(Bullet b,Tank t)
    {
    	switch (t.direct) {
    	//敌人坦克方向向上或向下
		case 0:
		case 2:
			if(b.x>t.x&&b.x<t.x+20&&b.y>t.y&&b.y<t.y+30)
			{
				b.live=false;
				t.live=false;
				//创建一次爆炸，放入Vector
				Bomb bo=new Bomb(t.x,t.y);
				bm.add(bo);
				
			}
			break;
		//敌人坦克方向向左或向右
		case 1:
		case 3:
			if(b.x>t.x&&b.x<t.x+30&&b.y>t.y&&b.y<t.y+20)
			{
				b.live=false;
				t.live=false;
				//创建一次爆炸，放入Vector
				Bomb bo=new Bomb(t.x,t.y);
				bm.add(bo);
			}	
			break;
		default:
			break;
		} 	 	
    }
    //判断是否击中敌人坦克
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
    //判断是否被击中
    public void hitme()
    {
    	//取出每一个敌人坦克
    	for(int i=0;i<this.en.size();i++)
    	{
    		//取出坦克
    		Enemy e=en.get(i);
    		//取出每一颗子弹
    		for(int j=0;j<e.bls.size();j++)
    		{
    			//取出子弹
    			Bullet b=e.bls.get(j);
    			this.hit(b, hero);
    		}
    	}
    }
    public void drawTank(int x,int y,Graphics g,int direct,int type)
    {
       //判断类型
       switch (type)
       {
       case 0:
           g.setColor(Color.cyan);break;
       case 1:
           g.setColor(Color.yellow);break;   
       }
       //判断方向
      
       switch(direct)
       {
       //向上
       case 0:
            //画出我的坦克（到时候再封装成一个函数）
            //1.画出左面的矩形
            //g.drawRect(hero.getX(), hero.getY(), 5, 30);
            g.fill3DRect(x, y, 5, 30,false);
            
            //2.画出右边的矩形
            g.fill3DRect(x+15, y, 5, 30,false);
            
            //3.画出坦克的中间矩形
            g.fill3DRect(x+5, y+5, 10, 20,false);
            //画出中间的圆
            g.fillOval(x+4, y+10,10,10);
            //画出线
            g.drawLine(x+9, y+15, x+9, y);
            break;
       //向右
       case 1:
    	   g.fill3DRect(x, y, 30, 5, false);
    	   g.fill3DRect(x, y+15, 30, 5, false);
    	   g.fill3DRect(x+5, y+5, 20, 10, false);
    	   g.fillOval(x+10, y+4, 10, 10);
    	   g.drawLine(x+15, y+9, x+30, y+9);
    	   break;
       //向下
       case 2:
    	   g.fill3DRect(x, y, 5, 30,false);
           g.fill3DRect(x+15, y, 5, 30,false);
           g.fill3DRect(x+5, y+5, 10, 20,false);
           g.fillOval(x+4, y+10,10,10);
           g.drawLine(x+9, y+15, x+9, y+30);
           break;
       //向左    
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
			
			//判断是否击中敌人的坦克
			this.hitenemy();
			//判断是否被击中
			this.hitme();
			repaint();
			
		}
	}
      
}
 
 
