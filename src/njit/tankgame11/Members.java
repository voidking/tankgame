/*
 * �����ࡢ��¼�ࡢ̹���ࡢ�ӵ���ȵ�
 */

package njit.tankgame11;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


//������������
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

	// ��¼�ܹ������˶��ٵ���
	public static int deadEnemy = 0;
	public static int heroNumber = Global.heroNumber;
	public static int enemyNumber = Global.enemyNumber;
	
	// ���ļ��лָ���¼��
	private static Vector<Node> nodes = new Vector<Node>();
	private static Vector<Enemy> enemyVector = new Vector<Enemy>();
	private static FileWriter fileWriter = null;
	private static BufferedWriter bufferedWriter = null;
	private static FileReader fileReader = null;
	private static BufferedReader bufferedReader = null;

	// �����¼
	public static void save() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("���Ϊ��");
		fileChooser
				.setFileFilter(new FileExtensionFilter("�ı��ļ�(*.txt)", "txt"));
		fileChooser.showSaveDialog(null);
		// ��Ĭ�ϵķ�ʽ��ʾ
		fileChooser.setVisible(true);

		// �õ��û�ϣ�����ļ����浽�δ�
		String file = fileChooser.getSelectedFile().getAbsolutePath();
		// ׼��д�뵽ָ�����ļ�

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(Recorder.enemyNumber + " " + Recorder.heroNumber
					+ " " + deadEnemy + "\r\n");

			for (int i = 0; i < enemyVector.size(); i++) {
				// ȡ����һ��̹��
				Enemy enemy = enemyVector.get(i);

				if (enemy.live) {
					String node = enemy.x + " " + enemy.y + " " + enemy.direct;
					// д��
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

	// ���ļ��ж�ȡ��¼
	public static Vector<Node> loadGame() {
		JFileChooser fileChooser = new JFileChooser();
		// ��������
		fileChooser.setDialogTitle("��ѡ���ļ���");
		fileChooser
				.setFileFilter(new FileExtensionFilter("�ı��ļ�(*.txt)", "txt"));
		fileChooser.showOpenDialog(null);
		// ��ʾ
		fileChooser.setVisible(true);
		// �õ��û�ѡ����ļ�·��
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

	// �������
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

	// ���캯��
	public Tank(int x, int y, Direct d, Color color, int speed) {
		this.x = x;
		this.y = y;
		this.direct = d;
		this.color = color;
		this.speed = speed;
	}

	// ����̹�˺���
	public void drawTank(Graphics g) {
		// �趨������ɫ
		g.setColor(this.color);

		// ���ݷ��򻭳������Ρ�Բ�κ��ߣ����̹��
		switch (this.direct) {
		case UP:
			// 1.��������ľ���
			g.fill3DRect(this.x, this.y, 5, 30, false);
			// 2.�����ұߵľ���
			g.fill3DRect(this.x + 15, this.y, 5, 30, false);
			// 3.����̹�˵��м����
			g.fill3DRect(this.x + 5, this.y + 5, 10, 20, false);
			// 4.�����м��Բ
			g.fillOval(this.x + 4, this.y + 10, 10, 10);
			// 5.������
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

	// ���巢���ӵ�����
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

		// �����ӵ��߳�
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
			// ̹����������
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
			// ����
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
			// ����
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
			// ����
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
			// ��̹���������һ������
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
					// ��һ��̹�����ϻ�����
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// ���
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
						// �ҵ�
						if (this.x + 20 >= anotherEnemy.x
								&& this.x + 20 <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// ��һ��̹�����������
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// ���
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
						// �ҵ�
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
					// ��һ��̹�����ϻ�����
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// ���
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 30) {
							return true;
						}
						// �ҵ�
						if (this.x + 20 >= anotherEnemy.x
								&& this.x + 20 <= anotherEnemy.x + 20
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// ��һ��̹�����������
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// ���
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y + 30 >= anotherEnemy.y
								&& this.y + 30 <= anotherEnemy.y + 20) {
							return true;
						}
						// �ҵ�
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
					// ��һ��̹�����ϻ�����
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// �ϵ�
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
						// �µ�
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 20
								&& this.y + 20 >= anotherEnemy.y
								&& this.y + 20 <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// ��һ��̹�����������
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// �ϵ�
						if (this.x >= anotherEnemy.x
								&& this.x <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
						// �µ�
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
					// ��һ��̹�����ϻ�����
					if (anotherEnemy.direct == Direct.UP
							|| anotherEnemy.direct == Direct.DOWN) {
						// �ϵ�
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 20
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 30) {
							return true;
						}
						// �µ�
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 20
								&& this.y + 20 >= anotherEnemy.y
								&& this.y + 20 <= anotherEnemy.y + 30) {
							return true;
						}
					}
					// ��һ��̹�����������
					if (anotherEnemy.direct == Direct.LEFT
							|| anotherEnemy.direct == Direct.RIGHT) {
						// �ϵ�
						if (this.x + 30 >= anotherEnemy.x
								&& this.x + 30 <= anotherEnemy.x + 30
								&& this.y >= anotherEnemy.y
								&& this.y <= anotherEnemy.y + 20) {
							return true;
						}
						// �µ�
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

	// ʵ��Runnable�ӿ�
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
			// ���ӵ�������ܣ���壩ʱ��ȥ��ͬʱѭ������
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

	// ���屬ը������
	int x, y;

	// ��ը������ʱ��
	int lifeTime = 9;

	Boolean live = true;

	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// ��������ʱ��
	public void lifeDown() {
		if (lifeTime > 0) {
			lifeTime--;
		} else {
			this.live = false;
		}
	}

}// End of Bomb

