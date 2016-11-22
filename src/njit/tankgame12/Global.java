/*
 * 定义Global类，实现全局变量效果。
 */

package njit.tankgame12;

import java.awt.Color;

public class Global {
	//框架属性
	public static String title = new String("简易坦克大战");
	public static int frameWide = 500;
	public static int frameHeight = 500;
	
	//面板属性
	public static Color panelBackground = Color.black;
	public static int panelWide = 500;
	public static int panelHeight = 400;
	
	//我的坦克属性
	public static Direct heroDirect = Direct.UP;
	public static Color heroColor = Color.cyan;
    public static int heroX = 200;
    public static int heroY = 200;
    public static int heroNumber = 1;
    public static int heroSpeed = 4;
    public static int heroBulletNumber = 5;
    public static int heroBulletSpeed = 5;
	
	//敌人坦克属性
	public static Direct enemyDirect = Direct.DOWN;
	public static Color enemyColor = Color.yellow;
    public static int enemySpeed = 2;    
    public static int enemyNumber = 5;
    public static int enemyDistance = 60;
    public static int enemyBulletNumber = 4;
    public static int enemyBulletSpeed = 4;

}
