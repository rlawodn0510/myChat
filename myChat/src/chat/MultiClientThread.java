package chat;

public class MultiClientThread extends Thread {
	private MultiClient mc;

	public MultiClientThread(MultiClient mc) {
		this.mc = mc;
	}

	public void run() {
		String message = null;
		String[] receivedMsg = null;
		boolean isStop = false;
		while (!isStop) {
			try {
				// 메세지
				message = (String) mc.getOis().readObject();
				// #으로 아이디, 메세지를 구별한다
				receivedMsg = message.split("#");
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true;
			}
			// 전송확인
			System.out.println(receivedMsg[0] + ":" + receivedMsg[1]);
			if (receivedMsg[1].equals("exit")) {
				if (receivedMsg[0].equals(mc.getId())) {
					mc.exit();
				} else {
					mc.getJta().append(receivedMsg[0] + "님이 종료하셨습니다." + System.getProperty("line.separator"));
					mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
				}
			} else {
				mc.getJta().append(receivedMsg[0] + " : " + receivedMsg[1] + System.getProperty("line.separator"));
				mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
			}
		}
	}
}
