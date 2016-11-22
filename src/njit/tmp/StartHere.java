package njit.tmp;

/*********************************
 *程序文件名称：StartHere.java
 *功能：快闪窗口,启动游戏主体界面
 *********************************/
import javax.swing.*;
import java.awt.*;
import java.net.*;
//JWindow 是一个能够在用户桌面的任何地方显示的容器。所以能够使用它构成程序刚运行时的splash画面。
public class StartHere extends JWindow implements Runnable
{
	Thread splashThread=null;
	public StartHere()
	{
		JPanel splash=new JPanel(new BorderLayout());
		//读取图片文件
		URL url=getClass().getResource("img\\welcome.jpg");
		if(url!=null)
		{
			//图片放在JLabel中，JLabel放在面板中
			splash.add(new JLabel(new ImageIcon(url)),BorderLayout.CENTER);
		}
		setContentPane(splash);
		//Java的一个类，封装了一个构件的高度和宽度
		Dimension screen=getToolkit().getScreenSize();
		//调整此窗口的大小，以适合其子组件的首选大小和布局。
		pack();
		//居中显示
		setLocation((screen.width-getSize().width)/2,(screen.height-getSize().height)/2);
	}
	public void start()
	{
		//当前窗口在最前面显示
		this.toFront();
		//创建多线程
		splashThread=new Thread(this);
		splashThread.start();
	}
	//延时3秒后关闭窗口
	public void run()
	{
		try
		{
			show();			
			Thread.sleep(3000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
		this.dispose();
		//在关闭快闪窗口后实例化一个MyTankGame对象，启动游戏主体窗口
		MyTankGame mtg=new MyTankGame();
	}
	public static void main(String[] args)
	{			
		StartHere splash =new StartHere();
		splash.start();		
	}		
}

