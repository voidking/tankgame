package njit.tmp;

/*********************************
 *�����ļ����ƣ�MyTankGame.java
 *���ܣ�ҳ�沼�ֵ�������������Ϸ��������������
 *********************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;
//�ӵ���������
public class MyTankGame extends JFrame 
{
	public static void main(String[] args) 
	{
		MyTankGame mtg=new MyTankGame();		
	}	
	Image image1=null;
	Image image2=null;
	Image image3=null;
	Image image4=null;
	Image image5=null;
	Image image6=null;
	Image image7=null;
	//�з�̹���������
	int enSize=4;
	//ͳ�Ƶз�̹��ʣ������
	int countEnemiesTank=enSize;
	//������˵�̹�˼���
	Vector <EnemiesTank> ets=new Vector <EnemiesTank>();	
	//����һ��ը������,������
	Vector<Bomb> bombs=new Vector<Bomb>();
	//����һ��ʥ��������,������
	Vector<Tree> trees=new Vector<Tree>();	
	//��Ϸ�е����ֲ�����,��i==1ʱ��ʾ����һ�飬��i==2ʱ��ʾѭ�����š�
	public void musicPlayer(String musicName,int i)
	{
		AudioClip clip = Applet.newAudioClip(getClass().getResource(musicName));
		if(i==1)  
			clip.play();
		if(i==2)
			clip.loop();
	}
	//̹����Ϸ�Ĺ�����
	public MyTankGame()
	{
		//���ñ���������
		super("����̹�˴�ս--ʥ����");	
		this.setLayout(new GridLayout(1,2));
		MyPanel mp =new MyPanel();
		RightPanel rp = new RightPanel();
		Thread t=new Thread(mp);
		t.start();		
		this.add(mp);
		//ע�����
		this.addKeyListener(mp);
		//����Ĭ����Ϸ���ڴ�С
		this.setSize(550,400);
		this.setVisible(true);
		//ʹ������ʾ����Ļ����		
		this.setLocationRelativeTo(null);
		//ʹ�����޷��ı��С 
		this.setResizable(false);
		this.musicPlayer("/music//christmas.mid",2);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(rp);
	}
	//�����ұߵ����
	public class RightPanel extends JPanel implements ActionListener
	{
		 
		Button btnReplay;
		public RightPanel()
		{
			this.setSize(100,400);
			btnReplay=new Button("������Ϸ");
			add(btnReplay);
			btnReplay.setBounds(450,100,80,30);
			btnReplay.addActionListener(this);
		  	setVisible(true);
		  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 }		 
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==btnReplay)
			{
				try
				{
					Runtime rt = Runtime.getRuntime(); 
				  	rt.exec("java MyTankGame");
				} 
				catch(Exception ee)
				{
				}
				 //�ر�����Ӧ�ó���.���ֻ������رյ�ǰ����,������dispose();				
				System.exit(0);
			}
		}		 
	}
		
	//�ҵ���� ��Ϸ������
	class MyPanel extends JPanel implements KeyListener,Runnable
	{		
		//����һ���ҵ�̹��
		Hero hero=null;
		//MyPanel�Ĺ�����
		public MyPanel()
		{			
			hero=new Hero(315,261);
			//ÿ��forѭ������һ�����˵�̹�˶���
			for(int i=0;i<enSize;i++)
			{	
				//����̹��λ��
				EnemiesTank et=new EnemiesTank((i)*50,0);
				et.setDirect(2);
				//��������̹��
				Thread t =new Thread(et);
				t.start();
				//������̹�˼�һ���ӵ�
				Bullet b=new Bullet(et.x+10,et.y+34,2);
				//���ӵ���������������̹������д���ӵ�������
				et.bs.add(b);
				//�ѵ���̹�˼�������
				Thread t2=new Thread(b);
				t2.start();
				ets.add(et);
			}				
			//��ʼ��������������ͼƬ���л���������ըЧ��
			image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//bomb_1.gif"));
			image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//bomb_2.gif"));
			image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//bomb_3.gif"));
			//��ʾ����
			image4=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//base.jpg"));
			//�������Ͻǵ�ͼ��
			image5=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//tankLogo.jpg"));
			//ʥ����
			image6=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//tree.png"));
			//ս��
			image7=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//battleground.jpg"));
			//���ô���Сͼ��
			setIconImage(image5);
		}
		
	
		//��дpaint����
		public void paint(Graphics g)
		{
			super.paint(g);	
			g.drawString("�淨�� ��W��S��A��D����̵ķ�����ɿ���̹�˵��˶�����J��ո���ɽ����ڵ����䡣", 5, 330);
			g.drawString("ʤ���������ڱ�֤����̹�˺ͻ��ز����ڵ����е�������������е���̹�ˡ�", 5, 345);
			//����ս��
			//g.fillRect(0, 0, 400, 300);
			g.drawImage(image7,0,0, 400,300 ,this); 
			//��������
			g.drawImage(image4,343,263, 57,37 ,this); 
			g.setColor(Color.black);
			Font f=new Font("����",Font.BOLD,15);
			g.setFont(f);
			g.setColor(Color.red);
			g.drawString("ʣ�����̹�ˣ�"+countEnemiesTank+"��", 5, 365);		
			//�����Լ���̹��
			if(hero.isLive)
			{
				this.drawTank(hero.getX(),hero.getY(), g,this.hero.getDirect(), 6);
			}
			//�����ʧ�ܵ�ʱ����ʾ"GAME OVER"������û��ʵ��ͬʱ��������Ƭ�Ρ�
			if(hero.isLive==false)
			{							
				g.setFont(new Font("����",Font.BOLD,40));
				g.setColor(Color.ORANGE);
				g.drawString("GAME OVER", 90, 135);		
			}
			//���������е���̹�˵�ʱ����ʾ"MERRY XMAS"��
			if(countEnemiesTank==0&&hero.isLive==true)
			{
				g.setFont(new Font("����",Font.BOLD,40));
				g.setColor(Color.ORANGE);
				g.drawString("MERRY XMAS", 90, 135);
			}
			
			//�������˵�̹���Լ�����̹�˵�ÿ���ӵ�
			for(int i=0;i<ets.size();i++)
			{g.setColor(Color.red);
				EnemiesTank et=ets.get(i);
				if(et.isLive)
				{					
					this.drawTank(et.getX(),et.getY(), g,et.getDirect(),i);
					//�ٻ������� ���ӵ�
					for(int j=0;j<et.bs.size();j++)
					{
						//ȡ���ӵ�
						Bullet enemyShot=et.bs.get(j);
						if(enemyShot.isLive)
						{
							g.draw3DRect(enemyShot.x,enemyShot.y,1,1,false);
						}
						else
						{
							et.bs.remove(enemyShot);							
						}
					}//for
				}//if
			}//for
			
			//��hero̹�����ÿ���ӵ���
			for(int i=0;i<this.hero.bs.size();i++)
			{ 
				Bullet myBullet=hero.bs.get(i);				
				if(hero.b!=null&&hero.b.isLive==true)
				{
					//����һ���ӵ�
					g.draw3DRect(myBullet.x,myBullet.y,1,1,false);					
				}
				if(myBullet.isLive==false)
				{		
					//��������ɾ���ӵ�
					hero.bs.remove(myBullet);					
				}
			}
			//����ʥ����
			for(int i=0;i<trees.size();i++)
			{
				Tree tree =trees.get(i);
				g.drawImage(image6, tree.x,tree.y, 30,30 ,this);
			}
			
			
			//����ը��
			for(int i=0;i<bombs.size();i++)
			{
				//ȡ��ը��
				Bomb bomb=bombs.get(i);

				//��ͬʱ�̶�Ӧ��ͬͼƬ
				if(bomb.life>6)
				{
					g.drawImage(image1, bomb.x,bomb.y, 30,30 ,this);
				}
				else if(bomb.life>3)
				{
					g.drawImage(image2, bomb.x,bomb.y, 30,30 ,this);
				}
				else
					g.drawImage(image3, bomb.x,bomb.y, 30,30 ,this);
				//��bomb������ֵ��С����ը������ֵΪ0���ͰѸ�ը����������ȥ��			
				bomb.lifeDown();
				if(bomb.life==0)
				{
					bombs.remove(bomb);
				}
			}
			//this.showInfo(g);			
		}//paint
		
		
			
		//����̹�˵ķ���
		public void drawTank(int x,int y,Graphics g,int direct,int type)
		{	
			//�ж���̹�˵����ͣ���̹ͬ�˶�Ӧ��ͬ��ɫ
			switch(type)
			{
				case 0:
				g.setColor(Color.yellow);
				break;
				case 1:
				g.setColor(Color.blue);
				break;
				case 2:
				g.setColor(Color.red);
				break;
				case 3:
				g.setColor(Color.white);
				break;				
				case 4:
				g.setColor(Color.pink);
				break;				
				case 5:
				g.setColor(Color.gray);
				break;
				case 6:
				g.setColor(Color.green);
				break;
			}
			//�жϷ���
			switch(direct)
			{
				//̹������
				case 0:
					//��̹�˵����Ĵ�
					g.fill3DRect(x,y,5, 30,false);
					//��̹�˵����Ĵ�
					g.fill3DRect(x+15,y,5, 30,false);
					//��̹����
					g.fill3DRect(x+5,y+5,10, 20,false);
					//��̹�˸���
					g.fillOval(x+5,y+10,10, 10);
					//��̹���ڹ�
					g.drawLine(x+10,y+10,x+10,y-4);
					break;
				//̹������
				case 1:
					g.fill3DRect(x-5,y+5,30, 5,false);
					g.fill3DRect(x-5,y+20,30,5,false);
					g.fill3DRect(x,y+10,20, 10,false);
					g.fillOval(x+3,y+8,10, 10);
					g.drawLine(x+10,y+15,x+15+14,y+15);
					break;
				//̹������
				case 2:
					g.fill3DRect(x,y,5, 30,false);
					g.fill3DRect(x+15,y,5, 30,false);
					g.fill3DRect(x+5,y+5,10, 20,false);
					g.fillOval(x+5,y+10,10, 10);
					g.drawLine(x+10,y+10,x+10,y+20+14);
					break;
				//̹������
				case 3:
					g.fill3DRect(x-5,y+5,30, 5,false);
					g.fill3DRect(x-5,y+20,30,5,false);
					g.fill3DRect(x,y+10,20, 10,false);
					g.fillOval(x+3,y+8,10, 10);
					g.drawLine(x+5,y+15,x-9,y+15);
					break;
			}
		
		}//����̹�˵ķ���

		//�жϵ����ڵ��Ƿ���ж��ֵ�̹��
		public void hitTank(Bullet b,Tank et)
		{
			int count=countEnemiesTank;
			//�жϸ�̹�˵ķ���(����ͬ���ж�ʱ�����õ�����Ҳ��ͬ��
			switch(et.direct)
			{
				//�������̹�˷������ϻ���
				case 0:
				case 2:
					if(b.x>et.x&&b.x<et.x+20&&b.y>et.y&&b.y<et.y+30)
					{
						//�ӵ�����,����̹������
						b.isLive=false;
						et.isLive=false;
						musicPlayer("/music//bomb.wav",1);  
						//����һ��ը��
						Bomb bomb=new Bomb(et.x,et.y);
						//����Vector
						bombs.add(bomb);
						Tree tree= new Tree(et.x,et.y);
						trees.add(tree);					
						count--;
						countEnemiesTank=count;
						//System.out.println(countEnemiesTank);						
					}
					break;
					
				//�������̹�˷����������
				case 1:
				case 3:
					if(b.x>et.x-5&&b.x<et.x+25&&b.y>et.y+5&&b.y<et.y+25)
					{
						b.isLive=false;
						et.isLive=false;
						musicPlayer("/music//bomb.wav",1);
						Bomb bomb=new Bomb(et.x,et.y);
						//����Vector
						bombs.add(bomb);
						Tree tree= new Tree(et.x,et.y);
						trees.add(tree);						
						count--;
						countEnemiesTank=count;
						//System.out.println(countEnemiesTank);						
					}
					break;
			}//switch
		}//hitTank
		
		//�ж����̹�˵ĸ����ӵ��Ƿ���е���̹�˻��߻��أ�����д�������˿ɶ��ԣ�
		public void hitEnimiesTankOrBase()
		{
			for(int i=0;i<hero.bs.size();i++)
			{
				//ȡ������ҵ�ÿ���ӵ�
				Bullet myBullet=hero.bs.get(i);
				//�ж��ӵ��Ƿ񻹴���
				if(myBullet.isLive)
				{   //���ж����̹���Ƿ���л���
					if(myBullet.x>343&&myBullet.x<400&&myBullet.y>263&&myBullet.y<300)
					{
						hero.isLive=false;
						musicPlayer("/music//bomb.wav",1);
					}					
					//ȡ��ÿ������̹����֮�ж�
					for(int j=0;j<ets.size();j++)
					{
						//ȡ��̹��
						EnemiesTank et=ets.get(j);
						if(et.isLive)
						{
							this.hitTank(myBullet, et);
						}
					}
				}
			}	
		 }

		//�жϵ��˵�̹�˵ĸ����ӵ��Ƿ���������̹�˻��߻���
		public void hitHeroOrBase()
		{
			//ȡ��ÿ�����˵�̹��
			for(int i=0;i<ets.size();i++)
			{				
				EnemiesTank et=ets.get(i);
				//ȡ��ÿ���ӵ�
				for(int j=0;j<et.bs.size();j++)
				{
					Bullet enemyShot=et.bs.get(j);
					if(enemyShot.x>343&&enemyShot.x<400&&enemyShot.y>263&&enemyShot.y<300)
					{						 	
						hero.isLive=false;
					}
					if(hero.isLive)
					{
						this.hitTank(enemyShot,hero);
					}
				}
			}
		}
		
		public void run()
		{
			//ÿ��100�����ػ�һ��
			while(true)
			{
				try
				{
					Thread.sleep(100);					
				}
				catch(Exception e)
				{
					//�������д�ӡ�쳣��Ϣ�ڳ����г����λ�ü�ԭ��
					e.printStackTrace();
				}
				this.hitEnimiesTankOrBase();
				this.hitHeroOrBase();
				//�ػ�
				this.repaint();				
			}
		}

		//����̹��ǰ�������˶��ķ���
		//����a�������ϵķ����̹�������ƶ�
		//����s�������ҵķ����̹�������ƶ�
		//����w�������µķ����̹�������ƶ�
		//����d��������ķ����̹�������ƶ�
		public void keyPressed(KeyEvent arg0)
		{
			if(arg0.getKeyCode()==KeyEvent.VK_W||arg0.getKeyCode()==KeyEvent.VK_UP)
			{
				//�����ҵ�̹�˵ķ���
				this.hero.setDirect(0);	
				this.hero.moveUp();
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_D||arg0.getKeyCode()==KeyEvent.VK_RIGHT)
			{
				this.hero.setDirect(1);	
				this.hero.moveRight();
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_S||arg0.getKeyCode()==KeyEvent.VK_DOWN)
			{
				this.hero.setDirect(2);	
				this.hero.moveDown();
			}
			else if(arg0.getKeyCode()==KeyEvent.VK_A||arg0.getKeyCode()==KeyEvent.VK_LEFT)
			{
				this.hero.setDirect(3);	
				this.hero.moveLeft();
			}			
			//�ж�����Ƿ���J��ո������else if����������if��ʹ̹��һ����һ�߳��ӵ� ��
			if(arg0.getKeyCode()==KeyEvent.VK_J||arg0.getKeyCode()==KeyEvent.VK_SPACE)
			{
				//���ɷ���6���ӵ�
				if(this.hero.bs.size()<5)
				{
					this.hero.shotEnemy();
				}
			}
			//�����ػ�MyPanel
			this.repaint();
		}
		public void keyReleased(KeyEvent arg0)
		{
		}
		public void keyTyped(KeyEvent arg0)
		{
		}
	}

	
}
