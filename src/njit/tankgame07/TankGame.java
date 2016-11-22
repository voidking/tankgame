/*
 * 坦克大战07
 */

package njit.tankgame07;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TankGame extends JFrame implements ActionListener {
	public static void main(String[] args) 
	{
		TankGame tankgame = new TankGame();
	}

	StartPanel startPanel;
	MyPanel gamePanel;

	public TankGame() {
		// 菜单条
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu menu = new JMenu("菜单");
		menu.setMnemonic('M');
		menu.setToolTipText("Alt+M");

		// 开始游戏
		JMenu start = new JMenu("开始");
		
		JMenuItem newGame = new JMenuItem("新游戏");
		newGame.addActionListener(this);
		newGame.setActionCommand("newGame");
		
		JMenuItem loadGame = new JMenuItem("读取存档");
		loadGame.addActionListener(this);
		loadGame.setActionCommand("loadGame");
		
		JMenuItem netFight = new JMenuItem("网络对战");
		netFight.addActionListener(this);
		netFight.setActionCommand("netFight");
		
		
		start.add(newGame);
		start.add(loadGame);
		start.add(netFight);
		menu.add(start);

		// 存档
		JMenuItem save = new JMenuItem("存档");
		save.addActionListener(this);
		save.setActionCommand("save");
		menu.add(save);
		
		JMenuItem set = new JMenuItem("设置");
		set.addActionListener(this);
		set.setActionCommand("set");
		menu.add(set);
		
		JMenuItem quit = new JMenuItem("退出");
		quit.addActionListener(this);
		quit.setActionCommand("quit");		
		menu.add(quit);

		menuBar.add(menu);

		// 生成mypanel面板组件
		startPanel = new StartPanel();
		startPanel.setBackground(Color.gray);
		Thread thread = new Thread(startPanel);
		thread.start();

		// 把startPanel加入框架
		this.add(startPanel);

		// 设置外观
		this.setIconImage((new ImageIcon("images/tank.gif")).getImage());
		this.setTitle(Global.title);
		this.setSize(Global.frameWide, Global.frameHeight);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("newGame")) {
			// 创建战场面板
			gamePanel = new MyPanel("newGame");

			// 启动mp线程
			Thread t = new Thread(gamePanel);
			t.start();
			// 先删除旧的开始面板
			this.remove(startPanel);
			this.add(gamePanel);
			// 注册监听
			this.addKeyListener(gamePanel);
			// 显示,刷新JFrame
			this.setVisible(true);

		} else if (e.getActionCommand().equals("loadGame")) {	           
	           
			// 创建战场面板
			gamePanel = new MyPanel("loadGame");

			// 启动gamePanel线程
			Thread panelThread = new Thread(gamePanel);
			panelThread.start();
			// 先删除旧的开始面板
			this.remove(startPanel);
			this.add(gamePanel);
			// 注册监听
			this.addKeyListener(gamePanel);
			// 显示,刷新JFrame
			this.setVisible(true);
			
		} else if (e.getActionCommand().equals("netFight")) {
			// 创建战场面板
			gamePanel = new MyPanel("netFight");
			// 启动mp线程
			Thread t = new Thread(gamePanel);
			t.start();
			// 先删除旧的开始面板
			this.remove(startPanel);
			this.add(gamePanel);
			// 注册监听
			this.addKeyListener(gamePanel);
			// 显示,刷新JFrame
			this.setVisible(true);
			
		} else if (e.getActionCommand().equals("save")) {
		//Recorder recorder = new Recorder();
		Recorder.save();
		} else if (e.getActionCommand().equals("set")) {
			new SetFrame();
		} else if (e.getActionCommand().equals("quit")) {

			System.exit(0);
		}

	}

}// End of TankGame

class StartPanel extends JPanel implements Runnable {

	int times = 0;

	public void paint(Graphics g) {
		super.paint(g);
		if (times % 2 == 0) {
			g.setColor(Color.yellow);
			// 开关信息的字体
			Font myFont = new Font("华文新魏", Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("简易版坦克大战", 120, 200);
		}

	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			// 休眠
			try {

				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			times++;

			// 重画
			this.repaint();
		}

	}
}// End of StartPanel

class MyPanel extends JPanel implements KeyListener, Runnable {
	
	//仅仅是为了调用setEnemyVector
	Recorder recorder = new Recorder();
	
	// 定义我的坦克
	Hero hero = null;

	// 定义敌人坦克
	Vector<Enemy> enemyVector = new Vector<Enemy>();
	
	// 定义爆炸集合
	Vector<Bomb> bombVector = new Vector<Bomb>();

	// 定义三张图片,三张图片切换构成一个爆炸效果
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	//Vector<Hero> heros = new Vector<Hero>();
	
	String paintFlag;
	Vector<Hero> heros = new Vector<Hero>();
	
	public MyPanel(String flag) {
		
		paintFlag = flag;
		if (flag.equals("newGame")) {
			// 初始化我的坦克
			hero = new Hero(Global.heroX, Global.heroY, Global.heroDirect,
					Global.heroColor, Global.heroSpeed);
			// 初始化敌人坦克
			for (int i = 0; i < Global.enemyNumber; i++) {
				Enemy enemy = new Enemy((i + 1) * Global.enemyDistance, 0,
						Global.enemyDirect, Global.enemyColor,
						Global.enemySpeed);

				Thread enemyThread = new Thread(enemy);
				enemyThread.start();
				// 给敌人坦克添加一颗子弹
				Bullet enemyBullet = new Bullet(enemy.x + 9, enemy.y + 30,
						Direct.DOWN, Global.enemyBulletSpeed);
				enemy.bullets.add(enemyBullet);
				Thread bulletThread = new Thread(enemyBullet);
				bulletThread.start();
				enemyVector.add(enemy);

				enemy.getPanelTank(enemyVector);
			}
			recorder.setEnemyVector(enemyVector);
		}else if(flag.equals("loadGame")){
			
			//恢复记录 
			Vector<Node> nodes=new Vector<Node>();
			nodes = Recorder.loadGame();
			
			hero=new Hero(Global.heroX,Global.heroY,Direct.UP,Global.heroColor,Global.heroSpeed);
			//初始化敌人的坦克
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);

				Enemy enemy=new Enemy(node.x,node.y,node.direct,Global.enemyColor,Global.enemySpeed);
				Thread enemyThread=new Thread(enemy);
				enemyThread.start();
				//给敌人坦克添加一颗子弹
				Bullet enemyBullet=new Bullet(enemy.x+10,enemy.y+30,enemy.direct,Global.enemyBulletSpeed);
				enemy.bullets.add(enemyBullet);
				Thread bulletThread=new Thread(enemyBullet);
				bulletThread.start();
				enemyVector.add(enemy);
				enemy.getPanelTank(enemyVector);
			}
			recorder.setEnemyVector(enemyVector);
		}else if(flag.equals("netFight"))
		{
			NetClient netClient = new NetClient();			
			// 初始化我的坦克
			hero = new Hero(Global.heroX, Global.heroY, Global.heroDirect,
					Global.heroColor, Global.heroSpeed);
			netClient.connect("127.0.0.1", 8888,hero,heros);//connect方法在构造函数里调用会报错，为什么？
		}
		
		// 初始化图片
		try {
			image1 = ImageIO.read(new File("bomb_1.gif"));
			image2 = ImageIO.read(new File("bomb_2.gif"));
			image3 = ImageIO.read(new File("bomb_3.gif"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Audio apw=new Audio("./111.wav");
		apw.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (paintFlag.equals("newGame") || paintFlag.equals("loadGame")) {
			g.setColor(Global.panelBackground);
			g.fillRect(0, 0, Global.panelWide, Global.panelHeight);
			// 提示信息
			showInfo(g);
			// 画出我的坦克
			if (this.hero.live == true) {
				this.hero.drawTank(g);
			}
			// 画出我的子弹
			for (int i = 0; i < this.hero.bullets.size(); i++) {
				Bullet heroBulletInUse = hero.bullets.get(i);
				if (heroBulletInUse != null && heroBulletInUse.live == true) {
					g.draw3DRect(heroBulletInUse.x, heroBulletInUse.y, 1, 1,
							false);
				}

				// 如果子弹死亡，则从向量中去掉
				if (heroBulletInUse.live == false) {
					hero.bullets.remove(heroBulletInUse);
				}
			}
			// 画出敌人坦克
			for (int i = 0; i < enemyVector.size(); i++) {
				Enemy enemy = enemyVector.get(i);
				if (enemy.live == true) {
					this.enemyVector.get(i).drawTank(g);
					// 画出敌人子弹
					for (int j = 0; j < enemy.bullets.size(); j++) {
						// 取出子弹
						Bullet eb = enemy.bullets.get(j);
						if (eb.live == true) {
							g.draw3DRect(eb.x, eb.y, 1, 1, false);
						}
						if (eb.live == false) {
							enemy.bullets.remove(eb);
						}
					}
				}
			}
			// 画出爆炸效果
			for (int i = 0; i < bombVector.size(); i++) {
				// 取出炸弹
				Bomb bomb = bombVector.get(i);
				if (bomb.lifeTime > 6) {
					g.drawImage(image1, bomb.x, bomb.y, 30, 30, this);
				} else if (bomb.lifeTime > 3) {
					g.drawImage(image2, bomb.x, bomb.y, 30, 30, this);
				} else {
					g.drawImage(image3, bomb.x, bomb.y, 30, 30, this);
				}
				// 让b的生命值减少
				bomb.lifeDown();

				if (bomb.lifeTime == 0) {
					bombVector.remove(bomb);
				}

			}
			// 当玩家失败的时候显示"GAME OVER"
			if (hero.live == false) {
				g.setFont(new Font("黑体", Font.BOLD, 40));
				g.setColor(Color.ORANGE);
				g.drawString("GAME OVER", 150, 150);
			}
			// 当击毁所有敌人坦克的时候显示"YOU WIN"。
			if (Recorder.getDeadEnemy() == Global.enemyNumber
					&& hero.live == true) {
				g.setFont(new Font("黑体", Font.BOLD, 40));
				g.setColor(Color.ORANGE);
				g.drawString("YOU WIN !!!", 150, 150);
			}
		}else if(paintFlag.equals("netFight"))
		{
			g.setColor(Global.panelBackground);
			g.fillRect(0, 0, Global.frameWide, Global.frameHeight);
			g.setColor(Color.red);
			g.drawString("ID:"+hero.id, hero.x-10, hero.y-10);
			if (this.hero.live == true) {
				this.hero.drawTank(g);
			}
			
			for(int i = 0;i < heros.size();i++)
			{
				Hero otherHero = heros.get(i);
				g.drawString("ID:"+otherHero.id, otherHero.x-10, otherHero.y-10);
				if(otherHero.id != hero.id)
				{
					otherHero.drawTank(g);
				}			
			}
			
			// 画出我的子弹
			for (int i = 0; i < this.hero.bullets.size(); i++) {
				Bullet heroBulletInUse = hero.bullets.get(i);
				if (heroBulletInUse != null && heroBulletInUse.live == true) {
					g.draw3DRect(heroBulletInUse.x, heroBulletInUse.y, 1, 1,
							false);
				}

				// 如果子弹死亡，则从向量中去掉
				if (heroBulletInUse.live == false) {
					hero.bullets.remove(heroBulletInUse);
				}
			}
		}

	}

	// 画出提示信息
	public void showInfo(Graphics g) {
		// 画出提示信息坦克(该坦克不参与战斗)
		Tank enemyTank = new Tank(10, Global.panelHeight + 10, Direct.UP,
				Global.enemyColor, 0);
		enemyTank.drawTank(g);
		g.setColor(Color.black);
		g.drawString(Global.enemyNumber + "", 40, Global.panelHeight + 30);

		Tank heroTank = new Tank(70, Global.panelHeight + 10, Direct.UP,
				Global.heroColor, 0);
		heroTank.drawTank(g);
		g.setColor(Color.black);
		g.drawString(Global.heroNumber + "", 100, Global.panelHeight + 30);

		// 画出玩家的总成绩
		g.setColor(Color.black);
		Font f = new Font("宋体", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("消灭坦克数", 200, Global.panelHeight + 30);

		Tank deadTank = new Tank(310, Global.panelHeight + 10, Direct.DOWN,
				Global.enemyColor, 0);
		deadTank.drawTank(g);

		g.setColor(Color.black);
		g.drawString(Recorder.getDeadEnemy() + "", 350, Global.panelHeight + 30);
	}

	// 判断坦克是否被子弹击中
	public boolean hit(Bullet bullet, Tank tank) {
		boolean ishit = false;
		switch (tank.direct) {
		// 敌人坦克方向向上或向下
		case UP:
		case DOWN:
			if (bullet.x > tank.x && bullet.x < tank.x + 20
					&& bullet.y > tank.y && bullet.y < tank.y + 30) {
				bullet.live = false;
				tank.live = false;
				ishit = true;
				// 创建一次爆炸，放入Vector
				Bomb bomb = new Bomb(tank.x, tank.y);
				bombVector.add(bomb);
			}
			break;
		// 敌人坦克方向向左或向右
		case RIGHT:
		case LEFT:
			if (bullet.x > tank.x && bullet.x < tank.x + 30
					&& bullet.y > tank.y && bullet.y < tank.y + 20) {
				bullet.live = false;
				tank.live = false;
				ishit = true;
				// 创建一次爆炸，放入Vector
				Bomb bomb = new Bomb(tank.x, tank.y);
				bombVector.add(bomb);
			}
			break;
		default:
			break;
		}
		return ishit;
	}

	// 判断是否击中敌人坦克
	public void hitEnemy() {
		for (int i = 0; i < hero.bullets.size(); i++) {
			Bullet b = hero.bullets.get(i);
			if (b.live == true) {
				for (int j = 0; j < enemyVector.size(); j++) {
					Enemy e = enemyVector.get(j);
					if (e.live == true) {
						if (this.hit(b, e)) {
							// 减少敌人数量
							Global.enemyNumber--;
							// 增加我的记录
							Recorder.addDeadEnemy();
						}
					}
				}
			}
		}
	}

	// 判断是否被击中
	public void hitme() {
		if (this.hero.live == true) {
			// 取出每一个敌人坦克
			for (int i = 0; i < this.enemyVector.size(); i++) {
				// 取出坦克
				Enemy e = enemyVector.get(i);
				// 取出每一颗子弹
				for (int j = 0; j < e.bullets.size(); j++) {
					// 取出子弹
					Bullet b = e.bullets.get(j);
					this.hit(b, hero);
				}
			}
		}
	}

	// 监听按下键盘的操作
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			if (hero.y > 0) {
				// 当按键为“W”时，坦克方向变为上，坦克位置向上移动speed个单位像素
				hero.direct = Direct.UP;
				hero.y -= hero.speed;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			if (hero.x < Global.panelWide - 30) {
				hero.direct = Direct.RIGHT;
				hero.x += hero.speed;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			if (hero.y < Global.panelHeight - 30) {
				hero.direct = Direct.DOWN;
				hero.y += hero.speed;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			if (hero.x > 0) {
				hero.direct = Direct.LEFT;
				hero.x -= hero.speed;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_J) {

			// 当按下“J”时，坦克发射子弹
			if (this.hero.bullets.size() < Global.heroBulletNumber) {
				this.hero.fire(Global.heroBulletSpeed);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_T) {
			// 实现暂停功能
			// if (Global.isStop == false) {
			// Global.isStop = true;
			// }else {
			// Global.isStop = false;
			// }

		}

		// 为了看到移动效果，需要重绘图形（后来面板做成了线程，这个重绘函数可以删除）
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

	// 实现Runnable接口
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}

			this.hitEnemy();

			this.hitme();

			repaint();
		}
	}

}// End of MyPanel

class SetFrame extends JFrame implements ActionListener
{
	JRadioButton singleMode,doubleMode;
	
	JComboBox<String> comboHeroColor,comboEnemyColor;
	JComboBox<Integer> comboHeroNumber,comboEnemyNumber;
	JComboBox<Integer> comboHeroSpeed,comboEnemySpeed;
	JComboBox<Integer> comboHeroBulletNumber,comboEnemyBulletNumber;
	JComboBox<Integer> comboHeroBulletSpeed,comboEnemyBulletSpeed;
	
	String colors[] = {"红","橙","黄","绿","青","蓝"};
	Color realColors[] = {Color.red,Color.orange,Color.yellow,Color.green,Color.cyan,Color.blue};
	
	public SetFrame()
	{		
		//中部
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel heroSetPanel = new JPanel();
		heroSetPanel.setLayout(new GridLayout(5,2));
		
		JLabel heroColor = new JLabel("我的坦克颜色");
		heroSetPanel.add(heroColor);
		comboHeroColor = new JComboBox<String>(colors);
		heroSetPanel.add(comboHeroColor);
		
		JLabel heroNumber = new JLabel("我的坦克数量");
		heroSetPanel.add(heroNumber);
		Integer heroNumberArr[] ={1}; 
		comboHeroNumber = new JComboBox<Integer>(heroNumberArr);
		heroSetPanel.add(comboHeroNumber);
		
		JLabel heroSpeed = new JLabel("我的坦克速度");
		heroSetPanel.add(heroSpeed);
		Integer heroSpeedArr[] = {1,2,3,4,5,6};
		comboHeroSpeed = new JComboBox<Integer>(heroSpeedArr);
		heroSetPanel.add(comboHeroSpeed);
		
		JLabel heroBulletNumber = new JLabel("我的子弹数量");
		heroSetPanel.add(heroBulletNumber);
		Integer heroBulletNumberArr[] = {1,2,3,4,5,6,7,8,9,10};
		comboHeroBulletNumber = new JComboBox<Integer>(heroBulletNumberArr);
		heroSetPanel.add(comboHeroBulletNumber);
		
		JLabel heroBulletSpeed = new JLabel("我的子弹速度");
		heroSetPanel.add(heroBulletSpeed);
		Integer heroBullerSpeedArr[] = {1,2,3,4,5,6};
		comboHeroBulletSpeed = new JComboBox<Integer>(heroBullerSpeedArr);
		heroSetPanel.add(comboHeroBulletSpeed);
		
		JPanel enemySetPanel = new JPanel();
		enemySetPanel.setLayout(new GridLayout(5,2));
		
		JLabel enemyColor = new JLabel("敌人坦克颜色");
		enemySetPanel.add(enemyColor);
		comboEnemyColor = new JComboBox<String>(colors);
		enemySetPanel.add(comboEnemyColor);
		
		JLabel enemyNumber = new JLabel("敌人坦克数量");
		enemySetPanel.add(enemyNumber);
		Integer enemyNumberArr[] ={1,2,3,4,5,6}; 
		comboEnemyNumber = new JComboBox<Integer>(enemyNumberArr);
		enemySetPanel.add(comboEnemyNumber);
		
		JLabel enemySpeed = new JLabel("敌人坦克速度");
		enemySetPanel.add(enemySpeed);
		Integer enemySpeedArr[] = {1,2,3,4,5,6};
		comboEnemySpeed = new JComboBox<Integer>(enemySpeedArr);
		enemySetPanel.add(comboEnemySpeed);
		
		JLabel enemyBulletNumber = new JLabel("敌人子弹数量");
		enemySetPanel.add(enemyBulletNumber);
		Integer enemyBulletNumberArr[] = {1,2,3,4,5,6,7,8,9,10};
		comboEnemyBulletNumber = new JComboBox<Integer>(enemyBulletNumberArr);
		enemySetPanel.add(comboEnemyBulletNumber);
		
		JLabel enemyBulletSpeed = new JLabel("敌人子弹速度");
		enemySetPanel.add(enemyBulletSpeed);
		Integer enemyBullerSpeedArr[] = {1,2,3,4,5,6};
		comboEnemyBulletSpeed = new JComboBox<Integer>(enemyBullerSpeedArr);
		enemySetPanel.add(comboEnemyBulletSpeed);
		
		
		tabbedPane.add("我的坦克设置",heroSetPanel);
		tabbedPane.add("敌人坦克设置",enemySetPanel);
		this.add(tabbedPane,BorderLayout.CENTER);
		
		
		//南部
		JButton confirm = new JButton("确定");
		confirm.addActionListener(this);
		confirm.setActionCommand("confirm");
		this.add(confirm,BorderLayout.SOUTH);
		
		
		this.setTitle("设置");
		this.setSize(300,300);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource() instanceof JRadioButton)
		{
			if(e.getSource() == singleMode)
			{
				
			}
			if(e.getSource() == doubleMode)
			{
				
			}
		}
		
		Global.heroColor = realColors[(int)comboHeroColor.getSelectedIndex()];
		Global.heroNumber = (int)comboHeroNumber.getSelectedItem();
	    Global.heroSpeed = (int)comboHeroSpeed.getSelectedItem();
	    Global.heroBulletNumber = (int)comboHeroBulletNumber.getSelectedItem();
	    Global.heroBulletSpeed = (int)comboHeroBulletSpeed.getSelectedItem();
		

		Global.enemyColor = realColors[(int)comboEnemyColor.getSelectedIndex()];
	    Global.enemySpeed = (int)comboEnemySpeed.getSelectedItem();    
	    Global.enemyNumber = (int)comboEnemyNumber.getSelectedItem();
	    Global.enemyBulletNumber = (int)comboEnemyBulletNumber.getSelectedItem();
	    Global.enemyBulletSpeed = (int)comboEnemyBulletSpeed.getSelectedItem();
	    
	    if(e.getActionCommand().equals("confirm"))
	    {
	    	repaint();
	    }
		
	}
}

