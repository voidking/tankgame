/*
 * ����Global�࣬ʵ��ȫ�ֱ���Ч�����û���������������Լ���Ҫ������֮������Ͻ�ͼ�ν��棩��
 */

package njit.tankgame03;

import java.awt.Color;

public class Global {
	//�������
	public static String title = new String("̹�˴�ս��δ��ɣ�");
	public static int wide = 500;
	public static int hight = 400;
	public static int jframeX = 300;
	public static int jframeY = 300;
	
	//�������
	public static Color panelBackground = Color.black;
	
	//�ҵ�̹������
	public static Direct heroDirect = Direct.UP;
	public static Color heroColor = Color.cyan;
    public static int heroX = 200;
    public static int heroY = 200;
    public static int heroSpeed = 3;
	
	//����̹������
	public static Direct enemyDirect = Direct.DOWN;
	public static Color enemyColor = Color.yellow;
    public static int enemySpeed = 1;    
    public static int enemyNumber = 5;
    public static int enemyDistance = 60;
	
	
}
