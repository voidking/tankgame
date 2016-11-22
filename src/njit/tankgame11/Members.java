/*
 * 声音类、记录类、坦克类、子弹类等等
 */

package njit.tankgame11;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


//播放声音的类
class Audio extends Thread {

	private String filename;

	public Audio(String wavfile) {
		filename = wavfile;
	}

	public void run() {

		File soundFile = new File(filename);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine)AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		byte[] abData = new byte[512];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}
	}
}

class Node {
	int x;
	int y;
	Direct direct;

	public Node(int x, int y, Direct direct) {
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
}

class FileExtensionFilter extends FileFilter {

	private String description, extension;

	public FileExtensionFilter(String description, String extension) {
		this.description = description;
		this.extension = extension.toLowerCase();
	}

	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(this.extension);
	}

	public String getDescription() {
		return this.description;
	}

}

class Recorder {

	// 记录总共消灭了多少敌人
	public static int deadEnemy = 0;
	public static int heroNumber = Global.heroNumber;
	public static int enemyNumber = Global.enemyNumber;
	
	// 从文件中恢复记录点
	private static Vector<Node> nodes = new Vector<Node>();
	private static Vector<Enemy> enemyVector = new Vector<Enemy>();
	private static FileWriter fileWriter = null;
	private static BufferedWriter bufferedWriter = null;
	private static FileReader fileReader = null;
	private static BufferedReader bufferedReader = null;

	// 保存记录
	public static void save() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("另存为…");
		fileChooser
				.setFileFilter(new FileExtensionFilter("文本文件(*.txt)", "txt"));
		fileChooser.showSaveDialog(null);
		// 按默认的方式显示
		fileChooser.setVisible(true);

		// 得到用户希望把文件保存到何处
		String file = fileChooser.getSelectedFile().getAbsolutePath();
		// 准备写入到指定的文件

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(Recorder.enemyNumber + " " + Recorder.heroNumber
					+ " " + deadEnemy + "\r\n");

			for (int i = 0; i < enemyVector.size(); i++) {
				// 取出第一个坦克
				Enemy enemy = enemyVector.get(i);

				if (enemy.live) {
					String node = enemy.x + " " + enemy.y + " " + enemy.direct;
					// 写入
					bufferedWriter.write(node + "\r\n");

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				bufferedWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 从文件中读取记录
	public static Vector<Node> loadGame() {
		JFileChooser fileChooser = new JFileChooser();
		// 设置名字
		fileChooser.setDialogTitle("请选择文件：");
		fileChooser
				.setFileFilter(new FileExtensionFilter("文本文件(*.txt)", "txt"));
		fileChooser.showOpenDialog(null);
		// 显示
		fileChooser.setVisible(true);
		// 得到用户选择的文件路径
		String filename = fileChooser.getSelectedFile().getAbsolutePath();

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(filename);
			bufferedReader = new BufferedReader(fileReader);
			String aLine = bufferedReader.readLine();
			String[] aNode = aLine.split(" ");
			Recorder.enemyNumber = Integer.parseInt(aNode[0]);
			Recorder.heroNumber = Integer.parseInt(aNode[1]);
			Recorder.deadEnemy = Integer.parseInt(aNode[2]);

			while ((aLine = bufferedReader.readLine()) != null) {
				aNode = aLine.split(" ");
				Node node = new Node(Integer.parseInt(aNode[0]),
						Integer.parseInt(aNode[1]), Direct.parse(aNode[2]));
				nodes.add(node);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {

			try {
				bufferedReader.close();
				fileReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		return nodes;
	}

	// 消灭敌人
	public static void addDeadEnemy() {
		deadEnemy++;
	}

	public static int getDeadEnemy() {
		return deadEnemy;
	}

	public Vector<Enemy> getEnemyVector() {
		return enemyVector;
	}

	public void setEnemyVector(Vector<Enemy> enemyVector) {

		this.enemyVector = enemyVector;
	}
}// End of Recorder

class Tank {

	boolean live = true;
	int x = 0;
	int y = 0;
	Direct direct;
	Color color;
	int speed;
	Vector<Bullet> bullets = new Vector<Bullet>();
	Bullet bullet = null;

	// 构造函数
	public Tank(int x, int y, Direct d, Color color, int speed) {
		this.x = x;
		this.y = y;
		this.direct = d;
		this.color = color;
		this.speed = speed;
	}

	// 画出坦克函数
	public void drawTank(Graphics g) {
		// 设定画笔颜色
		g.setColor(this.color);

		// 根据方向画出长方形、圆形和线，组成坦克
		switch (this.direct) {
		case UP:
			// 1.画出左面的矩形
			g.fill3DRect(this.x, this.y, 5, 30, false);
			// 2.画出右边的矩形
			g.fill3DRect(this.x + 15, this.y, 5, 30, false);
			// 3.画出坦克的中间矩形
			g.fill3DRect(this.x + 5, this.y + 5, 10, 20, false);
			// 4.画出中间的圆
			g.fillOval(this.x + 4, this.y + 10, 10, 10);
			// 5.画出线
			g.drawLine(this.x + 9, this.y + 15, this.x + 9, this.y);
			g.drawLine(this.x + 10, this.y + 15, this.x + 10, this.y);
			break;

		case RIGHT:
			g.fill3DRect(this.x, this.y, 30, 5, false);
			g.fill3DRect(this.x, this.y + 15, 30, 5, false);
			g.fill3DRect(this.x + 5, this.y + 5, 20, 10, false);
			g.fillOval(this.x + 10, this.y + 4, 10, 10);
			g.drawLine(this.x + 15, this.y + 9, this.x + 30, this.y + 9);
			g.drawLine(this.x + 15, this.y + 10, this.x + 30, this.y + 10);
			break;

		case DOWN:
			g.fill3DRect(this.x, this.y, 5, 30, false);
			g.fill3DRect(this.x + 15, this.y, 5, 30, false);
			g.fill3DRect(this.x + 5, this.y + 5, 10, 20, false);
			g.fillOval(this.x + 4, this.y + 10, 10, 10);
			g.drawLine(this.x + 9, this.y + 15, this.x + 9, this.y + 30);
			g.drawLine(this.x + 10, this.y + 15, this.x + 10, this.y + 30);
			break;

		case LEFT:
			g.fill3DRect(this.x, this.y, 30, 5, false);
			g.fill3DRect(this.x, this.y + 15, 30, 5, false);
			g.fill3DRect(this.x + 5, this.y + 5, 20, 10, false);
			g.fillOval(this.x + 10, this.y + 4, 10, 10);
			g.drawLine(this.x + 15, this.y + 9, this.x, this.y + 9);
			g.drawLine(this.x + 15, this.y + 10, this.x, this.y + 10);
			break;
		}

	}

	// 定义发射子弹函数
	public void fire(int bulletSpeed) {

		switch (direct) {
		case UP:
			bullet = new Bullet(x + 9, y, Direct.UP, bulletSpeed);
			bullets.add(bullet);
			break;

		case RIGHT:
			bullet = new Bullet(x + 30, y + 9, Direct.RIGHT, bulletSpeed);
			bullets.add(bullet);
			break;
		case DOWN:
			bullet = new Bullet(x + 9, y + 30, Direct.DOWN, bulletSpeed);
			bullets.add(bullet);
			break;
		case LEFT:
			bullet = new Bullet(x, y + 9, Direct.LEFT, bulletSpeed);
			bullets.add(bullet);
			break;

		default:
			break;
		}

		// 启动子弹线程
		Thread t = new Thread(bullet);
		t.start();
	}// End of fire()

}// End of Tank

class Hero extends Tank {

	int id;
	public Hero(int x, int y, Direct direct, Color color, int speed) {
		super(x, y, direct, color, speed);
	}

}// End of Hero

class Enemy extends Tank implements Runnable {
	
	public Enemy(int x, int y, Direct direct, Color color, int speed) {
		super(x, y, direct, color, speed);

	}

	int times = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			switch (this.direct) {
			// 坦克正在向上
			case UP:
				for (int i = 0; i < 30; i++) {
					if (y > 0 && !this.isTouch()) {
						y -= speed;
					}

					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
				break;
			// 向右
			case RIGHT:
				for (int i = 0; i < 30; i++) {
					if (x < (Global.panelWide - 30) && !this.isTouch()) {
						x += speed;
					}

					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
				break;
			// 向下
			case DOWN:
				for (int i = 0; i < 30; i++) {
					if (y < (Global.panelHeight - 30) && !this.isTouch()) {
						y += speed;
					}

					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
				break;
			// 向左
			case LEFT:
				for (int i = 0; i < 30; i++) {
					if (x > 0 && !this.isTouch()) {
						x -= speed;
					}

					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
				break;

			default:
				break;
			}
			// 让坦克随机产生一个方向
			this.direct = Direct.valueOf((int) (Math.random() * 4));
			this.times++;
			if (times % 2 == 0 && this.live == true
					&& bullets.size() < Global.enemyBulletNumber) {
				this.fire(Global.enemyBulletSpeed);
			}
		}

	}

	Vector<Enemy> enemies = new Vector<Enemy>();

	public void getPanelTank(Vector<Enemy> enemies) {
		this.enemies = enemies;
	}

	public boolean isTouch() {
		switch (this.direct) {
		case UP:
			for (int i = 0; i < enemies.size(); i++) {
				Enemy anotherEnemy = enemies.get(i);
				if (anotherEnemy != this) {
					// 另一个坦克向上或向下
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// 左点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
						// 右点
						if (this.x + 20 >= anotherEnemy.x
								&& this.x + 20 <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// 另一个坦克向左或向右
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// 左点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
						// 右点
						if (this.x + 20 >= anotherEnemy.x
								&& this.x + 20 <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
					}

				}

			}
			break;
		case DOWN:
			for (int i = 0; i < enemies.size(); i++) {
				Enemy anotherEnemy = enemies.get(i);
				if (anotherEnemy != this) {
					// 另一个坦克向上或向下
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// 左点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 30) {
							return true;
						}
						// 右点
						if (this.x + 20 >= anotherEnemy.x
								&& this.x + 20 <= anotherEnemy.x + 20
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// 另一个坦克向左或向右
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// 左点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 20) {
							return true;
						}
						// 右点
						if (this.x + 20 >= anotherEnemy.x
								&& this.x + 20 <= anotherEnemy.x + 30
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 20) {
							return true;
						}
					}
				}

			}
			break;
		case LEFT:
			for (int i = 0; i < enemies.size(); i++) {
				Enemy anotherEnemy = enemies.get(i);
				if (anotherEnemy != this) {
					// 另一个坦克向上或向下
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// 上点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
						// 下点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y + 20 >= anotherEnemy.y
								&& this.y + 20 <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// 另一个坦克向左或向右
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// 上点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
						// 下点
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y + 20 >= anotherEnemy.y
								&& this.y + 20 <= anotherEnemy.y + 20) {
							return true;
						}
					}

				}

			}
			break;
		case RIGHT:
			for (int i = 0; i < enemies.size(); i++) {
				Enemy anotherEnemy = enemies.get(i);
				if (anotherEnemy != this) {
					// 另一个坦克向上或向下
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// 上点
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
						// 下点
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 20
								&& this.y + 20 >= anotherEnemy.y
								&& this.y + 20 <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// 另一个坦克向左或向右
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// 上点
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
						// 下点
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 30
								&& this.y + 20 >= anotherEnemy.y
								&& this.y + 20 <= anotherEnemy.y + 20) {
							return true;
						}
					}

				}

			}
			break;

		default:
			break;
		}

		return false;
	}

}// End of Enemy

class Bullet implements Runnable {

	// 实现Runnable接口
	public void run() {

		while (true) {

			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch (direct) {
			case UP:
				y -= speed;
				break;
			case RIGHT:
				x += speed;
				break;
			case DOWN:
				y += speed;
				break;
			case LEFT:
				x -= speed;
				break;

			default:
				break;
			}
			// 当子弹超出框架（面板）时死去，同时循环结束
			if (x < 0 || x > Global.panelWide || y < 0
					|| y > Global.panelHeight) {
				live = false;
				break;
			}
		}

	}

	int x;
	int y;
	Direct direct;
	int speed = 1;
	boolean live = true;
	public static int id = 1;

	public Bullet(int x, int y, Direct direct, int speed) {
		this.x = x;
		this.y = y;
		this.direct = direct;
		this.speed = speed;
		id++;
	}

}// End of Bullet

class Bomb {

	// 定义爆炸的坐标
	int x, y;

	// 爆炸的生命时间
	int lifeTime = 9;

	Boolean live = true;

	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// 减少生命时间
	public void lifeDown() {
		if (lifeTime > 0) {
			lifeTime--;
		} else {
			this.live = false;
		}
	}

}// End of Bomb

