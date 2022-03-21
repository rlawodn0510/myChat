package chatServer;

import java.net.*;
import java.io.*;

public class MultiServerThread implements Runnable {
	private Socket socket;
	private MultiServer ms;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public MultiServerThread(MultiServer ms) {
		this.ms = ms;
	}

	public synchronized void run() {
		boolean isStop = false;
		try {
			socket = ms.getSocket();
			// 객체 송수신을 위해서 objectInputstream과 objectOutputstream을 inputstream과 outputstream에
			// 스트림 연결
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			String message = null;
			//exit를 누를 때까지 채팅진행
			while (!isStop) {
				message = (String) ois.readObject();
				String[] str = message.split("#");
				if (str[1].equals("exit")) {
					broadCasting(message);
					isStop = true;
				} else {
					// 모든 클라이언트에게 메세지 전달
					broadCasting(message);
				}
			}
			// 정상종료
			ms.getList().remove(this);
			System.out.println(socket.getInetAddress() + "정상적으로 종료하셨습니다.");
			System.out.println("list size : " + ms.getList().size());
			// 비정상종료 Exception
		} catch (Exception e) {
			ms.getList().remove(this);
			System.out.println(socket.getInetAddress() + "비정상적으로 종료하셨습니다.");
		}
	}

	public void broadCasting(String message) throws IOException {
		for (MultiServerThread ct : ms.getList()) {
			ct.send(message);
		}
	}

	public void send(String message) throws IOException {
		oos.writeObject(message);
	}
}
