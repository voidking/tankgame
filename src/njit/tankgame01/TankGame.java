/*
 * 坦克大战01
 */
package njit.tankgame01;

import java.awt.*;
import javax.swing.*;
 
public class TankGame  extends JFrame
{
    MyPanel mp=null;
    public static void main(String[] args)
    {
       TankGame tankgame1=new TankGame();
      
    }
   
    public TankGame()
    {
       mp=new MyPanel();
      
       this.add(mp);
       this.setTitle("盗版坦克大战");
       this.setSize(400,300);
       this.setLocation(400,400);
       this.setVisible(true);
       this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }
 
}
 
class MyPanel extends JPanel
{
    //定义我的坦克，成员变量
    Hero hero=null;
   
    public void paint (Graphics g)
    {
     super.paint(g);
     g.fillRect(0,0,400,300);
     
     this.drawTank(hero.getX(), hero.getY(), g, 0, 1);
    }
   
    public void drawTank(int x,int y,Graphics g,int direct,int type)
    {
       //判断类型
       switch (type)
       {
       case 0:
           g.setColor(Color.cyan);break;
       case 1:
           g.setColor(Color.yellow);break;   
       }
       //判断方向
      
       switch(direct)
       {
       //向上
       case 0:
            //画出我的坦克（到时候再封装成一个函数）
            //1.画出左面的矩形
            //g.drawRect(hero.getX(), hero.getY(), 5, 30);
            g.fill3DRect(x, y, 5, 30,false);
            
            //2.画出右边的矩形
            g.fill3DRect(x+15, y, 5, 30,false);
            
            //3.画出坦克的中间矩形
            g.fill3DRect(x+5, y+5, 10, 20,false);
            //画出中间的圆
            g.fillOval(x+4, y+10,10,10);
            //画出线
            g.drawLine(x+9, y+15, x+9, 5);
            break;
          
      
       }
      
      
      
    }
   
   
    public MyPanel()
    {
       hero=new Hero(10,10);      
    }
      
}
 
 
class Tank
{
    public int getX() {
       return x;
    }
 
    public void setX(int x) {
       this.x = x;
    }
 
    public int getY() {
       return y;
    }
 
    public void setY(int y) {
       this.y = y;
    }
 
    //表示坦克的横坐标
    int x=0;
    //坦克的纵坐标
    int y=0;
   
    public Tank(int x,int y)
    {
       this.x=x;
       this.y=y;
      
    }
   
}
 
//我的坦克
class Hero extends Tank
{
 
    public Hero(int x, int y)
    {
       super(x, y);
    }
   
}
