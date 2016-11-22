package njit.tmp;

/*********************************
 *�����ļ����ƣ�StartHere.java
 *���ܣ���������,������Ϸ�������
 *********************************/
import javax.swing.*;
import java.awt.*;
import java.net.*;
//JWindow ��һ���ܹ����û�������κεط���ʾ�������������ܹ�ʹ�������ɳ��������ʱ��splash���档
public class StartHere extends JWindow implements Runnable
{
	Thread splashThread=null;
	public StartHere()
	{
		JPanel splash=new JPanel(new BorderLayout());
		//��ȡͼƬ�ļ�
		URL url=getClass().getResource("img\\welcome.jpg");
		if(url!=null)
		{
			//ͼƬ����JLabel�У�JLabel���������
			splash.add(new JLabel(new ImageIcon(url)),BorderLayout.CENTER);
		}
		setContentPane(splash);
		//Java��һ���࣬��װ��һ�������ĸ߶ȺͿ��
		Dimension screen=getToolkit().getScreenSize();
		//�����˴��ڵĴ�С�����ʺ������������ѡ��С�Ͳ��֡�
		pack();
		//������ʾ
		setLocation((screen.width-getSize().width)/2,(screen.height-getSize().height)/2);
	}
	public void start()
	{
		//��ǰ��������ǰ����ʾ
		this.toFront();
		//�������߳�
		splashThread=new Thread(this);
		splashThread.start();
	}
	//��ʱ3���رմ���
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
		//�ڹرտ������ں�ʵ����һ��MyTankGame����������Ϸ���崰��
		MyTankGame mtg=new MyTankGame();
	}
	public static void main(String[] args)
	{			
		StartHere splash =new StartHere();
		splash.start();		
	}		
}

