package chatServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {
	private ArrayList<MultiServerThread> list;
	private Socket socket;

	public MultiServer() throws IOException {
		// MultiServerThread객체를 저장 할 리스트선언
		list = new ArrayList<MultiServerThread>();
		// 대기서버소켓 생성 5000번 포트번호
		ServerSocket serverSocket = new ServerSocket(5000);
		// MultiServerThread 객체생성
		MultiServerThread mst = null;
		boolean isStop = false;
		while (!isStop) {
			// 서버 준비 완료
			System.out.println("Server Ready");
			// 클라이언트 연결요청이 접수되면 해당 클라이언트와 통신 가능한 소켓을 생성, 반환
			socket = serverSocket.accept();
			mst = new MultiServerThread(this);
			list.add(mst);
			Thread t = new Thread(mst);
			//스레드 시작 - 실행
			t.start();
		} // while
	}

	public ArrayList<MultiServerThread> getList() {
		return list;
	}

	public Socket getSocket() {
		return socket;
	}

	public static void main(String[] ar) throws IOException {
		new MultiServer();
	}

}
