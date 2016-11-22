/*
 * ̹�˴�ս01
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
       this.setTitle("����̹�˴�ս");
       this.setSize(400,300);
       this.setLocation(400,400);
       this.setVisible(true);
       this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
    }
 
}
 
class MyPanel extends JPanel
{
    //�����ҵ�̹�ˣ���Ա����
    Hero hero=null;
   
    public void paint (Graphics g)
    {
     super.paint(g);
     g.fillRect(0,0,400,300);
     
     this.drawTank(hero.getX(), hero.getY(), g, 0, 1);
    }
   
    public void drawTank(int x,int y,Graphics g,int direct,int type)
    {
       //�ж�����
       switch (type)
       {
       case 0:
           g.setColor(Color.cyan);break;
       case 1:
           g.setColor(Color.yellow);break;   
       }
       //�жϷ���
      
       switch(direct)
       {
       //����
       case 0:
            //�����ҵ�̹�ˣ���ʱ���ٷ�װ��һ��������
            //1.��������ľ���
            //g.drawRect(hero.getX(), hero.getY(), 5, 30);
            g.fill3DRect(x, y, 5, 30,false);
            
            //2.�����ұߵľ���
            g.fill3DRect(x+15, y, 5, 30,false);
            
            //3.����̹�˵��м����
            g.fill3DRect(x+5, y+5, 10, 20,false);
            //�����м��Բ
            g.fillOval(x+4, y+10,10,10);
            //������
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
 
    //��ʾ̹�˵ĺ�����
    int x=0;
    //̹�˵�������
    int y=0;
   
    public Tank(int x,int y)
    {
       this.x=x;
       this.y=y;
      
    }
   
}
 
//�ҵ�̹��
class Hero extends Tank
{
 
    public Hero(int x, int y)
    {
       super(x, y);
    }
   
}
