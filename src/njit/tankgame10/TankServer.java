package njit.tankgame10;

import java.io.*;
import java.net.*;
import java.util.*;

public class TankServer {
	
	public static void main(String[] args) {
		new TankServer().start();		
	}
	
	private final int TCP_PORT = 8888;
	private final int UDP_PORT = 6666;
	List<Client> clients = new ArrayList<Client>();

	public void start() {
		new Thread(new UDPThread()).start();
		try {
			ServerSocket serverSocket = new ServerSocket(TCP_PORT);
			System.out.println("TCP_PORT:" + TCP_PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("A Client Connect! Addr- "
						+ socket.getInetAddress() + ":" + socket.getPort());

				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String IP = socket.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				Client client = new Client(IP, udpPort);
				clients.add(client);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private class Client {
		String IP;
		int udpPort;

		public Client(String IP, int tcpPort) {
			this.IP = IP;
			this.udpPort = tcpPort;
		}
	}

	private class UDPThread implements Runnable {

		byte[] buf = new byte[1024];

		public void run() {
			DatagramSocket dategramSocket = null;
			try {
				dategramSocket = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			System.out.println("UDP_PORT:" + UDP_PORT);
			
			while (true) {
				while (dategramSocket != null) {
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try {
						dategramSocket.receive(packet);
						for (int i = 0; i < clients.size(); i++) {
							Client client = clients.get(i);
							packet.setSocketAddress(new InetSocketAddress(
									client.IP, client.udpPort));
							dategramSocket.send(packet);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
