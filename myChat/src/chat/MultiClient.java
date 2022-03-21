package chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class MultiClient implements ActionListener {
	// 서버랑 연결된 소켓
	private Socket socket;
	// 인풋 스트림
	private ObjectInputStream ois;
	// 아웃풋 스트림
	private ObjectOutputStream oos;
	private JFrame jframe;
	private JTextField jtf;
	private JTextArea jta;
	private JLabel jlb1, jlb2;
	private JPanel jp1, jp2;
	private String ip;
	private String id;
	private JButton jbtn;

	public MultiClient(String argIp, String argId) {
		ip = argIp;
		id = argId;

		// jframe 생성
		jframe = new JFrame("Multi Chatting");
		// 채팅창
		jtf = new JTextField(30);
		// 채팅오고가는 창
		jta = new JTextArea("", 10, 50);
		jta.setBackground(Color.pink);
		// 유저아이디
		jlb1 = new JLabel("Usage ID : [[ " + id + "]]");
		// 아이피
		jlb2 = new JLabel("IP: " + ip);
		jlb1.setBackground(Color.white);
		jlb2.setBackground(Color.LIGHT_GRAY);
		// 종료
		jbtn = new JButton("종료");

		jp1 = new JPanel();
		jp2 = new JPanel();

		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new BorderLayout());
		// 레이아웃에 추가
		jp1.add(jbtn, BorderLayout.EAST);
		jp1.add(jtf, BorderLayout.CENTER);
		jp2.add(jlb1, BorderLayout.CENTER);
		jp2.add(jlb2, BorderLayout.EAST);
		jframe.add(jp1, BorderLayout.SOUTH);
		jframe.add(jp2, BorderLayout.NORTH);

		JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jframe.add(jsp, BorderLayout.CENTER);
		jtf.addActionListener(this);
		jbtn.addActionListener(this);

		jframe.addWindowListener(new WindowAdapter() {
			// window close
			public void windowClosing(WindowEvent e) {
				try {
					// #를 같이 붙여줌(구별)
					oos.writeObject(id + "#exit");
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				System.exit(0);
			}

			// window 가 실행되면 focus
			public void windowOpened(WindowEvent e) {
				jtf.requestFocus();
			}
		});
		// 클라이언트 실행시 레이아웃위치
		jta.setEditable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		jframe.pack();
		jframe.setLocation((screenWidth - jframe.getWidth()) / 2, (screenHeight - jframe.getHeight()) / 2);
		jframe.setResizable(false);
		jframe.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String msg = jtf.getText();
		if (obj == jtf) {
			// 메세지 안쓰고 엔터누르면
			if (msg == null || msg.length() == 0) { 
				JOptionPane.showMessageDialog(jframe, "글을쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					// #를 같이 붙여줌(구별)
					oos.writeObject(id + "#" + msg);
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				jtf.setText("");
			}
		// 종료버튼 누르면
		} else if (obj == jbtn) { 
			try {
				oos.writeObject(id + "#exit");
			} catch (IOException ee) {
				ee.printStackTrace();
			}
			System.exit(0);
		}
	}

	public void exit() {
		System.exit(0);
	}

	// 입력받은 값들(ip,id)로 소켓만들고, thread start
	public void init() throws IOException {
		socket = new Socket(ip, 5000);
		System.out.println("connected");
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		MultiClientThread ct = new MultiClientThread(this);
		Thread t = new Thread(ct);
		t.start();
	}

	public static void main(String args[]) throws IOException {
		// 설정해 놓은 JFrame default값 trueifcon
		JFrame.setDefaultLookAndFeelDecorated(true);
		// MultiClient(ip주소,아이디) 생성
		MultiClient cc = new MultiClient("192.168.35.167", "yuki");
		// init()메서드 실행
		cc.init();
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public JTextArea getJta() {
		return jta;
	}

	public String getId() {
		return id;
	}

}
