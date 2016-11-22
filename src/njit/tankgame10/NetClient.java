package njit.tankgame10;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class NetClient {

	private final int UDP_PORT = 1992;
	Hero hero;
	Vector<Hero> heros = new Vector<Hero>();
	Socket socket = null;
	DatagramSocket dategramSocket;
	String sendLine;
	byte[] sendBuf;
	DatagramPacket sendPacket;

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
		sendHero();
		new Thread(new Receive()).start();
	}

	public void sendHero() {
		if (hero.live) {
			sendLine = hero.id + " " + hero.live + " " + hero.x + " " + hero.y
					+ " " + hero.direct;
			sendBuf = sendLine.getBytes();
			try {
				sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
						InetAddress.getByName("127.0.0.1"), 6666);
				dategramSocket.send(sendPacket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendFire() {
		try {
			sendLine = hero.id + " ";
			sendBuf = sendLine.getBytes();
			sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName("127.0.0.1"), 6666);
			dategramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendBomb(Hero tempHero) {
		sendLine = tempHero.id + " " + tempHero.live + " " + tempHero.x + " "
				+ tempHero.y + " " + tempHero.direct;
		sendBuf = sendLine.getBytes();
		try {
			sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName("127.0.0.1"), 6666);
			dategramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class Receive implements Runnable {
		byte[] receiveBuf = new byte[1024];

		public void run() {

			while (dategramSocket != null) {
				DatagramPacket receivePacket = new DatagramPacket(receiveBuf,
						receiveBuf.length);
				try {
					dategramSocket.receive(receivePacket);
					String receiveLine = new String(receivePacket.getData(), 0,
							receivePacket.getLength());
					String receiveNode[] = receiveLine.split(" ");
					// System.out.println("Ω” ’µΩ");

					if (receiveNode.length == 5) {
						if (hero.id == Integer.parseInt(receiveNode[0])) {
							if (!Boolean.parseBoolean(receiveNode[1])) {
								hero.live = false;
							}
						} else {
							boolean add = true;
							for (int i = 0; i < heros.size(); i++) {

								Hero tempHero = heros.get(i);
								if (tempHero.live
										&& tempHero.id == Integer
												.parseInt(receiveNode[0])) {
									tempHero.live = Boolean
											.parseBoolean(receiveNode[1]);
									tempHero.x = Integer
											.parseInt(receiveNode[2]);
									tempHero.y = Integer
											.parseInt(receiveNode[3]);
									tempHero.direct = Direct
											.parse(receiveNode[4]);
									add = false;
									break;
								}
							}
							if (add) {
								Hero addHero = new Hero(
										Integer.parseInt(receiveNode[2]),
										Integer.parseInt(receiveNode[3]),
										Direct.parse(receiveNode[4]),
										Global.heroColor, Global.heroSpeed);
								addHero.id = Integer.parseInt(receiveNode[0]);
								addHero.live = Boolean
										.parseBoolean(receiveNode[1]);
								heros.add(addHero);
							}
						}
					} else if (receiveNode.length == 1) {
						for (int i = 0; i < heros.size(); i++) {
							Hero tempHero = heros.get(i);
							if (tempHero.id == Integer.parseInt(receiveNode[0])) {
								tempHero.fire(Global.heroBulletSpeed);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
