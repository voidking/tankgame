package njit.tmp;

/*********************************
 *程序文件名称：MyTankGame.java
 *功能：页面布局的容器、绘制游戏场景、播放音乐
 *********************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.*;
//子弹可以连发
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
	//敌方坦克最大数量
	int enSize=4;
	//统计敌方坦克剩余数量
	int countEnemiesTank=enSize;
	//定义敌人的坦克集合
	Vector <EnemiesTank> ets=new Vector <EnemiesTank>();	
	//定义一个炸弹集合,是向量
	Vector<Bomb> bombs=new Vector<Bomb>();
	//定义一个圣诞树集合,是向量
	Vector<Tree> trees=new Vector<Tree>();	
	//游戏中的音乐播放器,当i==1时表示播放一遍，当i==2时表示循环播放。
	public void musicPlayer(String musicName,int i)
	{
		AudioClip clip = Applet.newAudioClip(getClass().getResource(musicName));
		if(i==1)  
			clip.play();
		if(i==2)
			clip.loop();
	}
	//坦克游戏的构造器
	public MyTankGame()
	{
		//设置标题栏名称
		super("简易坦克大战--圣诞版");	
		this.setLayout(new GridLayout(1,2));
		MyPanel mp =new MyPanel();
		RightPanel rp = new RightPanel();
		Thread t=new Thread(mp);
		t.start();		
		this.add(mp);
		//注册监听
		this.addKeyListener(mp);
		//设置默认游戏窗口大小
		this.setSize(550,400);
		this.setVisible(true);
		//使窗体显示在屏幕中央		
		this.setLocationRelativeTo(null);
		//使窗体无法改变大小 
		this.setResizable(false);
		this.musicPlayer("/music//christmas.mid",2);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(rp);
	}
	//窗体右边的面板
	public class RightPanel extends JPanel implements ActionListener
	{
		 
		Button btnReplay;
		public RightPanel()
		{
			this.setSize(100,400);
			btnReplay=new Button("重新游戏");
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
				 //关闭整个应用程序.如果只是是想关闭当前窗口,可以用dispose();				
				System.exit(0);
			}
		}		 
	}
		
	//我的面板 游戏主界面
	class MyPanel extends JPanel implements KeyListener,Runnable
	{		
		//定义一个我的坦克
		Hero hero=null;
		//MyPanel的构造器
		public MyPanel()
		{			
			hero=new Hero(315,261);
			//每趟for循环创建一辆敌人的坦克对象
			for(int i=0;i<enSize;i++)
			{	
				//设置坦克位置
				EnemiesTank et=new EnemiesTank((i)*50,0);
				et.setDirect(2);
				//启动敌人坦克
				Thread t =new Thread(et);
				t.start();
				//给敌人坦克加一颗子弹
				Bullet b=new Bullet(et.x+10,et.y+34,2);
				//把子弹加入向量（敌人坦克类中写有子弹向量）
				et.bs.add(b);
				//把敌人坦克加入向量
				Thread t2=new Thread(b);
				t2.start();
				ets.add(et);
			}				
			//初始化，利用这三张图片的切换来产生爆炸效果
			image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//bomb_1.gif"));
			image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//bomb_2.gif"));
			image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//bomb_3.gif"));
			//表示基地
			image4=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//base.jpg"));
			//窗体左上角的图标
			image5=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//tankLogo.jpg"));
			//圣诞树
			image6=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//tree.png"));
			//战场
			image7=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/img//battleground.jpg"));
			//设置窗体小图标
			setIconImage(image5);
		}
		
	
		//重写paint方法
		public void paint(Graphics g)
		{
			super.paint(g);	
			g.drawString("玩法： 按W、S、A、D或键盘的方向键可控制坦克的运动。按J或空格键可进行炮弹发射。", 5, 330);
			g.drawString("胜利条件：在保证您的坦克和基地不被炮弹击中的情况下消灭所有敌人坦克。", 5, 345);
			//画出战场
			//g.fillRect(0, 0, 400, 300);
			g.drawImage(image7,0,0, 400,300 ,this); 
			//画出基地
			g.drawImage(image4,343,263, 57,37 ,this); 
			g.setColor(Color.black);
			Font f=new Font("黑体",Font.BOLD,15);
			g.setFont(f);
			g.setColor(Color.red);
			g.drawString("剩余敌人坦克："+countEnemiesTank+"辆", 5, 365);		
			//画出自己的坦克
			if(hero.isLive)
			{
				this.drawTank(hero.getX(),hero.getY(), g,this.hero.getDirect(), 6);
			}
			//当玩家失败的时候显示"GAME OVER"，但还没能实现同时播放音乐片段。
			if(hero.isLive==false)
			{							
				g.setFont(new Font("黑体",Font.BOLD,40));
				g.setColor(Color.ORANGE);
				g.drawString("GAME OVER", 90, 135);		
			}
			//当击毁所有敌人坦克的时候显示"MERRY XMAS"。
			if(countEnemiesTank==0&&hero.isLive==true)
			{
				g.setFont(new Font("黑体",Font.BOLD,40));
				g.setColor(Color.ORANGE);
				g.drawString("MERRY XMAS", 90, 135);
			}
			
			//画出敌人的坦克以及敌人坦克的每颗子弹
			for(int i=0;i<ets.size();i++)
			{g.setColor(Color.red);
				EnemiesTank et=ets.get(i);
				if(et.isLive)
				{					
					this.drawTank(et.getX(),et.getY(), g,et.getDirect(),i);
					//再画出敌人 的子弹
					for(int j=0;j<et.bs.size();j++)
					{
						//取出子弹
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
			
			//画hero坦克类的每颗子弹。
			for(int i=0;i<this.hero.bs.size();i++)
			{ 
				Bullet myBullet=hero.bs.get(i);				
				if(hero.b!=null&&hero.b.isLive==true)
				{
					//画出一颗子弹
					g.draw3DRect(myBullet.x,myBullet.y,1,1,false);					
				}
				if(myBullet.isLive==false)
				{		
					//从向量中删除子弹
					hero.bs.remove(myBullet);					
				}
			}
			//画出圣诞树
			for(int i=0;i<trees.size();i++)
			{
				Tree tree =trees.get(i);
				g.drawImage(image6, tree.x,tree.y, 30,30 ,this);
			}
			
			
			//画出炸弹
			for(int i=0;i<bombs.size();i++)
			{
				//取出炸弹
				Bomb bomb=bombs.get(i);

				//不同时刻对应不同图片
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
				//让bomb的生命值减小，当炸弹生命值为0，就把该炸弹从向量中去掉			
				bomb.lifeDown();
				if(bomb.life==0)
				{
					bombs.remove(bomb);
				}
			}
			//this.showInfo(g);			
		}//paint
		
		
			
		//画出坦克的方法
		public void drawTank(int x,int y,Graphics g,int direct,int type)
		{	
			//判断是坦克的类型，不同坦克对应不同颜色
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
			//判断方向
			switch(direct)
			{
				//坦克向上
				case 0:
					//画坦克的左履带
					g.fill3DRect(x,y,5, 30,false);
					//画坦克的右履带
					g.fill3DRect(x+15,y,5, 30,false);
					//画坦克身
					g.fill3DRect(x+5,y+5,10, 20,false);
					//画坦克盖子
					g.fillOval(x+5,y+10,10, 10);
					//画坦克炮管
					g.drawLine(x+10,y+10,x+10,y-4);
					break;
				//坦克向右
				case 1:
					g.fill3DRect(x-5,y+5,30, 5,false);
					g.fill3DRect(x-5,y+20,30,5,false);
					g.fill3DRect(x,y+10,20, 10,false);
					g.fillOval(x+3,y+8,10, 10);
					g.drawLine(x+10,y+15,x+15+14,y+15);
					break;
				//坦克向下
				case 2:
					g.fill3DRect(x,y,5, 30,false);
					g.fill3DRect(x+15,y,5, 30,false);
					g.fill3DRect(x+5,y+5,10, 20,false);
					g.fillOval(x+5,y+10,10, 10);
					g.drawLine(x+10,y+10,x+10,y+20+14);
					break;
				//坦克向左
				case 3:
					g.fill3DRect(x-5,y+5,30, 5,false);
					g.fill3DRect(x-5,y+20,30,5,false);
					g.fill3DRect(x,y+10,20, 10,false);
					g.fillOval(x+3,y+8,10, 10);
					g.drawLine(x+5,y+15,x-9,y+15);
					break;
			}
		
		}//画出坦克的方法

		//判断单颗炮弹是否击中对手的坦克
		public void hitTank(Bullet b,Tank et)
		{
			int count=countEnemiesTank;
			//判断该坦克的方向(方向不同，判断时所采用的坐标也不同）
			switch(et.direct)
			{
				//如果对手坦克方向是上或下
				case 0:
				case 2:
					if(b.x>et.x&&b.x<et.x+20&&b.y>et.y&&b.y<et.y+30)
					{
						//子弹死亡,敌人坦克死亡
						b.isLive=false;
						et.isLive=false;
						musicPlayer("/music//bomb.wav",1);  
						//创建一颗炸弹
						Bomb bomb=new Bomb(et.x,et.y);
						//放入Vector
						bombs.add(bomb);
						Tree tree= new Tree(et.x,et.y);
						trees.add(tree);					
						count--;
						countEnemiesTank=count;
						//System.out.println(countEnemiesTank);						
					}
					break;
					
				//如果对手坦克方向是左或右
				case 1:
				case 3:
					if(b.x>et.x-5&&b.x<et.x+25&&b.y>et.y+5&&b.y<et.y+25)
					{
						b.isLive=false;
						et.isLive=false;
						musicPlayer("/music//bomb.wav",1);
						Bomb bomb=new Bomb(et.x,et.y);
						//放入Vector
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
		
		//判断玩家坦克的各颗子弹是否击中敌人坦克或者基地（这种写法增加了可读性）
		public void hitEnimiesTankOrBase()
		{
			for(int i=0;i<hero.bs.size();i++)
			{
				//取出子玩家的每颗子弹
				Bullet myBullet=hero.bs.get(i);
				//判断子弹是否还存在
				if(myBullet.isLive)
				{   //先判断玩家坦克是否击中基地
					if(myBullet.x>343&&myBullet.x<400&&myBullet.y>263&&myBullet.y<300)
					{
						hero.isLive=false;
						musicPlayer("/music//bomb.wav",1);
					}					
					//取出每辆敌人坦克与之判断
					for(int j=0;j<ets.size();j++)
					{
						//取出坦克
						EnemiesTank et=ets.get(j);
						if(et.isLive)
						{
							this.hitTank(myBullet, et);
						}
					}
				}
			}	
		 }

		//判断敌人的坦克的各颗子弹是否击中了玩家坦克或者基地
		public void hitHeroOrBase()
		{
			//取出每个敌人的坦克
			for(int i=0;i<ets.size();i++)
			{				
				EnemiesTank et=ets.get(i);
				//取出每颗子弹
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
			//每隔100毫秒重画一次
			while(true)
			{
				try
				{
					Thread.sleep(100);					
				}
				catch(Exception e)
				{
					//在命令行打印异常信息在程序中出错的位置及原因
					e.printStackTrace();
				}
				this.hitEnimiesTankOrBase();
				this.hitHeroOrBase();
				//重绘
				this.repaint();				
			}
		}

		//控制坦克前后左右运动的方法
		//按下a键或向上的方向键坦克向上移动
		//按下s键或向右的方向键坦克向右移动
		//按下w键或向下的方向键坦克向下移动
		//按下d键或向左的方向键坦克向左移动
		public void keyPressed(KeyEvent arg0)
		{
			if(arg0.getKeyCode()==KeyEvent.VK_W||arg0.getKeyCode()==KeyEvent.VK_UP)
			{
				//设置我的坦克的方向
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
			//判断玩家是否按下J或空格键（与else if的区别在于if可使坦克一边跑一边出子弹 ）
			if(arg0.getKeyCode()==KeyEvent.VK_J||arg0.getKeyCode()==KeyEvent.VK_SPACE)
			{
				//最多可发射6颗子弹
				if(this.hero.bs.size()<5)
				{
					this.hero.shotEnemy();
				}
			}
			//必须重画MyPanel
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
