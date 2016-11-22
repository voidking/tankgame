/*
 * ̹�˴�ս06
 */

package njit.tankgame06;

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
		// �˵���
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu menu = new JMenu("�˵�");
		menu.setMnemonic('M');
		menu.setToolTipText("Alt+M");

		// ��ʼ��Ϸ
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

		// �浵�˳�
		JMenuItem save = new JMenuItem("�浵");
		save.addActionListener(this);
		save.setActionCommand("save");
		JMenuItem quit = new JMenuItem("�˳�");
		quit.addActionListener(this);
		quit.setActionCommand("quit");
		menu.add(save);
		menu.add(quit);

		menuBar.add(menu);

		// ����mypanel������
		startPanel = new StartPanel();
		startPanel.setBackground(Color.gray);
		Thread thread = new Thread(startPanel);
		thread.start();

		// ��startPanel������
		this.add(startPanel);

		// �������
		this.setIconImage((new ImageIcon("images/tank.gif")).getImage());
		this.setTitle(Global.title);
		this.setSize(Global.frameWide, Global.frameHeight);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("newGame")) {
			// ����ս�����
			gamePanel = new MyPanel("newGame");

			// ����mp�߳�
			Thread t = new Thread(gamePanel);
			t.start();
			// ��ɾ���ɵĿ�ʼ���
			this.remove(startPanel);
			this.add(gamePanel);
			// ע�����
			this.addKeyListener(gamePanel);
			// ��ʾ,ˢ��JFrame
			this.setVisible(true);

		} else if (e.getActionCommand().equals("loadGame")) {	           
	           
			// ����ս�����
			gamePanel = new MyPanel("loadGame");

			// ����gamePanel�߳�
			Thread panelThread = new Thread(gamePanel);
			panelThread.start();
			// ��ɾ���ɵĿ�ʼ���
			this.remove(startPanel);
			this.add(gamePanel);
			// ע�����
			this.addKeyListener(gamePanel);
			// ��ʾ,ˢ��JFrame
			this.setVisible(true);
		}else if (e.getActionCommand().equals("save")) {
			//Recorder recorder = new Recorder();
			Recorder.save();
		}else if(e.getActionCommand().equals("quit")){
			
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
			// ������Ϣ������
			Font myFont = new Font("������κ", Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("���װ�̹�˴�ս", 120, 200);
		}

	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			// ����
			try {

				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			times++;

			// �ػ�
			this.repaint();
		}

	}
}// End of StartPanel

class MyPanel extends JPanel implements KeyListener, Runnable {
	
	//������Ϊ�˵���setEnemyVector
	Recorder recorder = new Recorder();
	
	// �����ҵ�̹��
	Hero hero = null;

	// �������̹��
	Vector<Enemy> enemyVector = new Vector<Enemy>();
	
	// ���屬ը����
	Vector<Bomb> bombVector = new Vector<Bomb>();

	// ��������ͼƬ,����ͼƬ�л�����һ����ըЧ��
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;

	public MyPanel(String flag) {
		
		
		if (flag.equals("newGame")) {
			// ��ʼ���ҵ�̹��
			hero = new Hero(Global.heroX, Global.heroY, Global.heroDirect,
					Global.heroColor, Global.heroSpeed);
			// ��ʼ������̹��
			for (int i = 0; i < Global.enemyNumber; i++) {
				Enemy enemy = new Enemy((i + 1) * Global.enemyDistance, 0,
						Global.enemyDirect, Global.enemyColor,
						Global.enemySpeed);

				Thread enemyThread = new Thread(enemy);
				enemyThread.start();
				// ������̹�����һ���ӵ�
				Bullet enemyBullet = new Bullet(enemy.x + 9, enemy.y + 30,
						Direct.DOWN, Global.enemyBulletSpeed);
				enemy.bullets.add(enemyBullet);
				Thread bulletThread = new Thread(enemyBullet);
				bulletThread.start();
				enemyVector.add(enemy);

				enemy.getPanelTank(enemyVector);
			}
			recorder.setEnemyVector(enemyVector);
		}else{
			
			//�ָ���¼ 
			Vector<Node> nodes=new Vector<Node>();
			nodes = Recorder.loadGame();
			
			hero=new Hero(Global.heroX,Global.heroY,Direct.UP,Global.heroColor,Global.heroSpeed);
			//��ʼ�����˵�̹��
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);

				Enemy enemy=new Enemy(node.x,node.y,node.direct,Global.enemyColor,Global.enemySpeed);
				Thread enemyThread=new Thread(enemy);
				enemyThread.start();
				//������̹�����һ���ӵ�
				Bullet enemyBullet=new Bullet(enemy.x+10,enemy.y+30,enemy.direct,Global.enemyBulletSpeed);
				enemy.bullets.add(enemyBullet);
				Thread bulletThread=new Thread(enemyBullet);
				bulletThread.start();
				enemyVector.add(enemy);
				enemy.getPanelTank(enemyVector);
			}
			recorder.setEnemyVector(enemyVector);
		}
		// ��ʼ��ͼƬ
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
		g.setColor(Global.panelBackground);
		g.fillRect(0, 0, Global.panelWide, Global.panelHeight);
		// ��ʾ��Ϣ
		showInfo(g);

		// �����ҵ�̹��
		if (this.hero.live == true) {
			this.hero.drawTank(g);
		}

		// �����ҵ��ӵ�
		for (int i = 0; i < this.hero.bullets.size(); i++) {
			Bullet heroBulletInUse = hero.bullets.get(i);
			if (heroBulletInUse != null && heroBulletInUse.live == true) {
				g.draw3DRect(heroBulletInUse.x, heroBulletInUse.y, 1, 1, false);
			}

			// ����ӵ����������������ȥ��
			if (heroBulletInUse.live == false) {
				hero.bullets.remove(heroBulletInUse);
			}
		}

		// ��������̹��
		for (int i = 0; i < enemyVector.size(); i++) {
			Enemy enemy = enemyVector.get(i);
			if (enemy.live == true) {
				this.enemyVector.get(i).drawTank(g);
				// ���������ӵ�
				for (int j = 0; j < enemy.bullets.size(); j++) {
					// ȡ���ӵ�
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
		// ������ըЧ��
		for (int i = 0; i < bombVector.size(); i++) {
			// ȡ��ը��
			Bomb bomb = bombVector.get(i);
			if (bomb.lifeTime > 6) {
				g.drawImage(image1, bomb.x, bomb.y, 30, 30, this);
			} else if (bomb.lifeTime > 3) {
				g.drawImage(image2, bomb.x, bomb.y, 30, 30, this);
			} else {
				g.drawImage(image3, bomb.x, bomb.y, 30, 30, this);
			}
			// ��b������ֵ����
			bomb.lifeDown();

			if (bomb.lifeTime == 0) {
				bombVector.remove(bomb);
			}

		}
		// �����ʧ�ܵ�ʱ����ʾ"GAME OVER"������û��ʵ��ͬʱ��������Ƭ�Ρ�
		if (hero.live == false) {
			g.setFont(new Font("����", Font.BOLD, 40));
			g.setColor(Color.ORANGE);
			g.drawString("GAME OVER", 150, 150);
		}
		// ���������е���̹�˵�ʱ����ʾ"MERRY XMAS"��
		if (Recorder.getDeadEnemy() == Global.enemyNumber && hero.live == true) {
			g.setFont(new Font("����", Font.BOLD, 40));
			g.setColor(Color.ORANGE);
			g.drawString("YOU WIN !!!", 150, 150);
		}

	}

	// ������ʾ��Ϣ
	public void showInfo(Graphics g) {
		// ������ʾ��Ϣ̹��(��̹�˲�����ս��)
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

		// ������ҵ��ܳɼ�
		g.setColor(Color.black);
		Font f = new Font("����", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("����̹����", 200, Global.panelHeight + 30);

		Tank deadTank = new Tank(310, Global.panelHeight + 10, Direct.DOWN,
				Global.enemyColor, 0);
		deadTank.drawTank(g);

		g.setColor(Color.black);
		g.drawString(Recorder.getDeadEnemy() + "", 350, Global.panelHeight + 30);
	}

	// �ж�̹���Ƿ��ӵ�����
	public boolean hit(Bullet bullet, Tank tank) {
		boolean ishit = false;
		switch (tank.direct) {
		// ����̹�˷������ϻ�����
		case UP:
		case DOWN:
			if (bullet.x > tank.x && bullet.x < tank.x + 20
					&& bullet.y > tank.y && bullet.y < tank.y + 30) {
				bullet.live = false;
				tank.live = false;
				ishit = true;
				// ����һ�α�ը������Vector
				Bomb bomb = new Bomb(tank.x, tank.y);
				bombVector.add(bomb);
			}
			break;
		// ����̹�˷������������
		case RIGHT:
		case LEFT:
			if (bullet.x > tank.x && bullet.x < tank.x + 30
					&& bullet.y > tank.y && bullet.y < tank.y + 20) {
				bullet.live = false;
				tank.live = false;
				ishit = true;
				// ����һ�α�ը������Vector
				Bomb bomb = new Bomb(tank.x, tank.y);
				bombVector.add(bomb);
			}
			break;
		default:
			break;
		}
		return ishit;
	}

	// �ж��Ƿ���е���̹��
	public void hitEnemy() {
		for (int i = 0; i < hero.bullets.size(); i++) {
			Bullet b = hero.bullets.get(i);
			if (b.live == true) {
				for (int j = 0; j < enemyVector.size(); j++) {
					Enemy e = enemyVector.get(j);
					if (e.live == true) {
						if (this.hit(b, e)) {
							// ���ٵ�������
							Global.enemyNumber--;
							// �����ҵļ�¼
							Recorder.addDeadEnemy();
						}
					}
				}
			}
		}
	}

	// �ж��Ƿ񱻻���
	public void hitme() {
		if (this.hero.live == true) {
			// ȡ��ÿһ������̹��
			for (int i = 0; i < this.enemyVector.size(); i++) {
				// ȡ��̹��
				Enemy e = enemyVector.get(i);
				// ȡ��ÿһ���ӵ�
				for (int j = 0; j < e.bullets.size(); j++) {
					// ȡ���ӵ�
					Bullet b = e.bullets.get(j);
					this.hit(b, hero);
				}
			}
		}
	}

	// �������¼��̵Ĳ���
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			if (hero.y > 0) {
				// ������Ϊ��W��ʱ��̹�˷����Ϊ�ϣ�̹��λ�������ƶ�speed����λ����
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

			// �����¡�J��ʱ��̹�˷����ӵ�
			if (this.hero.bullets.size() < Global.heroBulletNumber) {
				this.hero.fire(Global.heroBulletSpeed);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_T) {
			// ʵ����ͣ����
			// if (Global.isStop == false) {
			// Global.isStop = true;
			// }else {
			// Global.isStop = false;
			// }

		}

		// Ϊ�˿����ƶ�Ч������Ҫ�ػ�ͼ�Σ���������������̣߳�����ػ溯������ɾ����
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

	// ʵ��Runnable�ӿ�
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// �ж��Ƿ���е��˵�̹��
			this.hitEnemy();

			// �ж��Ƿ񱻻���
			this.hitme();

			repaint();
		}
	}

}// End of MyPanel

