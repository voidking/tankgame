/*
 * ����Global�࣬ʵ��ȫ�ֱ���Ч�����û���������������Լ���Ҫ������֮������Ͻ�ͼ�ν��棩��
 */

package njit.tankgame06;

import java.awt.Color;

public class Global {
	//�������
	public static String title = new String("����̹�˴�ս");
	public static int frameWide = 500;
	public static int frameHeight = 500;
	
	//�������
	public static Color panelBackground = Color.black;
	public static int panelWide = 500;
	public static int panelHeight = 400;
	
	//�ҵ�̹������
	public static Direct heroDirect = Direct.UP;
	public static Color heroColor = Color.cyan;
    public static int heroX = 200;
    public static int heroY = 200;
    public static int heroNumber = 1;
    public static int heroSpeed = 3;
    public static int heroBulletNumber = 5;
    public static int heroBulletSpeed = 3;
	
	//����̹������
	public static Direct enemyDirect = Direct.DOWN;
	public static Color enemyColor = Color.yellow;
    public static int enemySpeed = 1;    
    public static int enemyNumber = 5;
    public static int enemyDistance = 60;
    public static int enemyBulletNumber = 3;
    public static int enemyBulletSpeed = 3;
	
	public static boolean isStart = true;
	public static boolean isStop = false;
}
