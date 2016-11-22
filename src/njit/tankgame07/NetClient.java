package njit.tankgame07;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class NetClient {

	private final int UDP_PORT = 1993;
	Hero hero;
	Vector<Hero> heros = new Vector<Hero>();
	Socket socket = null;
	DatagramSocket dategramSocket;

	public void connect(String IP, int port, Hero hero, Vector<Hero> heros) {
		this.hero = hero;
		hero.id = UDP_PORT;
		this.heros = heros;

		Socket socket = null;
		try {
			socket = new Socket(IP, port);
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			out.writeInt(UDP_PORT);
			dategramSocket = new DatagramSocket(UDP_PORT);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		new Send().start();
		new Thread(new Receive()).start();
	}

	private class Receive implements Runnable {
		byte[] receiveBuf = new byte[10240];

		public void run() {

			while (dategramSocket != null) {
				DatagramPacket receivePacket = new DatagramPacket(receiveBuf,
						receiveBuf.length);
				try {
					dategramSocket.receive(receivePacket);
					String receiveLine = new String(receivePacket.getData(), 0,
							receivePacket.getLength());
					String receiveNode[] = receiveLine.split(" ");
					//System.out.println("Ω” ’µΩ");

					if (hero.id != Integer.parseInt(receiveNode[3])) {
						boolean add = true;
						for (int i = 0; i < heros.size(); i++) {
							Hero tempHero = heros.get(i);
							if (tempHero.id == Integer.parseInt(receiveNode[3])) {
								tempHero.x = Integer.parseInt(receiveNode[0]);
								tempHero.y = Integer.parseInt(receiveNode[1]);
								tempHero.direct = Direct.parse(receiveNode[2]);
								add = false;
								break;
							}
						}
						if (add) {
							Hero addHero = new Hero(
									Integer.parseInt(receiveNode[0]),
									Integer.parseInt(receiveNode[1]),
									Direct.parse(receiveNode[2]),
									Global.heroColor, Global.heroSpeed);
							addHero.id = Integer.parseInt(receiveNode[3]);
							heros.add(addHero);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			

		}
	}
	class Send extends Thread
	{
		public void run()
		{
			while (true) {
				try {
					Thread.sleep(20);
					String sendLine = hero.x + " " + hero.y + " " + hero.direct
							+ " " + hero.id;
					byte[] sendBuf = sendLine.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendBuf,
							sendBuf.length, InetAddress.getByName("127.0.0.1"),
							6666);
					dategramSocket.send(sendPacket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
