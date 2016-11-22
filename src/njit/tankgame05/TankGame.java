/*
 * ̹�˴�ս05
 */

package njit.tankgame05;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TankGame  extends JFrame implements ActionListener
{
    public static void main(String[] args)
    {
    	TankGame tankgame=new TankGame();      
    }

    StartPanel startPanel;
    MyPanel gamePanel;
    
    public TankGame()
    {      	
    	//�˵���
    	JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("�˵�");
		menu.setMnemonic('M');
		menu.setToolTipText("Alt+M");
		
		//��ʼ��Ϸ
		JMenu start = new JMenu("��ʼ");
		JMenuItem newGame = new JMenuItem("����Ϸ");
		newGame.addActionListener(this);
		newGame.setActionCommand("newGame");
		JMenuItem loadGame = new JMenuItem("��ȡ�浵");
		loadGame.addActionListener(this);
		loadGame.setActionCommand("loadGame");
		start.add(newGame);
		start.add(loadGame);
		menu.add(start);
		
		//�浵�˳�
		JMenuItem save = new JMenuItem("�浵");
		save.addActionListener(this);
		save.setActionCommand("save");
		JMenuItem quit = new JMenuItem("�˳�");
		quit.addActionListener(this);
		quit.setActionCommand("quit");
		menu.add(save);
		menu.add(quit);
		
		menuBar.add(menu);
		
    	//����mypanel������
    	startPanel = new StartPanel();
    	Thread thread = new Thread(startPanel);
    	thread.start();
    	
    	//��startPanel������
    	this.add(startPanel);
        
    	//�������
    	this.setIconImage((new ImageIcon("images/tank.gif")).getImage());
    	this.setTitle(Global.title);
    	this.setSize(Global.frameWide,Global.frameHeight);
    	this.setLocationRelativeTo(null);
    	this.setVisible(true);
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }


	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("newGame"))
		{
			//����ս�����
			gamePanel = new MyPanel();
			
			//����mp�߳�
			Thread t=new Thread(gamePanel);
			t.start();
			//��ɾ���ɵĿ�ʼ���
			this.remove(startPanel);
			this.add(gamePanel);
			//ע�����
			this.addKeyListener(gamePanel);
			//��ʾ,ˢ��JFrame
			this.setVisible(true);
			
		}
		else if (e.getActionCommand().equals("save")) 
		{
			JFileChooser jfc2 = new JFileChooser();
			jfc2.setDialogTitle("���Ϊ��");
			jfc2.showSaveDialog(null);
			// ��Ĭ�ϵķ�ʽ��ʾ
			jfc2.setVisible(true);

			// �õ��û�ϣ�����ļ����浽�δ�
			String file = jfc2.getSelectedFile().getAbsolutePath();
			// ׼��д�뵽ָ�����ļ�

			FileWriter fw = null;
			BufferedWriter bw = null;

			try {
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);

				// bw.write(this.jta.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {

				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}

	}
 
}//End of TankGame

class StartPanel extends JPanel implements Runnable
{

	int times=0;
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, Global.panelWide, Global.panelHeight);
		//��ʾ��Ϣ
		
		if(times%2==0)
		{
			g.fillRect(0, 0, Global.panelWide, Global.panelHeight);
			g.setColor(Color.yellow);
			//������Ϣ������
			Font myFont=new Font("������κ",Font.BOLD,30);
			g.setFont(myFont);
			g.drawString("stage: 1", 200, 200);
		}
		
	}

	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			//����
			try {
				
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			times++;
			
			//�ػ�
			this.repaint();
		}
		
	}
}//End of StartPanel
 
class MyPanel extends JPanel implements KeyListener,Runnable
{	
    //�����ҵ�̹��
    Hero hero=null;
    
    //�������̹��
    Vector<Enemy> enemyVector=new Vector<Enemy>();
    
    //���屬ը����
    Vector<Bomb> bombVector=new Vector<Bomb>();
    
	//��������ͼƬ,����ͼƬ�л�����һ����ըЧ��
    Image image1=null;
    Image image2=null;
    Image image3=null;
    
    public MyPanel()
    {
    	//��ʼ���ҵ�̹��
    	hero=new Hero(Global.heroX,Global.heroY,Global.heroDirect,Global.heroColor,Global.heroSpeed);
    	
    	//��ʼ������̹��
    	for(int i=0;i<Global.enemyNumber;i++)
    	{
    		Enemy enemy=new Enemy((i+1)*Global.enemyDistance, 0,Global.enemyDirect, Global.enemyColor, Global.enemySpeed);   		
    		
    		Thread enemyThread=new Thread(enemy);
    	 	enemyThread.start();
    	 	//������̹�����һ���ӵ�
    	 	Bullet enemyBullet=new Bullet(enemy.x+9, enemy.y+30,Direct.DOWN, Global.enemyBulletSpeed);
    	 	enemy.bullets.add(enemyBullet);
    	 	Thread tb=new Thread(enemyBullet);
    	 	tb.start();
    	 	enemyVector.add(enemy);
    	 	
    	 	enemy.getPanelTank(enemyVector);
    	} 
    	
    	
    	//��ʼ��ͼƬ
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
    	g.setColor(Global.panelBackground);
    	g.fillRect(0, 0, Global.panelWide, Global.panelHeight);
    	
    	Tank heroTank = new Tank(10, Global.panelHeight + 10, Direct.UP, Global.heroColor, 0);
    	heroTank.drawTank(heroTank, g);
    	//�����ҵ�̹�� 
    	if (this.hero.live == true) {			
			this.hero.drawTank(hero, g);
		}
    	
		//�����ҵ��ӵ�
	    for(int i = 0;i < this.hero.bullets.size();i++)
	    {
		    Bullet heroBulletInUse=hero.bullets.get(i);
	    	if(heroBulletInUse != null && heroBulletInUse.live == true)
		    {
		    	g.draw3DRect(heroBulletInUse.x, heroBulletInUse.y, 1, 1, false);
		    }
	    	 
	    	//����ӵ����������������ȥ��
	    	if(heroBulletInUse.live == false)
	    	{
	    		hero.bullets.remove(heroBulletInUse); 
	    	}
	    }
	    
	    //��������̹��	    
	    for(int i=0;i<enemyVector.size();i++)
	    {
	    	Enemy enemy=enemyVector.get(i);
	    	if(enemy.live == true)
	    	{
	    		this.enemyVector.get(i).drawTank(enemyVector.get(i), g);
	    		//���������ӵ�
	    		for(int j=0;j<enemy.bullets.size();j++)
	    		{
	    			//ȡ���ӵ�
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
	    //������ըЧ��
	    for(int i=0;i<bombVector.size();i++)
	    {
	    	 //ȡ��ը��
	    	 Bomb bomb=bombVector.get(i);
	    	 if(bomb.lifeTime>6)
	    	 {
	    		 g.drawImage(image1, bomb.x, bomb.y, 30, 30, this);
	    	 }else if(bomb.lifeTime>3){
	    		 g.drawImage(image2, bomb.x, bomb.y, 30, 30, this);
	    	 }else{
	    		 g.drawImage(image3, bomb.x, bomb.y, 30, 30, this);
	    	 }
	    	 //��b������ֵ����
	    	 bomb.lifeDown();
	    	 
	    	 if(bomb.lifeTime==0)
	    	 {
	    		 bombVector.remove(bomb);
	    	 }
	    	 
	     }
    }
    
    //�ж�̹���Ƿ��ӵ�����
    public void hit(Bullet bullet,Tank tank)
    {
    	switch (tank.direct) 
    	{
	    	//����̹�˷������ϻ�����
			case UP:
			case DOWN:
				if(bullet.x > tank.x && bullet.x<tank.x+20 && bullet.y>tank.y && bullet.y<tank.y+30)
				{
					bullet.live=false;
					tank.live=false;
					//����һ�α�ը������Vector
					Bomb bomb=new Bomb(tank.x,tank.y);
					bombVector.add(bomb);
				}
				break;
			//����̹�˷������������
			case RIGHT:
			case LEFT:
				if(bullet.x>tank.x&&bullet.x<tank.x+30&&bullet.y>tank.y&&bullet.y<tank.y+20)
				{
					bullet.live=false;
					tank.live=false;
					//����һ�α�ը������Vector
					Bomb bomb=new Bomb(tank.x,tank.y);
					bombVector.add(bomb);
				}	
				break;
			default:
				break;
		} 	 	
    }
    
    //�ж��Ƿ���е���̹��
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
    
    //�ж��Ƿ񱻻���
    public void hitme()
    {
    	if (this.hero.live == true) 
    	{
			//ȡ��ÿһ������̹��
			for (int i = 0; i < this.enemyVector.size(); i++) 
			{
				//ȡ��̹��
				Enemy e = enemyVector.get(i);
				//ȡ��ÿһ���ӵ�
				for (int j = 0; j < e.bullets.size(); j++) 
				{
					//ȡ���ӵ�
					Bullet b = e.bullets.get(j);
					this.hit(b, hero);
				}
			}
		}
    }
         
	//�������¼��̵Ĳ���
	public void keyPressed(KeyEvent e) 
	{		
		if(e.getKeyCode()==KeyEvent.VK_W)
		{	
			if (hero.y > 0) {
				//������Ϊ��W��ʱ��̹�˷����Ϊ�ϣ�̹��λ�������ƶ�speed����λ����
				hero.direct = Direct.UP;
				hero.y -= hero.speed;
			}
		}else if (e.getKeyCode()==KeyEvent.VK_D) {
			if (hero.x < Global.panelWide - 30) {
				hero.direct = Direct.RIGHT;
				hero.x += hero.speed;
			}
		}else if (e.getKeyCode()==KeyEvent.VK_S) {
			if (hero.y < Global.panelHeight - 30) {
				hero.direct = Direct.DOWN;
				hero.y += hero.speed;
			}
		}else if (e.getKeyCode()==KeyEvent.VK_A) {
			if (hero.x > 0) {
				hero.direct = Direct.LEFT;
				hero.x -= hero.speed;
			}
		}else if(e.getKeyCode()==KeyEvent.VK_J) {
			
			//�����¡�J��ʱ��̹�˷����ӵ�
			if(this.hero.bullets.size()<Global.heroBulletNumber)
			{
				this.hero.fire(Global.heroBulletSpeed);
			}
		}else if(e.getKeyCode()==KeyEvent.VK_T)
		{
			//ʵ����ͣ����
//			if (Global.isStop == false) {
//				Global.isStop = true;
//			}else {
//				Global.isStop = false;
//			} 
		   
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
			
			//�ж��Ƿ���е��˵�̹��
			this.hitEnemy();
			
			//�ж��Ƿ񱻻���
			this.hitme();
			
			repaint();			
		}
	}
      
}//End of MyPanel
 
