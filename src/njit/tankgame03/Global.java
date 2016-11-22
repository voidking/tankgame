/*
 * 定义Global类，实现全局变量效果。用户可以在这里根据自己需要调整（之后会整合进图形界面）。
 */

package njit.tankgame03;

import java.awt.Color;

public class Global {
	//框架属性
	public static String title = new String("坦克大战（未完成）");
	public static int wide = 500;
	public static int hight = 400;
	public static int jframeX = 300;
	public static int jframeY = 300;
	
	//面板属性
	public static Color panelBackground = Color.black;
	
	//我的坦克属性
	public static Direct heroDirect = Direct.UP;
	public static Color heroColor = Color.cyan;
    public static int heroX = 200;
    public static int heroY = 200;
    public static int heroSpeed = 3;
	
	//敌人坦克属性
	public static Direct enemyDirect = Direct.DOWN;
	public static Color enemyColor = Color.yellow;
    public static int enemySpeed = 1;    
    public static int enemyNumber = 5;
    public static int enemyDistance = 60;
	
	
}
